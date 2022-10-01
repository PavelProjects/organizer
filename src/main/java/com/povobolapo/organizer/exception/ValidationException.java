package com.povobolapo.organizer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ValidationException extends ResponseStatusException {

    public ValidationException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
