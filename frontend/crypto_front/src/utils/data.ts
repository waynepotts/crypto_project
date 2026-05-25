import type {TimeframeValue} from "../App";
import {
  type CoinHistoryPointDto, type CoinHistoryResponseDto,
  type CoinMarketDataDto,
  type CoinResponseDto, getHistoryChart,
  getMarketCapRank,
} from "../generated/api.ts";
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
async function generateMockCurrencies2(): Promise<Currency[]> {

  const baseData = generateMockCurrencies();
  try {
    const response = await getMarketCapRank();
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-expect-error
    const data = response as CoinMarketDataDto[];
    for (let i = 0; i < Math.min(data.length, 5); i++) {
      console.log("data " + data[i].name);
      const base = baseData[i];
      const d: CoinMarketDataDto = data[i];
      if (d.coinId != null) base.rId = d.coinId;
      if (d.name != null) base.name = d.name;
      if (d.symbol != null) base.symbol = d.symbol;
      if (d.currentPrice != null) base.price = d.currentPrice;
      if (d.marketCap != null) base.marketCap = d.marketCap;

           if (d.priceChange24h != null && d.currentPrice != null) {
            base.change24h = (d.priceChange24h / d.currentPrice) * 100.0;
            base.basePrice = d.currentPrice + d.priceChange24h;
           }
    }
  } catch (e) {
    console.error("Failed to fetch market data, using mock data", e);
  }


  return baseData;
}

export default generateMockCurrencies2
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

export async function priceHistory(
    currency: Currency,
    timeframe: TimeframeValue,
    signal?: AbortSignal
): Promise<CoinHistory> {
  let dayCount: number = 1;
  if (timeframe === "1W") {
    dayCount = 7;
  } else if (timeframe === "30D") {
    dayCount = 30;
  } else if (timeframe === "90D") {
    dayCount = 90;
  }
  const ret: CoinHistoryPointDto[] = [];
  const resp = await getHistoryChart(currency.rId, {days: dayCount, daily: false}, { signal });
  const response = resp as CoinHistoryResponseDto;
  const coin = response.coinDto as CoinResponseDto;
  const chartData = response.chartData as CoinHistoryPointDto[];
  chartData.forEach((d) => {
    const timestamp:string = d.timestamp as string;
    d.timestamp = clampTime(toDate(timestamp)).toISOString();
    ret.push(d);
  });

  return {coin: coin, coinHistory: ret, currency: currency};
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

