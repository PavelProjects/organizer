package com.povobolapo.organizer.controller;

import com.povobolapo.organizer.dao.service.TTaskService;
import com.povobolapo.organizer.model.TTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController()
@RequestMapping(value = "/task")
public class TaskController {
    private TTaskService tTaskService;

    @Autowired
    public TaskController(TTaskService tTaskService) {
        this.tTaskService = tTaskService;
    }

    @PostMapping("/create")
    public TTask createTask(@RequestBody TTask task){
        return tTaskService.createNewTask(task);
    }
}
