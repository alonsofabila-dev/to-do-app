package com.encoramx.backendtodoapp.persistance;


import com.encoramx.backendtodoapp.entities.Task;

import org.springframework.stereotype.Repository;

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
        return tasksList.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElse(null);
    }


    /**
     * @param newTask (Object, required): Task to be inserted in the List.
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

    @Override
    public Task updateTask(Task task) {
        return null;
    }
}
