import { createContext, useEffect, useState } from 'react';
import { getAverages, getFilteredToDos } from "../services/serviceTasks.js";
import { toast } from "react-hot-toast";
import { TOAST_ERROR_STYLE } from "../constants.js";
import PropTypes from "prop-types";

export const TaskListContext = createContext();

export const TaskListProvider = ({ children }) => {
    const [tasks, setTasks] = useState([]);
    const [totalTasks, setTotalTasks] = useState(0);
    const [filters, setFilters] = useState({ number: 1, content: '', priority: '', dueDate: '', isCompleted: '' });
    const [sorting, setSorting] = useState({ sortPriorityDirection: '', sortDueDateDirection: '' });
    const [averages, setAverages] = useState({});

    useEffect(() => {
        refreshTasks();
    }, [filters, sorting]);

    const refreshTasks = () => {
        const { number, content, priority, dueDate, isCompleted } = filters;
        const { sortPriorityDirection, sortDueDateDirection } = sorting;

        getFilteredToDos(
            number - 1,
            content,
            priority,
            dueDate,
            isCompleted,
            sortPriorityDirection,
            sortDueDateDirection
        ).then(response => {
            setTasks(response.data.tasks);
            setTotalTasks(response.data.totalSize);
            average();
        }).catch(error => {
            toast.error(error.message, TOAST_ERROR_STYLE);
        });
    };

    const average = () => {
        getAverages().then(response => {
            setAverages(response.data);
        }).catch(error => {
            toast.error(error.message, TOAST_ERROR_STYLE);
        });
    };

    return (
        <TaskListContext.Provider value={{ tasks, setTasks, totalTasks, setTotalTasks, filters, setFilters, sorting, setSorting, averages, setAverages, refreshTasks }}>
            {children}
        </TaskListContext.Provider>
    );
};

TaskListProvider.propTypes = {
    children: PropTypes.node.isRequired,
};
