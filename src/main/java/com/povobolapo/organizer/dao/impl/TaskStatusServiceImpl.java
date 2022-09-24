package com.povobolapo.organizer.dao.impl;

import com.povobolapo.organizer.dao.api.TaskStatusRepository;
import com.povobolapo.organizer.dao.api.TaskStatusService;
import com.povobolapo.organizer.model.DictTaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("taskServiceImpl")
public class TaskStatusServiceImpl implements TaskStatusService {
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    public TaskStatusServiceImpl(TaskStatusRepository taskStatusRepository) {
        this.taskStatusRepository = taskStatusRepository;
    }

    @Override
    public DictTaskStatus getTaskStatus(String name) {
        return taskStatusRepository.getById(1);
    }
}
