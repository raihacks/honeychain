package com.honeychain.service;

public class IllegalBatchTransitionException extends RuntimeException {
    public IllegalBatchTransitionException(String message) {
        super(message);
    }
}
