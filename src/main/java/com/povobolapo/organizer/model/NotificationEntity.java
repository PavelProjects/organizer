package com.povobolapo.organizer.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "_notification")
public class NotificationEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "_notification_id_seq")
    @Column(name = "id")
    private Integer id;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user", referencedColumnName = "login")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task", referencedColumnName = "id")
    private TaskEntity task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator", referencedColumnName = "login")
    private UserEntity creator;

    @Column(name = "body")
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type", referencedColumnName = "name")
    private DictNotifyType type;

    @Column(name = "checked")
    private boolean checked = false;

    public NotificationEntity() {
    }

    public NotificationEntity(Integer id, LocalDate creationDate, UserEntity user,
                              TaskEntity task, UserEntity creator, String body, DictNotifyType type) {
        this.id = id;
        this.creationDate = creationDate;
        this.user = user;
        this.task = task;
        this.creator = creator;
        this.body = body;
        this.type = type;
    }

    public NotificationEntity(LocalDate creationDate, UserEntity user,
                              TaskEntity task, UserEntity creator, String body, DictNotifyType type) {
        this.creationDate = creationDate;
        this.user = user;
        this.task = task;
        this.creator = creator;
        this.body = body;
        this.type = type;
    }

    public NotificationEntity(UserEntity user, String body, DictNotifyType notifyType) {
        this.user = user;
        this.body = body;
        this.type = notifyType;

        this.creationDate = LocalDate.now();
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public TaskEntity getTask() {
        return task;
    }

    public void setTask(TaskEntity task) {
        this.task = task;
    }

    public UserEntity getCreator() {
        return creator;
    }

    public void setCreator(UserEntity creator) {
        this.creator = creator;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public DictNotifyType getType() {
        return type;
    }

    public void setType(DictNotifyType type) {
        this.type = type;
    }
}
