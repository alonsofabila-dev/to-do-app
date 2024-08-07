package com.encoramx.backendtodoapp.services;


import com.encoramx.backendtodoapp.entities.Task;
import com.encoramx.backendtodoapp.persistance.DAOTask;

import org.springframework.stereotype.Service;

import java.util.LinkedList;


@Service
public class TaskService {

    private final DAOTask daoTask;

    public TaskService(DAOTask daoTask) {
        this.daoTask = daoTask;
    }

    public LinkedList<Task> getTasks(int page) {
        return daoTask.findTasks(page);
    }

    public Task getTaskById(int id) {
        return daoTask.findById(id);
    }

    public void createTask(Task task) {
        daoTask.createTask(task);
    }

    public void updateTask(int id, String content, String dueDate, String priority) {
        daoTask.updateTask(id, content, dueDate, priority);
    }

    public void updateCompletedTask(int id, boolean isCompleted) {
        daoTask.updateCompleted(id, isCompleted);
    }
}
