package com.example.auction_backend.exception;

public class LoggedUserException extends RuntimeException{

    private final String message;
    public LoggedUserException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}