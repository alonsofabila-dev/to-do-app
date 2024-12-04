package com.encoramx.backendtodoapp.dto;

import com.encoramx.backendtodoapp.entity.Task;

import java.util.List;

public record TaskResponseDTO (
        List<Task> tasks,
        int totalSize
) {
}
