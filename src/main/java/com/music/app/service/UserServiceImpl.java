package com.music.app.service;

import com.music.app.config.EmailConfiguration;
import com.music.app.config.exception.BusinessException;
import com.music.app.dto.UserRegisterDTO;
import com.music.app.entity.Role;
import com.music.app.entity.User;
import com.music.app.entity.token.VerificationToken;
import com.music.app.repo.RoleRepo;
import com.music.app.repo.UserRepo;
import com.music.app.repo.VerificationTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
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
    public User confirmRegistration(String token) throws BusinessException{
        VerificationToken verificationToken = verificationTokenService.findByToken(token);

        if(token != null){
            User user = userRepo.findByEmail(verificationTokenService.findByToken(token).getUser().getEmail());
            userRepo.enableUser(user.getEmail());
            return user;
        }else{
            throw new BusinessException(404,"User not found!");
        }
    }




    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);
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
