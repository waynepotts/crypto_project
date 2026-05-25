import {useCallback, useEffect, useMemo, useRef, useState} from "react";
import {Header} from "./components/Header";
import {CurrencyList} from "./components/CurrencyList";
import PriceChart from "./components/PriceChart";
import {SearchBar} from "./components/SearchBar";
import generateMockCurrencies2, {type Currency, priceHistory} from "./utils/data";

import {type CoinHistory} from "./types/ChartDisplayData.ts";

export type TimeframeValue = "1H" | "1D" | "1W" | "30D" | "90D";
export type UpdateFrequency = 10 | 30 | 60 | 120;
export type CurrencySymbol = "USD" | "EUR" | "GBP" | "JPY" | "BTC";

export interface ChartCurrency {
  currency: Currency;
  color: string;
}

const AVAILABLE_COLORS = [
  "#10b981", // emerald
  "#3b82f6", // blue
  "#f59e0b", // amber
  "#8b5cf6", // violet
  "#ec4899", // pink
];

const EXCHANGE_RATES: Record<CurrencySymbol, number> = {
  USD: 1,
  EUR: 0.92,
  GBP: 0.79,
  JPY: 149.50,
  BTC: 0.000023,
};

export function App() {
    const [currencies, setCurrencies] = useState<Currency[]>([]);
    const [searchQuery, setSearchQuery] = useState("");
    const [isLoading, setIsLoading] = useState(true);
    const [timeframe, setTimeframe] = useState<TimeframeValue>("1D");
    const [showRelative, setShowRelative] = useState(false);
    const [updateFrequency, setUpdateFrequency] =  useState<UpdateFrequency>(30);
    const [timeRemaining, setTimeRemaining] = useState(30);
    const [isRefreshing, setIsRefreshing] = useState(false);
    const [displayCurrency, setDisplayCurrency] = useState<CurrencySymbol>("USD");
    const [priceData, setPriceData] = useState<Currency[]>([]);
    const [priceDtos, setPriceDtos] = useState<CoinHistory[]>([]);
    const [theme, setTheme] = useState<"light" | "dark">(() => {
      if (typeof window !== "undefined") {
        const saved = localStorage.getItem("cryptodash-theme");
        return saved === "dark" ? "dark" : "light";
      }
      return "light";
    });
    const countdownRef = useRef<ReturnType<typeof setTimeout> | null>(null);
    const refreshTimeoutRef = useRef<ReturnType<typeof setTimeout> | null>(null);
    useEffect(() => {
      const root = document.documentElement;
      if (theme === "dark") {
        root.classList.add("dark");
      } else {
        root.classList.remove("dark");
      }
      localStorage.setItem("cryptodash-theme", theme);
    }, [theme]);

    const updatePrices = useCallback(() => {
        setIsRefreshing(true);
        if (refreshTimeoutRef.current) clearTimeout(refreshTimeoutRef.current);
        refreshTimeoutRef.current = setTimeout(() => {
            setIsRefreshing(false);
        }, 1000);
      setCurrencies((prev) =>
          prev.map((currency) => ({
            ...currency,
            price: currency.price * (1 + (Math.random() - 0.5) * 0.01),
            change24h: currency.change24h + (Math.random() - 0.5) * 0.2,
          }))
      );
    }, []);

    // Manual refresh with visual feedback
    const handleManualRefresh = useCallback(() => {


      setTimeRemaining(updateFrequency);
        updatePrices();
      // Brief visual feedback
      /*setTimeout(() => {
          updatePrices();
        setIsRefreshing(false);
      }, 500);*/
    }, [updatePrices, updateFrequency]);

    useEffect(() => {
      let cancelled = false;
      generateMockCurrencies2().then(c => {
        if (cancelled) return;
        setCurrencies(c);
        setIsLoading(false);
        c[0].color = AVAILABLE_COLORS[0];
        setPriceData([c[0]]);
      });
      return () => { cancelled = true; };
    }, []);

    // Manage update interval
    useEffect(() => {
      if (countdownRef.current) clearInterval(countdownRef.current);
      //setTimeRemaining(updateFrequency);

      // Countdown timer (every second)
      countdownRef.current = setInterval(() => {
        setTimeRemaining((prev) => {
          if (prev <= 1) {
              updatePrices();
            return updateFrequency;
          }
          return prev - 1;
        });
      }, 1000);

      // Price update interval
      /*intervalRef.current = setInterval(() => {
        updatePrices();
        setTimeRemaining(updateFrequency);
      }, updateFrequency * 1000);*/

      return () => {
        if (countdownRef.current) clearInterval(countdownRef.current);
      };
    }, [updateFrequency, updatePrices]);

    // Cleanup timeouts/refs on unmount
    useEffect(() => {
      return () => {
        if (refreshTimeoutRef.current) clearTimeout(refreshTimeoutRef.current);
      };
    }, []);

    const filteredCurrencies = useMemo(() => {
      return currencies.filter(
          (currency) =>
              currency.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
              currency.symbol.toLowerCase().includes(searchQuery.toLowerCase())
      );
    }, [currencies, searchQuery]);

    const handleCurrencySelect = (currency: Currency) => {
      const isAlreadySelected = priceData.some((c) => c.id === currency.id);
      if (isAlreadySelected) {
          //setChartCurrencies((prev) => prev.filter((c) => c.currency.id !== currency.id));
          setPriceData((prev) => prev.filter((c => c.id !== currency.id)));
          setPriceDtos((prev) => prev.filter(c => c.coin.id !== c.coin.id));
      } else {
          const usedColors = priceData.map((c) => c.color);
          currency.color = AVAILABLE_COLORS.find((c) => !usedColors.includes(c)) || AVAILABLE_COLORS[0];
          const prices: Currency[] = priceData.map(m => m);
          prices.push(currency);
          console.log(prices);
          setPriceData(prices);
      }
    };

    const handleColorChange = (currencyId: string, color: string) => {
      setPriceData((prev) =>
          prev.map((c) => (c.id === currencyId ? {...c, color} : c))
      );
      currencies.forEach(c=>{
          if(c.id === currencyId) {
              c.color = color;
          }
      })
    };

    const toggleTheme = () => {
      setTheme((prev) => (prev === "light" ? "dark" : "light"));
    };
    useEffect(() => {
      const abortController = new AbortController();
      let cancelled = false;
      const fetchData = async () => {
         if (priceData.length === 0) return;
         const results = await Promise.all(
             priceData.map(c => priceHistory(c, timeframe, abortController.signal))
         );
         if (!cancelled) {
           setPriceDtos(results);
         }
       };
       fetchData();
      return () => { cancelled = true; abortController.abort(); };
  }, [priceData, timeframe]);
    const exchangeRate = EXCHANGE_RATES[displayCurrency];

    return (
            <div  className={`min-h-screen transition-colors duration-300 ${theme === "dark" ? "bg-slate-950" : "bg-slate-50"}`}>
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-10">
              <Header
                  theme={theme}
                  toggleTheme={toggleTheme}
                  timeRemaining={timeRemaining}
                  updateFrequency={updateFrequency}
                  onUpdateFrequency={setUpdateFrequency}
                  onManualRefresh={handleManualRefresh}
                  isRefreshing={isRefreshing}
                  displayCurrency={displayCurrency}
                  onCurrencyChange={setDisplayCurrency}
              />

              <main className="mt-8 space-y-8">
                <SearchBar
                    searchQuery={searchQuery}
                    setSearchQuery={setSearchQuery}
                    isLoading={isLoading}/>

                <CurrencyList
                    currencies={filteredCurrencies}
                    selectedCurrencies={priceData.map((c) => c)}
                    onSelect={handleCurrencySelect}
                    isLoading={isLoading}
                    displayCurrency={displayCurrency}
                    exchangeRate={exchangeRate}
                />
                  <PriceChart
                      data={priceDtos}
                      chartCurrencies={priceData}
                      onColorChange={handleColorChange}
                      isLoading={isLoading}
                      timeframe={timeframe}
                      onTimeframeChange={setTimeframe}
                      showRelative={showRelative}
                      onToggleRelative={() => setShowRelative((prev) => !prev)}
                      displayCurrency={displayCurrency}
                      exchangeRate={exchangeRate}
                      theme={theme}
                  />
              </main>

              <footer className="mt-16 pt-8 border-t border-slate-200 dark:border-slate-800">
                <p className="text-center text-sm text-slate-500 dark:text-slate-400">
                  Prices update every {updateFrequency} seconds · Data is simulated for demo purposes
                </p>
              </footer>
            </div>
          </div>
  );
  }

export default App;