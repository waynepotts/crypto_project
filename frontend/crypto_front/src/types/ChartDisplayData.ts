import type {CoinHistoryPointDto, CoinResponseDto} from "../generated/api.ts";
import type {Currency} from "../utils/data.ts";
import type {TimeframeValue} from "../App.tsx";

export interface CoinHistory {
    coin: CoinResponseDto;
    coinHistory: CoinHistoryPointDto[];
    currency: Currency;
}
export interface ChartDisplayData {
    coinId:number; // TODO: should be able to remove
    timestamp:string;
    prices: Record<string,number>;
    currencies: Currency[]; // TODO: should be able to remove
}

export function createChartHistoryData(data:CoinHistory[], exchangeRate:number,  isRelative:boolean, frame:TimeframeValue): ChartDisplayData[] {
    const dataMap = new Map<string, ChartDisplayData>();
    const oldest = getOldestTime(frame, new Date());
    for(let i = 0; i < data.length; i++) {
        const d = data[i];
        if(d) {
            let first:number = 1;
            d.coinHistory.forEach((h, idx) => {
                if(idx === 0) {
                    first = h.price;
                }
                const date: Date = new Date(h.timestamp as string );

                if(date >= oldest) {
                    const date: string = h.timestamp as string;
                    const cData: ChartDisplayData = (dataMap.has(date) ? dataMap.get(date)  :
                        {timestamp: date, coinId: d.coin.id, currencies: []}) as ChartDisplayData;
                    if (cData.currencies && !cData.currencies.some(c => c.id === d.currency.id)) {
                        cData.currencies.push(d.currency);
                    } else {
                        cData.currencies = [];
                        cData.currencies.push(d.currency);
                    }
                    let price: number = isRelative ? h.price / first : h.price;

                    if (!isRelative) {
                        price = price * exchangeRate;
                    }
                    if(cData.prices) {
                        cData.prices[d.coin.symbol as string] = price;
                    } else{
                        cData.prices = {[d.coin.symbol as string]:price};
                    }

                    cData.currencies.push(d.currency);
                    // eval('cData.coin' + d.coin.symbol + ' =  price');
                    dataMap.set(date, cData);
                    //console.log(cData);
                }
            });
        }
    }

    const keys = Array.from(dataMap.keys());
    const ret: ChartDisplayData[] = [];
    keys.sort().forEach(k=> ret.push(dataMap.get(k) as ChartDisplayData));
    return ret;
}

function getOldestTime(frame:TimeframeValue, date:Date):Date {
    let millis = 0;
    switch(frame){
        case "1H":
            millis = 60 * 60 * 1000;
            break;
        case "1D":
            millis = 24 * 60 * 60 * 1000;
            break;
        case "1W":
            millis = 7 * 24 * 60 * 60 * 1000;
            break;
        case "30D":
            millis = 30 * 24 * 60 * 60 * 1000;
            break;
        case "90D":
            millis = 90 * 24 * 60 * 60 * 1000;
            break;
    }
    return new Date(date.getTime() - millis);
}
