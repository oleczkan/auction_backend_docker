package com.example.auction_backend.exception;

public class DoubleBiddingException extends RuntimeException{
    private final String message;

    public DoubleBiddingException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}