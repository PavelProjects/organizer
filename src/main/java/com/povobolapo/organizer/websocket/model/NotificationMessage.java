package com.povobolapo.organizer.websocket.model;

import com.povobolapo.organizer.controller.model.user.UserDto;
import com.povobolapo.organizer.utils.Event;

public class NotificationMessage implements Event {
    private UserDto userTo;
    private String title;
    private String body;
    private UserDto userFrom;

    public NotificationMessage() {
    }


    public NotificationMessage(UserDto userTo, String title, String body) {
        this.userTo = userTo;
        this.title = title;
        this.body = body;
    }

    public NotificationMessage(UserDto userTo, UserDto userFrom, String title, String body) {
        this.userTo = userTo;
        this.title = title;
        this.body = body;
        this.userFrom = userFrom;
    }

    public UserDto getUserTo() {
        return userTo;
    }

    public void setUserTo(UserDto userTo) {
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

    public UserDto getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(UserDto userFrom) {
        this.userFrom = userFrom;
    }

    @Override
    public String toString() {
        return String.format("to :: %s\ntitle :: %s\nbody :: %s\n",
                userTo.getLogin(), title, body);
    }
}
