import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';
import gatewayUrl from './config';

test('Always pass', () => {
  expect(true).toBe(true);
});

test('renders gateway_url correctly', () => {
  render(<App />);

  // Check if the text with the expected gateway URL appears in the document
  const gatewayElement = screen.getByText(`gateway_url = ${gatewayUrl}`);

  expect(gatewayElement).toBeInTheDocument();
});