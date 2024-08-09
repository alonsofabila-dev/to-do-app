import api from "../api/api.js";
import { Modal } from 'antd';
import { PRIORITY_OPTIONS, TOAST_ERROR_STYLE, TOAST_SUCCESS_STYLE } from "../constants.js";
import { useState } from "react";
import { TaskList } from "../components/TaskList.jsx";
import { toast } from "react-hot-toast";

export function ToDosPage() {
    const [open, setOpen] = useState(false);
    const [confirmLoading, setConfirmLoading] = useState(false);
    const [content, setContent] = useState("");
    const [dueDate, setDueDate] = useState("");
    const [priority, setPriority] = useState("");

    const showModal = () => {
        setOpen(true);
    };

    const handleOk = () => {
        setConfirmLoading(true);

        api.post("/tasks", {
            content: content,
            dueDate: dueDate,
            priority: priority
        }).then(response => {
            setTimeout(() => {
                setOpen(false);
                setConfirmLoading(false);
                toast.success(response.data, TOAST_SUCCESS_STYLE);
            }, 1000);
        }).catch(error => {
            toast.error(error.message, TOAST_ERROR_STYLE);
        });

    };

    const handleCancel = () => {
        setContent("");
        setDueDate("");
        setPriority("");
        setConfirmLoading(false);
        setOpen(false);
    };


    return (
        <div className="p-4">

            <div className="mb-4">
                <button className="bg-green-700 hover:bg-green-800 text-white py-2 px-4 rounded" onClick={showModal}>
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
                            <label htmlFor="content">Due Date</label>
                            <div>
                                <input
                                    id="content"
                                    type="date"
                                    value={dueDate}
                                    required={false}
                                    onChange={(e) => setDueDate(e.target.value)}
                                    className="block w-full rounded-md border-0 py-2 px-3 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                ></input>
                            </div>
                        </div>

                        <div>
                            <label htmlFor="task-priority">Status</label>
                            <div>
                                <select
                                    id="task-priority"
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

            <TaskList/>
        </div>
    )
}
