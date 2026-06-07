import {useCallback, useEffect, useMemo, useRef, useState} from "react";
import {Header} from "./components/Header/Header.tsx";
import {CurrencyList} from "./components/CurrencyList/CurrencyList.tsx";
import PriceChart from "./components/PriceChart/PriceChart.tsx";
import {SearchBar} from "./components/SearchBar/SearchBar.tsx";
import {type Currency, getExchange, priceHistory, generateMockCurrencies2} from "./utils/data";

import {type ChartDisplayData, type CoinHistory, createChartHistoryData} from "./types/ChartDisplayData.ts";
import type {CoinGeckoExchangeResponseDto} from "./generated/api.ts";


export type TimeframeValue = "1H" | "1D" | "1W" | "30D" | "90D";
export type UpdateFrequency = 120 | 300 | 600 | 900;
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
    USD: 0.00001,
    EUR: 0.00092,
    GBP: 0.00079,
    JPY: 0.000001,
    BTC: 1,
};

export function App() {
    const [currencies, setCurrencies] = useState<Currency[]>([]);
    const [searchQuery, setSearchQuery] = useState("");
    const [isLoading, setIsLoading] = useState(true);
    const [timeframe, setTimeframe] = useState<TimeframeValue>("1D");
    const [showRelative, setShowRelative] = useState(false);
    const [updateFrequency, setUpdateFrequency] =  useState<UpdateFrequency>(300);
    const [timeRemaining, setTimeRemaining] = useState(300);
    const [isRefreshing, setIsRefreshing] = useState(false);
    const [displayCurrency, setDisplayCurrency] = useState<CurrencySymbol>("USD");
    const [priceData, setPriceData] = useState<Currency[]>([]);
    const [priceDtos, setPriceDtos] = useState<CoinHistory[]>([]);
    const [convertedData, setConvertedData] = useState<ChartDisplayData[]>([]);
    const [exchangeRate, setExchangeRate] = useState<number>(1);
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

    const exchangeAbortRef = useRef<AbortController | null>(null);
    const updateExchangeRates = useCallback(()=>{
        if (exchangeAbortRef.current) exchangeAbortRef.current.abort();
        const abortController = new AbortController();
        exchangeAbortRef.current = abortController;
        const fetchExchangeRates = async () => {
            await getExchange(abortController.signal)
                .then((exchange)=>{
                    //const btc: number = exchange.rates["btc"]?.value;
                    const exch = 1 / exchange.rates["usd"]?.value;

                    Object.keys(EXCHANGE_RATES).forEach((key) => {
                        const value:number = exchange.rates[key.toLowerCase()]?.value * exch;
                        EXCHANGE_RATES[key as keyof CurrencySymbol] = value;
                        console.log(EXCHANGE_RATES[key as keyof CurrencySymbol]);
                    });
                    setExchangeRate(EXCHANGE_RATES[displayCurrency]);
                });
        };
        fetchExchangeRates().catch(() => {});
    }, [displayCurrency]);

    const updatePrices = useCallback(() => {
        setIsRefreshing(true);
        if (refreshTimeoutRef.current) clearTimeout(refreshTimeoutRef.current);

        setPriceData(prev => prev.map(c => c));
        setCurrencies((prev) =>
            prev.map((currency) => ({
                ...currency,
                price: currency.price,
            }))
        );
        refreshTimeoutRef.current = setTimeout(() => {
            setIsRefreshing(false);
        }, 1000);
    }, []);

    // Manual refresh with visual feedback
    const handleManualRefresh = useCallback(() => {

        setTimeRemaining(0);
        updatePrices();
        // Brief visual feedback
        /*setTimeout(() => {
            updatePrices();
          setIsRefreshing(false);
        }, 500);*/
    }, [updatePrices, updateFrequency]);

    useEffect(() => {
        updateExchangeRates();
        let cancelled = false;
        generateMockCurrencies2().then(c => {
            if (cancelled) return;
            setCurrencies(c);
            setIsLoading(false);
            c[0].color = AVAILABLE_COLORS[0];
            setPriceData([c[0]]);
        }).catch(() => {});
        return () => {
            cancelled = true;
            if (exchangeAbortRef.current) exchangeAbortRef.current.abort();
        };
    }, [updateExchangeRates]);

    useEffect(() => {
        const rate = EXCHANGE_RATES[displayCurrency];
        //setExchangeRate(rate);
    }, [displayCurrency]);

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

        return () => {
            if (countdownRef.current) clearInterval(countdownRef.current);
        };
    }, [updateFrequency, updatePrices]);

    // Cleanup timeouts/refs on unmount
    useEffect(() => {
        return () => {
            if (refreshTimeoutRef.current) clearTimeout(refreshTimeoutRef.current);
            if (countdownRef.current) clearTimeout(countdownRef.current);
        };
    }, []);

    const filteredCurrencies = useMemo(() => {
        return currencies.filter(
            (currency) =>
                currency.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
                currency.symbol.toLowerCase().includes(searchQuery.toLowerCase())
        );
    }, [currencies, searchQuery]);
    const handleCryptoSelect = (currency: Currency) => {
        const isAlreadySelected = priceData.some((c) => c.id === currency.id);
        if (isAlreadySelected) {
            //setChartCurrencies((prev) => prev.filter((c) => c.currency.id !== currency.id));
            setPriceData((prev) => prev.filter((c => c.id !== currency.id)));
            // setPriceDtos((prev) => prev.filter(c => c.coin.id !== c.coin.id));
        } else {
            const usedColors = priceData.map((c) => c.color);
            if(!currency.color){
                currency.color = AVAILABLE_COLORS.find((c) => !usedColors.includes(c)) || AVAILABLE_COLORS[0];
            }

            const prices: Currency[] = priceData.map(m => m);
            prices.push(currency);
            // console.log(prices);
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
        // console.log("updating prices");
        updateExchangeRates();
        const abortController = new AbortController();
        let cancelled = false;
        const fetchData = async () => {
            if (priceData.length === 0) return;
            const usdExchange = EXCHANGE_RATES["USD"];
            const results = await Promise.all(
                priceData.map(c => priceHistory(c, timeframe, usdExchange, abortController.signal))
            );
            if (!cancelled) {
                setPriceDtos(results);
                setConvertedData(createChartHistoryData(results, exchangeRate, showRelative));
            }
        };
        fetchData().catch(err => "Aborted by user?");
        return () => {
            cancelled = true;
            try {
                abortController.abort("surplus to requirements");
            } catch (error) {
                console.log("print the error" + error);
            }
        };
    }, [priceData, timeframe, exchangeRate, showRelative, updateExchangeRates]);
    // const exchangeRate = EXCHANGE_RATES[displayCurrency];

    return (
        <div  className={`min-h-screen transition-colors duration-300 text-slate-700 dark:text-slate-300 ${theme === "dark" ? "bg-slate-950" : "bg-slate-50"}`}>
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
                        onSelect={handleCryptoSelect}
                        isLoading={isLoading}
                        displayCurrency={displayCurrency}
                        exchangeRate={exchangeRate}
                    />
                    <PriceChart
                        data={priceDtos}
                        chartCurrencies={priceData}
                        convertedData={convertedData}
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
                        Prices update every {updateFrequency} seconds
                    </p>
                </footer>
            </div>
        </div>
    );
}

export default App;