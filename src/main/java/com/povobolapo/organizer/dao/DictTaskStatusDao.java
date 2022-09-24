package com.povobolapo.organizer.dao;

import org.hibernate.Session;

public class DictTaskStatusDao {
    private final Session session;

    public DictTaskStatusDao (Session session) {
        this.session = session;
    }

    public DictTaskStatus getTaskStatus(String name) {
        return (DictTaskStatus) session.createQuery(
                "select * from ?1 where name = ?2")
                .setParameter(1, DictTaskStatus.NAME)
                .setParameter(2, name)
                .getSingleResult();
    }
}
