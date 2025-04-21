import React from 'react';
import { render, screen } from '@testing-library/react';
import Player from './Player';

// Mock the icons to keep the test lightweight
jest.mock('./Icons', () => ({
    PlayIcon: () => <div>PlayIcon</div>,
    SkipBackIcon: () => <div>SkipBackIcon</div>,
    SkipForwardIcon: () => <div>SkipForwardIcon</div>,
    RepeatIcon: () => <div>RepeatIcon</div>,
    ShuffleIcon: () => <div>ShuffleIcon</div>,
    HeartIcon: () => <div>HeartIcon</div>,
    VolumeIcon: () => <div>VolumeIcon</div>,
    MaximizeIcon: () => <div>MaximizeIcon</div>,
}));

describe('Player', () => {
    const mockSong = {
        title: 'Test Song',
        artist: 'Test Artist',
        duration: '3:45',
    };

    it('renders current song title, artist, and duration', () => {
        render(<Player currentSong={mockSong} />);

        // Check if song title and artist are rendered
        expect(screen.getByText('Test Song')).toBeInTheDocument();
        expect(screen.getByText('Test Artist')).toBeInTheDocument();

        // Check if duration is rendered
        expect(screen.getByText('3:45')).toBeInTheDocument();
    });

    it('renders control buttons and icons', () => {
        render(<Player currentSong={mockSong} />);

        // Check if the icons are rendered (using mocked ones)
        expect(screen.getByText('PlayIcon')).toBeInTheDocument();
        expect(screen.getByText('SkipBackIcon')).toBeInTheDocument();
        expect(screen.getByText('SkipForwardIcon')).toBeInTheDocument();
        expect(screen.getByText('RepeatIcon')).toBeInTheDocument();
        expect(screen.getByText('ShuffleIcon')).toBeInTheDocument();
        expect(screen.getByText('HeartIcon')).toBeInTheDocument();
        expect(screen.getByText('VolumeIcon')).toBeInTheDocument();
        expect(screen.getByText('MaximizeIcon')).toBeInTheDocument();
    });

    it('shows the first letter of the song title in the song cover', () => {
        render(<Player currentSong={mockSong} />);

        // Check that the first letter of title ("T" for "Test Song") is shown in the song cover
        expect(screen.getByText('T')).toBeInTheDocument();
    });
});
