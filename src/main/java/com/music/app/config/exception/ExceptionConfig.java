package com.music.app.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionConfig {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity parseBusinessException(BusinessException ex){
        Map<String,Object> responseMessage = new LinkedHashMap<>();
        responseMessage.put("status",ex.getStatus());
        responseMessage.put("message",ex.getMessage());

        return new ResponseEntity(responseMessage, HttpStatus.valueOf(ex.getStatus()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, Object> responseMessage = new LinkedHashMap<>();
        String fieldName;
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            fieldName = error.getClass().getName();
            responseMessage.put(fieldName, error.getDefaultMessage());
        }
        responseMessage.put("status", 400);
        return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);
    }
}
