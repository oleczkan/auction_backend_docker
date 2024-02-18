package com.example.auction_backend.exception;

public class EndOfAuctionException extends RuntimeException{

    private final String message;

    public EndOfAuctionException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}