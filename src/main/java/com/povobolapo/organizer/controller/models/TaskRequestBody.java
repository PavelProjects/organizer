package com.povobolapo.organizer.controller.models;

import com.povobolapo.organizer.model.DictTaskStatus;
import com.povobolapo.organizer.model.TaskEntity;
import com.povobolapo.organizer.model.UserEntity;

import javax.validation.constraints.*;
import java.util.Date;

public class TaskRequestBody {

    @Positive
    private Integer id;
    @Size(max = 128)
    @NotNull
    private String name;

    private String description;

    @Size(max = 32)
    @NotNull
    private String author;

    @Size(max = 32)
    private String status = "new"; //Дефолтное значение

    private Date deadline;

    public TaskRequestBody() {
    }

    public TaskRequestBody(String name, String author) {
        this.name = name;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TaskEntity toTask() {
        TaskEntity task = new TaskEntity();
        if (this.id != null) {
            task.setId(this.id);
        }
        task.setName(this.name);
        task.setAuthor(new UserEntity(this.author));
        if (this.description != null) {
            task.setDescription(this.description);
        }
        task.setTaskStatus(new DictTaskStatus(this.status));
        if (this.deadline != null) {
            task.setDeadline(this.deadline);
        }
        return task;
    }
}
