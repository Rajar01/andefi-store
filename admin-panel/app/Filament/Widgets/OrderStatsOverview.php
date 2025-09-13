<?php

namespace App\Filament\Widgets;

use App\Models\Order;
use Filament\Widgets\StatsOverviewWidget;
use Filament\Widgets\StatsOverviewWidget\Stat;

class OrderStatsOverview extends StatsOverviewWidget
{
    protected function getStats(): array
    {
        return [
            Stat::make(__("orders.total_orders"), Order::count()),
            Stat::make(__("orders.shipped_orders"), Order::where('order_status', 'SHIPPED')->count()),
            Stat::make(__("orders.completed_orders"), Order::where('order_status', 'COMPLETED')->count()),
            Stat::make(__("orders.total_revenue_completed"), 'Rp ' . number_format(Order::where('order_status', 'COMPLETED')->sum('grand_total'), 0, ',', '.')),
        ];
    }
}
