package com.example.controller;

import com.example.domain.NotFoundException;
import com.example.dto.ExceptionMessage;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.invoke.WrongMethodTypeException;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionRestHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> notFoundHandle(final Exception exception){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ExceptionMessage.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(WrongMethodTypeException.class)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ResponseEntity<Object> wrongMediaTypeHandle(final Exception exception){
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ExceptionMessage.builder()
                        .status(HttpStatus.NOT_ACCEPTABLE.value())
                        .message(exception.getMessage())
                        .build());
    }
}
