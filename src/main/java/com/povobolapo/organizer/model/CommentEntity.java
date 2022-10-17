package com.povobolapo.organizer.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "_comment")
public class CommentEntity implements Serializable {
    @Id
    @GenericGenerator(name = "entity_id", strategy = "com.povobolapo.organizer.model.EntityIdGenerator")
    @GeneratedValue(generator = "entity_id")
    @Column(name = "id")
    private String id;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "author")
    private String authorLogin;

    @Column(name = "task_id")
    private String taskId;

    @Column(name = "body")
    private String body;

    public CommentEntity() {
    }

    public CommentEntity(LocalDate creationDate, String authorLogin, String taskId, String body) {
        this.creationDate = creationDate;
        this.authorLogin = authorLogin;
        this.taskId = taskId;
        this.body = body;
    }

    public CommentEntity(String id, LocalDate creationDate, String authorLogin, String taskId, String body) {
        this.id = id;
        this.creationDate = creationDate;
        this.authorLogin = authorLogin;
        this.taskId = taskId;
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
