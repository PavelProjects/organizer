package com.povobolapo.organizer.controller;

import com.povobolapo.organizer.dao.impl.TTaskService;
import com.povobolapo.organizer.model.TTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController("/task")
public class TaskController {
    private TTaskService tTaskService;

    @Autowired
    public TaskController(TTaskService tTaskService) {
        this.tTaskService = tTaskService;
    }

    @PostMapping("/create")
    public TTask createTask(@RequestBody TTask task){
        return tTaskService.createTask(task);
    }
}
