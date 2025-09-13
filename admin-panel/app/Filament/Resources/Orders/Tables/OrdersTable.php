<?php

namespace App\Filament\Resources\Orders\Tables;

use Filament\Actions\ViewAction;
use Filament\Tables\Columns\SelectColumn;
use Filament\Tables\Columns\TextColumn;
use Filament\Tables\Filters\SelectFilter;
use Filament\Tables\Table;

class OrdersTable
{
    public static function configure(Table $table): Table
    {
        return $table
            ->columns([
                TextColumn::make("id")
                    ->copyable(),
                TextColumn::make("account.full_name")
                    ->copyable()
                    ->searchable()
                    ->label(__("accounts.full_name")),
                TextColumn::make("grand_total")
                    ->money("IDR")
                    ->sortable()
                    ->label(__("orders.grand_total")),
                TextColumn::make("transaction_status")
                    ->placeholder("N/A")
                    ->formatStateUsing(fn($state) => match ($state) {
                        "CAPTURE" => __("orders.transaction_status.capture"),
                        "SETTLEMENT" => __("orders.transaction_status.settlement"),
                        "PENDING" => __("orders.transaction_status.pending"),
                        "DENY" => __("orders.transaction_status.deny"),
                        "CANCEL" => __("orders.transaction_status.cancel"),
                        "EXPIRE" => __("orders.transaction_status.expire"),
                        "FAILURE" => __("orders.transaction_status.failure"),
                        "REFUND" => __("orders.transaction_status.refund"),
                        "PARTIAL_REFUND" => __("orders.transaction_status.partial_refund"),
                        "AUTHORIZE" => __("orders.transaction_status.authorize"),
                        default => "N/A",
                    })
                    ->badge()
                    ->label(__("orders.transaction_status_label")),
                SelectColumn::make('order_status')
                    ->options([
                        'UNPAID' => __("orders.order_status.unpaid"),
                        'PAID' => __("orders.order_status.paid"),
                        'PACKED' => __("orders.order_status.packed"),
                        'SHIPPED' => __("orders.order_status.shipped"),
                        'COMPLETED' => __("orders.order_status.completed"),
                        'CANCELED' => __("orders.order_status.canceled"),
                        'REFUNDED' => __("orders.order_status.refunded"),
                        'PARTIAL_REFUNDED' => __("orders.order_status.partial_refunded"),
                    ])
                    ->label(__("orders.order_status_label")),
                TextColumn::make("created_at")
                    ->date("d F Y")
                    ->sortable()
                    ->label(__("orders.created_at")),
            ])
            ->filters([
                SelectFilter::make('order_status')
                    ->multiple()
                    ->options([
                        'UNPAID' => __("orders.order_status.unpaid"),
                        'PAID' => __("orders.order_status.paid"),
                        'PACKED' => __("orders.order_status.packed"),
                        'SHIPPED' => __("orders.order_status.shipped"),
                        'COMPLETED' => __("orders.order_status.completed"),
                        'CANCELED' => __("orders.order_status.canceled"),
                        'REFUNDED' => __("orders.order_status.refunded"),
                        'PARTIAL_REFUNDED' => __("orders.order_status.partial_refunded"),
                    ])
                    ->label(__("orders.order_status_label")),
                SelectFilter::make('transaction_status')
                    ->multiple()
                    ->options([
                        "CAPTURE" => __("orders.transaction_status.capture"),
                        "SETTLEMENT" => __("orders.transaction_status.settlement"),
                        "PENDING" => __("orders.transaction_status.pending"),
                        "DENY" => __("orders.transaction_status.deny"),
                        "CANCEL" => __("orders.transaction_status.cancel"),
                        "EXPIRE" => __("orders.transaction_status.expire"),
                        "FAILURE" => __("orders.transaction_status.failure"),
                        "REFUND" => __("orders.transaction_status.refund"),
                        "PARTIAL_REFUND" => __("orders.transaction_status.partial_refund"),
                        "AUTHORIZE" => __("orders.transaction_status.authorize"),
                    ])
                    ->label(__("orders.transaction_status_label")),
            ])
            ->recordActions([
                ViewAction::make(),
            ]);
    }
}
