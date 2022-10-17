package com.povobolapo.organizer.service;

import com.povobolapo.organizer.repository.TaskStatusRepository;
import com.povobolapo.organizer.model.DictTaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("singleton")
public class TaskStatusService {
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    public DictTaskStatus getTaskStatus(String name) {
        return taskStatusRepository.findByName(name);
    }
}
