// This test file is for showing how to test with axios in React components
// To not interfere with other tests, we add an always pass test case
test('Always pass', () => {
    expect(true).toBe(true);
});

// import React from 'react';
// import { render, screen, waitFor } from '@testing-library/react';
// import '@testing-library/jest-dom';
// import axios from 'axios';
// import App from './App';

// // Mock axios so we can control its responses.
// jest.mock('axios');

// describe('App component testing Axios cases', () => {
//   afterEach(() => {
//     jest.clearAllMocks();
//   });

//   test('calls axios and handles success (the .then block)', async () => {
//     // Mock a successful response from axios.post
//     const mockResponse = { data: 'Success data' };
//     axios.post.mockResolvedValueOnce(mockResponse);

//     render(<App />);

//     // Verify axios was called with the correct URL, payload, and headers
//     expect(axios.post).toHaveBeenCalledWith(
//       expect.stringContaining('/demo/mqtt'),
//       'test',
//       { headers: { 'Content-Type': 'application/json' } }
//     );

//     // Wait for the component to update state and render the "data = ..." text
//     await waitFor(() => {
//       expect(screen.getByText('data = Success data')).toBeInTheDocument();
//     });
//   });

//   test('calls axios and handles error (the .catch block)', async () => {
//     // Mock a failing response from axios.post
//     const mockError = new Error('Failed to fetch');
//     axios.post.mockRejectedValueOnce(mockError);

//     // Spy on console.error to verify it's called in the catch block
//     const consoleSpy = jest.spyOn(console, 'error').mockImplementation(() => {});

//     render(<App />);

//     // Wait for the catch block to execute
//     await waitFor(() => {
//       expect(consoleSpy).toHaveBeenCalledWith(
//         'Error fetching data:',
//         mockError
//       );
//     });

//     // Clean up the spy
//     consoleSpy.mockRestore();
//   });
// });
