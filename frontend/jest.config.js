module.exports = {
    // Tells Jest to run Babel on all .js/.jsx
    transform: {
        '^.+\\.[jt]sx?$': 'babel-jest',
    },

    // The KEY: Don’t ignore axios under node_modules
    // This pattern ignores everything in node_modules EXCEPT axios
    transformIgnorePatterns: ['[/\\\\]node_modules[/\\\\](?!axios[/\\\\].*)'],

    // Your coverage settings, environment, etc.
    collectCoverage: true,
    collectCoverageFrom: [
        'src/**/*.{js,jsx,ts,tsx}',
        '!src/index.js',
        '!*/config.js',
        '!*/index.jsx',
        '!src/reportWebVitals.js',
        '!src/setupTests.js',
        '!**/node_modules/**',
        '!src/serviceWorker.js',
        '!**/E2E/**',
        '!src/TestPage/**',
        '!src/components/Icons.jsx',
    ],
    testEnvironment: 'jsdom',
    setupFilesAfterEnv: ['<rootDir>/jest.setup.js'],
    moduleNameMapper: {
        '\\.(css|less|sass|scss)$': 'identity-obj-proxy',
    },
    testPathIgnorePatterns: ['<rootDir>/E2E/.*'],
};
