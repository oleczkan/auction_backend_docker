package com.example.auction_backend.api.exceptionsHendler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/* Przechwytywanie wyjątków z aplikacji */
@ControllerAdvice
public class ApiExceptionHandler {



    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Exception exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

}