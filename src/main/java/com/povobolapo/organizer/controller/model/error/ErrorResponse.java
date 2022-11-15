package com.povobolapo.organizer.controller.model.error;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// Класс для отправки юзеру данных об произошедшей ошибке
@Data
public class ErrorResponse {
    private String message;
    private Exception exception;
    private List<Violation> violations = new ArrayList<>();

    public ErrorResponse() {
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(String message, Exception exception) {
        this.message = message;
        this.exception = exception;
    }
}
