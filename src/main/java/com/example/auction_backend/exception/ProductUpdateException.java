package com.example.auction_backend.exception;

public class ProductUpdateException extends RuntimeException{
    private final String message;

    public ProductUpdateException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}