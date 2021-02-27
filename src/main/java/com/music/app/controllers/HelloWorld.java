package com.music.app.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloWorld {

    @GetMapping(path = "/hello")
    public ResponseEntity<Map<String,String>> sayHello(){
        final Map<String, String> message = new HashMap<>();
        message.put("message", "Hello world");

        return ResponseEntity.ok(message);
    }
}
