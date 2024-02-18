package com.example.auction_backend.exception;

public class WrongNewPriceException extends RuntimeException{

    private final String message;
    public WrongNewPriceException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}