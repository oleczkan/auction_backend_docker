package com.example.auction_backend.exception;

public class NoUserException extends RuntimeException{

    private final String message;
    public NoUserException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}