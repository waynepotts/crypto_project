import {useCallback, useEffect, useMemo, useRef, useState} from "react";
import {Header} from "./components/Header";
import {CurrencyList} from "./components/CurrencyList";
import PriceChart, {PriceChart2} from "./components/PriceChart";
import {SearchBar} from "./components/SearchBar";
import {type Currency, generateMockCurrencies2, generatePriceHistory, priceHistory} from "./utils/data";
import type {CoinHistoryPointDto} from "./generated/api.ts";
import {CartesianGrid, Legend, Line, LineChart, XAxis, YAxis} from "recharts";
import {RechartsDevtools} from "@recharts/devtools";
import {type ChartDisplayData, type CoinHistory, createChartHistoryData} from "./types/ChartDisplayData.ts";

//import './App.css';
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
    const [chartCurrencies, setChartCurrencies] = useState<ChartCurrency[]>([]);
    const [updateFrequency, setUpdateFrequency] =  useState<UpdateFrequency>(30);
    const [timeRemaining, setTimeRemaining] = useState(30);
    const [isRefreshing, setIsRefreshing] = useState(false);
    const [displayCurrency, setDisplayCurrency] = useState<CurrencySymbol>("USD");
    const [count, setCount] = useState(0);
    const [priceData, setPriceData] = useState<Currency[]>([]);
    const [theme, setTheme] = useState<"light" | "dark">(() => {
      if (typeof window !== "undefined") {
        const saved = localStorage.getItem("cryptodash-theme");
        return saved === "dark" ? "dark" : "light";
      }
      return "light";
    });
    const intervalRef = useRef<ReturnType<typeof setTimeout> | null>(null);
    const countdownRef = useRef<ReturnType<typeof setTimeout> | null>(null);
    useEffect(() => {
      const root = document.documentElement;
      if (theme === "dark") {
        root.classList.add("dark");
      } else {
        root.classList.remove("dark");
      }
      localStorage.setItem("cryptodash-theme", theme);
    }, [theme]);

    // Update prices function
    const updatePrices = useCallback(() => {
        setIsRefreshing(true);
        setTimeout(() => {
            setIsRefreshing(false);
        }, 1000);
        console.log("update prices");


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

    // Initialize data
    useEffect(() => {
      const loadData = () => {
        const mockData = generateMockCurrencies2();
        setCurrencies(mockData);
        setChartCurrencies([{currency: mockData[0], color: AVAILABLE_COLORS[0]}]);
        setIsLoading(false);
        mockData[0].color = AVAILABLE_COLORS[0];
        setPriceData([mockData[0]]);
      };

      loadData();
    }, []);

    // Manage update interval
    useEffect(() => {
      if (intervalRef.current) clearInterval(intervalRef.current);
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
        if (intervalRef.current) clearInterval(intervalRef.current);
        if (countdownRef.current) clearInterval(countdownRef.current);
      };
    }, [updateFrequency, updatePrices]);

    // Update chart currency prices when main list updates
    /*useEffect(() => {
      setChartCurrencies((prev) =>
          prev.map((item) => {
            const updated = currencies.find((c) => c.id === item.currency.id);
            return updated ? {...item, currency: updated} : item;
          })
      );
    }, [currencies]);*/

    const filteredCurrencies = useMemo(() => {
      return currencies.filter(
          (currency) =>
              currency.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
              currency.symbol.toLowerCase().includes(searchQuery.toLowerCase())
      );
    }, [currencies, searchQuery]);

    const chartData = useMemo(() => {
      if (chartCurrencies.length === 0){

          return [];
      }
        return generatePriceHistory(
            chartCurrencies.map((c) => ({basePrice: c.currency.basePrice, id: c.currency.id})),
          timeframe
      );
    }, [chartCurrencies, timeframe]);

    const handleCurrencySelect = (currency: Currency) => {
      const isAlreadySelected = chartCurrencies.some((c) => c.currency.id === currency.id);
      if (isAlreadySelected) {
        setChartCurrencies((prev) => prev.filter((c) => c.currency.id !== currency.id));
        setPriceData((prev) => prev.filter((c => c.id !== currency.id)));
      } else {
        const usedColors = chartCurrencies.map((c) => c.color);
        const nextColor = AVAILABLE_COLORS.find((c) => !usedColors.includes(c)) || AVAILABLE_COLORS[0];
        currency.color = nextColor;
        setChartCurrencies((prev) => [...prev, {currency, color: nextColor}]);
        let prices: Currency[] = priceData.map(m=> m);
        prices.push(currency);
        console.log(prices);
        setPriceData(prices);
      }
    };

    const handleColorChange = (currencyId: string, color: string) => {
      setChartCurrencies((prev) =>
          prev.map((c) => (c.currency.id === currencyId ? {...c, color} : c))
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

    const exchangeRate = EXCHANGE_RATES[displayCurrency];
    const priceDtos:CoinHistory[] = useMemo(() => {
        let history:CoinHistory[] = [];
        if(!isRefreshing) {
            // console.log("refreshing...");
            priceData.forEach((c) => {
                priceHistory(c, timeframe).then(r => {
                    // history = history.map(m => m);
                    history.push(r);
                });
            });
        }
        return history;
    },[priceData, timeframe, isRefreshing]);





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
                    selectedCurrencies={chartCurrencies.map((c) => c.currency)}
                    onSelect={handleCurrencySelect}
                    isLoading={isLoading}
                    displayCurrency={displayCurrency}
                    exchangeRate={exchangeRate}
                />

                {/*<PriceChart
                    data={chartData}
                    chartCurrencies={chartCurrencies}
                    onColorChange={handleColorChange}
                    isLoading={isLoading}
                    timeframe={timeframe}
                    onTimeframeChange={setTimeframe}
                    showRelative={showRelative}
                    onToggleRelative={() => setShowRelative((prev) => !prev)}
                    displayCurrency={displayCurrency}
                    exchangeRate={exchangeRate}/>*/}
                  <PriceChart2
                      data={priceDtos}
                      chartCurrencies={chartCurrencies}
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
/*return (
  <>
    <section id="center">
      <div className="hero">
        <img src={heroImg} className="base" width="170" height="179" alt="" />
        <img src={reactLogo} className="framework" alt="React logo" />
        <img src={viteLogo} className="vite" alt="Vite logo" />
      </div>
      <div>
        <h1>Get started</h1>
        <p>
          Edit <code>src/App.tsx</code> and save to test file changes<code>HMR</code>
        </p>
      </div>
      <button
        type="button"
        className="counter"
        onClick={() => setCount((count) => count + 1)}
      >
        Count is {count}
      </button>
    </section>

    <div className="ticks"></div>

    <section id="next-steps">
      <div id="docs">
        <svg className="icon" role="presentation" aria-hidden="true">
          <use href="/icons.svg#documentation-icon"></use>
        </svg>
        <h2>Documentation</h2>
        <p>Your questions, answered</p>
        <ul>
          <li>
            <a href="https://vite.dev/" target="_blank">
              <img className="logo" src={viteLogo} alt="" />
              Explore Vite
            </a>
          </li>
          <li>
            <a href="https://react.dev/" target="_blank">
              <img className="button-icon" src={reactLogo} alt="" />
              Learn more
            </a>
          </li>
        </ul>
      </div>
      <div id="social">
        <svg className="icon" role="presentation" aria-hidden="true">
          <use href="/icons.svg#social-icon"></use>
        </svg>
        <h2>Connect with us</h2>
        <p>Join the Vite community</p>
        <ul>
          <li>
            <a href="https://github.com/vitejs/vite" target="_blank">
              <svg
                className="button-icon"
                role="presentation"
                aria-hidden="true"
              >
                <use href="/icons.svg#github-icon"></use>
              </svg>
              GitHub
            </a>
          </li>
          <li>
            <a href="https://chat.vite.dev/" target="_blank">
              <svg
                className="button-icon"
                role="presentation"
                aria-hidden="true"
              >
                <use href="/icons.svg#discord-icon"></use>
              </svg>
              Discord
            </a>
          </li>
          <li>
            <a href="https://x.com/vite_js" target="_blank">
              <svg
                className="button-icon"
                role="presentation"
                aria-hidden="true"
              >
                <use href="/icons.svg#x-icon"></use>
              </svg>
              X.com
            </a>
          </li>
          <li>
            <a href="https://bsky.app/profile/vite.dev" target="_blank">
              <svg
                className="button-icon"
                role="presentation"
                aria-hidden="true"
              >
                <use href="/icons.svg#bluesky-icon"></use>
              </svg>
              Bluesky
            </a>
          </li>
        </ul>
      </div>
    </section>

    <div className="ticks"></div>
    <section id="spacer"></section>
  </>
)
}

export default App*/

