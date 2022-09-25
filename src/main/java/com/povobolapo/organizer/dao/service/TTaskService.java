package com.povobolapo.organizer.dao.service;

import com.povobolapo.organizer.dao.repository.TTaskRepository;
import com.povobolapo.organizer.model.DictTaskStatus;
import com.povobolapo.organizer.model.TTask;
import com.povobolapo.organizer.model.TUser;
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

    public TTask createNewTask(String author, TTask tTask) {
        DictTaskStatus status = taskStatusService.getTaskStatus("new");
        TUser authorUser = userService.getUserByLogin(author);
        tTask.setAuthor(authorUser);
        tTask.setTaskStatus(status);
        tTask.setCreationDate(new Date());
        return tTaskRepository.save(tTask);
    }

}
