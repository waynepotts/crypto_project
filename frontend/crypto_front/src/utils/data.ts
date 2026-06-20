import type {TimeframeValue} from "../App";
import {
    type CoinGeckoExchangeResponseDto,
    type CoinHistoryPointDto, type CoinHistoryResponseDto,
    type CoinMarketDataDto,
    type CoinResponseDto, getHistoryChart,
    getMarketCapRank,
    getExchangeRates,
} from "../generated/api.ts";
import type {CoinHistory} from "../types/ChartDisplayData.ts";

export interface Currency {
    id: number;
    coinId: number;
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

export async function getCryptoPrices(): Promise<Currency[]> {

    //const baseData:Currency[] = [];//  = generateMockCurrencies();
    const mappedData:Map<string, Currency> = new Map<string, Currency>();
    try {
        const response = await getMarketCapRank({start: 0, end: 250});
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-expect-error
        const data = response as CoinMarketDataDto[];
        for (let i = 0; i < data.length; i++) {
            const base:Currency = {};
            const d: CoinMarketDataDto = data[i];
            if(d.id!= null) base.id = d.id;
            if (d.coinId != null) base.coinId = d.coinId;
            if (d.name != null) base.name = d.name;
            if (d.symbol != null) base.symbol = d.symbol;
            if (d.currentPrice != null) base.price = d.currentPrice;
            if (d.marketCap != null) base.marketCap = d.marketCap;

            if (d.priceChange24h != null && d.currentPrice != null) {
                base.change24h = (d.priceChange24h / d.currentPrice) * 100.0;
                base.basePrice = d.currentPrice + d.priceChange24h;
                mappedData.set(base.symbol, base);
            }
        }
        // console.log(mappedData);
    } catch (e) {
        console.error("Failed to fetch market data, using mock data", e);
    }


    return [...mappedData.values()];
}

export function generateMockCurrencies(): Currency[] {
    const baseData = [
        {
            id: "bitcoin",
            rId: 1,
            name: "Bitcoin",
            symbol: "BTC",
            basePrice: 67432.50,
            marketCap: 1324000000000,
            color: "black"
        },
        {
            id: "ethereum",
            rId: 2,
            name: "Ethereum",
            symbol: "ETH",
            basePrice: 3521.80,
            marketCap: 423000000000,
            color: "black"
        },
        {
            id: "solana",
            rId: 3,
            name: "Solana",
            symbol: "SOL",
            basePrice: 172.45,
            marketCap: 78000000000,
            color: "black"
        },
        {
            id: "cardano",
            rId: 4,
            name: "Cardano",
            symbol: "ADA",
            basePrice: 0.62,
            marketCap: 22000000000,
            color: "black"
        },
        {
            id: "polkadot",
            rId: 5,
            name: "Polkadot",
            symbol: "DOT",
            basePrice: 7.85,
            marketCap: 10000000000,
            color: "black"
        },
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
    exchangeRate: number,
    signal?: AbortSignal
): Promise<CoinHistory> {
    let dayCount: number = 1;
    let chrono: number = 1;
    if(timeframe ==="1H"){
        dayCount = 1;
        chrono = 1;
    }
    else if (timeframe === "1W") {
        dayCount = 7;
        chrono = 2;
    } else if (timeframe === "30D") {
        dayCount = 30;
        chrono = 3;
    } else if (timeframe === "90D") {
        dayCount = 90;
        chrono = 3;
    }

    const ret: CoinHistoryPointDto[] = [];
    const resp = await getHistoryChart(currency.coinId, {days: dayCount, chronoUnit:chrono}, {signal});
    const response = resp as CoinHistoryResponseDto;
    const coin = response.coinDto as CoinResponseDto;
    const chartData = response.chartData as CoinHistoryPointDto[];
    chartData.forEach((d) => {
        const timestamp: string = d.timestamp as string;
        d.timestamp = toDate(timestamp).toISOString();
        d.price = d.price * exchangeRate;
        ret.push(d);
    });

    return {coin: coin, coinHistory: ret, currency: currency};
}

export async function getExchange(signal: AbortSignal): Promise<CoinGeckoExchangeResponseDto> {
    const response = await getExchangeRates({signal});
    return response as CoinGeckoExchangeResponseDto;
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

