package com.povobolapo.organizer.controller.model.error;

import lombok.Data;

@Data
public class Violation {

    private final String fieldName;
    private final String message;
}
