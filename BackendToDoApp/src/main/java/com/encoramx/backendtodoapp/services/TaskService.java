package com.encoramx.backendtodoapp.services;


import com.encoramx.backendtodoapp.entities.Task;
import com.encoramx.backendtodoapp.persistance.DAOTask;

import org.springframework.stereotype.Service;


@Service
public class TaskService {

    private final DAOTask daoTask;

    public TaskService(DAOTask daoTask) {
        this.daoTask = daoTask;
    }

    public void createTask(Task task) {
        daoTask.createTask(task);
    }
}
