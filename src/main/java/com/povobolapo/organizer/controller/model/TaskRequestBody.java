package com.povobolapo.organizer.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskRequestBody {

    @Positive
    private String id;
    @Size(max = 128)
    @NotNull
    private String name;

    private String description;

    @Size(max = 32)
    @NotNull
    private String author;

    @Size(max = 32)
    private String status;

    private Date deadline;

    public TaskRequestBody(String name, String description, String author) {
        this.name = name;
        this.description = description;
        this.author = author;
    }

    public TaskRequestBody(String id, String description) {
        this.id = id;
        this.description = description;
    }
}
