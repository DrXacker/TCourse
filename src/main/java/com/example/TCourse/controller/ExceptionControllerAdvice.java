package com.example.TCourse.controller;

import com.example.TCourse.exception.ConflictException;
import com.example.TCourse.exception.NotFoundException;
import com.example.TCourse.exception.UnauthorizedException;
import com.example.TCourse.payload.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleCustomDataConflictExceptions(Exception e) {
        HttpStatus status = HttpStatus.CONFLICT;
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage()), status);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomDataNotFoundExceptions(Exception e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage()), status);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleCustomDataUnauthorizedExceptions(Exception e) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage()), status);
    }
}
