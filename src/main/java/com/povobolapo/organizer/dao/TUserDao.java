package com.povobolapo.organizer.dao;

import org.hibernate.Session;

public class TUserDao {
    private final Session session;

    public TUserDao (Session session) {
        this.session = session;
    }

    public void createUser(TUser user) {
        /*session.beginTransaction();

        session.save(user);*/

    }
}
