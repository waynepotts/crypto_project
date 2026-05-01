import { CartesianGrid, Legend, Line, LineChart, XAxis, YAxis } from 'recharts';
import { RechartsDevtools } from '@recharts/devtools';
import React from 'react';
// #endregion

class DataPoint{
    constructor(name: string,
    uv: number,
    pv: number,
    amt: number) {
        this.name = name;
        this.uv = uv;
        this.pv = pv;
        this.amt = amt;
    }
    name: string;
    uv: number;
    pv: number;
    amt: number;
}
// #region Sample data

class CryptoChart extends React.Component {


// #endregion
    private readonly _data:DataPoint[];
    constructor() {
        super(0);
        this._data = [
            {
                name: 'Page A',
                uv: 400,
                pv: 2400,
                amt: 2400,
            },
            {
                name: 'Page B',
                uv: 300,
                pv: 4567,
                amt: 2400,
            },
            {
                name: 'Page C',
                uv: 320,
                pv: 1398,
                amt: 2400,
            },
            {
                name: 'Page D',
                uv: 200,
                pv: 9800,
                amt: 2400,
            },
            {
                name: 'Page E',
                uv: 278,
                pv: 3908,
                amt: 2400,
            },
            {
                name: 'Page F',
                uv: 189,
                pv: 4800,
                amt: 2400,
            },
        ];
    }
    render() {
        return (
            <LineChart
                style={{width: '100%', aspectRatio: 1.618, maxWidth: 600}}
                responsive
                data={this._data}
                margin={{
                    top: 20,
                    right: 20,
                    bottom: 5,
                    left: 0,
                }}
            >
                <CartesianGrid stroke="#aaa" strokeDasharray="5 5"/>
                <Line type="monotone" dataKey="uv" stroke="purple" strokeWidth={2} name="My data series name"/>
                <XAxis dataKey="name"/>
                <YAxis width="auto" label={{value: 'UV', position: 'insideLeft', angle: -90}}/>
                <Legend align="right"/>
                <RechartsDevtools/>
            </LineChart>
        );
    }
}

export default CryptoChart;