package com.povobolapo.organizer.dao;

import org.hibernate.Session;

public class TTaskDao {
    private final Session session;

    public TTaskDao (Session session) {
        this.session = session;
    }

    public void createTask(String taskName, String description) {

    }

}
