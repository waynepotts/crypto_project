import { render, screen, fireEvent } from '@testing-library/react';
import { PriceChart } from './PriceChart';
import type { CoinHistory } from "../../types/ChartDisplayData.ts";
import type { Currency } from "../../utils/data.ts";

const mockCurrency: Currency = {
  id: 1, coinId: 1, name: "Bitcoin", symbol: "BTC",
  price: 67432.50, change24h: 2.5, marketCap: 1324000000000,
  basePrice: 67000, color: "#10b981",
};

const mockHistory: CoinHistory = {
  coin: { id: 1, coingeckoId: "bitcoin", name: "Bitcoin", symbol: "BTC" },
  coinHistory: [
    {
      timestamp: "2024-01-01T00:00:00Z", price: 67000,
      marketCap: 0,
      volume: 0
    },
    {
      timestamp: "2024-01-02T00:00:00Z", price: 67432.50,
      marketCap: 0,
      volume: 0
    },
  ],
  currency: mockCurrency,
};

describe('PriceChart', () => {
  test('shows skeleton when loading', () => {
    const { container } = render(
      <PriceChart
          data={[]}
          chartCurrencies={[]}
          onColorChange={() => {
          }}
          isLoading={true}
          timeframe="1D"
          onTimeframeChange={() => {
          }}
          showRelative={false}
          onToggleRelative={() => {
          }}
          displayCurrency="USD"
          exchangeRate={1}
          theme="light" convertedData={[]} onRemoveCurrency={function (): void {
        throw new Error("Function not implemented.");
      }}      />
    );
    expect(container.querySelector('.animate-pulse')).toBeInTheDocument();
  });

  test('shows empty message when no currencies selected', () => {
    render(
      <PriceChart
          data={[]}
          chartCurrencies={[]}
          onColorChange={() => {
          }}
          isLoading={false}
          timeframe="1D"
          onTimeframeChange={() => {
          }}
          showRelative={false}
          onToggleRelative={() => {
          }}
          displayCurrency="USD"
          exchangeRate={1}
          theme="light" convertedData={[]} onRemoveCurrency={function (): void {
        throw new Error("Function not implemented.");
      }}      />
    );
    expect(screen.getByText("Select currencies from the list above to view their price history")).toBeInTheDocument();
  });

  test('renders Price History title', () => {
    render(
      <PriceChart
          data={[mockHistory]}
          chartCurrencies={[mockCurrency]}
          onColorChange={() => {
          }}
          isLoading={false}
          timeframe="1D"
          onTimeframeChange={() => {
          }}
          showRelative={false}
          onToggleRelative={() => {
          }}
          displayCurrency="USD"
          exchangeRate={1}
          theme="light" convertedData={[]} onRemoveCurrency={function (): void {
        throw new Error("Function not implemented.");
      }}      />
    );
    expect(screen.getByText("Price History")).toBeInTheDocument();
  });

  test('renders timeframe option buttons', () => {
    render(
      <PriceChart
          data={[mockHistory]}
          chartCurrencies={[mockCurrency]}
          onColorChange={() => {
          }}
          isLoading={false}
          timeframe="1D"
          onTimeframeChange={() => {
          }}
          showRelative={false}
          onToggleRelative={() => {
          }}
          displayCurrency="USD"
          exchangeRate={1}
          theme="light" convertedData={[]} onRemoveCurrency={function (): void {
        throw new Error("Function not implemented.");
      }}      />
    );
    expect(screen.getByText("1H")).toBeInTheDocument();
    expect(screen.getByText("1D")).toBeInTheDocument();
    expect(screen.getByText("1W")).toBeInTheDocument();
    expect(screen.getByText("30D")).toBeInTheDocument();
    expect(screen.getByText("90D")).toBeInTheDocument();
  });

  test('renders Relative % toggle button', () => {
    render(
      <PriceChart
          data={[mockHistory]}
          chartCurrencies={[mockCurrency]}
          onColorChange={() => {
          }}
          isLoading={false}
          timeframe="1D"
          onTimeframeChange={() => {
          }}
          showRelative={false}
          onToggleRelative={() => {
          }}
          displayCurrency="USD"
          exchangeRate={1}
          theme="light" convertedData={[]} onRemoveCurrency={function (): void {
        throw new Error("Function not implemented.");
      }}      />
    );
    expect(screen.getByText("Relative %")).toBeInTheDocument();
  });

  test('calls onTimeframeChange when timeframe button clicked', () => {
    const onTimeframeChange = vi.fn();
    render(
      <PriceChart
          data={[mockHistory]}
          chartCurrencies={[mockCurrency]}
          onColorChange={() => {
          }}
          isLoading={false}
          timeframe="1D"
          onTimeframeChange={onTimeframeChange}
          showRelative={false}
          onToggleRelative={() => {
          }}
          displayCurrency="USD"
          exchangeRate={1}
          theme="light" convertedData={[]} onRemoveCurrency={function (): void {
        throw new Error("Function not implemented.");
      }}      />
    );
    fireEvent.click(screen.getByText("1W"));
    expect(onTimeframeChange).toHaveBeenCalledWith("1W");
  });

  test('calls onToggleRelative when Relative % button clicked', () => {
    const onToggleRelative = vi.fn();
    render(
      <PriceChart
          data={[mockHistory]}
          chartCurrencies={[mockCurrency]}
          onColorChange={() => {
          }}
          isLoading={false}
          timeframe="1D"
          onTimeframeChange={() => {
          }}
          showRelative={false}
          onToggleRelative={onToggleRelative}
          displayCurrency="USD"
          exchangeRate={1}
          theme="light" convertedData={[]} onRemoveCurrency={function (): void {
        throw new Error("Function not implemented.");
      }}      />
    );
    fireEvent.click(screen.getByText("Relative %"));
    expect(onToggleRelative).toHaveBeenCalled();
  });

  test('renders chart view by default', () => {
    const { container } = render(
      <PriceChart
          data={[mockHistory]}
          chartCurrencies={[mockCurrency]}
          onColorChange={() => {
          }}
          isLoading={false}
          timeframe="1D"
          onTimeframeChange={() => {
          }}
          showRelative={false}
          onToggleRelative={() => {
          }}
          displayCurrency="USD"
          exchangeRate={1}
          theme="light" convertedData={[]} onRemoveCurrency={function (): void {
        throw new Error("Function not implemented.");
      }}      />
    );
    const chart = container.querySelector('.recharts-responsive-container');
    expect(chart).toBeInTheDocument();
  });

  test('renders color picker select for chart currency', () => {
    render(
      <PriceChart
          data={[mockHistory]}
          chartCurrencies={[mockCurrency]}
          onColorChange={() => {
          }}
          isLoading={false}
          timeframe="1D"
          onTimeframeChange={() => {
          }}
          showRelative={false}
          onToggleRelative={() => {
          }}
          displayCurrency="USD"
          exchangeRate={1}
          theme="light" convertedData={[]} onRemoveCurrency={function (): void {
        throw new Error("Function not implemented.");
      }}      />
    );
    const selects = document.querySelectorAll('select');
    expect(selects.length).toBeGreaterThanOrEqual(1);
  });

  test('switches to table view when Table button clicked', () => {
    const { container } = render(
      <PriceChart
          data={[mockHistory]}
          chartCurrencies={[mockCurrency]}
          onColorChange={() => {
          }}
          isLoading={false}
          timeframe="1D"
          onTimeframeChange={() => {
          }}
          showRelative={false}
          onToggleRelative={() => {
          }}
          displayCurrency="USD"
          exchangeRate={1}
          theme="light" convertedData={[]} onRemoveCurrency={function (): void {
        throw new Error("Function not implemented.");
      }}      />
    );
    fireEvent.click(screen.getByText("Table"));
    expect(container.querySelector('table')).toBeInTheDocument();
  });

  test('renders remove currency buttons for each chart currency', () => {
    render(
      <PriceChart
          data={[mockHistory]}
          chartCurrencies={[mockCurrency]}
          onColorChange={() => {
          }}
          isLoading={false}
          timeframe="1D"
          onTimeframeChange={() => {
          }}
          showRelative={false}
          onToggleRelative={() => {
          }}
          displayCurrency="USD"
          exchangeRate={1}
          theme="light"
          onRemoveCurrency={() => {
          }} convertedData={[]}      />
    );
    const buttons = document.querySelectorAll('button[variant="ghost"]');
    expect(buttons.length).toBeGreaterThan(0);
  });

  test('calls onRemoveCurrency when remove button clicked', () => {
    const onRemoveCurrency = vi.fn();
    const secondMockCurrency: Currency = {
      id: 2, coinId: 2, name: "Ethereum", symbol: "ETH",
      price: 3521.80, change24h: 1.2, marketCap: 423000000000,
      basePrice: 3500, color: "#3b82f6"
    };
    render(
      <PriceChart
          data={[mockHistory]}
          chartCurrencies={[mockCurrency, secondMockCurrency]}
          onColorChange={() => {
          }}
          isLoading={false}
          timeframe="1D"
          onTimeframeChange={() => {
          }}
          showRelative={false}
          onToggleRelative={() => {
          }}
          displayCurrency="USD"
          exchangeRate={1}
          theme="light"
          onRemoveCurrency={onRemoveCurrency} convertedData={[]}      />
    );
    const buttons = document.querySelectorAll('button');
    if (buttons.length > 0) {
      fireEvent.click(buttons[buttons.length - 1]);
    }
    expect(onRemoveCurrency).toHaveBeenCalled();
  });

  test('renders all three table views correctly', () => {
    const { container } = render(
      <PriceChart
          data={[mockHistory]}
          chartCurrencies={[mockCurrency]}
          onColorChange={() => {
          }}
          isLoading={false}
          timeframe="1D"
          onTimeframeChange={() => {
          }}
          showRelative={false}
          onToggleRelative={() => {
          }}
          displayCurrency="USD"
          exchangeRate={1}
          theme="light"
          onRemoveCurrency={() => {
          }} convertedData={[]}      />
    );

    fireEvent.click(screen.getByText("Compact"));
    expect(container.querySelector('table')).toBeInTheDocument();

    fireEvent.click(screen.getByText("Detailed"));
    expect(container.querySelector('table')).toBeInTheDocument();
  });
});
