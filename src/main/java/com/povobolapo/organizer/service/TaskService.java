package com.povobolapo.organizer.service;

import com.povobolapo.organizer.controller.models.TaskRequestBody;
import com.povobolapo.organizer.controller.models.TaskSearchRequest;
import com.povobolapo.organizer.exception.NotFoundException;
import com.povobolapo.organizer.repository.TaskRepository;
import com.povobolapo.organizer.model.DictTaskStatus;
import com.povobolapo.organizer.model.TaskEntity;
import com.povobolapo.organizer.model.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

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

    @Transactional
    public List<TaskEntity> getAllTasks(TaskSearchRequest request) {
        log.info("Searching for allTasks (request={})", request);
        PageRequest pagesRequest = PageRequest.of(request.getPage(), request.getSize());
        Example<TaskEntity> example = createExampleForSearch(request);

        Page<TaskEntity> found = taskRepository.findAll(example, pagesRequest);

        log.debug("Found next values: " + found.getContent());
        return found.getContent();
    }

    @Transactional
    public TaskEntity getTaskById(Integer id) {
        Optional<TaskEntity> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            log.warn("Task is NULL.");
            throw new NotFoundException("Task with id [" + id + "] not found.");
        }
        return task.get();
    }

    @Transactional
    public TaskEntity createNewTask(String author, TaskRequestBody taskRequest) {
        DictTaskStatus status = taskStatusService.getTaskStatus("new");
        UserEntity authorUser = userService.getUserByLogin(author);
        TaskEntity task = taskRequest.toTask();
        task.setAuthor(authorUser);
        task.setTaskStatus(status);
        task.setCreationDate(new Date());
        return taskRepository.save(task);
    }

    //TODO авторизация, чтобы обновлял только автор/админ
    @Transactional
    public TaskEntity updateTask(TaskRequestBody taskRequest) {
        Optional<TaskEntity> baseTask = taskRepository.findById(taskRequest.getId());
        if (baseTask.isEmpty()) {
            log.warn("Task is NULL, update stopped.");
            throw new NotFoundException("Task with id [" + taskRequest.getId() + "] not found.");
        }
        log.debug("Found task (id={}).", taskRequest.getId());
        baseTask.get().setDescription(taskRequest.getDescription());
        baseTask.get().setDeadline(taskRequest.getDeadline());
        if (taskRequest.getStatus() != null) {
            log.debug("Searching for new status (name={})", taskRequest.getStatus());
            DictTaskStatus status = taskStatusService.getTaskStatus(taskRequest.getStatus());
            if (status != null) baseTask.get().setTaskStatus(status);
        }
        baseTask.get().setName(taskRequest.getName());
        return taskRepository.save(baseTask.get());
    }

    @Transactional
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

    private Example<TaskEntity> createExampleForSearch(TaskSearchRequest request) {
        DictTaskStatus status = null;
        UserEntity author = null;
        if (!StringUtils.isEmpty(request.getStatus())) {
            status = taskStatusService.getTaskStatus(request.getStatus());
        }
        if (!StringUtils.isEmpty(request.getLogin())) {
            author = userService.getUserByLogin(request.getLogin());
        }
        log.debug("Create example<> with status={}, author={}", status, author);
        return Example.of(new TaskEntity(status, author));
    }
}
