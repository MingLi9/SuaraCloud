import React, { useState } from 'react';
import { supabase } from '../supabaseClient';
import './Login.css';
import { SpotifyLogo, DiscordLogo } from '../components/Icons';

function Login() {
    const [mode, setMode] = useState('login'); // 'login', 'register', or 'reset'
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [message, setMessage] = useState(null);
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [oauthLoading, setOauthLoading] = useState(false);

    async function handleLogin(e) {
        e.preventDefault();
        setError(null);
        setLoading(true);

        try {
            const { error } = await supabase.auth.signInWithPassword({
                email,
                password,
            });

            if (error) throw error;
        } catch (error) {
            setError(error.message || 'An error occurred during login');
        } finally {
            setLoading(false);
        }
    }

    async function handleRegister(e) {
        e.preventDefault();
        setError(null);
        setLoading(true);

        if (password !== confirmPassword) {
            setError('Passwords do not match');
            setLoading(false);
            return;
        }

        try {
            const { error } = await supabase.auth.signUp({
                email,
                password,
            });

            if (error) throw error;

            setMessage(
                'Registration successful! Please check your email for verification.'
            );
            setMode('login');
        } catch (error) {
            setError(error.message || 'An error occurred during registration');
        } finally {
            setLoading(false);
        }
    }

    async function handleResetPassword(e) {
        e.preventDefault();
        setError(null);
        setLoading(true);

        try {
            const { error } = await supabase.auth.resetPasswordForEmail(email, {
                redirectTo: window.location.origin + '/reset-password',
            });

            if (error) throw error;

            setMessage('Password reset email sent. Please check your inbox.');
            setMode('login');
        } catch (error) {
            setError(
                error.message || 'An error occurred during password reset'
            );
        } finally {
            setLoading(false);
        }
    }

    async function handleDiscordLogin() {
        setError(null);
        setOauthLoading(true);

        try {
            const { error } = await supabase.auth.signInWithOAuth({
                provider: 'discord',
                options: {
                    redirectTo: window.location.origin,
                },
            });

            if (error) throw error;
        } catch (error) {
            setError(error.message || 'An error occurred during Discord login');
            setOauthLoading(false);
        }
        // Note: We don't set oauthLoading to false here because the page will redirect
    }

    return (
        <div className='login-container'>
            <div className='login-header'>
                <SpotifyLogo className='spotify-logo' />
                <h1>Suara Cloud</h1>
            </div>

            <div className='login-card'>
                {message && <div className='message-box'>{message}</div>}

                {error && <div className='error-box'>{error}</div>}

                {mode === 'login' && (
                    <>
                        <h2>Log in to continue</h2>
                        <form onSubmit={handleLogin} className='login-form'>
                            <div className='form-group'>
                                <label htmlFor='email'>Email address</label>
                                <input
                                    id='email'
                                    type='email'
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    placeholder='Email address'
                                    required
                                />
                            </div>
                            <div className='form-group password-group'>
                                <label htmlFor='password'>Password</label>
                                <div className='password-input-container'>
                                    <input
                                        id='password'
                                        type={
                                            showPassword ? 'text' : 'password'
                                        }
                                        value={password}
                                        onChange={(e) =>
                                            setPassword(e.target.value)
                                        }
                                        placeholder='Password'
                                        required
                                    />
                                    <button
                                        type='button'
                                        data-testid='toggle-password-visibility'
                                        className='password-toggle-btn'
                                        onClick={() =>
                                            setShowPassword(!showPassword)
                                        }
                                    >
                                        {showPassword ? 'Hide' : 'Show'}
                                    </button>
                                </div>
                            </div>
                            <button
                                type='submit'
                                data-testid='login-submit'
                                className='login-button'
                                disabled={loading}
                            >
                                {loading ? 'Logging in...' : 'LOG IN'}
                            </button>
                        </form>
                        <div className='social-login'>
                            <div className='divider'>
                                <span>OR</span>
                            </div>
                            <button
                                type='button'
                                data-testid='discord-login'
                                className='discord-button'
                                onClick={handleDiscordLogin}
                                disabled={oauthLoading}
                            >
                                <div className='discord-icon'>
                                    <DiscordLogo className='discord-logo' />
                                </div>
                                <span>
                                    {oauthLoading
                                        ? 'Connecting...'
                                        : 'Continue with Discord'}
                                </span>
                            </button>
                        </div>
                        <div className='login-footer'>
                            <button
                                data-testid='switch-to-reset'
                                className='text-button'
                                onClick={() => setMode('reset')}
                            >
                                Forgot your password?
                            </button>
                            <div className='register-prompt'>
                                <span>Don't have an account?</span>
                                <button
                                    data-testid='switch-to-register'
                                    className='text-button'
                                    onClick={() => setMode('register')}
                                >
                                    SIGN UP
                                </button>
                            </div>
                        </div>
                    </>
                )}

                {mode === 'register' && (
                    <>
                        <h2>Sign up for free</h2>
                        <form onSubmit={handleRegister} className='login-form'>
                            <div className='form-group'>
                                <label htmlFor='register-email'>
                                    Email address
                                </label>
                                <input
                                    id='register-email'
                                    type='email'
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    placeholder='Email address'
                                    required
                                />
                            </div>
                            <div className='form-group password-group'>
                                <label htmlFor='register-password'>
                                    Password
                                </label>
                                <div className='password-input-container'>
                                    <input
                                        id='register-password'
                                        type={
                                            showPassword ? 'text' : 'password'
                                        }
                                        value={password}
                                        onChange={(e) =>
                                            setPassword(e.target.value)
                                        }
                                        placeholder='Create a password'
                                        required
                                    />
                                    <button
                                        type='button'
                                        data-testid='toggle-password-visibility'
                                        className='password-toggle-btn'
                                        onClick={() =>
                                            setShowPassword(!showPassword)
                                        }
                                    >
                                        {showPassword ? 'Hide' : 'Show'}
                                    </button>
                                </div>
                            </div>
                            <div className='form-group password-group'>
                                <label htmlFor='confirm-password'>
                                    Confirm Password
                                </label>
                                <div className='password-input-container'>
                                    <input
                                        id='confirm-password'
                                        type={
                                            showConfirmPassword
                                                ? 'text'
                                                : 'password'
                                        }
                                        value={confirmPassword}
                                        onChange={(e) =>
                                            setConfirmPassword(e.target.value)
                                        }
                                        placeholder='Confirm your password'
                                        required
                                    />
                                    <button
                                        type='button'
                                        data-testid='toggle-confirm-password-visibility'
                                        className='password-toggle-btn'
                                        onClick={() =>
                                            setShowConfirmPassword(
                                                !showConfirmPassword
                                            )
                                        }
                                    >
                                        {showConfirmPassword ? 'Hide' : 'Show'}
                                    </button>
                                </div>
                            </div>
                            <button
                                type='submit'
                                data-testid='sign-up-submit'
                                className='login-button'
                                disabled={loading}
                            >
                                {loading ? 'Signing up...' : 'SIGN UP'}
                            </button>
                        </form>

                        <div className='social-login'>
                            <div className='divider'>
                                <span>OR</span>
                            </div>
                            <button
                                type='button'
                                data-testid='discord-sign-up'
                                className='discord-button'
                                onClick={handleDiscordLogin}
                                disabled={oauthLoading}
                            >
                                <div className='discord-icon'>
                                    <DiscordLogo className='discord-logo' />
                                </div>
                                <span>
                                    {oauthLoading
                                        ? 'Connecting...'
                                        : 'Sign up with Discord'}
                                </span>
                            </button>
                        </div>
                        <div className='login-footer'>
                            <div className='register-prompt'>
                                <span>Already have an account?</span>
                                <button
                                    className='text-button'
                                    onClick={() => setMode('login')}
                                >
                                    LOG IN
                                </button>
                            </div>
                        </div>
                    </>
                )}

                {mode === 'reset' && (
                    <>
                        <h2 data-testid='reset-password-title'>
                            Reset your password
                        </h2>
                        <p
                            data-testid='reset-password-description'
                            className='reset-description'
                        >
                            We'll send you an email with a link to reset your
                            password.
                        </p>
                        <form
                            onSubmit={handleResetPassword}
                            className='login-form'
                        >
                            <div className='form-group'>
                                <label htmlFor='reset-email'>
                                    Email address
                                </label>
                                <input
                                    id='reset-email'
                                    type='email'
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    placeholder='Email address'
                                    required
                                />
                            </div>
                            <button
                                type='submit'
                                data-testid='reset-submit'
                                className='login-button'
                                disabled={loading}
                            >
                                {loading ? 'Sending...' : 'SEND RESET LINK'}
                            </button>
                        </form>
                        <div className='login-footer'>
                            <button
                                className='text-button'
                                onClick={() => setMode('login')}
                            >
                                Back to login
                            </button>
                        </div>
                    </>
                )}
            </div>
        </div>
    );
}

export default Login;
