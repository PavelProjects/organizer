package com.povobolapo.organizer.controller.model;

import lombok.Data;

@Data
public class SearchRequest {
    private int page = 0;
    private int size = 20;
    private String sort = "ASC";
    private String sortBy = "id";
}
