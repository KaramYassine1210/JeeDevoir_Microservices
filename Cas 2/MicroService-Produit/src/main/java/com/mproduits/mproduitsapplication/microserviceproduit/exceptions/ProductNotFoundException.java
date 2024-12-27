package com.mproduits.mproduitsapplication.microserviceproduit.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
