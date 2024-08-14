import { render, screen } from "@testing-library/react";
import { AveragesCard } from "../AveragesCard.jsx";
import { describe, it, expect } from 'vitest';

describe('AveragesCard Component', () => {
    it('renders without crashing', () => {
        const averages = {
            totalAverage: 5.75,
            lowAverage: 3.45,
            mediumAverage: 4.67,
            highAverage: 6.89,
        };

        render(<AveragesCard averages={averages} />);

        expect(screen.getByText(/Average time to finish tasks:/i)).toBeInTheDocument();
        expect(screen.getByText(/Average time to finish tasks by priority:/i)).toBeInTheDocument();

        expect(screen.getByText(/5.75 minutes/i)).toBeInTheDocument();
        expect(screen.getByText(/Low: 3.45 minutes/i)).toBeInTheDocument();
        expect(screen.getByText(/Medium: 4.67 minutes/i)).toBeInTheDocument();
        expect(screen.getByText(/High: 6.89 minutes/i)).toBeInTheDocument();
    });

    it('displays default values when no averages are provided', () => {
        render(<AveragesCard averages={{}} />);

        expect(screen.getByText(/Average time to finish tasks:/i)).toBeInTheDocument();
        expect(screen.getByText(/Average time to finish tasks by priority:/i)).toBeInTheDocument();

        expect(screen.getAllByText(/0:00 minutes/i).length).toBe(4);
    });
});
