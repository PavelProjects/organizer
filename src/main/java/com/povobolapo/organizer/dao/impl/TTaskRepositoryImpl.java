package com.povobolapo.organizer.dao.impl;

import com.povobolapo.organizer.dao.api.TTaskRepository;
import com.povobolapo.organizer.dao.api.TaskStatusRepository;
import com.povobolapo.organizer.model.DictTaskStatus;
import com.povobolapo.organizer.model.TTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class TTaskRepositoryImpl implements TTaskRepository {

    private EntityManager em;
    private TaskStatusRepository statusDao;

    @Autowired
    public TTaskRepositoryImpl(@Qualifier("taskServiceImpl") TaskStatusRepository statusDao, EntityManager em) {
        this.statusDao = statusDao;
        this.em = em;
    }

    @Override
    public TTask createTask(String taskName, String description) {
        DictTaskStatus status = statusDao.getTaskStatus("new");
        TTask tTask = new TTask();
        tTask.setTaskStatus(status);
        tTask.setName(taskName);
        tTask.setDescription(description);
        em.persist(tTask);
        return tTask;
    }

}
