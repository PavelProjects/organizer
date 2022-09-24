package com.povobolapo.organizer.dao.impl;

import com.povobolapo.organizer.dao.repository.TaskStatusRepository;
import com.povobolapo.organizer.model.DictTaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TaskStatusService {
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    public TaskStatusService(TaskStatusRepository taskStatusRepository) {
        this.taskStatusRepository = taskStatusRepository;
    }

    public DictTaskStatus getTaskStatus(String name) {
        return taskStatusRepository.getById(1);
    }
}
