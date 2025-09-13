<?php

namespace App\Filament\Pages;

use App\Filament\Widgets\LatestOrders;
use App\Filament\Widgets\OrderStatsOverview;
use Filament\Pages\Dashboard as BaseDashboard;

class DashboardCustom extends BaseDashboard
{
    public static function getNavigationIcon(): string
    {
        return 'heroicon-m-home';
    }

    public function getWidgets(): array
    {
        return [
            OrderStatsOverview::class,
            LatestOrders::class
        ];
    }
}