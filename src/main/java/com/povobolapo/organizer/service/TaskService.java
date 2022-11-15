package com.povobolapo.organizer.service;

import com.povobolapo.organizer.controller.model.task.TaskRequestBody;
import com.povobolapo.organizer.controller.model.task.TaskSearchRequest;
import com.povobolapo.organizer.exception.NotFoundException;
import com.povobolapo.organizer.mapper.TaskMapper;
import com.povobolapo.organizer.model.DictTaskStatus;
import com.povobolapo.organizer.model.TaskEntity;
import com.povobolapo.organizer.model.UserEntity;
import com.povobolapo.organizer.repository.TaskRepository;
import com.povobolapo.organizer.utils.TaskSpecifications;
import com.povobolapo.organizer.utils.TemplateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;


@Service
@Scope("singleton")
public class TaskService {
    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final TaskStatusService taskStatusService;
    private final UserService userService;
    private final TaskMapper taskMapper;
    private final NotificationService notificationService;

    private final static String NOTIFICATION_PARTICIPANT_ADDED =  "Вы добавлены участником в задачу " + TemplateConverter.TASK_NAME_TEMPLATE_PARAMETER;
    private final static String NOTIFICATION_TASK_UPDATED =  "Задача " + TemplateConverter.TASK_NAME_TEMPLATE_PARAMETER + " была обновлена!";

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskStatusService taskStatusService,
                       UserService userService, TaskMapper taskMapper, NotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.taskStatusService = taskStatusService;
        this.userService = userService;
        this.taskMapper = taskMapper;
        this.notificationService = notificationService;
    }

    @Transactional
    public List<TaskEntity> getAllTasks(TaskSearchRequest request) {
        log.info("Searching for allTasks (request={})", request);
        // Тут собирается тело запроса для получения страницы
        // Дефолтные значения: страница 0, размер 10, сортировка ASC по столбцу id.
        PageRequest pagesRequest = PageRequest.of(request.getPage(), request.getSize(),
                Sort.by(Sort.Direction.fromString(request.getSort()), request.getSortBy()));

        Specification<TaskEntity> spec = createSpecForSearch(request);
        Page<TaskEntity> found = taskRepository.findAll(spec, pagesRequest);
        log.debug("Found next values: " + found.getContent());
        return found.getContent();
    }

    @Transactional
    public TaskEntity getTaskById(String id) {
        return taskRepository.findById(id).get();
    }

    @Transactional
    public TaskEntity create(TaskRequestBody taskRequest) throws AuthenticationException {
        UserEntity authorUser = userService.getCurrentUser();
        TaskEntity task = taskMapper.toEntity(taskRequest);
        task.setAuthor(authorUser);
        DictTaskStatus status;
        if (task.getDictTaskStatus() == null) {
            status = taskStatusService.getTaskStatus("new");
        } else {
            status = taskStatusService.getTaskStatus(taskRequest.getStatus());
        }
        task.setDictTaskStatus(status);
        task.setCreationDate(new Date());
        if (!taskRequest.getParticipants().isEmpty()) {
            Set<UserEntity> party = taskRequest.getParticipants().stream()
                    .map(userService::getUserByLogin).collect(Collectors.toSet());
            task.setParticipants(party);
        }
        notificationService.createTaskNotification(task, NOTIFICATION_PARTICIPANT_ADDED);
        return taskRepository.save(task);
    }

    @Transactional
    public void update(TaskRequestBody taskRequest) throws AuthenticationException {
        Optional<TaskEntity> baseTask = taskRepository.findById(taskRequest.getId());
        if (baseTask.isEmpty()) {
            throw new NotFoundException("Task with id [" + taskRequest.getId() + "] not found.");
        }
        if (!UserAuthoritiesService.canModifyUserData(baseTask.get().getAuthor().getLogin())) {
            throw new AuthenticationException("Нельзя редактировать чужую задачу.");
        }

        Set<UserEntity> oldParticipants = baseTask.get().getParticipants();
        TaskEntity task = baseTask.get();

        if (taskRequest.getName() != null) {
            task.setName(taskRequest.getName());
        }
        if (taskRequest.getDescription() != null) {
            task.setDescription(taskRequest.getDescription());
        }
        if (taskRequest.getStatus() != null) {
            DictTaskStatus status = taskStatusService.getTaskStatus(taskRequest.getStatus());
            if (status == null) {
                throw new NotFoundException("Wrong task status " + taskRequest.getStatus());
            }
            task.setDictTaskStatus(status);
        }
        if (taskRequest.getDeadline() != null) {
            task.setDeadline(taskRequest.getDeadline());
        }
        if (!taskRequest.getParticipants().isEmpty()) {
            Set<UserEntity> party = taskRequest.getParticipants().stream()
                    .map(userService::getUserByLogin).collect(Collectors.toSet());
            task.setParticipants(party);
        }

        taskRepository.save(task);

        for (UserEntity userEntity:task.getParticipants()){
            notificationService.createTaskNotification(userEntity, task, oldParticipants.contains(userEntity) ? NOTIFICATION_TASK_UPDATED : NOTIFICATION_PARTICIPANT_ADDED);
        }
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

    private Specification<TaskEntity> createSpecForSearch(TaskSearchRequest request) {
        Specification<TaskEntity> spec = where(null);
        if (request.getAuthor() != null) {
            spec = spec.and(TaskSpecifications.hasAuthor(request.getAuthor()));
        }
        if (request.getStatus() != null) {
            spec = spec.and(TaskSpecifications.hasStatus(request.getStatus()));
        }
        if (!request.getParticipants().isEmpty()) {
            spec = spec.and(TaskSpecifications.hasParticipants(request.getParticipants()));
        }
        return spec;
    }
}
