package com.example.auction_backend.exception;

public class OwnAuctionBidding extends RuntimeException{
    private final String message;

    public OwnAuctionBidding(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}