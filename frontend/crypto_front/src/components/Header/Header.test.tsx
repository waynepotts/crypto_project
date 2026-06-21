import { render, screen, fireEvent } from '@testing-library/react';
import { Header } from './Header';

describe('Header', () => {
  test('renders CryptoDash title', () => {
    render(
      <Header
          theme="light"
          toggleTheme={() => {
          }}
          startTime={new Date()}
          updateFrequency={300}
          onUpdateFrequency={() => {
          }}
          onManualRefresh={() => {
          }}
          isRefreshing={false}
          displayCurrency="USD"
          onCurrencyChange={() => {
          }} timeOut={0}      />
    );
    expect(screen.getByText("CryptoDash")).toBeInTheDocument();
  });

  test('renders subtitle', () => {
    render(
      <Header
          theme="light"
          toggleTheme={() => {
          }}
          startTime={new Date()}
          updateFrequency={300}
          onUpdateFrequency={() => {
          }}
          onManualRefresh={() => {
          }}
          isRefreshing={false}
          displayCurrency="USD"
          onCurrencyChange={() => {
          }} timeOut={0}      />
    );
    expect(screen.getByText("Real-time cryptocurrency tracker")).toBeInTheDocument();
  });

  test('shows Moon icon in light theme', () => {
    const { container } = render(
      <Header
          theme="light"
          toggleTheme={() => {
          }}
          startTime={new Date()}
          updateFrequency={300}
          onUpdateFrequency={() => {
          }}
          onManualRefresh={() => {
          }}
          isRefreshing={false}
          displayCurrency="USD"
          onCurrencyChange={() => {
          }} timeOut={0}      />
    );
    const moonIcons = container.querySelectorAll('svg.lucide-moon');
    expect(moonIcons.length).toBeGreaterThanOrEqual(1);
  });

  test('shows Sun icon in dark theme', () => {
    const { container } = render(
      <Header
          theme="dark"
          toggleTheme={() => {
          }}
          startTime={new Date()}
          updateFrequency={300}
          onUpdateFrequency={() => {
          }}
          onManualRefresh={() => {
          }}
          isRefreshing={false}
          displayCurrency="USD"
          onCurrencyChange={() => {
          }} timeOut={0}      />
    );
    const sunIcons = container.querySelectorAll('svg.lucide-sun');
    expect(sunIcons.length).toBeGreaterThanOrEqual(1);
  });

  test('calls toggleTheme when theme button clicked', () => {
    const toggleTheme = vi.fn();
    const { container } = render(
      <Header
          theme="light"
          toggleTheme={toggleTheme}
          startTime={new Date()}
          updateFrequency={300}
          onUpdateFrequency={() => {
          }}
          onManualRefresh={() => {
          }}
          isRefreshing={false}
          displayCurrency="USD"
          onCurrencyChange={() => {
          }} timeOut={0}      />
    );
    const themeIcon = container.querySelector('.lucide-moon');
    const themeButton = themeIcon?.closest('button');
    fireEvent.click(themeButton!);
    expect(toggleTheme).toHaveBeenCalled();
  });

  test('calls onManualRefresh when refresh button clicked', () => {
    const onManualRefresh = vi.fn();
    render(
      <Header
          theme="light"
          toggleTheme={() => {
          }}
          startTime={new Date()}
          updateFrequency={300}
          onUpdateFrequency={() => {
          }}
          onManualRefresh={onManualRefresh}
          isRefreshing={false}
          displayCurrency="USD"
          onCurrencyChange={() => {
          }} timeOut={0}      />
    );
    const refreshButton = screen.getByTitle("Refresh prices now");
    fireEvent.click(refreshButton);
    expect(onManualRefresh).toHaveBeenCalled();
  });

  test('shows currency dropdown when currency button clicked', () => {
    render(
      <Header
          theme="light"
          toggleTheme={() => {
          }}
          startTime={new Date()}
          updateFrequency={300}
          onUpdateFrequency={() => {
          }}
          onManualRefresh={() => {
          }}
          isRefreshing={false}
          displayCurrency="USD"
          onCurrencyChange={() => {
          }} timeOut={0}      />
    );
    const currencyButton = screen.getByTitle("Select display currency");
    fireEvent.click(currencyButton);
    expect(screen.getByText("Display Currency")).toBeInTheDocument();
    expect(screen.getByText("US Dollar")).toBeInTheDocument();
  });

  test('shows settings dropdown when settings button clicked', () => {
    const { container } = render(
      <Header
          theme="light"
          toggleTheme={() => {
          }}
          startTime={new Date()}
          updateFrequency={300}
          onUpdateFrequency={() => {
          }}
          onManualRefresh={() => {
          }}
          isRefreshing={false}
          displayCurrency="USD"
          onCurrencyChange={() => {
          }} timeOut={0}      />
    );
    const settingsIcon = container.querySelector('.lucide-settings');
    const settingsButton = settingsIcon?.closest('button');
    fireEvent.click(settingsButton!);
    expect(screen.getByText("Update Frequency")).toBeInTheDocument();
    expect(screen.getByText("10s")).toBeInTheDocument();
  });

  test('calls onCurrencyChange when currency option selected', () => {
    const onCurrencyChange = vi.fn();
    render(
      <Header
          theme="light"
          toggleTheme={() => {
          }}
          startTime={new Date()}
          updateFrequency={300}
          onUpdateFrequency={() => {
          }}
          onManualRefresh={() => {
          }}
          isRefreshing={false}
          displayCurrency="USD"
          onCurrencyChange={onCurrencyChange} timeOut={0}      />
    );
    const currencyButton = screen.getByTitle("Select display currency");
    fireEvent.click(currencyButton);
    fireEvent.click(screen.getByText("Euro"));
    expect(onCurrencyChange).toHaveBeenCalledWith("EUR");
  });

  test('calls onUpdateFrequency when frequency option selected', () => {
    const onUpdateFrequency = vi.fn();
    const { container } = render(
      <Header
          theme="light"
          toggleTheme={() => {
          }}
          startTime={new Date()}
          updateFrequency={300}
          onUpdateFrequency={onUpdateFrequency}
          onManualRefresh={() => {
          }}
          isRefreshing={false}
          displayCurrency="USD"
          onCurrencyChange={() => {
          }} timeOut={0}      />
    );
    const settingsIcon = container.querySelector('.lucide-settings');
    const settingsButton = settingsIcon?.closest('button');
    fireEvent.click(settingsButton!);
    fireEvent.click(screen.getByText("1m"));
    expect(onUpdateFrequency).toHaveBeenCalledWith(60);
  });

  test('refresh button is disabled when refreshing', () => {
    render(
      <Header
          theme="light"
          toggleTheme={() => {
          }}
          startTime={new Date()}
          updateFrequency={300}
          onUpdateFrequency={() => {
          }}
          onManualRefresh={() => {
          }}
          isRefreshing={true}
          displayCurrency="USD"
          onCurrencyChange={() => {
          }} timeOut={0}      />
    );
    const refreshButton = screen.getByTitle("Refresh prices now");
    expect(refreshButton).toBeDisabled();
  });
});
