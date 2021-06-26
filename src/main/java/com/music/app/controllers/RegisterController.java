package com.music.app.controllers;

import com.music.app.config.exception.BusinessException;
import com.music.app.dto.UserRegisterDTO;
import com.music.app.entity.User;
import com.music.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(path = "/register")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RegisterController {

    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<User> register(@RequestBody @Valid final UserRegisterDTO userRegisterDTO) throws BusinessException {
        long id = userService.save(userRegisterDTO);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(path="/confirm-register")
    public ResponseEntity<String> confirmAccount(@RequestParam final String token) throws BusinessException{
        return ResponseEntity.ok(userService.confirmRegistration(token));
    }
}
