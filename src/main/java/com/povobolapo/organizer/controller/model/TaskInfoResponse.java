package com.povobolapo.organizer.controller.model;

import com.povobolapo.organizer.model.DictTaskStatus;
import com.povobolapo.organizer.model.TaskEntity;

import java.util.Date;

public class TaskInfoResponse {


    private Integer id;
    private UserInfoResponse author;
    private String name;
    private DictTaskStatus status;
    private Date deadline;

    public TaskInfoResponse(Integer id, UserInfoResponse author, String name, DictTaskStatus status, Date deadline) {
        this.id = id;
        this.author = author;
        this.name = name;
        this.status = status;
        this.deadline = deadline;
    }

    public TaskInfoResponse(TaskEntity task) {
        this.id = task.getId();
        this.author = new UserInfoResponse(task.getAuthor());
        this.name = task.getName();
        this.deadline = task.getDeadline();
        this.status = task.getTaskStatus();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserInfoResponse getAuthor() {
        return author;
    }

    public void setAuthor(UserInfoResponse author) {
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

    public DictTaskStatus getStatus() {
        return status;
    }

    public void setStatus(DictTaskStatus status) {
        this.status = status;
    }
}
