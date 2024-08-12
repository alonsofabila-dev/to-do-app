import PropTypes from "prop-types";
import { PRIORITY_OPTIONS } from "../constants.js";
import { useState } from "react";


export function SearchTaskForm({ filteredSearch }) {
    const [content, setContent] = useState("");
    const [priority, setPriority] = useState("");
    const [dueDate, setDueDate] = useState("");
    const [isCompleted, setIsCompleted] = useState("");

    const handleSubmit = (e) => {
        e.preventDefault(); // Prevent the default form submission behavior
        filteredSearch(content, priority, dueDate, isCompleted);
    };

    return (
        <div className="flex justify-center">
            <div className="bg-white rounded-lg shadow-md p-6 w-full max-w-7xl">
                <form onSubmit={handleSubmit} className="flex space-x-4 items-center">
                    <div className="flex flex-col w-1/3">
                        <label htmlFor="content" className="mb-1">Content</label>
                        <input
                            id="content"
                            type="text"
                            value={content}
                            required={false}
                            onChange={(e) => setContent(e.target.value)}
                            maxLength={120}
                            className="block w-full rounded-md border-0 py-2 px-3 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                        />
                    </div>

                    <div className="flex flex-col w-1/3">
                        <label htmlFor="priority" className="mb-1">Priority</label>
                        <select
                            id="priority"
                            value={priority}
                            onChange={(e) => setPriority(e.target.value)}
                            required={false}
                            className="block w-full rounded-md border-0 py-2 px-3 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                        >
                            <option key="All" value="">
                                All
                            </option>
                            {PRIORITY_OPTIONS.map((option) => (
                                <option key={option.value} value={option.label}>
                                    {option.label}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="flex flex-col w-1/3">
                        <label htmlFor="isCompleted" className="mb-1">Is Completed</label>
                        <select
                            id="isCompleted"
                            value={isCompleted}
                            onChange={(e) => setIsCompleted(e.target.value)}
                            required={false}
                            className="block w-full rounded-md border-0 py-2 px-3 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                        >
                            <option key="All" value="">All</option>
                            <option key="Done" value="true">Done</option>
                            <option key="Undone" value="false">Undone</option>
                        </select>
                    </div>

                    <div className="flex flex-col w-1/3">
                    <label htmlFor="dueDate" className="mb-1">Due Date</label>
                        <input
                            id="dueDate"
                            type="date"
                            value={dueDate}
                            onChange={(e) => setDueDate(e.target.value)}
                            required={false}
                            className="block w-full rounded-md border-0 py-2 px-3 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                        />
                    </div>

                    <button
                        type="submit"
                        className="ml-4 inline-flex items-center bg-blue-400 hover:bg-blue-600 text-white py-2 px-4 rounded text-sm font-medium shadow-sm"
                    >
                        Search
                    </button>
                </form>
            </div>
        </div>
    );
}

SearchTaskForm.propTypes = {
    filteredSearch: PropTypes.func.isRequired
}