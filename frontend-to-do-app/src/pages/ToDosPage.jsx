import { Modal, Pagination } from 'antd';
import { PRIORITY_OPTIONS, TOAST_ERROR_STYLE, TOAST_SUCCESS_STYLE } from "../constants.js";
import { useEffect, useState } from "react";
import { TaskList } from "../components/TaskList.jsx";
import { toast } from "react-hot-toast";
import { createTodo, updateComplete, getAverages, getFilteredToDos } from "../services/serviceTasks.js";
import { AveragesCard } from "../components/AveragesCard.jsx";
import { SearchTaskForm } from "../components/SearchTaskForm.jsx";


export function ToDosPage() {
    const [open, setOpen] = useState(false);
    const [confirmLoading, setConfirmLoading] = useState(false);
    const [content, setContent] = useState("");
    const [dueDate, setDueDate] = useState("");
    const [priority, setPriority] = useState("LOW");
    const [isCompleted, setIsCompleted] = useState("");

    const [tasks, setTasks] = useState([])
    const [number, setNumber] = useState(1)
    const [totalTasks, setTotalTasks] = useState()
    const [averages, setAverages] = useState()

    useEffect(() => {
        getFilteredTasks();
    }, [number]);

    const getFilteredTasks = (content, priority, dueDate, isCompleted, sortPriorityDirection = "", sortDueDateDirection = "") => {
        setIsCompleted(isCompleted)
        getFilteredToDos(number - 1, content, priority, dueDate, isCompleted, sortPriorityDirection, sortDueDateDirection).then(response => {
            setTasks(response.data.tasks);
            setTotalTasks(response.data.totalSize);
            average();
        }).catch(error => {
            toast.error(error.message);
        })
    }

    const handleCheckboxChange = (taskId, completed) => {
        updateComplete(taskId, completed).then(() => {
            getFilteredTasks(content, priority, dueDate, isCompleted);
        }).catch(error => {
            toast.error(error.message, TOAST_ERROR_STYLE);
        });
    };

    const average = () => {
        getAverages().then(response => {
            setAverages(response.data)
        }).catch(error => {
            toast.error(error.message);
        })
    }

    const showModal = () => {
        setOpen(true);
    };

    const handleOk = () => {
        setConfirmLoading(true);
        createTodo(content, dueDate, priority).then(response => {
            setTimeout(() => {
                handleCancel()
                toast.success(response.data, TOAST_SUCCESS_STYLE);
            }, 250);
            if (response.status === 201) {
                getFilteredTasks()
            }
        }).catch(() => {
            setConfirmLoading(false);
            setOpen(false);
            toast.error('Missing Content', TOAST_ERROR_STYLE);
        })
    };

    const handleCancel = () => {
        setContent("");
        setDueDate("");
        setPriority("LOW");
        setConfirmLoading(false);
        setOpen(false);
    };


    return (
        <div className="p-2">

            <SearchTaskForm filteredSearch={getFilteredTasks}/>

            <div className="mb-3">
                <button className="bg-green-700 hover:bg-green-800 text-white py-2 px-4 rounded text-sm" onClick={showModal}>
                    New To Do
                </button>
                <Modal
                    title="Create New To Do"
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

            <TaskList tasks={tasks} onCheckboxChange={handleCheckboxChange} refreshTasks={getFilteredTasks}/>

            <div className="mt-3 flex justify-center">
                <div className="bg-white rounded-lg shadow-md p-2">
                    <Pagination
                        align="center"
                        current={number}
                        total={totalTasks}
                        pageSize={10}
                        onChange={(page) => setNumber(page)}
                    />
                </div>
            </div>

            <AveragesCard averages={averages} />
        </div>
    )
}
