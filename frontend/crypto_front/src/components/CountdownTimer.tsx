import { PieChart, Pie, Cell } from "recharts";

interface CountdownTimerProps {
  timeRemaining: number;
  total: number;
  theme: "light" | "dark";
}

export function CountdownTimer({ timeRemaining, total, theme }: CountdownTimerProps) {
  const percentage = (timeRemaining / total) * 100;
  const remainingAngle = (percentage / 100) * 360;

  const data = [
    { value: remainingAngle, name: "remaining" },
    { value: 360 - remainingAngle, name: "elapsed" },
  ];

  const remainingColor = theme === "dark" ? "#10b981" : "#10b981";
  const elapsedColor = theme === "dark" ? "#334155" : "#e2e8f0";

  return (
    <div className="relative flex items-center gap-2">
      <div className="relative w-10 h-10">
        <PieChart width={40} height={40}>
          <Pie
            data={data}
            cx={15}
            cy={15}
            innerRadius={12}
            outerRadius={16}
            startAngle={90}
            endAngle={-270}
            dataKey="value"
            stroke="none"
          >
            <Cell fill={remainingColor} />
            <Cell fill={elapsedColor} />
          </Pie>
        </PieChart>
        <div className="absolute inset-0 flex items-center justify-center">
          <span className="text-xs font-bold text-slate-700 dark:text-slate-300">
            { timeRemaining < (total / 4) ? timeRemaining : "" }
          </span>
        </div>
      </div>
    </div>
  );
}