package com.encoramx.backendtodoapp.controllers;


import com.encoramx.backendtodoapp.entities.Task;
import com.encoramx.backendtodoapp.services.TaskService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    private TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * @param task (Object, Required) Task to be created
     * @return status code 201 if created, otherwise status code 209 existing pk conflict.
     */
    @PostMapping
    public ResponseEntity<String> createTask(@RequestBody Task task) {
        try {
            taskService.createTask(task);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Location", "/tasks/" + task.getId())
                    .body("Task created successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
