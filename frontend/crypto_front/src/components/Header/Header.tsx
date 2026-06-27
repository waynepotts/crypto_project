import {Sun, Moon, Settings, Clock, RefreshCw, DollarSign, LanguagesIcon} from "lucide-react";
import {Button} from "../ui/button.tsx";
import { CountdownTimer } from "../CoundownTimer/CountdownTimer.tsx";
import type {UpdateFrequency, CurrencySymbol} from "../../App.tsx";
import { useState, useRef, useEffect, type Key, type JSXElementConstructor, type ReactElement, type ReactNode, type ReactPortal} from "react";
import '../../i18n';
import {useTranslation} from "react-i18next";
import {SUPPORTED_LANGUAGES} from "@/config/languages";

interface HeaderProps {
  theme: "light" | "dark";
  toggleTheme: () => void;
  timeOut: number;
  startTime: Date;
  updateFrequency: UpdateFrequency;
  onUpdateFrequency: (frequency: UpdateFrequency) => void;
  onManualRefresh: () => void;
  onUpdateLanguage: (lang: string) => void;
  isRefreshing: boolean;
  displayCurrency: CurrencySymbol;
  onCurrencyChange: (currency: CurrencySymbol) => void;
}

const FREQUENCY_OPTIONS: { value: UpdateFrequency; label: string }[] = [
  {value: 120, label: "2m"},
  {value: 300, label: "5m"},
  {value: 600, label: "10m"},
  {value: 900, label: "15m"},
];

const CURRENCY_OPTIONS: { value: CurrencySymbol; label: string; symbol: string }[] = [
  {value: "USD", label: "US Dollar", symbol: "$"},
  {value: "EUR", label: "Euro", symbol: "€"},
  {value: "GBP", label: "British Pound", symbol: "£"},
  {value: "JPY", label: "Japanese Yen", symbol: "¥"},
  {value: "BTC", label: "Bitcoin", symbol: "₿"},
];

export function Header({
                         theme,
                         toggleTheme,
                         timeOut,
                         startTime,
                         updateFrequency,
                         onUpdateFrequency,
                         onManualRefresh,
                         onUpdateLanguage,
                         isRefreshing,
                         displayCurrency,
                         onCurrencyChange,
                       }: HeaderProps) {
  const [showSettings, setShowSettings] = useState(false);
  const [showCurrency, setShowCurrency] = useState(false);
  const [showLanguage, setShowLanguage] = useState(false);
  const settingsRef = useRef<HTMLDivElement>(null);
  const currencyRef = useRef<HTMLDivElement>(null);
  const languageRef = useRef<HTMLDivElement>(null);
  const [timeRemaining, setTimeRemaining] = useState(timeOut);
  //const [syncTime, setSyncTime] = useState(startTime);
  const countdownRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const {t, i18n} = useTranslation();

  useEffect(() => {
    if (countdownRef.current) clearInterval(countdownRef.current);
    //setTimeRemaining(updateFrequency);

    // Countdown timer (every second)
    countdownRef.current = setInterval(() => {
      setTimeRemaining((prev) => {
        // console.log("header timer " + prev);
        if (prev <= 1 || isRefreshing) {
          return updateFrequency;
        }
        return Math.min(prev, updateFrequency) - 1;
      });
    }, 1000);

    return () => {
      if (countdownRef.current) clearInterval(countdownRef.current);
    };
  }, [isRefreshing, updateFrequency, startTime]);
  // Close dropdowns when clicking outside
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (settingsRef.current && !settingsRef.current.contains(event.target as Node)) {
        setShowSettings(false);
      }
      if (currencyRef.current && !currencyRef.current.contains(event.target as Node)) {
        setShowCurrency(false);
      }
      if (languageRef.current && !languageRef.current.contains(event.target as Node)) {
        setShowLanguage(false);
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
            <Clock className="w-5 h-5 text-emerald-600 dark:text-emerald-400"/>
          </div>
          <div>
            <h1 className="text-2xl font-bold text-slate-900 dark:text-white tracking-tight">
              CryptoDash
            </h1>
            <p className="text-sm text-slate-500 dark:text-slate-400">
              {t("real_time_cryptocurrency_tracker")}
            </p>
          </div>
        </div>

        <div className="flex items-center gap-2">
          {/* Countdown Timer */}
          <CountdownTimer
              timeRemaining={timeRemaining}
              total={updateFrequency}
              theme={theme}
              isRefreshing={isRefreshing}
          />

          {/* Refresh Button */}
          <Button
              variant="ghost"
              size="sm"
              onClick={onManualRefresh}
              disabled={isRefreshing}
              className="p-2 rounded-lg hover:bg-slate-100 dark:hover:bg-slate-800 transition-all"
              title={t('refresh_prices_now')}
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
                title={t("select_display_currency")}
            >
              <DollarSign className="w-4 h-4 text-slate-600 dark:text-slate-400"/>
              <span className="text-sm font-medium text-slate-700 dark:text-slate-300">
              {currentCurrency?.symbol}
            </span>
            </Button>

            {showCurrency && (
                <div
                    className="absolute right-0 top-full mt-2 w-44 bg-white dark:bg-slate-900 rounded-lg shadow-lg border border-slate-200 dark:border-slate-700 p-2 z-50">
                  <p className="text-xs font-medium text-slate-500 dark:text-slate-400 mb-2 px-2 uppercase tracking-wider">
                    {t("display_currency")}
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
              <Settings className="w-5 h-5 text-slate-600 dark:text-slate-400"/>
            </Button>

            {showSettings && (
                <div
                    className="absolute right-0 top-full mt-2 w-48 bg-white dark:bg-slate-900 rounded-lg shadow-lg border border-slate-200 dark:border-slate-700 p-3 z-50">
                  <p className="text-xs font-medium text-slate-500 dark:text-slate-400 mb-2 uppercase tracking-wider">
                    {t("update_frequency")}
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
          <div className="relative" ref={languageRef}>
            <Button
                variant="ghost"
                size="sm"
                onClick={() => {
                  setShowLanguage(!showLanguage);
                }}
                className="flex items-center gap-1.5 px-3 py-2 rounded-lg hover:bg-slate-100 dark:hover:bg-slate-800"
                title={t("select_display_language")}
            >
              <LanguagesIcon/>
              {i18n.language}
            </Button>
            {showLanguage && (
                <div
                    className="absolute right-0 top-full mt-2 w-48 bg-white dark:bg-slate-900 rounded-lg shadow-lg border border-slate-200 dark:border-slate-700 p-3 z-50">
                  <p className="text-xs font-medium text-slate-500 dark:text-slate-400 mb-2 uppercase tracking-wider">
                    {t("select_language")}
                  </p>
                  <div className="grid grid-cols-2 gap-2">
                    {SUPPORTED_LANGUAGES.map((language: { code: string }) => (
                      <Button
                          key={language.code}
                          variant="ghost"
                          size="sm"
                          onClick={() => {
                            onUpdateLanguage(language.code);
                            setShowLanguage(false);
                          }}
                          className={`text-xs font-medium rounded-lg transition-all ${
                              i18n.language === language.code
                                  ? "bg-emerald-100 dark:bg-emerald-900/50 text-emerald-700 dark:text-emerald-300"
                                  : "text-slate-600 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800"
                          }`}
                      >
                        {language.code}
                      </Button>
                  ))}
                  {/*
                      <Button
                          key={"da"}
                          variant="ghost"
                          size="sm"
                          onClick={() => {
                            onUpdateLanguage("da");
                            setShowLanguage(false);
                          }
                          }
                          className={`text-xs font-medium rounded-lg transition-all ${
                              i18n.language === "da"
                                  ? "bg-emerald-100 dark:bg-emerald-900/50 text-emerald-700 dark:text-emerald-300"
                                  : "text-slate-600 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800"
                          }`}
                      >
                        da
                      </Button>
                  */}
                  {/*
                    <Button
                        key={"en"}
                        variant="ghost"
                        size="sm"
                        onClick={() =>{
                          onUpdateLanguage("en");
                          setShowLanguage(false);
                        }

                        }
                        className={`text-xs font-medium rounded-lg transition-all ${
                            i18n.language === "en"
                                ? "bg-emerald-100 dark:bg-emerald-900/50 text-emerald-700 dark:text-emerald-300"
                                : "text-slate-600 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800"
                        }`}
                    >
                      en
                    </Button>
                  */}
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