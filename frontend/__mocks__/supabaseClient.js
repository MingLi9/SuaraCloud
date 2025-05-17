export const supabase = {
    auth: {
        signInWithPassword: jest.fn(),
        signUp: jest.fn(),
        resetPasswordForEmail: jest.fn(),
        signInWithOAuth: jest.fn(),
    },
};
