package com.example.auction_backend.exception;

public class UnloggedUserException extends RuntimeException{

    private final String message;
    public UnloggedUserException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}