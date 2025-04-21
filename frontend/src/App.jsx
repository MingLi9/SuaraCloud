import './App.css';
import React, { useEffect, useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Home from './Home';
import TestPage from './TestPage';
import Login from './Login';
import { supabase } from './supabaseClient';

function App() {
    const [session, setSession] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchSession = async () => {
            const { data } = await supabase.auth.getSession();
            setSession(data.session);
            setLoading(false);
        };

        fetchSession();

        const { data: authListener } = supabase.auth.onAuthStateChange(
            (_event, session) => {
                setSession(session);
            }
        );

        return () => {
            authListener.subscription.unsubscribe();
        };
    }, []);

    async function handleLogout() {
        const { error } = await supabase.auth.signOut();
        if (error) {
            console.error('Logout error:', error);
            alert('Failed to logout: ' + error.message);
        } else {
            // After logout, go back to login page
            setSession(null);
        }
    }

    if (loading) {
        return <div>Loading...</div>;
    }

    return (
        <BrowserRouter>
            <Routes>
                <Route
                    path='/login'
                    element={!session ? <Login /> : <Navigate to='/' />}
                />
                <Route
                    path='/'
                    element={
                        session ? (
                            <Home logout={handleLogout} />
                        ) : (
                            <Navigate to='/login' />
                        )
                    }
                />
                <Route
                    path='/test'
                    element={session ? <TestPage /> : <Navigate to='/login' />}
                />
                <Route
                    path='*'
                    element={<Navigate to={session ? '/' : '/login'} />}
                />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
