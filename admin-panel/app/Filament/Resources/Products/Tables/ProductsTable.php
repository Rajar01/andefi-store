<?php

namespace App\Filament\Resources\Products\Tables;

use Filament\Actions\BulkActionGroup;
use Filament\Actions\DeleteBulkAction;
use Filament\Actions\EditAction;
use Filament\Actions\ViewAction;
use Filament\Tables\Columns\ImageColumn;
use Filament\Tables\Columns\TextColumn;
use Filament\Tables\Table;
use Illuminate\Database\Eloquent\Builder;
use Illuminate\Support\Collection;

class ProductsTable
{
    public static function configure(Table $table): Table
    {
        // TODO: Display product primary image in the table
        // TODO: Add filter by category name
        return $table
            ->modifyQueryUsing(function (Builder $query) {
                $query->with(["stock", "categories"]);
            })
            ->defaultSort('id', 'desc')
            ->columns([
                TextColumn::make("id")
                    ->copyable(),
                ImageColumn::make("media_urls.primary_image")
                    ->imageSize(50)
                    ->alignCenter()
                    ->label(__("products.media_urls.primary_image")),
                TextColumn::make("name")
                    ->label(__("products.name"))
                    ->copyable()
                    ->searchable(),
                TextColumn::make("categories.name")
                    ->label(__("products.categories.name"))
                    ->badge()
                    ->wrap(),
                TextColumn::make("price")
                    ->money("IDR")->label(__("products.price"))
                    ->sortable(),
                TextColumn::make("discount_percentage")->suffix(" %")
                    ->alignCenter()
                    ->label(__("products.discount_percentage"))
                    ->sortable(),
                TextColumn::make("created_at")
                    ->date("d F Y")
                    ->label(__("products.created_at"))
                    ->sortable(),
            ])
            ->filters([])
            ->recordActions([
                ViewAction::make(),
                EditAction::make(),
            ])
            ->toolbarActions([
                BulkActionGroup::make([
                    DeleteBulkAction::make()->action(function (Collection $records) {
                        $records->each(function ($record) {
                            $record->delete();

                            $record->stock()->delete();
                        });
                    }),
                ]),
            ]);
    }
}
