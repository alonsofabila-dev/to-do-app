package com.encoramx.backendtodoapp.persistance;


import com.encoramx.backendtodoapp.entities.Task;
import com.encoramx.backendtodoapp.entities.TaskPair;

import java.util.LinkedList;


public interface IDAOTask {
    TaskPair<LinkedList<Task>, Integer> findTasks(int page);
    Task findById(int id);
    void createTask(Task task);
    void updateTask(int id, String content, String dueDate, String priority);
    void updateCompleted(int id, boolean isCompleted);
}
