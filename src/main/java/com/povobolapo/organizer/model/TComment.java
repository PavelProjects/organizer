package com.povobolapo.organizer.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "t_comment")
public class TComment implements Serializable {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "author")
    private String authorLogin;

    @Column(name = "task_id")
    private Integer taskId;

    @Column(name = "body")
    private String body;

    public TComment() {
    }

    public TComment(LocalDate creationDate, String authorLogin, Integer taskId, String body) {
        this.creationDate = creationDate;
        this.authorLogin = authorLogin;
        this.taskId = taskId;
        this.body = body;
    }

    public TComment(Integer id, LocalDate creationDate, String authorLogin, Integer taskId, String body) {
        this.id = id;
        this.creationDate = creationDate;
        this.authorLogin = authorLogin;
        this.taskId = taskId;
        this.body = body;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getAuthorLogin() {
        return authorLogin;
    }

    public void setAuthorLogin(String authorLogin) {
        this.authorLogin = authorLogin;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
