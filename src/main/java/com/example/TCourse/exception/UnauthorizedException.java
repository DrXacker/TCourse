package com.example.TCourse.exception;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String msg) {
        super(msg);
    }
}