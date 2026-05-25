import { Sun, Moon, Settings, Clock, RefreshCw, DollarSign } from "lucide-react";
import Button from '@mui/material/Button';
import { CountdownTimer } from "../CoundownTimer/CountdownTimer.tsx";
import type {UpdateFrequency, CurrencySymbol} from "../../App.tsx";
import { useState, useRef, useEffect } from "react";

interface HeaderProps {
  theme: "light" | "dark";
  toggleTheme: () => void;
  timeRemaining: number;
  updateFrequency: UpdateFrequency;
  onUpdateFrequency: (frequency: UpdateFrequency) => void;
  onManualRefresh: () => void;
  isRefreshing: boolean;
  displayCurrency: CurrencySymbol;
  onCurrencyChange: (currency: CurrencySymbol) => void;
}

const FREQUENCY_OPTIONS: { value: UpdateFrequency; label: string }[] = [
  { value: 10, label: "10s" },
  { value: 30, label: "30s" },
  { value: 60, label: "1m" },
  { value: 120, label: "2m" },
];

const CURRENCY_OPTIONS: { value: CurrencySymbol; label: string; symbol: string }[] = [
  { value: "USD", label: "US Dollar", symbol: "$" },
  { value: "EUR", label: "Euro", symbol: "€" },
  { value: "GBP", label: "British Pound", symbol: "£" },
  { value: "JPY", label: "Japanese Yen", symbol: "¥" },
  { value: "BTC", label: "Bitcoin", symbol: "₿" },
];

export function Header({
  theme,
  toggleTheme,
  timeRemaining,
  updateFrequency,
  onUpdateFrequency,
  onManualRefresh,
  isRefreshing,
  displayCurrency,
  onCurrencyChange,
}: HeaderProps) {
  const [showSettings, setShowSettings] = useState(false);
  const [showCurrency, setShowCurrency] = useState(false);
  const settingsRef = useRef<HTMLDivElement>(null);
  const currencyRef = useRef<HTMLDivElement>(null);

  // Close dropdowns when clicking outside
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (settingsRef.current && !settingsRef.current.contains(event.target as Node)) {
        setShowSettings(false);
      }
      if (currencyRef.current && !currencyRef.current.contains(event.target as Node)) {
        setShowCurrency(false);
      }
    };

    if (showSettings || showCurrency) {
      document.addEventListener("mousedown", handleClickOutside);
    }

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [showSettings, showCurrency]);

  const currentCurrency = CURRENCY_OPTIONS.find((c) => c.value === displayCurrency);

  return (
    <header className="flex items-center justify-between">
      <div className="flex items-center gap-3">
        <div className="p-2 rounded-lg bg-emerald-100 dark:bg-emerald-900/50">
          <Clock className="w-5 h-5 text-emerald-600 dark:text-emerald-400" />
        </div>
        <div>
          <h1 className="text-2xl font-bold text-slate-900 dark:text-white tracking-tight">
            CryptoDash
          </h1>
          <p className="text-sm text-slate-500 dark:text-slate-400">
            Real-time cryptocurrency tracker
          </p>
        </div>
      </div>

      <div className="flex items-center gap-2">
        {/* Countdown Timer */}
        <CountdownTimer
          timeRemaining={timeRemaining}
          total={updateFrequency}
          theme={theme}
        />

        {/* Refresh Button */}
        <Button
          variant="ghost"
          size="sm"
          onClick={onManualRefresh}
          disabled={isRefreshing}
          className="p-2 rounded-lg hover:bg-slate-100 dark:hover:bg-slate-800 transition-all"
          title="Refresh prices now"
        >
          <RefreshCw
            className={`w-5 h-5 text-slate-600 dark:text-slate-400 transition-transform duration-500 ${
              isRefreshing ? "animate-spin" : ""
            }`}
          />
        </Button>

        {/* Currency Selector */}
        <div className="relative" ref={currencyRef}>
          <Button
            variant="ghost"
            size="sm"
            onClick={() => setShowCurrency(!showCurrency)}
            className="flex items-center gap-1.5 px-3 py-2 rounded-lg hover:bg-slate-100 dark:hover:bg-slate-800"
            title="Select display currency"
          >
            <DollarSign className="w-4 h-4 text-slate-600 dark:text-slate-400" />
            <span className="text-sm font-medium text-slate-700 dark:text-slate-300">
              {currentCurrency?.symbol}
            </span>
          </Button>

          {showCurrency && (
            <div className="absolute right-0 top-full mt-2 w-44 bg-white dark:bg-slate-900 rounded-lg shadow-lg border border-slate-200 dark:border-slate-700 p-2 z-50">
              <p className="text-xs font-medium text-slate-500 dark:text-slate-400 mb-2 px-2 uppercase tracking-wider">
                Display Currency
              </p>
              <div className="space-y-1">
                {CURRENCY_OPTIONS.map((option) => (
                  <Button
                    key={option.value}
                    variant="ghost"
                    size="sm"
                    onClick={() => {
                      onCurrencyChange(option.value);
                      setShowCurrency(false);
                    }}
                    className={`w-full justify-start text-sm font-medium rounded-lg transition-all ${
                      displayCurrency === option.value
                        ? "bg-emerald-100 dark:bg-emerald-900/50 text-emerald-700 dark:text-emerald-300"
                        : "text-slate-600 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800"
                    }`}
                  >
                    <span className="mr-2">{option.symbol}</span>
                    <span>{option.label}</span>
                  </Button>
                ))}
              </div>
            </div>
          )}
        </div>

        {/* Settings Dropdown */}
        <div className="relative" ref={settingsRef}>
          <Button
            variant="ghost"
            size="sm"
            onClick={() => setShowSettings(!showSettings)}
            className="p-2 rounded-lg hover:bg-slate-100 dark:hover:bg-slate-800"
          >
            <Settings className="w-5 h-5 text-slate-600 dark:text-slate-400" />
          </Button>

          {showSettings && (
            <div className="absolute right-0 top-full mt-2 w-48 bg-white dark:bg-slate-900 rounded-lg shadow-lg border border-slate-200 dark:border-slate-700 p-3 z-50">
              <p className="text-xs font-medium text-slate-500 dark:text-slate-400 mb-2 uppercase tracking-wider">
                Update Frequency
              </p>
              <div className="grid grid-cols-2 gap-2">
                {FREQUENCY_OPTIONS.map((option) => (
                  <Button
                    key={option.value}
                    variant="ghost"
                    size="sm"
                    onClick={() => {
                      onUpdateFrequency(option.value);
                      setShowSettings(false);
                    }}
                    className={`text-xs font-medium rounded-lg transition-all ${
                      updateFrequency === option.value
                        ? "bg-emerald-100 dark:bg-emerald-900/50 text-emerald-700 dark:text-emerald-300"
                        : "text-slate-600 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800"
                    }`}
                  >
                    {option.label}
                  </Button>
                ))}
              </div>
            </div>
          )}
        </div>

        {/* Theme Toggle */}
        <Button
          variant="ghost"
          size="sm"
          onClick={toggleTheme}
          className="p-2 rounded-lg hover:bg-slate-100 dark:hover:bg-slate-800"
        >
          {theme === "light" ? (
            <Moon className="w-5 h-5 text-slate-600" />
          ) : (
            <Sun className="w-5 h-5 text-slate-400" />
          )}
        </Button>
      </div>
    </header>
  );
}