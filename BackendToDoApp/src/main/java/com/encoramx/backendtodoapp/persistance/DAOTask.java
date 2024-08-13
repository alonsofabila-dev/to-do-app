package com.encoramx.backendtodoapp.persistance;


import com.encoramx.backendtodoapp.entities.Task;

import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Map;


@Repository
public class DAOTask implements IDAOTask {

    private final LinkedList<Task> tasksList = new LinkedList<>();


    /**
     * @return (List) List of task.
     */
    @Override
    public LinkedList<Task> findTasks() {
        return new LinkedList<>(tasksList);
    }


    /**
     * @return dict with the average time for task to be done by priority.
     */
    @Override
    public Map<String, Object> averageDoneTimePerPriority() {
        // get the difference between creation date and done date, ans get average.
        Double totalAverage = tasksList.stream()
                .mapToDouble(task -> Duration.between(task.getCreationDate(), task.getDoneDate()).toMinutes())
                .average()
                .orElse(0.0);

        // filter by priority, then get the difference between creation date and done date, ans get average.
        Double lowAverage = tasksList.stream()
                .filter(task -> Task.Priority.LOW.equals(task.getPriority()))
                .mapToDouble(task -> Duration.between(task.getCreationDate(), task.getDoneDate()).toMinutes())
                .average()
                .orElse(0.0);

        Double mediumAverage = tasksList.stream()
                .filter(task -> Task.Priority.MEDIUM.equals(task.getPriority()))
                .mapToDouble(task -> Duration.between(task.getCreationDate(), task.getDoneDate()).toMinutes())
                .average()
                .orElse(0.0);

        Double highAverage = tasksList.stream()
                .filter(task -> Task.Priority.HIGH.equals(task.getPriority()))
                .mapToDouble(task -> Duration.between(task.getCreationDate(), task.getDoneDate()).toMinutes())
                .average()
                .orElse(0.0);

        return Map.of(
                "totalAverage", totalAverage,
                "lowAverage", lowAverage,
                "mediumAverage", mediumAverage,
                "highAverage", highAverage
        );
    }


    /**
     * @param id (int, Required) ID to search in the list.
     * @return (Object) The task with the specified ID.
     */
    @Override
    public Task findById(int id) {
        return getTask(id);
    }


    /**
     * @param newTask (Object, required) Task to be inserted in the List.
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


    /**
     * @param id (int, Required) ID of the task to be updated.
     * @param content (String, Required) new content change.
     * @param dueDate (String, Optional) new dueDate to change or keep the same one.
     * @param priority (String, Required) new priority for the task.
     */
    @Override
    public void updateTask(int id, String content, String dueDate, String priority) {
        Task task = getTask(id);
        task.setContent(content);
        task.setPriority(Task.Priority.valueOf(priority));
        if (dueDate != null && !dueDate.isEmpty()) {
            task.setDueDate(LocalDate.parse(dueDate));
        } else {
            task.setDueDate(null);
        }
    }


    /**
     * @param id (int, Required) ID of the task to be updated.
     * @param isCompleted (boolean, Required) status to be changed on the task.
     */
    @Override
    public void updateCompleted(int id, boolean isCompleted) {
        Task task = getTask(id);
        if (isCompleted) {
            task.setDoneDate(LocalDateTime.now());
            task.setCompleted(isCompleted);
        } else {
            task.setDoneDate(task.getCreationDate());
            task.setCompleted(isCompleted);
        }
    }

    /**
     * @param id (int, Required) ID of the task to be deleted.
     */
    @Override
    public void deleteTask(int id) {
        Task task = getTask(id);
        tasksList.remove(task);
    }


    public Task getTask(int id) {
        // search fos task with existing ID.
        return tasksList.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
