package com.encoramx.backendtodoapp.dto;

public record AverageTimesDTO (
        Double totalAverage,
        Double lowAverage,
        Double mediumAverage,
        Double highAverage
) {
}
