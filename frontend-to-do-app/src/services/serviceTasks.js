import api from './api.js';

export const getToDos = (page = 0) => {
    return api.get(`/tasks?page=${page}`);
};

export const updateComplete = (taskId, completed) => {
    return api.patch(`/tasks/${taskId}`, { isCompleted: completed });
}

export const createTodo = (content, dueDate, priority) => {
    return api.post("/tasks", {
        content: content,
        dueDate: dueDate,
        priority: priority
    })
}