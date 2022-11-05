package com.povobolapo.organizer.service;

import com.povobolapo.organizer.controller.model.task.TaskRequestBody;
import com.povobolapo.organizer.controller.model.task.TaskSearchRequest;
import com.povobolapo.organizer.exception.NotFoundException;
import com.povobolapo.organizer.model.DictTaskStatus;
import com.povobolapo.organizer.model.TaskEntity;
import com.povobolapo.organizer.model.UserEntity;
import com.povobolapo.organizer.repository.TaskRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Scope("singleton")
public class TaskService {
    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final TaskStatusService taskStatusService;
    private final UserService userService;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskStatusService taskStatusService, UserService userService) {
        this.taskRepository = taskRepository;
        this.taskStatusService = taskStatusService;
        this.userService = userService;
    }

    @Transactional
    public List<TaskEntity> getAllTasks(TaskSearchRequest request) {
        log.info("Searching for allTasks (request={})", request);
        // Тут собирается тело запроса для получения страницы
        // Дефолтные значения: страница 0, размер 10, сортировка ASC по столбцу id.
        PageRequest pagesRequest = PageRequest.of(request.getPage(), request.getSize(),
                Sort.by(Sort.Direction.fromString(request.getSort()), request.getSortBy()));
        //Тут собирается пример запроса, если требуется сортировка по автору и/или статусу
        Example<TaskEntity> example = createExampleForSearch(request);
        //Получаем страницу из БД
        Page<TaskEntity> found = taskRepository.findAll(example, pagesRequest);
        log.debug("Found next values: " + found.getContent());
        return found.getContent();
    }

    @Transactional
    public TaskEntity getTaskById(String id) {
        return taskRepository.findById(id).get();
    }

    @Transactional
    public TaskEntity createNewTask(TaskEntity task) throws AuthenticationException {
        DictTaskStatus status = taskStatusService.getTaskStatus("new");
        UserEntity authorUser = userService.getCurrentUser();
        task.setAuthor(authorUser);
        task.setDictTaskStatus(status);
        task.setCreationDate(new Date());
        return taskRepository.save(task);
    }

    //TODO авторизация, чтобы обновлял только автор/админ
    //TODO рефакторинг, надо унифицировать или упростить апдейт, чтобы обновлялись только notnull поля
    // TODO @vola юзай UserService.getCurrentUser() для получения авторизованного юзера
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
            if (status != null) baseTask.get().setDictTaskStatus(status);
        }
        if (taskRequest.getName() != null) {
            baseTask.get().setName(taskRequest.getName());
        }
        return taskRepository.save(baseTask.get());
    }

    @Transactional
    public boolean deleteTaskById(String id) {
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
