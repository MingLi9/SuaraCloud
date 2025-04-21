import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';

// Mock pages
jest.mock('./Home', () => () => <div>Home Page</div>);
jest.mock('./TestPage', () => () => <div>Test Page</div>);

// Mock react-router-dom to simulate routes
jest.mock('react-router-dom', () => {
    const originalModule = jest.requireActual('react-router-dom');
    return {
        ...originalModule,
        BrowserRouter: ({ children }) => <div>{children}</div>,
        Routes: ({ children }) => <div>{children}</div>,
        Route: ({ element }) => element,
    };
});

describe('App', () => {
    it('renders Home page', () => {
        render(<App />);

        // App should render Home by default
        expect(screen.getByText('Home Page')).toBeInTheDocument();
    });

    it('renders Test page', () => {
        render(<App />);

        // Simulate navigation to Test page
        const testPageLink = screen.getByText('Test Page');
        testPageLink.click();

        // App should render Test page
        expect(screen.getByText('Test Page')).toBeInTheDocument();
    });
});
