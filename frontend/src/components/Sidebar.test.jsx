import React from 'react';
import { render, screen } from '@testing-library/react';
import Sidebar from './Sidebar';

// Mock the icons to keep the tests simple
jest.mock('./Icons', () => ({
    HomeIcon: () => <div>HomeIcon</div>,
    SearchIcon: () => <div>SearchIcon</div>,
    LibraryIcon: () => <div>LibraryIcon</div>,
    PlusIcon: () => <div>PlusIcon</div>,
    HeartIcon: () => <div>HeartIcon</div>,
}));

describe('Sidebar', () => {
    it('renders navigation buttons', () => {
        render(<Sidebar />);

        expect(screen.getByText('Home')).toBeInTheDocument();
        expect(screen.getByText('Search')).toBeInTheDocument();
        expect(screen.getByText('Your Library')).toBeInTheDocument();
    });

    it('renders the PlusIcon button', () => {
        render(<Sidebar />);

        expect(screen.getByText('PlusIcon')).toBeInTheDocument();
    });

    it('renders "Liked Songs" playlist', () => {
        render(<Sidebar />);

        expect(screen.getByText('Liked Songs')).toBeInTheDocument();
        expect(screen.getByText('Playlist • 123 songs')).toBeInTheDocument();
        expect(screen.getByText('HeartIcon')).toBeInTheDocument();
    });

    it('renders 10 user playlists', () => {
        render(<Sidebar />);

        for (let i = 1; i <= 10; i++) {
            expect(screen.getByText(`Playlist ${i}`)).toBeInTheDocument();
            expect(screen.getAllByText('Playlist • User')).toHaveLength(10);
        }
    });
});
