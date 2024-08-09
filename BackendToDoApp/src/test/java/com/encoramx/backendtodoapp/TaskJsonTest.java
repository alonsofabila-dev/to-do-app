package com.encoramx.backendtodoapp;


import com.encoramx.backendtodoapp.entities.Task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDate;

import java.util.LinkedList;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
public class TaskJsonTest {

    @Autowired
    private JacksonTester<Task> json;

    @Autowired
    private JacksonTester<LinkedList<Task>> jsonList;

    private LinkedList<Task> tasks;


    @BeforeEach
    void setUp() {
        tasks = new LinkedList<>();
        tasks.add(new Task("task 1", Task.Priority.HIGH, true, LocalDate.of(2024, 8, 5)));
        tasks.add(new Task("task 2", Task.Priority.MEDIUM, false));
        tasks.add(new Task("task 3", Task.Priority.LOW, false, LocalDate.of(2024, 9, 5)));
    }


    @Test
    void taskSerializationTest() throws IOException {
        Task task = tasks.getFirst();

        assertThat(json.write(task)).hasJsonPath("$.id");
        assertThat(json.write(task)).hasJsonPath("$.content");
        assertThat(json.write(task)).hasJsonPath("$.dueDate");
        assertThat(json.write(task)).hasJsonPath("$.priority");
        assertThat(json.write(task)).hasJsonPath("$.completed");
        assertThat(json.write(task)).hasJsonPath("$.creationDate");

        assertThat(json.write(task)).extractingJsonPathNumberValue("$.id").isNotNull();
        assertThat(json.write(task)).extractingJsonPathStringValue("$.content").isEqualTo("task 1");
        assertThat(json.write(task)).extractingJsonPathStringValue("$.dueDate").isNotNull();
        assertThat(json.write(task)).extractingJsonPathStringValue("$.priority").isEqualTo("HIGH");
        assertThat(json.write(task)).extractingJsonPathBooleanValue("$.completed").isEqualTo(true);
        assertThat(json.write(task)).extractingJsonPathStringValue("$.creationDate").isNotNull();
    }


    @Test
    void taskDeserializationTest() throws IOException {
        String expected = """
            {
               "content":"task 1",
               "dueDate":"2024-08-05",
               "priority":"HIGH",
               "completed":true
            }
            """;

        Task task = json.parseObject(expected);

        assertThat(task.getId()).isGreaterThan(0);
        assertThat(task.getContent()).isEqualTo("task 1");
        assertThat(task.getDueDate())
                .isEqualTo(LocalDate.of(2024, 8, 5));
        assertThat(task.getPriority()).isEqualTo(Task.Priority.HIGH);
        assertThat(task.isCompleted()).isEqualTo(true);
        assertThat(task.getCreationDate()).isNotNull(); // This will still check if creationDate is set
    }


    @Test
    void taskListSerializationTest() throws IOException {
        JsonContent<LinkedList<Task>> jsonContent = jsonList.write(tasks);

        assertThat(jsonContent).hasJsonPath("$[0].id");
        assertThat(jsonContent).hasJsonPath("$[1].id");
        assertThat(jsonContent).hasJsonPath("$[2].id");
        assertThat(jsonContent).hasJsonPath("$[0].dueDate");
        assertThat(jsonContent).hasJsonPath("$[1].dueDate");
        assertThat(jsonContent).hasJsonPath("$[2].dueDate");

        assertThat(jsonContent).extractingJsonPathStringValue("$[0].dueDate").isNotNull();
        assertThat(jsonContent).extractingJsonPathStringValue("$[1].dueDate").isNull();
        assertThat(jsonContent).extractingJsonPathStringValue("$[2].dueDate").isNotNull();
    }


    @Test
    void taskListDeserializationTest() throws IOException {
        String expected = """
        [
            {"content": "task 1","dueDate": "2024-08-05","completed": true,"priority": "HIGH"},
            {"content": "task 2","dueDate": "","completed": false,"priority": "MEDIUM"},
            {"content": "task 3","dueDate": "2024-09-05","completed": false,"priority": "LOW"}
        ]
        """;

        LinkedList<Task> tasks = jsonList.parseObject(expected);

        assertThat(tasks.getFirst().getContent()).isEqualTo("task 1");
        assertThat(tasks.get(1).getContent()).isEqualTo("task 2");
        assertThat(tasks.getLast().getContent()).isEqualTo("task 3");
        assertThat(tasks.getFirst().getDueDate()).isNotNull();
        assertThat(tasks.get(1).getDueDate()).isNull();
        assertThat(tasks.getLast().getDueDate()).isNotNull();
    }
}
