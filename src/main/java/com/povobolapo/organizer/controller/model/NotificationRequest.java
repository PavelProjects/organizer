package com.povobolapo.organizer.controller.model;

import java.util.List;

public class NotificationRequest {
    private List<Integer> ids;

    public NotificationRequest() {
    }

    public NotificationRequest(List<Integer> ids) {
        this.ids = ids;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return String.format("ids: %s", ids);
    }
}
