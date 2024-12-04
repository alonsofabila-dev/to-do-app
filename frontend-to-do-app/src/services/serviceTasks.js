import api from './api.js';


export const getFilteredToDos = (page = 0, content = "", priority = "", dueDate = "", isCompleted = "", sortPriorityDirection = "", sortDueDateDirection = "") => {
    return api.get(`/tasks?page=${page}&content=${content}&priority=${priority}&dueDate=${dueDate}&isCompleted=${isCompleted}&sortPriorityDirection=${sortPriorityDirection}&sortDueDateDirection=${sortDueDateDirection}`);
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

export const updateTodo = (taskId, content, dueDate, priority) => {
    return api.put(`/tasks/${taskId}`, {
        content: content,
        dueDate: dueDate,
        priority: priority
    })
}

export const getAverages = () => {
    return api.get(`/tasks/average-times`);
}

export const deleteTodo = (taskId) => {
    return api.delete(`/tasks/${taskId}`);
}