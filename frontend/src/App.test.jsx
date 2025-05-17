import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import App from './App';
import { supabase } from './supabaseClient'; // Import the mocked supabase

// Mock pages
jest.mock('./Home', () => () => <div>Home Page</div>);
jest.mock('./TestPage', () => () => <div>Test Page</div>);

// Mock react-router-dom structure
jest.mock('react-router-dom', () => {
    const originalModule = jest.requireActual('react-router-dom');
    return {
        ...originalModule,
        BrowserRouter: ({ children }) => <div>{children}</div>,
        Routes: ({ children }) => <div>{children}</div>,
        Route: ({ element }) => element,
        Navigate: ({ to }) => <div>Redirected to {to}</div>,
    };
});

beforeEach(() => {
    jest.clearAllMocks();
});

describe('App', () => {
    it('renders Home page when logged in', async () => {
        supabase.auth.getSession.mockResolvedValue({
            data: { session: { user: { id: 'test-user-id' } } },
            error: null,
        });
        supabase.auth.onAuthStateChange.mockReturnValue({
            data: { subscription: { unsubscribe: jest.fn() } },
        });

        render(
            <MemoryRouter initialEntries={['/']}>
                <App />
            </MemoryRouter>
        );

        await waitFor(() => {
            expect(screen.getByText('Home Page')).toBeInTheDocument();
        });
    });

    it('renders Test page when logged in', async () => {
        supabase.auth.getSession.mockResolvedValue({
            data: { session: { user: { id: 'test-user-id' } } },
            error: null,
        });
        supabase.auth.onAuthStateChange.mockReturnValue({
            data: { subscription: { unsubscribe: jest.fn() } },
        });

        render(
            <MemoryRouter initialEntries={['/test']}>
                <App />
            </MemoryRouter>
        );

        await waitFor(() => {
            expect(screen.getByText('Test Page')).toBeInTheDocument();
        });
    });

    it('redirects to login when not logged in on Home page', async () => {
        supabase.auth.getSession.mockResolvedValue({
            data: { session: null },
            error: null,
        });
        supabase.auth.onAuthStateChange.mockReturnValue({
            data: { subscription: { unsubscribe: jest.fn() } },
        });

        render(
            <MemoryRouter initialEntries={['/']}>
                <App />
            </MemoryRouter>
        );

        const redirects = await screen.findAllByText('Redirected to /login');
        expect(redirects.length).toBeGreaterThan(0);
    });

    it('redirects to login when not logged in on Test page', async () => {
        supabase.auth.getSession.mockResolvedValue({
            data: { session: null },
            error: null,
        });
        supabase.auth.onAuthStateChange.mockReturnValue({
            data: { subscription: { unsubscribe: jest.fn() } },
        });

        render(
            <MemoryRouter initialEntries={['/test']}>
                <App />
            </MemoryRouter>
        );

        const redirects = await screen.findAllByText('Redirected to /login');
        expect(redirects.length).toBeGreaterThan(0);
    });

    it('redirects to login on unknown page when not logged in', async () => {
        supabase.auth.getSession.mockResolvedValue({
            data: { session: null },
            error: null,
        });
        supabase.auth.onAuthStateChange.mockReturnValue({
            data: { subscription: { unsubscribe: jest.fn() } },
        });

        render(
            <MemoryRouter initialEntries={['/unknown']}>
                <App />
            </MemoryRouter>
        );

        const redirects = await screen.findAllByText('Redirected to /login');
        expect(redirects.length).toBeGreaterThan(0);
    });

    it('redirects to Home on unknown page when logged in', async () => {
        supabase.auth.getSession.mockResolvedValue({
            data: { session: { user: { id: 'test-user-id' } } },
            error: null,
        });
        supabase.auth.onAuthStateChange.mockReturnValue({
            data: { subscription: { unsubscribe: jest.fn() } },
        });

        render(
            <MemoryRouter initialEntries={['/unknown']}>
                <App />
            </MemoryRouter>
        );

        await waitFor(() => {
            expect(screen.getByText('Home Page')).toBeInTheDocument();
        });
    });
});
