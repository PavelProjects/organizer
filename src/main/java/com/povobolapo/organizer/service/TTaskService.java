package com.povobolapo.organizer.service;

import com.povobolapo.organizer.repository.TTaskRepository;
import com.povobolapo.organizer.model.DictTaskStatus;
import com.povobolapo.organizer.model.TaskEntity;
import com.povobolapo.organizer.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component("taskServiceImpl")
public class TTaskService {
    private TTaskRepository tTaskRepository;
    private TaskStatusService taskStatusService;
    private UserService userService;

    @Autowired
    public TTaskService(TTaskRepository tTaskRepository, TaskStatusService taskStatusService, UserService userService) {
        this.taskStatusService = taskStatusService;
        this.tTaskRepository = tTaskRepository;
        this.userService = userService;
    }

    public TaskEntity createNewTask(String author, TaskEntity taskEntity) {
        DictTaskStatus status = taskStatusService.getTaskStatus("new");
        UserEntity authorUser = userService.getUserByLogin(author);
        taskEntity.setAuthor(authorUser);
        taskEntity.setTaskStatus(status);
        taskEntity.setCreationDate(new Date());
        return tTaskRepository.save(taskEntity);
    }

}
