package com.encoramx.backendtodoapp.persistance;


import com.encoramx.backendtodoapp.entities.Task;

import java.util.LinkedList;


public interface IDAOTask {
    LinkedList<Task> findTasks();
    LinkedList<Task> averageDoneTimePerPriority();
    Task findById(int id);
    void createTask(Task task);
    void updateTask(int id, String content, String dueDate, String priority);
    void updateCompleted(int id, boolean isCompleted);
    void deleteTask(int id);
}
