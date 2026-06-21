import { render, screen, fireEvent } from '@testing-library/react';
import { CurrencyList } from './CurrencyList';
import type { Currency } from "../../utils/data";

const mockCurrencies: Currency[] = [
  {
    id: 1,
    name: "Bitcoin",
    symbol: "BTC",
    price: 67432.50,
    change24h: 2.5,
    marketCap: 1324000000000,
    basePrice: 67000,
    color: "#10b981",
    coinId: 0
  },
  {
    id: 2,
    name: "Ethereum",
    symbol: "ETH",
    price: 3521.80,
    change24h: -1.2,
    marketCap: 423000000000,
    basePrice: 3500,
    color: "#3b82f6",
    coinId: 0
  },
];

describe('CurrencyList', () => {
  test('renders skeleton cards when loading', () => {
    const { container } = render(
      <CurrencyList
        currencies={[]}
        selectedCurrencies={[]}
        onSelect={() => {}}
        isLoading={true}
        displayCurrency="USD"
        exchangeRate={1}
      />
    );
    const skeletons = container.querySelectorAll('.animate-pulse');
    expect(skeletons).toHaveLength(5);
  });

  test('shows empty message when no currencies', () => {
    render(
      <CurrencyList
        currencies={[]}
        selectedCurrencies={[]}
        onSelect={() => {}}
        isLoading={false}
        displayCurrency="USD"
        exchangeRate={1}
      />
    );
    expect(screen.getByText("No currencies found")).toBeInTheDocument();
  });

  test('renders currency names and symbols', () => {
    render(
      <CurrencyList
        currencies={mockCurrencies}
        selectedCurrencies={[]}
        onSelect={() => {}}
        isLoading={false}
        displayCurrency="USD"
        exchangeRate={1}
      />
    );
    expect(screen.getByText("Bitcoin")).toBeInTheDocument();
    expect(screen.getByText("BTC")).toBeInTheDocument();
    expect(screen.getByText("Ethereum")).toBeInTheDocument();
    expect(screen.getByText("ETH")).toBeInTheDocument();
  });

  test('shows positive change with plus sign and TrendingUp icon', () => {
    render(
      <CurrencyList
        currencies={[mockCurrencies[0]]}
        selectedCurrencies={[]}
        onSelect={() => {}}
        isLoading={false}
        displayCurrency="USD"
        exchangeRate={1}
      />
    );
    expect(screen.getByText("+2.50%")).toBeInTheDocument();
  });

  test('shows negative change without plus sign and TrendingDown icon', () => {
    render(
      <CurrencyList
        currencies={[mockCurrencies[1]]}
        selectedCurrencies={[]}
        onSelect={() => {}}
        isLoading={false}
        displayCurrency="USD"
        exchangeRate={1}
      />
    );
    expect(screen.getByText("-1.20%")).toBeInTheDocument();
  });

  test('shows check icon when currency is selected', () => {
    render(
      <CurrencyList
        currencies={mockCurrencies}
        selectedCurrencies={[mockCurrencies[0]]}
        onSelect={() => {}}
        isLoading={false}
        displayCurrency="USD"
        exchangeRate={1}
      />
    );
    const container = document.querySelector('.grid');
    expect(container?.querySelector('.rounded-full.bg-emerald-500')).toBeInTheDocument();
  });

  test('calls onSelect when a currency card is clicked', () => {
    const onSelect = vi.fn();
    const { container } = render(
      <CurrencyList
        currencies={mockCurrencies}
        selectedCurrencies={[]}
        onSelect={onSelect}
        isLoading={false}
        displayCurrency="USD"
        exchangeRate={1}
      />
    );
    const cards = container.querySelectorAll('.cursor-pointer');
    fireEvent.click(cards[0]);
    expect(onSelect).toHaveBeenCalledWith(mockCurrencies[0]);
  });

  test('formats BTC price correctly', () => {
    render(
      <CurrencyList
        currencies={[mockCurrencies[0]]}
        selectedCurrencies={[]}
        onSelect={() => {}}
        isLoading={false}
        displayCurrency="BTC"
        exchangeRate={0.000023}
      />
    );
    const btcPrice = (67432.50 * 0.000023).toFixed(8);
    expect(screen.getByText(btcPrice)).toBeInTheDocument();
  });
});
