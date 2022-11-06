package com.povobolapo.organizer.controller;

import com.povobolapo.organizer.controller.groups.OnCreate;
import com.povobolapo.organizer.controller.groups.OnUpdate;
import com.povobolapo.organizer.controller.model.task.TaskDto;
import com.povobolapo.organizer.controller.model.task.TaskRequestBody;
import com.povobolapo.organizer.controller.model.task.TaskSearchRequest;
import com.povobolapo.organizer.mapper.TaskMapper;
import com.povobolapo.organizer.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController()
@RequestMapping(value = "/task")
@Validated
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    //todo Pageable improve probably????
    @GetMapping("/all")
    public List<TaskDto> getAllTasks(@RequestBody(required = false) TaskSearchRequest request) {
        log.debug("GET-request: getAllTasks (request={})", request);
        if (request == null) {
            request = new TaskSearchRequest();
        }
        return taskService.getAllTasks(request).stream()
                .map(taskMapper::toDtoShort).collect(Collectors.toList());
    }

    @GetMapping("/info")
    public TaskDto getTaskInfoById(
            @RequestParam String id) {
        log.debug("GET-request: getTaskInfoById (id={})", id);
        return taskMapper.toDto(taskService.getTaskById(id));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(OnCreate.class)
    public TaskDto createTask(@RequestBody @Valid TaskRequestBody task) throws AuthenticationException {
        log.debug("POST-request: createTask: {}", task);
        return taskMapper.toDto(taskService.create(taskMapper.toEntity(task), task.getParticipants()));
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Validated(OnUpdate.class)
    public TaskDto updateTask(@RequestBody @Valid TaskRequestBody task) {
        log.debug("GET-request: updateTask: (id={})", task.getId());
        return taskMapper.toDto(taskService.update(task));
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteTaskById(@RequestParam String id) {
        log.debug("DELETE-request: deleteTaskById (id={})", id);
        return taskService.deleteTaskById(id);
    }
}
