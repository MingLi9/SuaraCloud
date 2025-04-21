import React from 'react';
import { render, screen } from '@testing-library/react';
import Home from './index';

// Mock child components to focus just on Home logic
jest.mock('../components/Sidebar', () => () => <div>Sidebar</div>);
jest.mock('../components/MainContent', () => (props) => <div>MainContent</div>);
jest.mock('../components/Player', () => (props) => <div>Player</div>);

describe('Home', () => {
    it('renders Sidebar, MainContent, and Player components', () => {
        render(<Home />);

        expect(screen.getByText('Sidebar')).toBeInTheDocument();
        expect(screen.getByText('MainContent')).toBeInTheDocument();
        expect(screen.getByText('Player')).toBeInTheDocument();
    });
});
