package com.encoramx.backendtodoapp.persistance;


import com.encoramx.backendtodoapp.entities.Task;
import com.encoramx.backendtodoapp.entities.TaskPair;

import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Repository
public class DAOTask implements IDAOTask {

    private final LinkedList<Task> tasksList = new LinkedList<>();


    /**
     * @return (List) List of task with length of 10.
     */
    @Override
    public TaskPair<LinkedList<Task>, Integer> findTasks(
            int page,
            String content,
            String dueDate,
            String priority,
            Boolean isCompleted,
            String sortPriorityDirection,
            String sortDueDateDirection
    ) {

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

        // Send a copy of the list limited to 10  depending on the page.
        LinkedList<Task> sublist = filteredTasks.stream()
                .skip((long) page * 10)
                .limit(10)
                .collect(Collectors.toCollection(LinkedList::new));

        return new TaskPair<>(new LinkedList<>(sublist), filteredTasks.size());
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
