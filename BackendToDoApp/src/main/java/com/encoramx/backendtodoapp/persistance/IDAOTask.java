package com.encoramx.backendtodoapp.persistance;


import com.encoramx.backendtodoapp.entities.Task;

import java.util.LinkedList;


public interface IDAOTask {
    LinkedList<Task> findTasks(int page);
    Task findById(int id);
    void createTask(Task task);
    Task updateTask(Task task);
}
