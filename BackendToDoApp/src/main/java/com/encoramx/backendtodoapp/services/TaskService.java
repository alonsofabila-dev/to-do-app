package com.encoramx.backendtodoapp.services;


import com.encoramx.backendtodoapp.entities.Task;
import com.encoramx.backendtodoapp.entities.TaskPair;
import com.encoramx.backendtodoapp.persistance.DAOTask;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class TaskService {

    private final DAOTask daoTask;

    public TaskService(DAOTask daoTask) {
        this.daoTask = daoTask;
    }

    // public TaskPair<LinkedList<Task>, Integer> getTasks(int page, String content, String dueDate, String priority, Boolean isCompleted, String sortPriorityDirection, String sortDueDateDirection) {
    //     return daoTask.findTasks(page, content, dueDate, priority, isCompleted, sortPriorityDirection, sortDueDateDirection);
    // }

    public TaskPair<LinkedList<Task>, Integer> getTasks(int page, String content, String dueDate, String priority, Boolean isCompleted, String sortPriorityDirection, String sortDueDateDirection) {

        LinkedList<Task> tasksList = daoTask.findTasks();

        Stream<Task> streamList = tasksList.stream();

        if (content != null && !content.isEmpty()) {
            streamList = streamList.filter(task -> task.getContent().contains(content));
        }
        if (dueDate != null && !dueDate.isEmpty()) {
            streamList = streamList.filter(task -> task.getDueDate() != null && task.getDueDate().equals(LocalDate.parse(dueDate)));
        }
        if (priority != null && !priority.isEmpty()) {
            streamList = streamList.filter(task -> task.getPriority().equals(Task.Priority.valueOf(priority)));
        }
        if (isCompleted != null) {
            streamList = streamList.filter(task -> task.isCompleted() == isCompleted);
        }

        LinkedList<Task> filteredTasks = streamList.collect(Collectors.toCollection(LinkedList::new));

        if (sortPriorityDirection != null && !sortPriorityDirection.isEmpty()) {
            if (sortPriorityDirection.equals("asc")) {
                filteredTasks.sort(Comparator.comparing(Task::getPriority));
            } else if (sortPriorityDirection.equals("desc")) {
                filteredTasks.sort(Comparator.comparing(Task::getPriority).reversed());
            }
        }

        if (sortDueDateDirection != null && !sortDueDateDirection.isEmpty()) {
            if (sortDueDateDirection.equals("asc")) {
                filteredTasks.sort(Comparator.comparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())));
            } else if (sortDueDateDirection.equals("desc")) {
                filteredTasks.sort(Comparator.comparing(Task::getDueDate, Comparator.nullsLast(Comparator.reverseOrder())));
            }
        }

        LinkedList<Task> sublist = filteredTasks.stream()
                .skip((long) page * 10)
                .limit(10)
                .collect(Collectors.toCollection(LinkedList::new));

        return new TaskPair<>(sublist, filteredTasks.size());
    }

    public Map<String, Object> calculateAverageCompletionTimes() {
        return daoTask.averageDoneTimePerPriority();
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

    public void deleteTask(int id) {
        daoTask.deleteTask(id);
    }
}
