package com.encoramx.backendtodoapp.dto;

public record UpdateTaskDTO(
        String content,
        String dueDate,
        String priority
) {
}
