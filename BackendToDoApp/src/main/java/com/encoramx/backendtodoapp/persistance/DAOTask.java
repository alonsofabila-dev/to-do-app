package com.encoramx.backendtodoapp.persistance;


import com.encoramx.backendtodoapp.entities.Task;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.stream.Collectors;


@Repository
public class DAOTask implements IDAOTask {

    private final LinkedList<Task> tasksList = new LinkedList<>();


    /**
     * @return (List) List of task with length of 10.
     */
    @Override
    public LinkedList<Task> findTasks(int page) {
        // Send a copy of the list limited to 10  depending on the page.
        return tasksList.stream()
                .skip((long) page * 10)
                .limit(10)
                .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * @param id (int, Required) ID to search in the list.
     * @return (Object) The task with the specified ID.
     */
    @Override
    public Task findById(int id) {
        // Search for task with existing id.
        return getTask(id);
    }


    /**
     * @param newTask (Object, required) Task to be inserted in the List.
     */
    @Override
    public void createTask(Task newTask) {
        if (newTask.getContent().length() > 120) {
            throw new IllegalArgumentException("Content exceeds maximum 120 characters");
        }
        // Check for existing Id before insertion of newTask.
        if (tasksList.stream().anyMatch(task -> task.getId() == newTask.getId())) {
            throw new IllegalArgumentException("Task already exists");
        }
        tasksList.add(newTask);
    }


    /**
     * @param id (int, Required) ID of the task to be updated.
     * @param content (String, Required) new content change.
     * @param dueDate (String, Optional) new dueDate to change or keep the same one.
     * @param priority (String, Required) new priority for the task.
     */
    @Override
    public void updateTask(int id, String content, String dueDate, String priority) {
        Task task = getTask(id);
        task.setContent(content);
        task.setPriority(Task.Priority.valueOf(priority));
        if (!dueDate.isEmpty()) {
            task.setDueDate(LocalDate.parse(dueDate));
        }
        task.setDueDate(null);
    }


    /**
     * @param id (int, Required) ID of the task to be updated.
     * @param isCompleted (boolean, Required) status to be changed on the task.
     */
    @Override
    public void updateCompleted(int id, boolean isCompleted) {
        Task task = getTask(id);
        if (isCompleted) {
            task.setDoneDate(LocalDateTime.now());
        } else {
            task.setDoneDate(task.getCreationDate());
            task.setCompleted(isCompleted);
        }
    }


    public Task getTask(int id) {
        return tasksList.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
