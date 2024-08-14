import { render, screen, fireEvent } from '@testing-library/react';
import { TaskListProvider } from '../TaskListContext';
import { SearchTaskForm } from '../SearchTaskForm.jsx';
import { describe, it, expect } from 'vitest';


describe('SearchTaskForm Component', () => {

    const renderWithMockContext = () => {
        render(
            <TaskListProvider>
                <SearchTaskForm />
            </TaskListProvider>
        );
    };

    it('renders without crashing', () => {
        renderWithMockContext();
        const contentLabel = screen.getByLabelText(/Content/i);
        expect(contentLabel).toBeInTheDocument();
    });

    it('updates content input field correctly', () => {
        renderWithMockContext();
        const contentInput = screen.getByLabelText(/Content/i);
        fireEvent.change(contentInput, { target: { value: 'New Content' } });
        expect(contentInput.value).toBe('New Content');
    });

});
