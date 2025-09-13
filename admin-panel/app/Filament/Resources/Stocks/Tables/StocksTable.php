<?php

namespace App\Filament\Resources\Stocks\Tables;

use Filament\Actions\EditAction;
use Filament\Tables\Columns\TextColumn;
use Filament\Tables\Table;

class StocksTable
{
    public static function configure(Table $table): Table
    {
        return $table
            ->columns([
                TextColumn::make("id")
                    ->copyable(),
                TextColumn::make("product.name")
                    ->copyable()
                    ->searchable()
                    ->label(__("products.name")),
                TextColumn::make('available_quantity')
                    ->numeric()
                    ->sortable()
                    ->label(__("stocks.available_quantity")),
                TextColumn::make('quantity_on_hand')
                    ->numeric()
                    ->sortable()
                    ->label(__("stocks.quantity_on_hand")),
                TextColumn::make('reserved_quantity')
                    ->numeric()
                    ->sortable()
                    ->label(__("stocks.reserved_quantity")),
                TextColumn::make('sold_quantity')
                    ->numeric()
                    ->sortable()
                    ->label(__("stocks.sold_quantity")),
                TextColumn::make('created_at')
                    ->dateTime()
                    ->sortable()
                    ->toggleable(isToggledHiddenByDefault: true)
                    ->label(__("stocks.created_at")),
                TextColumn::make('updated_at')
                    ->dateTime()
                    ->sortable()
                    ->toggleable(isToggledHiddenByDefault: true)
                    ->label(__("stocks.updated_at")),
            ])
            ->filters([])
            ->recordActions([
                EditAction::make(),
            ])
            ->toolbarActions([]);
    }
}
