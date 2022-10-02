package com.povobolapo.organizer.service;

import com.povobolapo.organizer.controller.models.TaskRequestBody;
import com.povobolapo.organizer.exception.NotFoundException;
import com.povobolapo.organizer.repository.TaskRepository;
import com.povobolapo.organizer.model.DictTaskStatus;
import com.povobolapo.organizer.model.TaskEntity;
import com.povobolapo.organizer.model.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Component("taskServiceImpl")
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskStatusService taskStatusService;
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskStatusService taskStatusService, UserService userService) {
        this.taskStatusService = taskStatusService;
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public List<TaskEntity> getAllTasks(Integer page, Integer size) {
        log.info("Searching for allTasks (page={} // size={})", page, size);
        PageRequest pagesRequest = PageRequest.of(page, size);
        Page<TaskEntity> found = taskRepository.findAll(pagesRequest);
        log.debug("Found next values: " + found.getContent());
        return found.getContent();
    }

    public TaskEntity getTaskById(Integer id) {
        Optional<TaskEntity> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            log.warn("Task is NULL.");
            throw new NotFoundException("Task with id [" + id + "] not found.");
        }
        return task.get();
    }

    public TaskEntity createNewTask(String author, TaskRequestBody taskRequest) {
        DictTaskStatus status = taskStatusService.getTaskStatus("new");
        UserEntity authorUser = userService.getUserByLogin(author);
        TaskEntity task = taskRequest.toTask();
        task.setAuthor(authorUser);
        task.setTaskStatus(status);
        task.setCreationDate(new Date());
        return taskRepository.save(task);
    }

    public boolean deleteTaskById(Integer id) {
        Optional<TaskEntity> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            log.warn("Task is NULL.");
            throw new NotFoundException("Task with id [" + id + "] not found.");
        }
        log.debug("Found task (id={}).", id);
        taskRepository.delete(task.get());
        return true;
    }
}
