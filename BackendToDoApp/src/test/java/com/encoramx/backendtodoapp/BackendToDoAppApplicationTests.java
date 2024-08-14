package com.encoramx.backendtodoapp;


import com.encoramx.backendtodoapp.entities.Task;

import com.jayway.jsonpath.DocumentContext;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URI;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BackendToDoAppApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;


	@Test
	@DirtiesContext
	void shouldCreateNewTaskObject() {
		Task newTask = new Task(
				"Lorem ipsum dolor sit amet",
				Task.Priority.HIGH,
				false,
				LocalDate.of(2024, 8, 5)
		);

		ResponseEntity<Void> createResponse = restTemplate.postForEntity("/tasks", newTask, Void.class);

		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI locationOfNewTask = createResponse.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewTask, String.class);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		Number id = documentContext.read("$.id");

		assertThat(id).isNotNull();
	}


	@Test
	@DirtiesContext
	void shouldNotCreateNewTaskObjectWithExceedingContent() {
		Task newTask = new Task(
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse nibh urna, " +
						"vestibulum sit amet mauris a, aliquam faucibus orci. Aliquam nec feugiat diam. Vestibulum " +
						"efficitur venenatis justo et convallis. Mauris elementum velit eget metus bibendum, eget" +
						" laoreet lorem lacinia. Cras pellentesque pellentesque tincidunt. Fusce vehicula nec dui" +
						" sit amet varius. Morbi fermentum mollis magna ut ultrices. Vivamus justo massa, malesuada " +
						"pellentesque arcu eu, condimentum consectetur turpis. Praesent quis auctor lacus, id " +
						"elementum quam. Sed rhoncus enim vel dolor sodales dignissim. Phasellus posuere facilisis mi " +
						"eget semper. Duis pharetra ex sem.",
				Task.Priority.HIGH,
				false,
				LocalDate.of(2024, 8, 5)
		);

		ResponseEntity<Void> createResponse = restTemplate.postForEntity("/tasks", newTask, Void.class);

		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
	}


	@Test
	@DirtiesContext
	void shouldReturnASavedTask() {
		Task newTask = new Task(
				"Lorem ipsum dolor sit amet",
				Task.Priority.HIGH,
				false
		);

		ResponseEntity<Void> createResponse = restTemplate.postForEntity("/tasks", newTask, Void.class);
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI locationOfNewTask = createResponse.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewTask, String.class);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		Number id = documentContext.read("$.id");

		assertThat(id).isNotNull();
	}


	@Test
	void shouldNotReturnATaskWithUnknownId() {
		ResponseEntity<String> getResponse = restTemplate.getForEntity("/tasks/1", String.class);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(getResponse.getBody()).isBlank();
	}

	@Test
	void shouldUpdateASavedTask() {
		Task newTask = new Task(
				"Lorem ipsum dolor sit amet",
				Task.Priority.HIGH,
				false
		);

		ResponseEntity<Void> createResponse = restTemplate.postForEntity("/tasks", newTask, Void.class);

		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI locationOfNewTask = createResponse.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewTask, String.class);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		Number id = documentContext.read("$.id");

		Task updatedTask = new Task(
				"Lorem ipsum dolor sit amet",
				Task.Priority.HIGH,
				false,
				LocalDate.of(2024, 8, 5)
		);

		HttpEntity<Task> request = new HttpEntity<>(updatedTask);
		ResponseEntity<Void> response = restTemplate.exchange("/tasks/" + id, HttpMethod.PUT, request, Void.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}


	@Test
	void shouldNotUpdateTaskWithInvalidId() {
		Task newTask = new Task(
				"Lorem ipsum dolor sit amet",
				Task.Priority.HIGH,
				false
		);

		ResponseEntity<Void> createResponse = restTemplate.postForEntity("/tasks", newTask, Void.class);
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI locationOfNewTask = createResponse.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewTask, String.class);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		Number id = documentContext.read("$.id");

		Task uodatedTask = new Task(
				"Lorem ipsum dolor sit amet",
				Task.Priority.HIGH,
				false,
				LocalDate.of(2024, 8, 5)
		);

		HttpEntity<Task> request = new HttpEntity<>(uodatedTask);
		ResponseEntity<Void> response = restTemplate.exchange("/tasks/-" + id, HttpMethod.PUT, request, Void.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}


	@Test
	void shouldNotUpdateAnEmptyTask() {
		Task newTask = new Task(
				"Lorem ipsum dolor sit amet",
				Task.Priority.HIGH,
				false
		);

		ResponseEntity<Void> createResponse = restTemplate.postForEntity("/tasks", newTask, Void.class);
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI locationOfNewTask = createResponse.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewTask, String.class);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		Number id = documentContext.read("$.id");

		Task updatedTask = new Task();

		HttpEntity<Task> request = new HttpEntity<>(updatedTask);
		ResponseEntity<Void> response = restTemplate.exchange("/tasks/" + id, HttpMethod.PUT, request, Void.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void shouldDeleteATask() {
		Task newTask = new Task(
				"Lorem ipsum dolor sit amet",
				Task.Priority.HIGH,
				false
		);

		ResponseEntity<Void> createResponse = restTemplate.postForEntity("/tasks", newTask, Void.class);
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI locationOfNewTask = createResponse.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewTask, String.class);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		Number id = documentContext.read("$.id");

		ResponseEntity<Void> deleteResponse = restTemplate.exchange("/tasks/" + id, HttpMethod.DELETE, null, Void.class);
		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

}
