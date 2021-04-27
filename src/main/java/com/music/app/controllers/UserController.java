package com.music.app.controllers;

import com.music.app.config.exception.BusinessException;
import com.music.app.constraint.PasswordConstraint;
import com.music.app.dto.PasswordResetDto;
import com.music.app.dto.UserLoginDTO;
import com.music.app.entity.ProfilePicture;
import com.music.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(path = "/users")
@Validated
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/authenticate")
    public ResponseEntity<String> loginUser(@RequestBody final UserLoginDTO userLoginDTO) throws BusinessException {
        return ResponseEntity.ok(userService.login(userLoginDTO));
    }

    @PostMapping(path = "/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam final String email) throws BusinessException {
        return ResponseEntity.ok(userService.forgotPassword(email));
    }

    //CONSTRAINT NOT WORKING
    @PostMapping(path = "/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam final String token, @RequestParam @Valid final PasswordResetDto password) throws BusinessException {
        userService.resetPassword(token,password);

        return ResponseEntity.ok("User password updated!");
    }

    @PostMapping(path = "/save-profile-image")
    public ResponseEntity<ProfilePicture> saveProfileImage(@RequestPart("file") MultipartFile file, HttpServletRequest request) throws BusinessException {
        long id = userService.saveProfilePicture(file,request);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
