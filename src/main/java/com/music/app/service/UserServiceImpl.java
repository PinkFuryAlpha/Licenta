package com.music.app.service;

import com.music.app.config.EmailConfiguration;
import com.music.app.config.exception.BusinessException;
import com.music.app.config.tokens.JWT;
import com.music.app.dto.UserLoginDTO;
import com.music.app.dto.UserRegisterDTO;
import com.music.app.entity.Role;
import com.music.app.entity.User;
import com.music.app.config.tokens.VerificationToken;
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

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
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
        } catch (BadCredentialsException e){
            throw new BusinessException(401,"Bad credentials!");
        }

        final UserDetails userDetails = loadUserByUsername(userLoginDTO.getUsername());

        return jwtToken.generateToken(userDetails);

    }

    @Override
    public Long save(UserRegisterDTO userRegisterDTO) throws BusinessException {

        User user = userRepo.findByEmail(userRegisterDTO.getEmail());

        if (user != null) {
            throw new BusinessException(403, "The email is already in use!");
        } else if (!userRegisterDTO.getFirstName().matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$")) {
            throw new BusinessException(403, "First name is not valid!");
        } else if (!userRegisterDTO.getLastName().matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$")) {
            throw new BusinessException(403, "Last name is not valid!");
        } else if (!userRegisterDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new BusinessException(403, "Email is not valid!");
        } else if (!userRegisterDTO.getUsername().matches("^[a-zA-Z0-9._-]{3,}$")) {
            throw new BusinessException(403, "Username is not valid!");
        } else if (!userRegisterDTO.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            throw new BusinessException(403, "Passowrd is not valid!");
        } else {
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

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("tinteanu.tudor@gmail.com");
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setText("Email was sent successfully!");

            mailSender.send(mailMessage);

            return id;
        }

    }

    @Override
    public User confirmRegistration(String token) throws BusinessException {
        VerificationToken verificationToken = verificationTokenService.findByToken(token);

        if (token != null) {
            User user = userRepo.findByEmail(verificationTokenService.findByToken(token).getUser().getEmail());
            userRepo.enableUser(user.getEmail());
            return user;
        } else {
            throw new BusinessException(404, "User not found!");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password!");
        }
        boolean enabled = !user.isEnabled();
        return new org.springframework.security.core.userdetails.User(user.getFirstName(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(String.format("ROLE_%s", role.getName())))
                .collect(Collectors.toList());
    }
}
