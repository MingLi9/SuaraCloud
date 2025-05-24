jest.mock('../supabaseClient');

import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import Login from './index';
import { supabase } from '../supabaseClient';

describe('Login Component', () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    test('renders login form by default', () => {
        render(<Login />);
        expect(screen.getByText(/Log in to continue/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/Email address/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/Password/i)).toBeInTheDocument();
    });

    test('can switch to register mode', () => {
        render(<Login />);
        fireEvent.click(screen.getByText(/sign up/i));
        expect(screen.getByText(/Sign up for free/i)).toBeInTheDocument();
    });

    test('can switch to reset password mode', () => {
        render(<Login />);
        fireEvent.click(screen.getByText(/Forgot your password\?/i));
        expect(screen.getByTestId('reset-password-title')).toBeInTheDocument();
    });

    test('successful login triggers supabase call', async () => {
        supabase.auth.signInWithPassword.mockResolvedValue({ error: null });

        render(<Login />);

        fireEvent.change(screen.getByLabelText(/Email address/i), {
            target: { value: 'test@example.com' },
        });
        fireEvent.change(screen.getByLabelText(/Password/i), {
            target: { value: 'password123' },
        });
        fireEvent.click(screen.getByRole('button', { name: /log in/i }));

        await waitFor(() => {
            expect(supabase.auth.signInWithPassword).toHaveBeenCalledWith({
                email: 'test@example.com',
                password: 'password123',
            });
        });
    });

    test('shows error message on failed login', async () => {
        supabase.auth.signInWithPassword.mockResolvedValue({
            error: { message: 'Invalid login credentials' },
        });

        render(<Login />);

        fireEvent.change(screen.getByLabelText(/Email address/i), {
            target: { value: 'wrong@example.com' },
        });
        fireEvent.change(screen.getByLabelText(/Password/i), {
            target: { value: 'wrongpassword' },
        });
        fireEvent.click(screen.getByRole('button', { name: /log in/i }));

        await waitFor(() => {
            expect(
                screen.getByText(/Invalid login credentials/i)
            ).toBeInTheDocument();
        });
    });

    test('successful registration triggers supabase call', async () => {
        supabase.auth.signUp.mockResolvedValue({ error: null });

        render(<Login />);

        fireEvent.click(screen.getByText(/sign up/i));

        fireEvent.change(screen.getByLabelText(/^Email address$/i), {
            target: { value: 'newuser@example.com' },
        });
        fireEvent.change(screen.getByLabelText(/^Password$/i), {
            target: { value: 'password123' },
        });
        fireEvent.change(screen.getByLabelText(/Confirm Password/i), {
            target: { value: 'password123' },
        });

        fireEvent.click(screen.getByTestId('sign-up-submit'));

        await waitFor(() => {
            expect(supabase.auth.signUp).toHaveBeenCalledWith({
                email: 'newuser@example.com',
                password: 'password123',
            });
        });
    });

    test('registration fails when passwords do not match', async () => {
        render(<Login />);

        fireEvent.click(screen.getByText(/sign up/i));

        fireEvent.change(screen.getByLabelText(/^Email address$/i), {
            target: { value: 'newuser@example.com' },
        });
        fireEvent.change(screen.getByLabelText(/^Password$/i), {
            target: { value: 'password123' },
        });
        fireEvent.change(screen.getByLabelText(/Confirm Password/i), {
            target: { value: 'differentpassword' },
        });

        fireEvent.click(screen.getByTestId('sign-up-submit'));

        const redirects = await screen.findAllByText('Passwords do not match');
        expect(redirects.length).toBeGreaterThan(0);
    });

    test('reset password sends request', async () => {
        supabase.auth.resetPasswordForEmail.mockResolvedValue({ error: null });

        render(<Login />);

        fireEvent.click(screen.getByText(/Forgot your password\?/i));

        fireEvent.change(screen.getByLabelText(/^Email address$/i), {
            target: { value: 'resetuser@example.com' },
        });

        fireEvent.click(
            screen.getByRole('button', { name: /send reset link/i })
        );

        await waitFor(() => {
            expect(supabase.auth.resetPasswordForEmail).toHaveBeenCalledWith(
                'resetuser@example.com',
                expect.objectContaining({
                    redirectTo: expect.stringContaining('/reset-password'),
                })
            );
        });
    });

    test('discord login triggers oauth', async () => {
        supabase.auth.signInWithOAuth.mockResolvedValue({ error: null });

        render(<Login />);

        fireEvent.click(
            screen.getByRole('button', { name: /continue with discord/i })
        );

        await waitFor(() => {
            expect(supabase.auth.signInWithOAuth).toHaveBeenCalledWith({
                provider: 'discord',
                options: {
                    redirectTo: window.location.origin,
                },
            });
        });
    });

    test('shows error on login failure (exception)', async () => {
        supabase.auth.signInWithPassword.mockRejectedValue(
            new Error('Login crash')
        );

        render(<Login />);

        fireEvent.change(screen.getByLabelText(/Email address/i), {
            target: { value: 'crash@example.com' },
        });
        fireEvent.change(screen.getByLabelText(/Password/i), {
            target: { value: 'password' },
        });

        fireEvent.click(screen.getByTestId('login-submit'));

        await waitFor(() => {
            expect(screen.getByText(/Login crash/i)).toBeInTheDocument();
        });
    });

    test('shows error on register failure (exception)', async () => {
        supabase.auth.signUp.mockRejectedValue(new Error('Register crash'));

        render(<Login />);

        fireEvent.click(screen.getByTestId('switch-to-register'));

        fireEvent.change(screen.getByLabelText(/^Email address$/i), {
            target: { value: 'new@example.com' },
        });
        fireEvent.change(screen.getByLabelText(/^Password$/i), {
            target: { value: 'password' },
        });
        fireEvent.change(screen.getByLabelText(/Confirm Password/i), {
            target: { value: 'password' },
        });

        fireEvent.click(screen.getByTestId('sign-up-submit'));

        await waitFor(() => {
            expect(screen.getByText(/Register crash/i)).toBeInTheDocument();
        });
    });

    test('shows error on reset password failure', async () => {
        supabase.auth.resetPasswordForEmail.mockRejectedValue(
            new Error('Reset crash')
        );

        render(<Login />);

        fireEvent.click(screen.getByTestId('switch-to-reset'));

        fireEvent.change(screen.getByLabelText(/^Email address$/i), {
            target: { value: 'reset@example.com' },
        });

        fireEvent.click(screen.getByTestId('reset-submit'));

        await waitFor(() => {
            expect(screen.getByText(/Reset crash/i)).toBeInTheDocument();
        });
    });

    test('shows error on Discord login failure', async () => {
        supabase.auth.signInWithOAuth.mockRejectedValue(
            new Error('OAuth crash')
        );

        render(<Login />);

        fireEvent.click(screen.getByTestId('discord-login'));

        await waitFor(() => {
            expect(screen.getByText(/OAuth crash/i)).toBeInTheDocument();
        });
    });

    test('can toggle password visibility in login', async () => {
        render(<Login />);

        const toggleButton = screen.getByTestId('toggle-password-visibility');

        // Password initially hidden
        expect(screen.getByLabelText(/Password/i)).toHaveAttribute(
            'type',
            'password'
        );

        fireEvent.click(toggleButton);

        // Password should now be shown
        expect(screen.getByLabelText(/Password/i)).toHaveAttribute(
            'type',
            'text'
        );
    });

    test('can toggle confirm password visibility in register', async () => {
        render(<Login />);

        fireEvent.click(screen.getByTestId('switch-to-register'));

        const toggleConfirmButton = screen.getByTestId(
            'toggle-confirm-password-visibility'
        );

        // Confirm Password initially hidden
        expect(screen.getByLabelText(/Confirm Password/i)).toHaveAttribute(
            'type',
            'password'
        );

        fireEvent.click(toggleConfirmButton);

        // Confirm Password should now be shown
        expect(screen.getByLabelText(/Confirm Password/i)).toHaveAttribute(
            'type',
            'text'
        );
    });
});
