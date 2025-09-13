<?php

namespace App\Filament\Resources\Orders\Schemas;

use Filament\Forms\Components\DatePicker;
use Filament\Forms\Components\Repeater;
use Filament\Forms\Components\Select;
use Filament\Forms\Components\Textarea;
use Filament\Forms\Components\TextInput;
use Filament\Schemas\Components\Flex;
use Filament\Schemas\Components\Grid;
use Filament\Schemas\Components\Section;
use Filament\Schemas\Schema;
use Filament\Support\RawJs;
use Filament\Tables\Columns\TextColumn;
use Illuminate\Support\Carbon;

class OrderForm
{
    public static function configure(Schema $schema): Schema
    {
        return $schema
            ->components([
                Section::make(__("orders.order_information"))
                    ->schema([
                        Flex::make([
                            TextInput::make("id"),
                            Select::make("account.full_name")
                                ->placeholder("N/A")
                                ->relationship("account", "full_name")
                                ->preload()
                                ->label(__("orders.customer_name")),
                        ])->columnSpanFull()->from("lg"),
                        Flex::make([
                            Select::make("order_status")
                                ->placeholder("N/A")
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
                            TextInput::make("transaction_status")
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
                                    default => $state,
                                })
                                ->placeholder("N/A")
                                ->label(__("orders.transaction_status_label")),
                        ])->columnSpanFull()->from("lg"),
                        Flex::make([
                            TextInput::make("payment_currency")
                                ->placeholder("N/A")
                                ->label(__("orders.payment_currency")),
                            TextInput::make("payment_method")
                                ->placeholder("N/A")
                                ->label(__("orders.payment_method")),
                        ])->columnSpanFull()->from("lg"),
                        Flex::make([
                            TextInput::make("grand_total")
                                ->mask(RawJs::make('$money($input)'))
                                ->stripCharacters(',')
                                ->prefix("Rp")
                                ->numeric()
                                ->integer()
                                ->label(__("orders.grand_total")),
                            TextInput::make("sub_total")
                                ->mask(RawJs::make('$money($input)'))
                                ->stripCharacters(',')
                                ->prefix("Rp")
                                ->numeric()
                                ->integer()
                                ->label(__("orders.sub_total")),
                            TextInput::make("total_discount")
                                ->mask(RawJs::make('$money($input)'))
                                ->stripCharacters(',')
                                ->prefix("Rp")
                                ->numeric()
                                ->integer()
                                ->label(__("orders.total_discount")),
                        ])->columnSpanFull()->from("lg"),
                        Grid::make()->columns(['lg' => 2, 'xl' => 4, '2xl' => 4,])->components([
                            DatePicker::make('created_at')
                                ->native(false)
                                ->displayFormat("d F Y")
                                ->readOnly()
                                ->label(__("orders.created_at")),
                            DatePicker::make("paid_at")
                                ->native(false)
                                ->placeholder("N/A")
                                ->displayFormat("d F Y")
                                ->readOnly()
                                ->label(__("orders.paid_at")),
                            DatePicker::make("shipped_at")
                                ->native(false)
                                ->placeholder("N/A")
                                ->displayFormat('d F Y')
                                ->readOnly()
                                ->label(__("orders.shipped_at")),
                            DatePicker::make("completed_at")
                                ->native(false)
                                ->placeholder("N/A")
                                ->displayFormat('d F Y')
                                ->readOnly()
                                ->label(__("orders.completed_at")),
                        ]),
                    ])->columnSpanFull(),
                Section::make(__("orders.order_items"))
                    ->components([
                        Repeater::make('orderItems')
                            ->relationship("orderItems")
                            ->schema([
                                TextInput::make('id'),
                                TextInput::make('product_name')->label(__("products.name")),
                                TextInput::make('quantity')->label(__("orders.order_items_quantity")),
                            ])
                            ->hiddenLabel()
                            ->columns(3)
                            ->addable(false)
                    ])->columnSpanFull(),
                Section::make(__("orders.shipping_address_information"))
                    ->components([
                        Textarea::make("shipping_address")
                            ->columnSpanFull()
                            ->autosize()
                            ->placeholder("N/A")
                            ->label(__("orders.shipping_address")),
                        TextInput::make("shipping_postal_code")
                            ->columnSpanFull()
                            ->placeholder("N/A")
                            ->label(__("orders.shipping_address_postal_code")),
                    ])->columnSpanFull(),
            ]);
    }
}
