import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import SongRow from './SongRow';

describe('SongRow', () => {
    const mockProps = {
        index: 1,
        title: 'Test Song',
        artist: 'Test Artist',
        duration: '3:20',
        isActive: false,
        onClick: jest.fn(),
    };

    it('renders song details correctly', () => {
        render(<SongRow {...mockProps} />);

        expect(screen.getByText('1')).toBeInTheDocument(); // index
        expect(screen.getByText('T')).toBeInTheDocument(); // first letter of title
        expect(screen.getByText('Test Song')).toBeInTheDocument(); // full title
        expect(screen.getByText('Test Artist')).toBeInTheDocument(); // artist
        expect(screen.getByText('3:20')).toBeInTheDocument(); // duration
    });

    it('calls onClick when clicked', () => {
        render(<SongRow {...mockProps} />);

        const row = screen.getByText('Test Song').closest('.song-row');
        fireEvent.click(row);

        expect(mockProps.onClick).toHaveBeenCalledTimes(1);
    });

    it('adds "active" class when isActive is true', () => {
        render(<SongRow {...mockProps} isActive={true} />);

        const row = screen.getByText('Test Song').closest('.song-row');
        expect(row).toHaveClass('active');
    });

    it('does not add "active" class when isActive is false', () => {
        render(<SongRow {...mockProps} isActive={false} />);

        const row = screen.getByText('Test Song').closest('.song-row');
        expect(row).not.toHaveClass('active');
    });
});
