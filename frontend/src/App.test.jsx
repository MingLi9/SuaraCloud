import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';
import gatewayUrl from './config';

test('Always pass', () => {
  expect(true).toBe(true);
});

test('renders gateway_url correctly', () => {
  render(<App />);
  const gatewayText = screen.getByText(`gateway_url = ${gatewayUrl}`);
  expect(gatewayText).toBeInTheDocument();
});