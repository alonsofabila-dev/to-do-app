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
     * @return ResponseEntity with status code 201 if the task is created successfully,
     * status code 400 if the task is null or missing priority
     * or status code 409 if there's a conflict.
     */
    @PostMapping
    public ResponseEntity<String> createTask(@RequestBody Task task) {
        try {
            if (task == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task cannot be null");
            }
            if (task.getPriority() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing priority");
            }
            taskService.createTask(task);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Location", "/tasks/" + task.getId())
                    .body("Task created successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
