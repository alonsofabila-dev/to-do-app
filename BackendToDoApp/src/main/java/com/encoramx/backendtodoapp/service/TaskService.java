package com.encoramx.backendtodoapp.service;


import com.encoramx.backendtodoapp.dto.AverageTimesDTO;
import com.encoramx.backendtodoapp.entity.Task;
import com.encoramx.backendtodoapp.entity.TaskPair;
import com.encoramx.backendtodoapp.repository.DAOTask;

import org.springframework.stereotype.Service;

import java.time.Duration;
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

    public TaskPair<LinkedList<Task>, Integer> getTasks(int page, String content, String dueDate, String priority, Boolean isCompleted, String sortPriorityDirection, String sortDueDateDirection) {
        LinkedList<Task> tasksList = daoTask.findTasks();

        Stream<Task> filteredStream = filterTasks(tasksList, content, dueDate, priority, isCompleted);

        LinkedList<Task> filteredTasks = filteredStream.collect(Collectors.toCollection(LinkedList::new));

        sortTasks(filteredTasks, sortPriorityDirection, sortDueDateDirection);

        LinkedList<Task> paginatedTasks = paginateTasks(filteredTasks, page);

        return new TaskPair<>(paginatedTasks, filteredTasks.size());
    }

    public AverageTimesDTO calculateAverageCompletionTimes() {
        LinkedList<Task> tasksList = daoTask.averageDoneTimePerPriority();

        double totalAverage;
        double lowAverage;
        double mediumAverage;
        double highAverage;

        Map<Task.Priority, Double> averages = tasksList.stream()
                .collect(Collectors.groupingBy(Task::getPriority,
                        Collectors.averagingDouble(task -> Duration.between(task.getCreationDate(), task.getDoneDate()).toMinutes())));

        totalAverage = tasksList.stream()
                .mapToDouble(task -> Duration.between(task.getCreationDate(), task.getDoneDate()).toMinutes())
                .average()
                .orElse(0.0);

        lowAverage = averages.getOrDefault(Task.Priority.LOW, 0.0);
        mediumAverage = averages.getOrDefault(Task.Priority.MEDIUM, 0.0);
        highAverage = averages.getOrDefault(Task.Priority.HIGH, 0.0);

        return new AverageTimesDTO(totalAverage, lowAverage, mediumAverage, highAverage);
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

    private Stream<Task> filterTasks(LinkedList<Task> tasks, String content, String dueDate, String priority, Boolean isCompleted) {
        Stream<Task> stream = tasks.stream();
        if (content != null && !content.isEmpty()) {
            stream = stream.filter(task -> task.getContent().contains(content));
        }
        if (dueDate != null && !dueDate.isEmpty()) {
            stream = stream.filter(task -> task.getDueDate() != null && task.getDueDate().equals(LocalDate.parse(dueDate)));
        }
        if (priority != null && !priority.isEmpty()) {
            stream = stream.filter(task -> task.getPriority().equals(Task.Priority.valueOf(priority)));
        }
        if (isCompleted != null) {
            stream = stream.filter(task -> task.isCompleted() == isCompleted);
        }
        return stream;
    }


    private void sortTasks(LinkedList<Task> tasks, String sortPriorityDirection, String sortDueDateDirection) {
        if (sortPriorityDirection != null && !sortPriorityDirection.isEmpty()) {
            Comparator<Task> priorityComparator = Comparator.comparing(Task::getPriority);
            if (sortPriorityDirection.equals("desc")) {
                priorityComparator = priorityComparator.reversed();
            }
            tasks.sort(priorityComparator);
        }

        if (sortDueDateDirection != null && !sortDueDateDirection.isEmpty()) {
            Comparator<Task> dueDateComparator = Comparator.comparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder()));
            if (sortDueDateDirection.equals("desc")) {
                dueDateComparator = Comparator.comparing(Task::getDueDate, Comparator.nullsLast(Comparator.reverseOrder()));
            }
            tasks.sort(dueDateComparator);
        }
    }


    private LinkedList<Task> paginateTasks(LinkedList<Task> tasks, int page) {
        return tasks.stream()
                .skip((long) page * 10)
                .limit(10)
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
