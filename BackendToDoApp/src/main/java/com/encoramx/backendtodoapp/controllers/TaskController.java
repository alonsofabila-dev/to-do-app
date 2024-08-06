package com.encoramx.backendtodoapp.controllers;


import com.encoramx.backendtodoapp.entities.Task;
import com.encoramx.backendtodoapp.services.TaskService;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    private TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    /**
     * @param page (int, optional): The page number to retrieve.
     * @return Response with status code 200 and a list of task with length of 10.
     */
    @GetMapping
    public ResponseEntity<LinkedList<Task>> getAllTasks(
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        try {
            LinkedList<Task> tasks = taskService.getTasks(page);
            return ResponseEntity.status(HttpStatus.OK).body(tasks);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * @param requestedId (int, Required): The ID of the task to be requested.
     * @return Response with status code 200 and the task requested.
     */
    @GetMapping("/{requestedId}")
    public ResponseEntity<Task> getTask(@PathVariable int requestedId) {
        try {
            Task task = taskService.getTaskById(requestedId);
            if (task != null) {
                return ResponseEntity.status(HttpStatus.OK).body(task);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * @param task (Object, Required) Task to be created.
     * @return Response with status code 201 if the task is created successfully,
     * status code 400 if the task is null or missing priority
     * or status code 409 if there's a conflict.
     */
    @PostMapping
    public ResponseEntity<String> createTask(@RequestBody Task task) {
        try {
            if (task == null || task.getPriority() == null) {
                logger.debug("Task is null or task priority is null");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request");
            }
            taskService.createTask(task);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Location", "/tasks/" + task.getId())
                    .body("Task created successfully");
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Invalid request");
        }
    }
}
