import { render, screen } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import { TaskList } from '../TaskList';
import { TaskListContext } from '../TaskListContext';

Object.defineProperty(window, 'matchMedia', {
    writable: true,
    value: (query) => ({
        matches: false,
        media: query,
        onchange: null,
        addListener: () => {},
        removeListener: () => {},
        addEventListener: () => {},
        removeEventListener: () => {},
        dispatchEvent: () => {},
    }),
});

describe('TaskList Component', () => {
    const mockTasks = [
        { id: 1, content: 'Task 1', priority: 'High', completed: false, dueDate: '2024-08-20' },
        { id: 2, content: 'Task 2', priority: 'Low', completed: true, dueDate: '2024-08-22' },
    ];

    const mockRefreshTasks = vi.fn();
    const mockSorting = { sortPriorityDirection: '', sortDueDateDirection: '' };
    const mockSetSorting = vi.fn();

    const renderComponent = () =>
        render(
            <TaskListContext.Provider value={{ refreshTasks: mockRefreshTasks, sorting: mockSorting, setSorting: mockSetSorting }}>
                <TaskList tasks={mockTasks} onCheckboxChange={() => {}} />
            </TaskListContext.Provider>
        );

    it('renders without crashing', () => {
        renderComponent();
        const task1 = screen.getByText(/Task 1/i);
        expect(task1).toBeInTheDocument();

        const task2 = screen.getByText(/Task 2/i);
        expect(task2).toBeInTheDocument();
    });

    it('displays a message when there are no tasks', () => {
        render(
            <TaskListContext.Provider value={{ refreshTasks: mockRefreshTasks, sorting: mockSorting, setSorting: mockSetSorting }}>
                <TaskList tasks={[]} onCheckboxChange={() => {}} />
            </TaskListContext.Provider>
        );
        const message = screen.queryByText(/Start creating some of them/i);
        expect(message).toBeInTheDocument();
    });
});
