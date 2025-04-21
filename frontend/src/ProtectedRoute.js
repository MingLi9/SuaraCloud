// src/ProtectedRoute.js
import React, { useEffect, useState } from 'react';
import { Navigate } from 'react-router-dom';
import { supabase } from './supabaseClient';

function ProtectedRoute({ children }) {
    const [isLoading, setIsLoading] = useState(true);
    const [session, setSession] = useState(null);

    useEffect(() => {
        const currentSession = supabase.auth.getSession().then(({ data }) => {
            setSession(data.session);
            setIsLoading(false);
        });

        const { data: listener } = supabase.auth.onAuthStateChange(
            (_event, session) => {
                setSession(session);
            }
        );

        return () => {
            listener.subscription.unsubscribe();
        };
    }, []);

    if (isLoading) {
        return <div>Loading...</div>;
    }

    if (!session) {
        return <Navigate to='/' />;
    }

    return children;
}

export default ProtectedRoute;
