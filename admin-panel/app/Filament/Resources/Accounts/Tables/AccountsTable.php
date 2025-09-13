<?php

namespace App\Filament\Resources\Accounts\Tables;

use Filament\Tables\Columns\TextColumn;
use Filament\Tables\Table;

class AccountsTable
{
    public static function configure(Table $table): Table
    {
        return $table
            ->columns([
                TextColumn::make('id')
                    ->copyable(),
                TextColumn::make('full_name')
                    ->copyable()
                    ->searchable()
                    ->label(__("accounts.full_name")),
                TextColumn::make('email')
                    ->copyable()
                    ->searchable()
                    ->label(__("accounts.email")),
                TextColumn::make('phone_number')
                    ->copyable()
                    ->searchable()
                    ->label(__("accounts.phone_number")),
                TextColumn::make('verified_at')
                    ->date("d F Y")
                    ->sortable()
                    ->label(__("accounts.verified_at")),
                TextColumn::make('created_at')
                    ->date("d F Y")
                    ->sortable()
                    ->label(__("accounts.created_at")),
            ]);
    }
}
