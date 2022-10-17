package com.povobolapo.organizer.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "_notification")
public class NotificationEntity implements Serializable {
    @Id
    @GenericGenerator(name = "entity_id", strategy = "com.povobolapo.organizer.model.EntityIdGenerator")
    @GeneratedValue(generator = "entity_id")
    @Column(name = "id")
    private String id;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "user_login")
    private String userLogin;

    @Column(name = "task_id")
    private String taskId;

    @Column(name = "creator")
    private String creatorLogin;

    @Column(name = "body")
    private String body;

    @Column(name = "type")
    private String type;

    public NotificationEntity() {
    }

    public NotificationEntity(String id, LocalDate creationDate, String userLogin,
                              String taskId, String creatorLogin, String body, String type) {
        this.id = id;
        this.creationDate = creationDate;
        this.userLogin = userLogin;
        this.taskId = taskId;
        this.creatorLogin = creatorLogin;
        this.body = body;
        this.type = type;
    }

    public NotificationEntity(LocalDate creationDate, String userLogin,
                              String taskId, String creatorLogin, String body, String type) {
        this.creationDate = creationDate;
        this.userLogin = userLogin;
        this.taskId = taskId;
        this.creatorLogin = creatorLogin;
        this.body = body;
        this.type = type;
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

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCreatorLogin() {
        return creatorLogin;
    }

    public void setCreatorLogin(String creatorLogin) {
        this.creatorLogin = creatorLogin;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
