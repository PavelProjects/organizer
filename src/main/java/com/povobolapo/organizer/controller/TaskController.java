package com.povobolapo.organizer.controller;

import com.povobolapo.organizer.controller.model.TaskInfoResponse;
import com.povobolapo.organizer.controller.model.TaskRequestBody;
import com.povobolapo.organizer.controller.model.TaskSearchRequest;
import com.povobolapo.organizer.service.TaskService;
import com.povobolapo.organizer.model.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController()
@RequestMapping(value = "/task")
public class TaskController {
    private final TaskService taskService;
    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    //todo Pageable improve probably????
    @GetMapping("/all")
    public List<TaskInfoResponse> getAllTasks(@RequestBody(required = false) TaskSearchRequest request) {
        log.debug("GET-request: getAllTasks (request={})", request);
        if (request == null) {
            request = new TaskSearchRequest();
        }
        return taskService.getAllTasks(request).stream()
                .map(TaskInfoResponse::new).collect(Collectors.toList());
    }

    @GetMapping
    public TaskEntity getTaskById(
            @RequestParam @Positive String id) {
        log.debug("GET-request: getTaskById (id={})", id);
        return taskService.getTaskById(id);
    }

    @GetMapping("/info")
    public TaskInfoResponse getTaskInfoById(
            @RequestParam @Positive String id) {
        log.debug("GET-request: getTaskInfoById (id={})", id);
        return new TaskInfoResponse(taskService.getTaskById(id));
    }

    @PostMapping("/create")
    public TaskInfoResponse createTask(@RequestBody TaskRequestBody task) {
        //todo PASS LOGIN FROM AUTH
        return new TaskInfoResponse(taskService.createNewTask(task.getAuthor(), task));
    }

    @PutMapping
    public TaskInfoResponse updateTask(@RequestBody TaskRequestBody task) {
        log.debug("GET-request: updateTask: (id={})", task.getId());
        Objects.requireNonNull(task.getId(), "ID can't be NULL in update-method!");
        return new TaskInfoResponse(taskService.updateTask(task));
    }

    @DeleteMapping("/delete")
    public boolean deleteTaskById(@RequestParam String id) {
        log.debug("DELETE-request: deleteTaskById (id={})", id);
        return taskService.deleteTaskById(id);
    }
}
