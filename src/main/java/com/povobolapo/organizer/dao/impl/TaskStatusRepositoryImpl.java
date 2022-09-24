package com.povobolapo.organizer.dao.impl;

import com.povobolapo.organizer.dao.api.TaskStatusRepository;
import com.povobolapo.organizer.model.DictTaskStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@Qualifier("taskServiceImpl")
public class TaskStatusRepositoryImpl implements TaskStatusRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public DictTaskStatus getTaskStatus(String name) {
        return (DictTaskStatus) em.createQuery(
                "select * from ?1 where name = ?2")
                .setParameter(1, DictTaskStatus.NAME)
                .setParameter(2, name)
                .getSingleResult();
    }
}
