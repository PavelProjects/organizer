package com.povobolapo.organizer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

// Ошибка для работы с контентом

public class StorageException extends ResponseStatusException {

    public StorageException(String reason) {
        super(HttpStatus.NOT_ACCEPTABLE, reason);
    }
}
