import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  CartesianGrid,
  Legend,
} from "recharts";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "./ui/card.tsx";
import {Button} from './ui/button.tsx';
import type {ChartCurrency, TimeframeValue, CurrencySymbol} from "../App";
import { useState } from "react";
import { LineChartIcon, TableIcon, LayoutGridIcon, ListIcon } from "lucide-react";

interface PriceChartProps {
  data: { date: string; [key: string]: string | number }[];
  chartCurrencies: ChartCurrency[];
  onColorChange: (currencyId: string, color: string) => void;
  isLoading: boolean;
  timeframe: TimeframeValue;
  onTimeframeChange: (value: TimeframeValue) => void;
  showRelative: boolean;
  onToggleRelative: () => void;
  displayCurrency: CurrencySymbol;
  exchangeRate: number;
}

type ViewMode = "chart" | "table-standard" | "table-compact" | "table-detailed";

const TIMEFRAME_OPTIONS: { value: TimeframeValue; label: string }[] = [
  { value: "1H", label: "1H" },
  { value: "1D", label: "1D" },
  { value: "1W", label: "1W" },
  { value: "30D", label: "30D" },
  { value: "90D", label: "90D" },
];

const COLOR_PALETTE = [
  "#10b981", // emerald
  "#3b82f6", // blue
  "#f59e0b", // amber
  "#8b5cf6", // violet
  "#ec4899", // pink
  "#06b6d4", // cyan
  "#f97316", // orange
  "#84cc16", // lime
];

const CURRENCY_SYMBOLS: Record<CurrencySymbol, string> = {
  USD: "$",
  EUR: "€",
  GBP: "£",
  JPY: "¥",
  BTC: "₿",
};

function formatChartPrice(value: number, currency: CurrencySymbol, exchangeRate: number): string {
  const convertedValue = value * exchangeRate;

  if (currency === "BTC") {
    return `${CURRENCY_SYMBOLS[currency]}${convertedValue.toFixed(8)}`;
  }

  if (Math.abs(convertedValue) < 100 && Math.abs(convertedValue) > 0.01) {
    return new Intl.NumberFormat("en-US", {
      style: "currency",
      currency: currency,
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    }).format(convertedValue);
  }

  return new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: currency,
    minimumFractionDigits: 0,
    maximumFractionDigits: 0,
  }).format(convertedValue);
}

function ChartSkeleton() {
  return (
      <Card className="bg-white dark:bg-slate-900 border-slate-200 dark:border-slate-800">
        <CardHeader>
          <div className="h-6 w-48 bg-slate-200 dark:bg-slate-700 rounded animate-pulse" />
        </CardHeader>
        <CardContent>
          <div className="h-80 bg-slate-100 dark:bg-slate-800 rounded animate-pulse" />
        </CardContent>
      </Card>
  );
}

interface ViewModeButtonProps {
  mode: ViewMode;
  currentMode: ViewMode;
  icon: React.ReactNode;
  label: string;
  onClick: () => void;
}

function ViewModeButton({ mode, currentMode, icon, label, onClick }: ViewModeButtonProps) {
  const isActive = mode === currentMode;
  return (
      <Button
          variant="ghost"
          size="sm"
          onClick={onClick}
          className={`flex items-center gap-1.5 px-3 py-1.5 text-xs font-medium rounded-lg transition-all ${
              isActive
                  ? "bg-blue-100 dark:bg-blue-900/30 text-blue-700 dark:text-blue-300"
                  : "text-slate-500 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800"
          }`}
      >
        {icon}
        <span className="hidden sm:inline">{label}</span>
      </Button>
  );
}

interface TableViewProps {
  data: { date: string; [key: string]: string | number }[];
  chartCurrencies: ChartCurrency[];
  displayCurrency: CurrencySymbol;
  exchangeRate: number;
  timeframe: TimeframeValue;
  variant: "standard" | "compact" | "detailed";
}

function TableView({ data, chartCurrencies, displayCurrency, exchangeRate, timeframe, variant }: TableViewProps) {
  const currencySymbol = CURRENCY_SYMBOLS[displayCurrency];

  const formatDate = (dateStr: string) => {
    const date = new Date(dateStr);
    if (timeframe === "1H" || timeframe === "1D") {
      return date.toLocaleTimeString("en-US", { hour: "2-digit", minute: "2-digit" });
    }
    return date.toLocaleDateString("en-US", { month: "short", day: "numeric" });
  };

  const formatFullDate = (dateStr: string) => {
    const date = new Date(dateStr);
    return date.toLocaleDateString("en-US", {
      weekday: "short",
      month: "short",
      day: "numeric",
      year: "numeric",
    });
  };

  // Compact table - minimal styling
  if (variant === "compact") {
    return (
        <div className="overflow-x-auto">
          <table className="w-full text-sm">
            <thead>
            <tr className="border-b border-slate-200 dark:border-slate-700">
              <th className="text-left py-2 px-3 font-medium text-slate-500 dark:text-slate-400">Date</th>
              {chartCurrencies.map((c) => (
                  <th key={c.currency.id} className="text-right py-2 px-3 font-medium text-slate-500 dark:text-slate-400">
                    {c.currency.symbol}
                  </th>
              ))}
            </tr>
            </thead>
            <tbody>
            {data.slice(-20).map((row, idx) => (
                <tr key={idx} className="border-b border-slate-100 dark:border-slate-800/50 hover:bg-slate-50 dark:hover:bg-slate-800/30">
                  <td className="py-2 px-3 text-slate-600 dark:text-slate-300">{formatDate(row.date)}</td>
                  {chartCurrencies.map((c) => (
                      <td key={c.currency.id} className="text-right py-2 px-3 font-mono text-slate-700 dark:text-slate-300">
                        {formatChartPrice(row[`${c.currency.id}_price`] as number, displayCurrency, exchangeRate)}
                      </td>
                  ))}
                </tr>
            ))}
            </tbody>
          </table>
        </div>
    );
  }

  // Detailed table - with change calculations
  if (variant === "detailed") {
    const getChange = (currentIdx: number, currencyId: string) => {
      if (currentIdx === 0) return { value: 0, percent: 0 };
      const prevPrice = data[currentIdx - 1][`${currencyId}_price`] as number;
      const currPrice = data[currentIdx][`${currencyId}_price`] as number;
      const change = currPrice - prevPrice;
      const percent = prevPrice !== 0 ? ((change / prevPrice) * 100) : 0;
      return { value: change * exchangeRate, percent };
    };

    return (
        <div className="overflow-x-auto">
          <table className="w-full text-sm">
            <thead>
            <tr className="border-b border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800/50">
              <th className="text-left py-3 px-4 font-semibold text-slate-700 dark:text-slate-300">Date</th>
              {chartCurrencies.map((c) => (
                  <th key={c.currency.id} className="text-right py-3 px-4 font-semibold text-slate-700 dark:text-slate-300">
                    <div className="flex flex-col items-end gap-0.5">
                      <span>{c.currency.symbol}</span>
                      <span className="text-xs font-normal text-slate-400">Price ({currencySymbol})</span>
                    </div>
                  </th>
              ))}
              {chartCurrencies.length === 1 && (
                  <>
                    <th className="text-right py-3 px-4 font-semibold text-slate-700 dark:text-slate-300">
                      Change
                    </th>
                    <th className="text-right py-3 px-4 font-semibold text-slate-700 dark:text-slate-300">
                      Change %
                    </th>
                  </>
              )}
            </tr>
            </thead>
            <tbody>
            {data.slice(-30).map((row, idx) => {
              const dataArray = data.slice(-30);
              const change = chartCurrencies.length === 1 ? getChange(idx, chartCurrencies[0].currency.id) : null;

              return (
                  <tr key={idx} className="border-b border-slate-100 dark:border-slate-800/50 hover:bg-slate-50 dark:hover:bg-slate-800/30 transition-colors">
                    <td className="py-3 px-4">
                      <div className="flex flex-col">
                        <span className="font-medium text-slate-700 dark:text-slate-300">{formatDate(row.date)}</span>
                        <span className="text-xs text-slate-400 dark:text-slate-500">{formatFullDate(row.date)}</span>
                      </div>
                    </td>
                    {chartCurrencies.map((c) => (
                        <td key={c.currency.id} className="text-right py-3 px-4">
                      <span className="font-mono font-medium text-slate-700 dark:text-slate-300">
                        {formatChartPrice(row[`${c.currency.id}_price`] as number, displayCurrency, exchangeRate)}
                      </span>
                        </td>
                    ))}
                    {change && (
                        <>
                          <td className="text-right py-3 px-4">
                        <span className={`font-mono text-sm ${change.value >= 0 ? 'text-emerald-600 dark:text-emerald-400' : 'text-rose-600 dark:text-rose-400'}`}>
                          {change.value >= 0 ? '+' : ''}{formatChartPrice(Math.abs(change.value), displayCurrency, 1)}
                        </span>
                          </td>
                          <td className="text-right py-3 px-4">
                        <span className={`inline-flex items-center px-2 py-0.5 rounded text-xs font-medium ${
                            change.percent >= 0
                                ? 'bg-emerald-100 dark:bg-emerald-900/30 text-emerald-700 dark:text-emerald-400'
                                : 'bg-rose-100 dark:bg-rose-900/30 text-rose-700 dark:text-rose-400'
                        }`}>
                          {change.percent >= 0 ? '+' : ''}{change.percent.toFixed(2)}%
                        </span>
                          </td>
                        </>
                    )}
                  </tr>
              );
            })}
            </tbody>
          </table>
        </div>
    );
  }

  // Standard table - default styling
  return (
      <div className="overflow-x-auto">
        <table className="w-full text-sm">
          <thead>
          <tr className="border-b-2 border-slate-200 dark:border-slate-700">
            <th className="text-left py-3 px-4 font-semibold text-slate-700 dark:text-slate-300">#</th>
            <th className="text-left py-3 px-4 font-semibold text-slate-700 dark:text-slate-300">Date</th>
            {chartCurrencies.map((c) => (
                <th key={c.currency.id} className="text-right py-3 px-4 font-semibold text-slate-700 dark:text-slate-300">
                  <div className="flex items-center justify-end gap-2">
                    <div className="w-2 h-2 rounded-full" style={{ backgroundColor: c.color }} />
                    {c.currency.symbol} Price
                  </div>
                </th>
            ))}
          </tr>
          </thead>
          <tbody>
          {data.map((row, idx) => (
              <tr key={idx} className="border-b border-slate-100 dark:border-slate-800/50 hover:bg-slate-50 dark:hover:bg-slate-800/30 transition-colors">
                <td className="py-3 px-4 text-slate-400 dark:text-slate-500 font-mono text-xs">{idx + 1}</td>
                <td className="py-3 px-4">
                  <div className="flex flex-col">
                    <span className="font-medium text-slate-700 dark:text-slate-300">{formatDate(row.date)}</span>
                  </div>
                </td>
                {chartCurrencies.map((c) => (
                    <td key={c.currency.id} className="text-right py-3 px-4">
                  <span className="font-mono text-slate-700 dark:text-slate-300">
                    {formatChartPrice(row[`${c.currency.id}_price`] as number, displayCurrency, exchangeRate)}
                  </span>
                    </td>
                ))}
              </tr>
          ))}
          </tbody>
        </table>
      </div>
  );
}

export function PriceChart({
                             data,
                             chartCurrencies,
                             onColorChange,
                             isLoading,
                             timeframe,
                             onTimeframeChange,
                             showRelative,
                             onToggleRelative,
                             displayCurrency,
                             exchangeRate,
                           }: PriceChartProps) {
  const [viewMode, setViewMode] = useState<ViewMode>("chart");

  if (isLoading) {
    return <ChartSkeleton />;
  }

  if (chartCurrencies.length === 0) {
    return (
        <Card className="bg-white dark:bg-slate-900 border-slate-200 dark:border-slate-800">
          <CardContent className="py-16">
            <div className="text-center">
              <p className="text-slate-500 dark:text-slate-400">
                Select currencies from the list above to view their price history
              </p>
            </div>
          </CardContent>
        </Card>
    );
  }

  // Convert all values for the selected display currency
  const convertedData = data.map((d) => {
    const newPoint: { date: string; [key: string]: string | number } = { date: d.date };
    chartCurrencies.forEach((c) => {
      const originalValue = d[`${c.currency.id}_price`] as number;
      newPoint[`${c.currency.id}_price`] = originalValue * exchangeRate;
    });
    return newPoint;
  });

  const allValues = convertedData.flatMap((d) =>
      chartCurrencies.map((c) => d[`${c.currency.id}_price`] as number)
  );
  const minPrice = Math.min(...allValues);
  const maxPrice = Math.max(...allValues);

  const yAxisMin = showRelative
      ? Math.floor(minPrice - 5)
      : Math.floor((minPrice * 0.9) / 1000) * 1000;
  const yAxisMax = showRelative
      ? Math.ceil(maxPrice + 5)
      : Math.ceil((maxPrice * 1.1) / 1000) * 1000;

  const currencySymbol = CURRENCY_SYMBOLS[displayCurrency];

  return (
      <Card className="bg-white dark:bg-slate-900 border-slate-200 dark:border-slate-800 overflow-hidden">
        <CardHeader className="pb-3 flex flex-col gap-4">
          <div className="flex flex-wrap items-center justify-between gap-3">
            <CardTitle className="text-lg font-semibold text-slate-900 dark:text-white">
              Price History
              <span className="ml-2 text-sm font-normal text-slate-500 dark:text-slate-400">
              ({displayCurrency})
            </span>
            </CardTitle>

            <div className="flex flex-wrap items-center gap-2">
              {/* View Mode Selector */}
              <div className="flex items-center gap-1 p-1 bg-slate-100 dark:bg-slate-800 rounded-lg">
                <ViewModeButton
                    mode="chart"
                    currentMode={viewMode}
                    icon={<LineChartIcon className="w-4 h-4" />}
                    label="Chart"
                    onClick={() => setViewMode("chart")}
                />
                <ViewModeButton
                    mode="table-standard"
                    currentMode={viewMode}
                    icon={<TableIcon className="w-4 h-4" />}
                    label="Table"
                    onClick={() => setViewMode("table-standard")}
                />
                <ViewModeButton
                    mode="table-compact"
                    currentMode={viewMode}
                    icon={<LayoutGridIcon className="w-4 h-4" />}
                    label="Compact"
                    onClick={() => setViewMode("table-compact")}
                />
                <ViewModeButton
                    mode="table-detailed"
                    currentMode={viewMode}
                    icon={<ListIcon className="w-4 h-4" />}
                    label="Detailed"
                    onClick={() => setViewMode("table-detailed")}
                />
              </div>

              {viewMode === "chart" && (
                  <Button
                      variant="ghost"
                      size="sm"
                      onClick={onToggleRelative}
                      className={`px-3 py-1.5 text-xs font-medium rounded-lg transition-all ${
                          showRelative
                              ? "bg-violet-100 dark:bg-violet-900/30 text-violet-700 dark:text-violet-300"
                              : "text-slate-500 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800"
                      }`}
                  >
                    Relative %
                  </Button>
              )}

              {TIMEFRAME_OPTIONS.map((option) => (
                  <Button
                      key={option.value}
                      variant="ghost"
                      size="sm"
                      onClick={() => onTimeframeChange(option.value)}
                      className={`px-3 py-1.5 text-xs font-medium rounded-lg transition-all ${
                          timeframe === option.value
                              ? "bg-emerald-100 dark:bg-emerald-900/30 text-emerald-700 dark:text-emerald-300"
                              : "text-slate-500 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800"
                      }`}
                  >
                    {option.label}
                  </Button>
              ))}
            </div>
          </div>

          {/* Color Pickers - only show for chart view */}
          {viewMode === "chart" && (
              <div className="flex flex-wrap gap-3 pt-2">
                {chartCurrencies.map((item) => (
                    <div
                        key={item.currency.id}
                        className="flex items-center gap-2 px-3 py-1.5 rounded-lg bg-slate-50 dark:bg-slate-800/50"
                    >
                      <select
                          value={item.color}
                          onChange={(e) => onColorChange(item.currency.id, e.target.value)}
                          className="w-6 h-6 rounded cursor-pointer border-0 bg-transparent"
                          style={{ backgroundColor: item.color }}
                      >
                        {COLOR_PALETTE.map((color) => (
                            <option key={color} value={color} style={{ backgroundColor: color }}>
                              {color}
                            </option>
                        ))}
                      </select>
                      <span className="text-sm font-medium text-slate-700 dark:text-slate-300">
                  {item.currency.symbol}
                </span>
                    </div>
                ))}
              </div>
          )}
        </CardHeader>

        <CardContent>
          {viewMode === "chart" ? (
              <div className="h-80 w-full">
                <ResponsiveContainer width="100%" height="100%">
                  <LineChart
                      data={convertedData}
                      margin={{ top: 10, right: 10, left: 0, bottom: 0 }}
                  >
                    <CartesianGrid
                        strokeDasharray="3 3"
                        stroke="#334155"
                        vertical={false}
                    />
                    <XAxis
                        dataKey="date"
                        tick={{ fontSize: 11, fill: "#64748b" }}
                        tickLine={false}
                        axisLine={{ stroke: "#334155" }}
                        tickFormatter={(value) => {
                          const date = new Date(value);
                          if (timeframe === "1H" || timeframe === "1D") {
                            return date.toLocaleTimeString("en-US", { hour: "2-digit", minute: "2-digit" });
                          }
                          return date.toLocaleDateString("en-US", { month: "short", day: "numeric" });
                        }}
                    />
                    <YAxis
                        domain={[yAxisMin, yAxisMax]}
                        tick={{ fontSize: 11, fill: "#64748b" }}
                        tickLine={false}
                        axisLine={{ stroke: "#334155" }}
                        tickFormatter={(value) =>
                            showRelative
                                ? `${value.toFixed(8)}%`
                                : displayCurrency === "BTC"
                                    ? `${currencySymbol}${value.toFixed(6)}`
                                    : `${currencySymbol}${(value / 1000).toFixed(0)}k`
                        }
                        width={50}
                    />
                    <Tooltip
                        contentStyle={{
                          backgroundColor: "#1e293b",
                          border: "none",
                          borderRadius: "8px",
                          boxShadow: "0 4px 6px -1px rgb(0 0 0 / 0.1)",
                          padding: "12px",
                        }}
                        labelStyle={{
                          color: "#94a3b8",
                          fontSize: "12px",
                          marginBottom: "4px",
                        }}
                        labelFormatter={(label) => {
                          const date = new Date(label);
                          if (timeframe === "1H" || timeframe === "1D") {
                            return date.toLocaleString("en-US", {
                              weekday: "short",
                              hour: "2-digit",
                              minute: "2-digit",
                            });
                          }
                          return date.toLocaleDateString("en-US", {
                            weekday: "short",
                            month: "short",
                            day: "numeric",
                          });
                        }}
                        formatter={(value: number, name: string) => {
                          const currencyId = name.replace("_price", "");
                          const currency = chartCurrencies.find((c) => c.currency.id === currencyId);
                          return [
                            showRelative
                                ? `${value.toFixed(2)}%`
                                : formatChartPrice(value, displayCurrency, 1),
                            currency?.currency.symbol || "",
                          ];
                        }}
                    />
                    <Legend
                        formatter={(value: string) => {
                          const currencyId = value.replace("_price", "");
                          const currency = chartCurrencies.find((c) => c.currency.id === currencyId);
                          return (
                              <span style={{ color: currency?.color || "#94a3b8" }}>
                        {currency?.currency.symbol || value}
                      </span>
                          );
                        }}
                    />
                    {chartCurrencies.map((item) => (
                        <Line
                            key={item.currency.id}
                            type="monotone"
                            dataKey={`${item.currency.id}_price`}
                            stroke={item.color}
                            strokeWidth={2}
                            dot={false}
                            activeDot={{
                              r: 5,
                              fill: item.color,
                              stroke: "#1e293b",
                              strokeWidth: 2,
                            }}
                        />
                    ))}
                  </LineChart>
                </ResponsiveContainer>
              </div>
          ) : (
              <div className="max-h-96 overflow-y-auto">
                <TableView
                    data={data}
                    chartCurrencies={chartCurrencies}
                    displayCurrency={displayCurrency}
                    exchangeRate={exchangeRate}
                    timeframe={timeframe}
                    variant={viewMode === "table-standard" ? "standard" : viewMode === "table-compact" ? "compact" : "detailed"}
                />
              </div>
          )}
        </CardContent>
      </Card>
  );
}

export default PriceChart
