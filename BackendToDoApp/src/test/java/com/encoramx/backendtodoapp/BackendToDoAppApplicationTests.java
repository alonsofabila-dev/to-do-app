package com.encoramx.backendtodoapp;

import com.encoramx.backendtodoapp.entities.Task;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class BackendToDoAppApplicationTests {

	@Test
	void shouldCreateNewTaskObject() {
		Task task = new Task();
		task.setContent("Hello World");
		task.setPriority(Task.Priority.HIGH);
		task.setCompleted(false);
		task.setDueDate(LocalDateTime.of(2024, 8, 5, 8, 41, 59));

		assertThat(task.getId()).isGreaterThan(0);
		assertThat(task.getContent()).isEqualTo("Hello World");
		assertThat(task.getPriority()).isEqualTo(Task.Priority.HIGH);
		assertThat(task.isCompleted()).isEqualTo(false);
		assertThat(task.getDueDate()).isEqualTo(LocalDateTime.of(2024, 8, 5, 8, 41, 59));
		assertThat(task.getCreationDate()).isNotNull();
	}


	@Test
	void shouldCreateNewTaskObjectWithoutDueDate() {
		Task task = new Task();
		task.setContent("Hello World");
		task.setPriority(Task.Priority.HIGH);
		task.setCompleted(false);

		assertThat(task.getId()).isGreaterThan(0);
		assertThat(task.getContent()).isEqualTo("Hello World");
		assertThat(task.getPriority()).isEqualTo(Task.Priority.HIGH);
		assertThat(task.isCompleted()).isEqualTo(false);
		assertThat(task.getCreationDate()).isNotNull();
	}
}
