package com.runners.exception;

import com.runners.exception.message.ErrorMessage;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
