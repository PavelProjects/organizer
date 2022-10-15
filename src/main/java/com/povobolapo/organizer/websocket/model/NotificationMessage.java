package com.povobolapo.organizer.websocket.model;

import com.povobolapo.organizer.model.UserEntity;
import com.povobolapo.organizer.utils.Event;

public class NotificationMessage implements Event {
    private UserEntity userTo;
    private String title;
    private String body;
    private UserEntity userFrom;

    public NotificationMessage() {
    }


    public NotificationMessage(UserEntity userTo, String title, String body) {
        this.userTo = userTo;
        this.title = title;
        this.body = body;
    }

    public NotificationMessage(UserEntity userTo, UserEntity userFrom, String title, String body) {
        this.userTo = userTo;
        this.title = title;
        this.body = body;
        this.userFrom = userFrom;
    }

    public UserEntity getUserTo() {
        return userTo;
    }

    public void setUserTo(UserEntity userTo) {
        this.userTo = userTo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public UserEntity getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(UserEntity userFrom) {
        this.userFrom = userFrom;
    }

    @Override
    public String toString() {
        return String.format("to :: %s\ntitle :: %s\nbody :: %s\n",
                userTo.getLogin(), title, body);
    }
}
