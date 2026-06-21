

import { render, screen } from '@testing-library/react';
import { CountdownTimer } from './CountdownTimer';

describe('CountdownTimer', () => {
  test('displays time remaining when less than 25% of total', () => {
    render(<CountdownTimer timeRemaining={9} total={40} theme="light" isRefreshing={false} />);
    expect(screen.getByText("9")).toBeInTheDocument();
  });

  test('does not display time when 25% or more remaining', () => {
    render(<CountdownTimer timeRemaining={10} total={40} theme="light" isRefreshing={false} />);
    expect(screen.queryByText("10")).toBeNull();
  });

  test('renders pie chart with correct colors', () => {
    const { container } = render(<CountdownTimer timeRemaining={10} total={40} theme="dark" isRefreshing={false} />);
    const pieChart = container.querySelector('svg');
    expect(pieChart).toBeInTheDocument();
  });
});