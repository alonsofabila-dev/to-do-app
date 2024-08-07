package com.encoramx.backendtodoapp.controllers;


import com.encoramx.backendtodoapp.entities.Task;
import com.encoramx.backendtodoapp.services.TaskService;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.Map;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    private TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    /**
     * @param page (int, optional) The page number to retrieve.
     * @return Response with status code 200 and a list of task with length of 10.
     */
    @GetMapping
    public ResponseEntity<LinkedList<Task>> getAllTasks(
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {

        try {
            if (page < 0) {
                logger.warn("Invalid page number");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

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
            if (requestedId <= 0) {
                logger.warn("Invalid Id");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

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
                logger.warn("Task is null or task priority is null");
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


    /**
     * @param requestedId (int, Required) the ID of the task to be updated.
     * @param updatedTask (Map, Required) new details of the task.
     * @return Response with status code 200.
     */
    @PutMapping("/{requestedId}")
    public ResponseEntity<String> updateTask(@PathVariable int requestedId, @RequestBody Map<String, Object> updatedTask) {

        try {
            if (requestedId <= 0) {
                logger.warn("Invalid Id");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            String content = (String) updatedTask.get("content");
            String dueDate = (String) updatedTask.get("dueDate");
            String priority = (String) updatedTask.get("priority");

            if (content == null || priority == null) {
                logger.warn("Task is null");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            taskService.updateTask(requestedId, content, dueDate, priority);

            return ResponseEntity.status(HttpStatus.OK).body("Task updated successfully");
        } catch (Exception e) {
            logger.error("Error updating task: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating task");
        }

    }


    /**
     * @param requestedId (int, Required) the ID of the task to be updated.
     * @param isCompleted (Map, Required) new status of the task.
     * @return Response with status code 200.
     */
    @PatchMapping("/{requestedId}")
    public ResponseEntity<String> updateTaskCompletion(@PathVariable int requestedId, @RequestBody Map<String, Boolean> isCompleted) {
        try {
            if (requestedId <= 0) {
                logger.warn("Invalid Id");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            boolean completed = isCompleted.get("isCompleted");
            taskService.updateCompletedTask(requestedId, completed);

            return ResponseEntity.status(HttpStatus.OK).body("Task updated successfully");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating task");
        }

    }
}
