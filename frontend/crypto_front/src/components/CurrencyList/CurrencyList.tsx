import {Card, CardContent} from '../ui/card.tsx';
import type {Currency} from "../../utils/data.ts";
import { TrendingUp, TrendingDown, Check } from "lucide-react";
import type {CurrencySymbol} from "../../App.tsx";

interface CurrencyListProps {
  currencies: Currency[];
  selectedCurrencies: Currency[];
  onSelect: (currency: Currency) => void;
  isLoading: boolean;
  displayCurrency: CurrencySymbol;
  exchangeRate: number;
}

const CURRENCY_SYMBOLS: Record<CurrencySymbol, string> = {
  USD: "$",
  EUR: "€",
  GBP: "£",
  JPY: "¥",
  BTC: "₿",
};

function formatPrice(price: number, currency: CurrencySymbol, exchangeRate: number): string {
  const convertedPrice = price * exchangeRate;
  // console.log(exchangeRate);
  const formatterOptions: Intl.NumberFormatOptions = {
    minimumFractionDigits: currency === "BTC" ? 6 : currency === "JPY" ? 0 : 2,
    maximumFractionDigits: currency === "BTC" ? 8 : currency === "JPY" ? 0 : 2,
  };

  if (currency === "BTC") {
    return `${convertedPrice.toFixed(8)}`;
  }

  return new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: currency,
    ...formatterOptions,
  }).format(convertedPrice);
}

function CurrencySkeleton() {
  return (

    <Card className="animate-pulse bg-white dark:bg-slate-900 border-slate-200 dark:border-slate-800">
      <CardContent className="p-5">
        <div className="flex items-start justify-between mb-4">
          <div className="space-y-2">
            <div className="h-5 w-20 bg-slate-200 dark:bg-slate-700 rounded" />
            <div className="h-3 w-10 bg-slate-200 dark:bg-slate-700 rounded" />
          </div>
          <div className="h-8 w-8 bg-slate-200 dark:bg-slate-700 rounded-full" />
        </div>
        <div className="space-y-2">
          <div className="h-7 w-24 bg-slate-200 dark:bg-slate-700 rounded" />
          <div className="h-4 w-16 bg-slate-200 dark:bg-slate-700 rounded" />
        </div>
      </CardContent>
    </Card>
  );
}

export function CurrencyList({
  currencies,
  selectedCurrencies,
  onSelect,
  isLoading,
  displayCurrency,
  exchangeRate
}: CurrencyListProps) {
  if (isLoading) {
    return (
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-4">
        {[...Array(5)].map((_, i) => (
          <CurrencySkeleton key={i} />
        ))}
      </div>
    );
  }

  if (currencies.length === 0) {
    return (
      <div className="text-center py-12">
        <p className="text-slate-500 dark:text-slate-400">No currencies found</p>
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-2 max-block-48 ">
      {currencies.map((currency, idx:number) => {
        const isSelected = selectedCurrencies.some((c) => c.id === currency.id);
        const isPositive = currency.change24h >= 0;
        if(idx < 5)
        return (
          <Card
            key={currency.id}
            onClick={() => onSelect(currency)}
            className={`cursor-pointer transition-all duration-200 hover:scale-[1.02] relative ${
              isSelected
                ? "ring-2 ring-emerald-500 border-emerald-500/50 bg-emerald-50 dark:bg-emerald-950/30 "
                : "bg-white  border-slate-200 hover:border-slate-300 dark:border-slate-800 dark:bg-slate-900 dark:hover:border-slate-700"
            }`}
          >
            {isSelected && (
              <div className="absolute top-2 right-2 p-0.5 bg-emerald-500 rounded-full">
                <Check className="w-3 h-3 dark:text-white" />
              </div>
            )}
            <CardContent className="p-1 dark:bg-slate-800 ">
              <div className="flex items-start justify-between mb-4">
                <div>
                  <h3 className="font-semibold text-slate-900 dark:text-white">
                    {currency.name}
                  </h3>
                  <p className="text-xs text-slate-500 dark:text-slate-400 uppercase tracking-wider">
                    {currency.symbol}
                  </p>
                </div>
                <div
                  className={`p-1.5 rounded-lg ${
                    isPositive
                      ? "bg-emerald-100 dark:bg-emerald-900/30"
                      : "bg-rose-100 dark:bg-rose-900/30"
                  }`}
                >
                  {isPositive ? (
                    <TrendingUp className="w-4 h-4 text-emerald-600 dark:text-emerald-400" />
                  ) : (
                    <TrendingDown className="w-4 h-4 text-rose-600 dark:text-rose-400" />
                  )}
                </div>
              </div>

              <div className="space-y-1">
                <p className="text-2xl font-bold text-slate-900 dark:text-white">
                  {formatPrice(currency.price, displayCurrency, exchangeRate)}
                </p>
                <p
                  className={`text-sm font-medium ${
                    isPositive
                      ? "text-emerald-600 dark:text-emerald-400"
                      : "text-rose-600 dark:text-rose-400"
                  }`}
                >
                  {isPositive ? "+" : ""}
                  {currency.change24h.toFixed(2)}%
                </p>
              </div>
            </CardContent>
          </Card>
        );
      })}
    </div>
  );
}