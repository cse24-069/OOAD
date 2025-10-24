package com.silverpalm.banking.core.exception;

/**
 * Custom exception for banking operations
 * Provides specific error messages for different banking scenarios
 */
public class BankingException extends Exception {
    public BankingException(String message) {
        super(message);
    }
    
    public BankingException(String message, Throwable cause) {
        super(message, cause);
    }
}