package com.music.app.controllers;

import com.music.app.config.exception.BusinessException;
import com.music.app.constraint.PhotoConstraint;
import com.music.app.dto.LoginDto;
import com.music.app.dto.PasswordResetDto;
import com.music.app.dto.UserLoginDTO;
import com.music.app.entity.Photo;
import com.music.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
import java.io.IOException;
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
    public ResponseEntity<LoginDto> loginUser(@RequestBody final UserLoginDTO userLoginDTO) throws BusinessException {
        return ResponseEntity.ok(userService.login(userLoginDTO));
    }

    @PostMapping(path = "/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam final String email) throws BusinessException {
        return ResponseEntity.ok(userService.forgotPassword(email));
    }

    @PostMapping(path = "/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam final String token, @RequestParam @Valid final PasswordResetDto password) throws BusinessException {
        userService.resetPassword(token, password);

        return ResponseEntity.ok("User password updated!");
    }

    @PostMapping(path = "/save-profile-image")
    public ResponseEntity<Long> saveProfileImage(@RequestPart("file") @PhotoConstraint MultipartFile file, HttpServletRequest request) throws BusinessException, IOException {
        long id = userService.saveProfilePicture(file, request);

//        final URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(id)
//                .toUri();

        return ResponseEntity.ok(id);
    }

    @PostMapping(path = "/update-to-artist")
    public ResponseEntity<String> updateUserRole(HttpServletRequest request) {
        userService.updateUserToArtist(request);

        return ResponseEntity.ok("User updated to artist!");
    }
}
