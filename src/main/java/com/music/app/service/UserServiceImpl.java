package com.music.app.service;

import com.music.app.config.EmailConfiguration;
import com.music.app.config.exception.BusinessException;
import com.music.app.config.mapper.UserToDto;
import com.music.app.dto.LoginDto;
import com.music.app.dto.PasswordResetDto;
import com.music.app.dto.UserLoginDTO;
import com.music.app.dto.UserRegisterDTO;
import com.music.app.entity.Photo;
import com.music.app.entity.Role;
import com.music.app.entity.User;
import com.music.app.entity.VerificationToken;
import com.music.app.repo.PhotoRepository;
import com.music.app.repo.RoleRepo;
import com.music.app.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private EmailConfiguration emailConfig;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JWT jwtToken;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${profile.photo.path}")
    private String profilePhotoDirectory;

    @Override
    public LoginDto login(UserLoginDTO userLoginDTO) throws BusinessException {

        if (Objects.isNull(userLoginDTO)) {
            throw new BusinessException(401, "Body null !");
        }

        if (Objects.isNull(userLoginDTO.getUsername())) {
            throw new BusinessException(400, "Username can't be null ! ");
        }

        if (Objects.isNull(userLoginDTO.getPassword())) {
            throw new BusinessException(400, "Password can't be null !");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDTO.getUsername(), userLoginDTO.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BusinessException(401, "Bad credentials!");
        }

        final UserDetails userDetails = loadUserByUsername(userLoginDTO.getUsername());

        User user = userRepo.findByUsername(userLoginDTO.getUsername());

        String jwt = jwtToken.generateToken(userDetails);

        return UserToDto.convertEntityToLoginDto(user, jwt);

    }

    @Override
    public String forgotPassword(String email) throws BusinessException {
        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new BusinessException(404, "Email is not found!");
        }

        VerificationToken token = verificationTokenService.saveVerificationToken(user, "PASSWORD");

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.emailConfig.getHost());
        mailSender.setPort(this.emailConfig.getPort());
        mailSender.setUsername(this.emailConfig.getUsername());
        mailSender.setPassword(this.emailConfig.getPassword());

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("tinteanu.tudor@gmail.com");
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Reset Password!");
        mailMessage.setText("Music App! " +
                "please click on the link bellow in order to reset your password:" + "\n" +
                "http://localhost:3000/password-reset" + "\n" + "Copy and paste the following code when you change password: " + token.getToken());

        mailSender.send(mailMessage);

        return token.getToken();

    }

    @Override
    public void resetPassword(String passwordResetToken, PasswordResetDto password) throws BusinessException {
        VerificationToken resetPasswordToken = verificationTokenService.findByToken(passwordResetToken);

        if (resetPasswordToken != null && resetPasswordToken.getExpireAt().isAfter(LocalDateTime.now()) && resetPasswordToken.getTokenType().equals("PASSWORD")) {
            Long userId = userRepo.findByEmail(verificationTokenService.findByToken(passwordResetToken).getUser().getEmail()).getId();
            userRepo.updatePassword(bCryptPasswordEncoder.encode(password.getPassword()), userId);
            verificationTokenService.removeToken(passwordResetToken);
        } else {
            throw new BusinessException(404, "Link expired or token is invalid!");
        }
    }

    @Transactional
    @Override
    public Long saveProfilePicture(MultipartFile media, HttpServletRequest request) throws BusinessException, IOException {
        Principal principal = request.getUserPrincipal();
        User user = userRepo.findByUsername(principal.getName());

        Long id = 2L;
        if (user.getProfilePicture() == null) {
            Photo photo = new Photo();
            photo.setUser(user);
            photo.setPhotoStoreLocation(mediaService.saveMedia(media, profilePhotoDirectory));
            user.setProfilePicture(photo);
            id = photoRepository.save(photo).getId();
        } else {
            Photo photo = user.getProfilePicture();
            mediaService.deletePhoto(photo.getId());
            String newPhotoLocation = mediaService.saveMedia(media, profilePhotoDirectory);
            photo.setPhotoStoreLocation(newPhotoLocation);
            id = photo.getId();
        }

        return id;
    }

    @Override
    public Long save(UserRegisterDTO userRegisterDTO) throws BusinessException {

        User user = userRepo.findByEmail(userRegisterDTO.getEmail());

        Collection<Role> roles = new HashSet<>();

        roleRepo.findById(2).ifPresent(roles::add);

        if (user != null) {
            throw new BusinessException(403, "The email is already in use!");
        } else if (userRepo.findByUsername(userRegisterDTO.getUsername()) != null) {
            throw new BusinessException(403, "The username is taken!");
        }

        Photo profilePicture = photoRepository.findById(1L).orElse(null);

        user = new User(userRegisterDTO.getFirstName(),
                userRegisterDTO.getLastName(),
                userRegisterDTO.getUsername(),
                bCryptPasswordEncoder.encode(userRegisterDTO.getPassword()),
                userRegisterDTO.getEmail(),
                roles,
                profilePicture);

        long id = userRepo.save(user).getId();

        VerificationToken token = verificationTokenService.saveVerificationToken(user, "REGISTER");

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.emailConfig.getHost());
        mailSender.setPort(this.emailConfig.getPort());
        mailSender.setUsername(this.emailConfig.getUsername());
        mailSender.setPassword(this.emailConfig.getPassword());

        String confirmationURL = "/register/confirm-register?token=" + token.getToken();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("tinteanu.tudor@gmail.com");
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("Email was sent successfully! " +
                "please click on the link bellow in order to confirm your email:" + "\n" +
                "http://localhost:8080" + confirmationURL);

        mailSender.send(mailMessage);

        return id;
    }

    @Override
    public String confirmRegistration(String token) throws BusinessException {
        VerificationToken verificationToken = verificationTokenService.findByToken(token);

        if (verificationToken != null && verificationToken.getExpireAt().isAfter(LocalDateTime.now()) && verificationToken.getTokenType().equals("REGISTER")) {
            User user = userRepo.findByEmail(verificationTokenService.findByToken(token).getUser().getEmail());
            userRepo.enableUser(user.getEmail());
            verificationTokenService.removeToken(token);
            return "<h1 style=\"display:flex;align-text:center\">User is activated</h1>";
        } else {
            throw new BusinessException(404, "Invalid, or expired link!");
        }
    }

    @Override
    public void updateUserToArtist(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userRepo.findByUsername(principal.getName());
        Collection<Role> userRoles = user.getRoles();

        roleRepo.findById(3).ifPresentOrElse(userRoles::add, () -> {
            try {
                throw new BusinessException(404, "Role not found!");
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        });
    }

    @Transactional
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void deleteExpiredUsers() {
        userRepo.deleteExpiredUsers();
        System.out.println("Deleted expired users!");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        if (user == null || !user.isEnabled()) {
            throw new UsernameNotFoundException("Invalid username or password!");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(String.format("ROLE_%s", role.getName())))
                .collect(Collectors.toList());
    }
}
