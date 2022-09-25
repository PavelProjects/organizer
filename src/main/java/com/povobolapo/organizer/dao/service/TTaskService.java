package com.povobolapo.organizer.dao.service;

import com.povobolapo.organizer.dao.repository.TTaskRepository;
import com.povobolapo.organizer.model.DictTaskStatus;
import com.povobolapo.organizer.model.TTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component("taskServiceImpl")
public class TTaskService {
    private TTaskRepository tTaskRepository;
    private TaskStatusService statusDao;

    @Autowired
    public TTaskService(TTaskRepository tTaskRepository, TaskStatusService statusDao) {
        this.statusDao = statusDao;
        this.tTaskRepository = tTaskRepository;
    }

    public TTask createNewTask(TTask tTask) {
        DictTaskStatus status = statusDao.getTaskStatus("new");
        tTask.setTaskStatus(status);
        tTask.setCreationDate(new Date());
        return tTaskRepository.save(tTask);
    }

}
