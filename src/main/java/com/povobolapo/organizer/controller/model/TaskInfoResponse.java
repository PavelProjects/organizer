package com.povobolapo.organizer.controller.model;

import com.povobolapo.organizer.model.TaskEntity;
import com.povobolapo.organizer.model.UserEntity;

import java.util.Date;

public class TaskInfoResponse {


    private Integer id;
    //TODO поменять на userInfoResponse
    private UserEntity author;
    private String name;
    private Date deadline;

    public TaskInfoResponse(Integer id, UserEntity author, String name, Date deadline) {
        this.id = id;
        this.author = author;
        this.name = name;
        this.deadline = deadline;
    }

    public TaskInfoResponse(TaskEntity task) {
        this.id = task.getId();
        this.author = task.getAuthor();
        this.name = task.getName();
        this.deadline = task.getDeadline();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
}
