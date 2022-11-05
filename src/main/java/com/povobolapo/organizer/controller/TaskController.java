package com.povobolapo.organizer.controller;

import com.povobolapo.organizer.controller.model.TaskDto;
import com.povobolapo.organizer.controller.model.TaskRequestBody;
import com.povobolapo.organizer.controller.model.TaskSearchRequest;
import com.povobolapo.organizer.mapper.TaskMapper;
import com.povobolapo.organizer.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController()
@RequestMapping(value = "/task")
public class TaskController {
    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

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
                .map(taskMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/info")
    public TaskDto getTaskInfoById(
            @RequestParam String id) {
        log.debug("GET-request: getTaskInfoById (id={})", id);
        return taskMapper.toDto(taskService.getTaskById(id));
    }

    @PostMapping("/create")
    public TaskDto createTask(@RequestBody TaskRequestBody task) throws AuthenticationException {
        return taskMapper.toDto(taskService.createNewTask(taskMapper.toEntity(task)));
    }

    @PutMapping("/update")
    public TaskDto updateTask(@RequestBody TaskRequestBody task) {
        log.debug("GET-request: updateTask: (id={})", task.getId());
        Objects.requireNonNull(task.getId(), "ID can't be NULL in update-method!");
        return taskMapper.toDto(taskService.updateTask(task));
    }

    @DeleteMapping("/delete")
    public boolean deleteTaskById(@RequestParam String id) {
        log.debug("DELETE-request: deleteTaskById (id={})", id);
        return taskService.deleteTaskById(id);
    }
}
