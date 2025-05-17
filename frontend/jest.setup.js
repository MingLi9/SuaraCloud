require('@testing-library/jest-dom');
require('dotenv').config({ path: '.env.test' });
const { TextEncoder, TextDecoder } = require('util');
global.TextEncoder = TextEncoder;
global.TextDecoder = TextDecoder;

jest.mock('./src/supabaseClient', () => ({
    supabase: {
        auth: {
            signInWithPassword: jest.fn(),
            signUp: jest.fn(),
            resetPasswordForEmail: jest.fn(),
            signInWithOAuth: jest.fn(),
            getSession: jest.fn().mockResolvedValue({
                data: { session: { user: { id: 'test-user-id' } } },
                error: null,
            }),
            onAuthStateChange: jest.fn().mockReturnValue({
                data: {
                    subscription: {
                        unsubscribe: jest.fn(),
                    },
                },
            }),
        },
    },
}));
