package com.povobolapo.organizer.controller;

import com.povobolapo.organizer.controller.models.TaskRequestBody;
import com.povobolapo.organizer.service.TaskService;
import com.povobolapo.organizer.model.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;


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
    public List<TaskEntity> getAllTasks(
            @RequestParam(defaultValue = "0", required = false) @Positive Integer page,
            @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        log.debug("GET-request: getAllTasks (page={}; size={})", page, size);
        return taskService.getAllTasks(page, size);
    }

    @GetMapping
    public TaskEntity getTaskById(
            @RequestParam @Positive Integer id) {
        log.debug("GET-request: getTaskById (id={})", id);
        return taskService.getTaskById(id);
    }

    @PostMapping("/create")
    public TaskEntity createTask(@RequestBody TaskRequestBody task) {
        //todo PASS LOGIN FROM AUTH
        return taskService.createNewTask(task.getAuthor(), task);
    }

    @DeleteMapping("/delete")
    public boolean deleteTaskById(@RequestParam Integer id) {
        log.debug("DELETE-request: deleteTaskById (id={})", id);
        return taskService.deleteTaskById(id);
    }
}
