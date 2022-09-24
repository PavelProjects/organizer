package com.povobolapo.organizer.dao.impl;

import org.hibernate.Session;

public class TUserDao {
    private final Session session;

    public TUserDao (Session session) {
        this.session = session;
    }

}
