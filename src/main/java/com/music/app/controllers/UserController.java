package com.music.app.controllers;

import com.music.app.config.exception.BusinessException;
import com.music.app.constraint.PasswordConstraint;
import com.music.app.dto.UserLoginDTO;
import com.music.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping(path = "/users")
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
    public ResponseEntity<String> resetPassword(@RequestParam final String token, @RequestParam @NotBlank @PasswordConstraint final String password) throws BusinessException {
        userService.resetPassword(token,password);

        return ResponseEntity.ok("User password updated!");
    }
}
