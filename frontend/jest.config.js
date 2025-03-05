module.exports = {
    collectCoverage: true,
    collectCoverageFrom: [
        "src/**/*.{js,jsx,ts,tsx}", // Include all source files
        "!src/index.js", // Exclude entry point
        "!*/config.js",
        "!*/index.jsx",
        "!src/reportWebVitals.js",
        "!src/setupTests.js",
        "!**/node_modules/**",
        "!src/serviceWorker.js",
    ],
    coverageDirectory: "coverage",
    coverageReporters: ["text", "lcov", "json", "html"],
    testEnvironment: "jsdom",
    setupFilesAfterEnv: ["<rootDir>/jest.setup.js"],
};
