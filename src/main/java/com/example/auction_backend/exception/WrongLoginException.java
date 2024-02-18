package com.example.auction_backend.exception;

public class WrongLoginException extends RuntimeException {
    private final String message;

    public WrongLoginException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}