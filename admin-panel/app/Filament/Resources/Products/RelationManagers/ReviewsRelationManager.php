<?php

namespace App\Filament\Resources\Products\RelationManagers;

use Filament\Actions\CreateAction;
use Filament\Resources\RelationManagers\RelationManager;
use Filament\Tables\Columns\TextColumn;
use Filament\Tables\Table;

// TODO: Add average rating, total ratings, and total reviews widget 
class ReviewsRelationManager extends RelationManager
{
    protected static string $relationship = 'reviews';

    public function table(Table $table): Table
    {
        return $table
            ->heading(__("reviews.plural"))
            ->columns([
                TextColumn::make('id'),
                TextColumn::make('orderItem.order.id')->label(__("reviews.order_id")),
                TextColumn::make('account.full_name')->label(__("reviews.account_name")),
                TextColumn::make('rating')->sortable()->label(__("reviews.rating")),
                TextColumn::make('content')->label(__("reviews.review_content")),
            ])
            ->headerActions([
                CreateAction::make(),
            ]);
    }

    public function isReadOnly(): bool
    {
        return true;
    }
}
