package com.encoramx.backendtodoapp.persistance;


import com.encoramx.backendtodoapp.entities.Task;

import org.springframework.stereotype.Repository;

import java.util.LinkedList;


@Repository
public class DAOTask implements IDAOTask {

    private final LinkedList<Task> tasksList = new LinkedList<>();

    @Override
    public LinkedList<Task> findAll() {
        return null;
    }


    /**
     * @param newTask (Object, required): Task to be inserted in the List
     */
    @Override
    public void createTask(Task newTask) {
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
