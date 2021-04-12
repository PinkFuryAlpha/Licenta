package com.music.app.service;

import com.music.app.config.EmailConfiguration;
import com.music.app.config.exception.BusinessException;
import com.music.app.dto.UserLoginDTO;
import com.music.app.dto.UserRegisterDTO;
import com.music.app.entity.Role;
import com.music.app.entity.User;
import com.music.app.entity.VerificationToken;
import com.music.app.repo.RoleRepo;
import com.music.app.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
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
    private VerificationTokenService verificationTokenService;

    @Autowired
    private EmailConfiguration emailConfig;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JWT jwtToken;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public String login(UserLoginDTO userLoginDTO) throws BusinessException {

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

        return jwtToken.generateToken(userDetails);

    }

    @Override
    public String forgotPassword(String email) throws BusinessException {
        System.out.println(email);
        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new BusinessException(404, "Email is not found!");
        }

        VerificationToken token = verificationTokenService.saveVerificationToken(user);

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.emailConfig.getHost());
        mailSender.setPort(this.emailConfig.getPort());
        mailSender.setUsername(this.emailConfig.getUsername());
        mailSender.setPassword(this.emailConfig.getPassword());

        String resetPasswordURL = "/users/reset-password?token=" + token.getToken();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("tinteanu.tudor@gmail.com");
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Reset Password!");
        mailMessage.setText("Music App! " +
                "please click on the link bellow in order to reset your password:" + "\n" +
                "http://localhost:8080" + resetPasswordURL);

        mailSender.send(mailMessage);

        return token.getToken();

    }

    @Override
    public void resetPassword(String passwordResetToken, String password) throws BusinessException {
        VerificationToken resetPasswordToken = verificationTokenService.findByToken(passwordResetToken);

        if (resetPasswordToken != null && resetPasswordToken.getExpireAt().isBefore(LocalDateTime.now())) {
            Long userId = userRepo.findByEmail(verificationTokenService.findByToken(passwordResetToken).getUser().getEmail()).getId();
            userRepo.updatePassword(bCryptPasswordEncoder.encode(password), userId);
            verificationTokenService.removeToken(passwordResetToken);
        } else {
            throw new BusinessException(404, "Link expired or token is invalid!");
        }
    }

    @Override
    public Long save(UserRegisterDTO userRegisterDTO) throws BusinessException {

        User user = userRepo.findByEmail(userRegisterDTO.getEmail());

        if (user != null) {
            throw new BusinessException(403, "The email is already in use!");
        } else if (userRepo.findByUsername(userRegisterDTO.getUsername()) != null) {
            throw new BusinessException(403, "The username is taken!");
        }

        user = new User(userRegisterDTO.getFirstName(),
                userRegisterDTO.getLastName(),
                userRegisterDTO.getUsername(),
                bCryptPasswordEncoder.encode(userRegisterDTO.getPassword()),
                userRegisterDTO.getEmail(),
                Collections.singletonList(new Role("BASIC_USER")));

        long id = userRepo.save(user).getId();

        VerificationToken token = verificationTokenService.saveVerificationToken(user);

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
    public User confirmRegistration(String token) throws BusinessException {
        VerificationToken verificationToken = verificationTokenService.findByToken(token);

        if (verificationToken != null && verificationToken.getExpireAt().isBefore(LocalDateTime.now())) {
            User user = userRepo.findByEmail(verificationTokenService.findByToken(token).getUser().getEmail());
            userRepo.enableUser(user.getEmail());
            verificationTokenService.removeToken(token);
            return user;
        } else {
            throw new BusinessException(404, "User not found, or link expired!");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password!");
        }
        //boolean enabled = !user.isEnabled();
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
