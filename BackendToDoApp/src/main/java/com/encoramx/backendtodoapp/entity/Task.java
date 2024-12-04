package com.encoramx.backendtodoapp.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class Task {

    private static int counter = 1;

    private final int id;
    private String content;
    private LocalDate dueDate;
    private boolean isCompleted;
    private Priority priority;
    private final LocalDateTime creationDate;
    private LocalDateTime doneDate;


    public Task() {
        this.id = counter++;
        this.creationDate = LocalDateTime.now();
        this.doneDate = LocalDateTime.now();
    }

    public Task(String content, Priority priority, boolean isCompleted, LocalDate dueDate) {
        this();
        this.content = content;
        this.priority = priority;
        this.isCompleted = isCompleted;
        this.dueDate = dueDate;
    }

    public Task(String content, Priority priority, boolean isCompleted) {
        this();
        this.content = content;
        this.priority = priority;
        this.isCompleted = isCompleted;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setDoneDate(LocalDateTime doneDate) {
        this.doneDate = doneDate;
    }

    public LocalDateTime getDoneDate() {
        return doneDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", dueDate='" + (dueDate != null ? dueDate : null) + '\'' +
                ", completed='" + isCompleted + '\'' +
                ", priority='" + priority + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", doneDate='" + (doneDate != null ? doneDate : null) + '\'' +
                '}';
    }


    public enum Priority {
        LOW, MEDIUM, HIGH
    }

}
