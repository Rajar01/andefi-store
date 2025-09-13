<?php

namespace App\Filament\Resources\Stocks\Schemas;

use Filament\Forms\Components\Select;
use Filament\Forms\Components\TextInput;
use Filament\Schemas\Schema;

class StockForm
{
    public static function configure(Schema $schema): Schema
    {
        return $schema
            ->components([
                Select::make("id")
                    ->disabled()
                    ->relationship("product", "name")
                    ->preload()
                    ->columnSpanFull()
                    ->label(__("products.name")),
                TextInput::make('quantity_on_hand')
                    ->numeric()
                    ->default(0)
                    ->minValue(0)
                    ->afterStateUpdated(function ($old, $set, $state, $record) {
                        $available = $old > $state ? $record->available_quantity - abs($state - $old) : $record->available_quantity + abs($state - $old);
                        $set('available_quantity', max($available, 0));
                    })
                    ->label(__("stocks.quantity_on_hand")),
                TextInput::make('available_quantity')
                    ->numeric()
                    ->default(0)
                    ->readOnly()
                    ->label(__("stocks.available_quantity")),
                TextInput::make('reserved_quantity')
                    ->numeric()
                    ->default(0)
                    ->readOnly()
                    ->label(__("stocks.reserved_quantity")),
                TextInput::make('sold_quantity')
                    ->numeric()
                    ->default(0)
                    ->readOnly()
                    ->label(__("stocks.sold_quantity")),
            ]);
    }
}
