package com.povobolapo.organizer.controller.model;

import com.povobolapo.organizer.model.NotificationEntity;

import java.time.LocalDate;

public class NotificationResponse {
    private String id;
    private LocalDate creationDate;
    private UserInfoResponse user;
    private UserInfoResponse creator;
    private String body;
    private boolean checked;

    public NotificationResponse() {
    }

    public NotificationResponse(String id, LocalDate creationDate,
                                UserInfoResponse user, UserInfoResponse creator,
                                String body, boolean checked) {
        this.id = id;
        this.creationDate = creationDate;
        this.user = user;
        this.creator = creator;
        this.body = body;
        this.checked = checked;
    }

    public NotificationResponse(NotificationEntity notificationEntity) {
        id = notificationEntity.getId();
        creationDate = notificationEntity.getCreationDate();
        body = notificationEntity.getBody();
        checked = notificationEntity.isChecked();

        if (notificationEntity.getUser() != null) {
            user = new UserInfoResponse(notificationEntity.getUser());
        }
        if (notificationEntity.getCreator() != null) {
            creator = new UserInfoResponse(notificationEntity.getCreator());
        }
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

    public UserInfoResponse getUser() {
        return user;
    }

    public void setUser(UserInfoResponse user) {
        this.user = user;
    }

    public UserInfoResponse getCreator() {
        return creator;
    }

    public void setCreator(UserInfoResponse creator) {
        this.creator = creator;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
