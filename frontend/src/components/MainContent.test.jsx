import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import MainContent from './MainContent';

// Mock the child components to make the test focus only on MainContent behavior
jest.mock('./Header', () => () => <div>Header</div>);
jest.mock('./AlbumCard', () => ({ title }) => <div>{title}</div>);
jest.mock('./SongRow', () => ({ title, onClick }) => (
    <div onClick={onClick}>{title}</div>
));

describe('MainContent', () => {
    it('calls setCurrentSong with correct song data when a SongRow is clicked', () => {
        const mockSetCurrentSong = jest.fn();

        render(<MainContent setCurrentSong={mockSetCurrentSong} />);

        // Find the first song (it should be 'Song 1')
        const songElement = screen.getByText('Song 1');

        // Click the first song
        fireEvent.click(songElement);

        // Expect the function to have been called once with the correct data
        expect(mockSetCurrentSong).toHaveBeenCalledTimes(1);
        expect(mockSetCurrentSong).toHaveBeenCalledWith({
            title: 'Song 1',
            artist: 'Artist 1',
            album: 'Album 1',
            duration: '3:20',
        });
    });
});
