package com.povobolapo.organizer.controller.model.notification;

import java.util.List;

public class NotificationRequest {
    private List<String> ids;

    public NotificationRequest() {
    }

    public NotificationRequest(List<String> ids) {
        this.ids = ids;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return String.format("ids: %s", ids);
    }
}
