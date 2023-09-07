package com.example.domain;

public class WrongMediaTypeException extends RuntimeException{
    public WrongMediaTypeException(final String message) {
        super(message);
    }
}

