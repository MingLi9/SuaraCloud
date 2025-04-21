import React from 'react';
import { render, screen } from '@testing-library/react';
import Header from './Header';

// Mock the icons to keep the test focused
jest.mock('./Icons', () => ({
    ChevronLeftIcon: () => <div>ChevronLeftIcon</div>,
    ChevronRightIcon: () => <div>ChevronRightIcon</div>,
}));

describe('Header', () => {
    it('renders navigation buttons with icons', () => {
        render(<Header />);

        expect(screen.getByText('ChevronLeftIcon')).toBeInTheDocument();
        expect(screen.getByText('ChevronRightIcon')).toBeInTheDocument();
    });

    it('renders upgrade button', () => {
        render(<Header />);

        expect(screen.getByText('Upgrade')).toBeInTheDocument();
    });

    it('renders user avatar', () => {
        render(<Header />);

        expect(screen.getByText('U')).toBeInTheDocument();
    });
});
