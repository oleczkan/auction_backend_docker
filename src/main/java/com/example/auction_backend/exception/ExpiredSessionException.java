package com.example.auction_backend.exception;

public class ExpiredSessionException extends RuntimeException{

    private final String message;

    public ExpiredSessionException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}