import type {TimeframeValue} from "../App";
import {
  type CoinHistoryPointDto,
  type CoinHistoryResponseDto,
  type CoinMarketDataDto,
  type CoinResponseDto,
  type ErrorResponseDto,
  getAllCoins,
  type getAllCoinsResponse, getHistoryChart, type getHistoryChartResponse, type getHistoryChartResponse200,
  type getHistoryChartResponse404, type getHistoryChartResponse409, type getHistoryChartResponse500,
  getMarketCapRank, type getMarketCapRankResponse
} from "../generated/api.ts";
import {generateMockData} from "@recharts/devtools";
import {stringify} from "postcss";
import {data} from "autoprefixer";
import type {CoinHistory} from "../types/ChartDisplayData.ts";

export interface Currency {
  id: string;
  rId: number;
  name: string;
  symbol: string;
  price: number;
  change24h: number;
  marketCap: number;
  basePrice: number;
  color: string;
}

export interface TimeframeParams {
  days: number;
  intervalMinutes: number;
}
export function generateMockCurrencies2(): Currency[] {

  const response:Promise<getMarketCapRankResponse> = getMarketCapRank();

  console.log("response: " + response + JSON.stringify(response));
  const baseData = generateMockCurrencies();
  response.then((data) => {
      console.log("response: " + JSON.stringify(data));
      for (let i = 0; i < 5; i++) {
        const base = baseData[i];
        const d:CoinMarketDataDto = data[i];
        if (d.id != null) {
          base.rId = d.coinId;
        }
        if (d.name != null) {
          base.name = d.name;
        }
        if (d.symbol != null) {
          base.symbol = d.symbol;
        } // d.symbol;
        //base.id = d.id?.toString();
        if (d.currentPrice != null) {
          base.price = d.currentPrice;
        }
        if (d.marketCap != null) {
          base.marketCap = d.marketCap;
        }
        if (d.priceChange24h != null && d.currentPrice != null) {
          base.change24h = (d.priceChange24h / d.currentPrice) * 100.0;
          base.basePrice = d.currentPrice + d.priceChange24h;
        }
      }

  });


  return baseData;
}
export function generateMockCurrencies(): Currency[] {
  const baseData = [
    { id: "bitcoin", rId:1, name: "Bitcoin", symbol: "BTC", basePrice: 67432.50, marketCap: 1324000000000, color:"black" },
    { id: "ethereum", rId:2, name: "Ethereum", symbol: "ETH", basePrice: 3521.80, marketCap: 423000000000, color:"black" },
    { id: "solana", rId:3, name: "Solana", symbol: "SOL", basePrice: 172.45, marketCap: 78000000000, color:"black" },
    { id: "cardano", rId:4, name: "Cardano", symbol: "ADA", basePrice: 0.62, marketCap: 22000000000, color:"black" },
    { id: "polkadot", rId:5, name: "Polkadot", symbol: "DOT", basePrice: 7.85, marketCap: 10000000000, color:"black" },
  ];

  return baseData.map((coin) => ({
    ...coin,
    price: coin.basePrice * (1 + (Math.random() - 0.5) * 0.02),
    change24h: (Math.random() - 0.4) * 10,
  }));
}

function getTimeframeParams(timeframe: TimeframeValue): TimeframeParams {
  switch (timeframe) {
    case "1H":
      return { days: 1 / 24, intervalMinutes: 2 };
    case "1D":
      return { days: 1, intervalMinutes: 30 };
    case "1W":
      return { days: 7, intervalMinutes: 0 };
    case "30D":
      return { days: 30, intervalMinutes: 0 };
    case "90D":
      return { days: 90, intervalMinutes: 0 };
    default:
      return { days: 30, intervalMinutes: 0 };
  }
}

export async function priceHistory(
    currency: Currency,
    // currencies: { basePrice: number; id: string }[],
    timeframe: TimeframeValue
): Promise<CoinHistory> {
  let dayCount: number = 1;
  if (timeframe === "1D") {
    dayCount = 1;
  } else if (timeframe === "1W") {
    dayCount = 7;
  } else if (timeframe === "30D") {
    dayCount = 30;
  } else if (timeframe === "90D") {
    dayCount = 90;
  }
  let ret: CoinHistoryPointDto[] = [];
  let coin:CoinResponseDto = null;
    const response = await getHistoryChart(currency.rId, {days: dayCount, daily: false});
    if (response && typeof response.completeness === "number") {
      coin = response.coinDto;
      response.chartData.forEach((d) => {
        d.timestamp = clampTime(toDate(d.timestamp)).toISOString();
        ret.push(d);
      })
    }
    return  {coin:coin, coinHistory: ret, currency: currency};
}
export function generatePriceHistory(
  currencies: { basePrice: number; id: string }[],
  timeframe: TimeframeValue
): { date: string; [key: string]: string | number }[] {
  const { days } = getTimeframeParams(timeframe);
  const now = new Date();

  let points: number;
  let stepMs: number;

  if (timeframe === "1H") {
    points = 30;
    stepMs = (60 * 60 * 1000) / points;
  } else if (timeframe === "1D") {
    points = 48;
    stepMs = (24 * 60 * 60 * 1000) / points;
  } else {
    points = Math.min(Math.floor(days), 90);
    stepMs = 24 * 60 * 60 * 1000;
  }

  // Initialize price tracks for each currency
  const priceTracks: Map<string, { prices: number[]; current: number }> = new Map();

  currencies.forEach((c) => {
    const startPrice = c.basePrice * 0.85;
    const prices: number[] = [];
    let current = startPrice;

    for (let i = 0; i < points; i++) {
      const volatility = c.basePrice * 0.015;
      const change = (Math.random() - 0.48) * volatility;
      current = Math.max(current + change, c.basePrice * 0.7);
      prices.push(Math.round(current * 100) / 100);
    }

    // Set last price to current base price
    prices[points - 1] = c.basePrice;
    priceTracks.set(c.id, { prices, current: startPrice });
  });

  // Build data array
  const data: { date: string; [key: string]: string | number }[] = [];

  for (let i = 0; i < points; i++) {
    const timestamp = new Date(now.getTime() - (points - 1 - i) * stepMs);
    const point: { date: string; [key: string]: string | number } = {
      date: timestamp.toISOString(),
    };

    currencies.forEach((c) => {
      const track = priceTracks.get(c.id);
      if (track) {
        point[`${c.id}_price`] = track.prices[i];
      }
    });

    data.push(point);
  }

  return data;
}

export function formatMarketCap(value: number): string {
  if (value >= 1e12) {
    return `$${(value / 1e12).toFixed(2)}T`;
  }
  if (value >= 1e9) {
    return `$${(value / 1e9).toFixed(2)}B`;
  }
  if (value >= 1e6) {
    return `$${(value / 1e6).toFixed(2)}M`;
  }
  return `$${value.toLocaleString()}`;
}

export function toDate(str: string): Date {
  return new Date(str);
}

export function clampTime(date:Date): Date{
  const ret = new Date(date);
  const minutes:number = Number((date.getMinutes() / 5).toFixed(0));

  ret.setMinutes(minutes, 0 ,0 );
  return ret;
}

