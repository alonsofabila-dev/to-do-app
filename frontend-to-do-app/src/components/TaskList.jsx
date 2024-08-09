import api from "../api/api.js";
import {TOAST_ERROR_STYLE, TOAST_SUCCESS_STYLE} from "../constants.js";
import {useEffect, useState} from "react";
import {toast} from "react-hot-toast";
import {Space, Table} from "antd";


export function TaskList() {
    const [tasks, setTasks] = useState([])
    const [number, setNumber] = useState(0)

    useEffect(() => {
        getTasks();
    }, [number]);

    const getTasks = () => {
        api.get(`/tasks?page=${number}`).then(response => {

            setTasks(response.data);

        }).catch(error => {
            toast.error(error.message, TOAST_SUCCESS_STYLE);
        })
    }

    const handleCheckboxChange = (taskId, completed) => {
        api.patch(`/tasks/${taskId}`, { isCompleted: completed }).then(response => {
            getTasks();
            console.log(response.data);
        }).catch(error => {
            toast.error(error.message, TOAST_ERROR_STYLE);
        });
    }

    const formatDate = (dateToFormat) => {
        if (!dateToFormat) {
            return dateToFormat
        }
        return new Date(dateToFormat).toLocaleDateString('en-CA');
    }

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
                        onChange={(e) => handleCheckboxChange(record.id, e.target.checked)}
                    />
                </div>
            ),
        },
        {
            title: 'Name',
            dataIndex: 'content',
            key: 'content',
            render: (content, record) => (
                <span className={record.completed ? 'line-through text-gray-500' : ''}>
                {content}
            </span>
            ),
        },
        {
            title: 'Priority',
            dataIndex: 'priority',
            key: 'priority',
        },
        {
            title: 'Due To',
            dataIndex: 'dueDate',
            key: 'dueDate',
            render: (dueDate) => formatDate(dueDate)
        },
        {
            title: <div className="text-center">Actions</div>,
            key: 'a',
            render: () => (
                <Space size="middle" className="flex items-center justify-center">
                    <button className="bg-blue-400 hover:bg-blue-600 text-white py-2 px-4 rounded">
                        Edit
                    </button>
                    <button className="bg-red-400 hover:bg-red-600 text-white py-2 px-4 rounded">
                        Delete
                    </button>
                </Space>),
        },
    ];

    return (
        <div className="bg-white rounded-lg shadow-md p-4">
            {tasks.length > 0 ?
                <Table dataSource={tasks} columns={columns} pagination={false}  />
                :
                <div className="flex items-center justify-center">
                    <div className="text-center">
                        <h2 className="text-2xl font-bold">It seems you don&apos;t have To-Do&apos;s</h2>
                        <h3 className="text-lg text-gray-600">Start creating some of them</h3>
                    </div>
                </div>
            }
        </div>
    )
}