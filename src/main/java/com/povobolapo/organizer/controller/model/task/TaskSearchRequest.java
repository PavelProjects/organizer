package com.povobolapo.organizer.controller.model.task;

import com.povobolapo.organizer.controller.model.SearchRequest;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class TaskSearchRequest extends SearchRequest {
    private String author = null;
    private String status = null;
    private Set<String> participants = new HashSet<>();

    public TaskSearchRequest() {
    }
}
