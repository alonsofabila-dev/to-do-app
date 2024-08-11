package com.encoramx.backendtodoapp.persistance;


import com.encoramx.backendtodoapp.entities.Task;
import com.encoramx.backendtodoapp.entities.TaskPair;

import java.util.LinkedList;
import java.util.Map;


public interface IDAOTask {
    TaskPair<LinkedList<Task>, Integer> findTasks(int page);
    Map<String, Object> averageDoneTimePerPriority();
    Task findById(int id);
    void createTask(Task task);
    void updateTask(int id, String content, String dueDate, String priority);
    void updateCompleted(int id, boolean isCompleted);
}
