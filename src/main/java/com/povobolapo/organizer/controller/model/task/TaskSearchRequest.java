package com.povobolapo.organizer.controller.model.task;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class TaskSearchRequest {
    private int page = 0;
    private int size = 10;
    private String sort = "ASC";
    private String sortBy = "id";
    private String author = null;
    private String status = null;
    private Set<String> participants = new HashSet<>();

    public TaskSearchRequest() {
    }

    public TaskSearchRequest(int page, int size, String sort, String sortBy, String author, String status) {
        this.page = page;
        this.size = size;
        this.sort = sort;
        this.sortBy = sortBy;
        this.author = author;
        this.status = status;
    }

    public TaskSearchRequest(String sort, String author, String status) {
        this.sort = sort;
        this.author = author;
        this.status = status;
    }
}
