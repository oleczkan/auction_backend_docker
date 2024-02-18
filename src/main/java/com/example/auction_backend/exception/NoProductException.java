package com.example.auction_backend.exception;

public class NoProductException extends RuntimeException{

    private final String message;
    public NoProductException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}