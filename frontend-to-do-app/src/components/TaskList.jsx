import api from "../api/api.js";
import { TOAST_ERROR_STYLE, TOAST_SUCCESS_STYLE } from "../constants.js";
import { useEffect, useState } from "react";
import { toast } from "react-hot-toast";
import { List } from 'antd';



export function TaskList() {
    const [tasks, setTasks] = useState([])
    const [number, setNumber] = useState(0)

    useEffect(() => {
        getTasks();
    }, [number]);

    const getTasks = () => {
        api.get(`/tasks?page=${number}`).then(response => {

            setTasks(response.data);
            console.log(response.data);
            setNumber(0);

        }).catch(error => {
            toast.error(error.message, TOAST_SUCCESS_STYLE);
        })
    }

    const handleCheckboxChange = (taskId, completed) => {
        api.patch(`/tasks/${taskId}`, { isCompleted: completed }).then(response => {
            getTasks();
            toast.success(response.data, TOAST_SUCCESS_STYLE);
        }).catch(error => {
            toast.error(error.message, TOAST_ERROR_STYLE);
        })
    }

    return (
        <div>
            <h1>TaskList</h1>
            {tasks.length > 0 ?
                <List
                    dataSource={tasks}
                    renderItem={(task) => (
                        <List.Item>
                            <List.Item.Meta
                                avatar={
                                    <input
                                        type="checkbox"
                                        checked={task.completed}
                                        onChange={(e) => handleCheckboxChange(task.id, e.target.checked)}
                                    />
                                }
                                title={task.content}
                                description={`Due to: ${task.dueDate}`}
                            />
                            <p style={{ marginLeft: '16px', fontWeight: 'bold' }}>{task.priority}</p>
                        </List.Item>
                    )}
                />
                :
                <div>Empty Tasks</div>
            }
        </div>
    )
}