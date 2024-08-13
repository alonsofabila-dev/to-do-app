import { useContext, useState } from "react";
import { Modal, Pagination } from 'antd';
import { PRIORITY_OPTIONS, TOAST_ERROR_STYLE, TOAST_SUCCESS_STYLE } from "../constants.js";
import { toast } from "react-hot-toast";
import { createTodo, updateComplete } from "../services/serviceTasks.js";
import { TaskList } from "../components/TaskList.jsx";
import { AveragesCard } from "../components/AveragesCard.jsx";
import { SearchTaskForm } from "../components/SearchTaskForm.jsx";
import { TaskListContext } from "../components/TaskListContext.jsx";

export function ToDosPage() {
    const {
        tasks,
        totalTasks,
        filters,
        setFilters,
        refreshTasks,
        averages
    } = useContext(TaskListContext);

    const [open, setOpen] = useState(false);
    const [content, setContent] = useState("");
    const [dueDate, setDueDate] = useState("");
    const [priority, setPriority] = useState("LOW");

    const handleCheckboxChange = (taskId, completed) => {
        updateComplete(taskId, completed).then(() => {
            refreshTasks();
        }).catch(error => {
            toast.error(error.message, TOAST_ERROR_STYLE);
        });
    };

    const showModal = () => {
        setOpen(true);
    };

    const handleOk = () => {
        createTodo(content, dueDate, priority).then(response => {
            setTimeout(() => {
                handleCancel();
                toast.success(response.data, TOAST_SUCCESS_STYLE);
            }, 250);
            if (response.status === 201) {
                refreshTasks();
            }
        }).catch(() => {
            setOpen(false);
            toast.error('Missing Content', TOAST_ERROR_STYLE);
        });
    };

    const handleCancel = () => {
        setContent("");
        setDueDate("");
        setPriority("LOW");
        setOpen(false);
    };

    return (
        <div className="p-2">
            <SearchTaskForm />

            <div className="mb-3">
                <button className="bg-green-700 hover:bg-green-800 text-white py-2 px-4 rounded text-sm" onClick={showModal}>
                    New To Do
                </button>
                <Modal
                    title="Create New To Do"
                    open={open}
                    onOk={handleOk}
                    onCancel={handleCancel}
                >
                    <form className="space-y-6">
                        <div>
                            <label htmlFor="content">Content</label>
                            <div>
                                <textarea
                                    id="content"
                                    value={content}
                                    required={true}
                                    maxLength={120}
                                    onChange={(e) => setContent(e.target.value)}
                                    className="block w-full rounded-md border-0 py-2 px-3 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                ></textarea>
                            </div>
                        </div>

                        <div>
                            <label htmlFor="dueDate">Due Date</label>
                            <div>
                                <input
                                    id="dueDate"
                                    type="date"
                                    value={dueDate}
                                    required={false}
                                    onChange={(e) => setDueDate(e.target.value)}
                                    className="block w-full rounded-md border-0 py-2 px-3 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                ></input>
                            </div>
                        </div>

                        <div>
                            <label htmlFor="priority">Priority</label>
                            <div>
                                <select
                                    id="priority"
                                    value={priority}
                                    required={true}
                                    onChange={(e) => setPriority(e.target.value)}
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

            <TaskList tasks={tasks} onCheckboxChange={handleCheckboxChange} refreshTasks={refreshTasks} />

            <div className="mt-3 flex justify-center">
                <div className="bg-white rounded-lg shadow-md p-2">
                    <Pagination
                        align="center"
                        current={filters.number}
                        total={totalTasks}
                        pageSize={10}
                        onChange={(page) => setFilters(prevFilters => ({ ...prevFilters, number: page }))}
                    />
                </div>
            </div>

            <AveragesCard averages={averages} />
        </div>
    );
}
