import React from 'react';
import { render, screen } from '@testing-library/react';
import AlbumCard from './AlbumCard';

// Mock the PlayIcon to keep the test light
jest.mock('./Icons', () => ({
    PlayIcon: () => <div>PlayIcon</div>,
}));

describe('AlbumCard', () => {
    const mockProps = {
        title: 'Test Album',
        subtitle: 'Test Artist',
        index: 1,
    };

    it('renders album title, subtitle, and index correctly', () => {
        render(<AlbumCard {...mockProps} />);

        // Album title
        expect(screen.getByText('Test Album')).toBeInTheDocument();

        // Album subtitle
        expect(screen.getByText('Test Artist')).toBeInTheDocument();

        // Album cover shows first letter of title + index
        expect(screen.getByText('T1')).toBeInTheDocument();
    });

    it('renders the play button with PlayIcon', () => {
        render(<AlbumCard {...mockProps} />);

        expect(screen.getByText('PlayIcon')).toBeInTheDocument();
    });
});
