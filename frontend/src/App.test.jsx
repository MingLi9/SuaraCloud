jest.mock('../supabaseClient');

import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import App from './App';
import { MemoryRouter } from 'react-router-dom';

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
    it('renders Home page', async () => {
        render(
            <MemoryRouter>
                <App />
            </MemoryRouter>
        );

        // Wait until "Home Page" appears after loading is done
        await waitFor(() => {
            expect(screen.getByText('Home Page')).toBeInTheDocument();
        });
    });

    it('renders Test page', async () => {
        render(
            <MemoryRouter>
                <App />
            </MemoryRouter>
        );

        await waitFor(() => {
            expect(screen.getByText('Test Page')).toBeInTheDocument();
        });
    });
});
