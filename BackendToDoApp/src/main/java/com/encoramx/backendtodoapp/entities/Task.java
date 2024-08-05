package com.encoramx.backendtodoapp.entities;

import java.time.LocalDateTime;


public class Task {

    private static int count = 1;

    private final int id;
    private String content;
    private LocalDateTime dueDate;
    private boolean isCompleted;
    private Priority priority;
    private final LocalDateTime creationDate;


    public Task() {
        this.id = count++;
        this.creationDate = LocalDateTime.now();
    }

    public Task(String content, Priority priority, boolean isCompleted, LocalDateTime dueDate) {
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
        if (content.length() > 120) {
            throw new IllegalArgumentException("Content exceeds maximum 120 characters");
        }
        this.content = content;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
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
                ", dueDate='" + (dueDate != null ? dueDate : "") + '\'' +
                ", completed='" + isCompleted + '\'' +
                ", priority='" + priority + '\'' +
                ", creationDate='" + creationDate + '\'' +
                '}';
    }


    public enum Priority {
        LOW, MEDIUM, HIGH
    }

}
