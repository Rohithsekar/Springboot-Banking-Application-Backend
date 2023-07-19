package com.banking.online_banking.exception;

public class UnsupportedAccountTypeException extends RuntimeException {
    public UnsupportedAccountTypeException(String message) {
        super(message);
    }
}
