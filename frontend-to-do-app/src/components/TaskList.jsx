import { Modal, Space, Table } from "antd";
import PropTypes from "prop-types";
import { PRIORITY_OPTIONS, TOAST_ERROR_STYLE, TOAST_SUCCESS_STYLE } from "../constants.js";
import { useState } from "react";
import { deleteTodo, updateTodo } from "../services/serviceTasks.js";
import { toast } from "react-hot-toast";
import { useContext } from "react";
import { TaskListContext } from "./TaskListContext.jsx"; // Import the context

export function TaskList({ tasks, onCheckboxChange }) {
    const [open, setOpen] = useState(false);
    const [confirmLoading, setConfirmLoading] = useState(false);
    const [selectedTask, setSelectedTask] = useState(null);

    const { refreshTasks, sorting, setSorting } = useContext(TaskListContext);

    const deleteTask = (task) => {
        deleteTodo(task.id).then(response => {
            refreshTasks();
            toast.success(response.data, TOAST_SUCCESS_STYLE);
        }).catch((error) => {
            toast.error(error.message, TOAST_ERROR_STYLE);
        })
    }

    const showModal = (task) => {
        setSelectedTask(task);
        setOpen(true);
    };

    const handleOk = () => {
        if (selectedTask) {
            const { id, content, dueDate, priority } = selectedTask;
            setConfirmLoading(true);
            updateTodo(id, content, dueDate, priority)
                .then(response => {
                    setTimeout(() => {
                        handleCancel();
                        toast.success(response.data, TOAST_SUCCESS_STYLE);
                    }, 250);
                    if (response.status === 200) {
                        refreshTasks();
                    }
                })
                .catch(error => {
                    handleCancel();
                    toast.error(error.message, TOAST_ERROR_STYLE);
                })
        }
    };

    const handleCancel = () => {
        setConfirmLoading(false);
        setOpen(false);
    };

    const handlePrioritySort = () => {
        let newDirection;
        if (sorting.sortPriorityDirection === "asc") {
            newDirection = "desc";
        } else if (sorting.sortPriorityDirection === "desc") {
            newDirection = "";
        } else {
            newDirection = "asc";
        }
        setSorting({ ...sorting, sortPriorityDirection: newDirection, sortDueDateDirection: '' });
        refreshTasks();
    };

    const handleDueDateSort = () => {
        let newDirection;
        if (sorting.sortDueDateDirection === "asc") {
            newDirection = "desc";
        } else if (sorting.sortDueDateDirection === "desc") {
            newDirection = "";
        } else {
            newDirection = "asc";
        }
        setSorting({ ...sorting, sortDueDateDirection: newDirection, sortPriorityDirection: '' });
        refreshTasks();
    };

    const getSortIndicator = (direction) => {
        switch (direction) {
            case 'asc':
                return '▲';
            case 'desc':
                return '▼';
            case '':
                return '—';
            default:
                return '';
        }
    };

    const columns = [
        {
            title: <div className="text-center">Done</div>,
            dataIndex: 'completed',
            key: 'completed',
            render: (completed, record) => (
                <div className="flex items-center justify-center">
                    <input
                        type="checkbox"
                        className="w-4 h-4"
                        checked={completed}
                        onChange={(e) => onCheckboxChange(record.id, e.target.checked)}
                    />
                </div>
            ),
        },
        {
            title: 'Content',
            dataIndex: 'content',
            key: 'content',
            render: (content, record) => (
                <span className={record.completed ? 'line-through text-gray-500' : ''}>
                    {content}
                </span>
            ),
        },
        {
            title: <a onClick={handlePrioritySort}>Priority {getSortIndicator(sorting.sortPriorityDirection)}</a>,
            dataIndex: 'priority',
            key: 'priority',
        },
        {
            title: <a onClick={handleDueDateSort}>Due Date {getSortIndicator(sorting.sortDueDateDirection)}</a>,
            dataIndex: 'dueDate',
            key: 'dueDate',
        },
        {
            title: <div className="text-center">Actions</div>,
            key: 'actions',
            render: (record) => (
                <Space size="middle" className="flex items-center justify-center">
                    <button
                        className="bg-blue-400 hover:bg-blue-600 text-white py-2 px-4 rounded"
                        onClick={() => showModal(record)}
                    >
                        Edit
                    </button>
                    <button
                        className="bg-red-400 hover:bg-red-600 text-white py-2 px-4 rounded"
                        onClick={() => deleteTask(record)}
                    >
                        Delete
                    </button>
                </Space>
            ),
        },
    ];

    return (
        <div className="bg-white rounded-lg shadow-md p-4">
            {tasks.length > 0 ? (
                <Table dataSource={tasks} columns={columns} rowKey="id" pagination={false} />
            ) : (
                <div className="flex items-center justify-center">
                    <div className="text-center">
                        <h2 className="text-2xl font-bold">It seems you don&apos;t have To-Do&apos;s</h2>
                        <h3 className="text-lg text-gray-600">Start creating some of them</h3>
                    </div>
                </div>
            )}

            <Modal
                title="Edit Task"
                open={open}
                onOk={handleOk}
                confirmLoading={confirmLoading}
                onCancel={handleCancel}
            >
                <form className="space-y-6">
                    <div>
                        <label htmlFor="content">Content</label>
                        <div>
                            <textarea
                                id="content"
                                value={selectedTask?.content || ""}
                                required={true}
                                maxLength={120}
                                onChange={(e) => setSelectedTask({ ...selectedTask, content: e.target.value })}
                                className="block w-full rounded-md border-0 py-2 px-3 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                            />
                        </div>
                    </div>

                    <div>
                        <label htmlFor="dueDate">Due Date</label>
                        <div>
                            <input
                                id="dueDate"
                                type="date"
                                value={selectedTask?.dueDate || ""}
                                required={false}
                                onChange={(e) => setSelectedTask({ ...selectedTask, dueDate: e.target.value })}
                                className="block w-full rounded-md border-0 py-2 px-3 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                            />
                        </div>
                    </div>

                    <div>
                        <label htmlFor="task-priority">Priority</label>
                        <div>
                            <select
                                id="task-priority"
                                value={selectedTask?.priority || ""}
                                required={true}
                                onChange={(e) => setSelectedTask({ ...selectedTask, priority: e.target.value })}
                                className="block w-full rounded-md border-0 py-2 px-3 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                            >
                                {PRIORITY_OPTIONS.map((option) => (
                                    <option key={option.value} value={option.label}>
                                        {option.label}
                                    </option>
                                ))}
                            </select>
                        </div>
                    </div>
                </form>
            </Modal>
        </div>
    );
}

TaskList.propTypes = {
    tasks: PropTypes.arrayOf(
        PropTypes.shape({
            id: PropTypes.number.isRequired,
            content: PropTypes.string.isRequired,
            priority: PropTypes.string,
            completed: PropTypes.bool.isRequired,
            dueDate: PropTypes.string,
            creationDate: PropTypes.string,
        })
    ).isRequired,
    onCheckboxChange: PropTypes.func.isRequired,
};