package com.povobolapo.organizer.dao.impl;

import com.povobolapo.organizer.dao.api.TTaskRepository;
import com.povobolapo.organizer.model.DictTaskStatus;
import com.povobolapo.organizer.model.TTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component("taskServiceImpl")
public class TTaskService {
    private TTaskRepository tTaskRepository;
    private TaskStatusService statusDao;

    @Autowired
    public TTaskService(TTaskRepository tTaskRepository,TaskStatusService statusDao) {
        this.statusDao = statusDao;
        this.tTaskRepository = tTaskRepository;
    }

    public TTask createTask(TTask tTask) {
        DictTaskStatus status = statusDao.getTaskStatus("new");
        tTask.setTaskStatus(status);
        return tTaskRepository.save(tTask);
    }

}
