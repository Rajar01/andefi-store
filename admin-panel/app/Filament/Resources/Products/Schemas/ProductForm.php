<?php

namespace App\Filament\Resources\Products\Schemas;

use Filament\Forms\Components\FileUpload;
use Filament\Forms\Components\KeyValue;
use Filament\Forms\Components\Select;
use Filament\Forms\Components\Textarea;
use Filament\Forms\Components\TextInput;
use Filament\Schemas\Components\Flex;
use Filament\Schemas\Components\Section;
use Filament\Schemas\Schema;
use Filament\Support\RawJs;
use Illuminate\Support\Facades\Storage;

class ProductForm
{
    public static function configure(Schema $schema): Schema
    {
        // TODO: Not allow input comma in discount percentage input
        // TODO: Add microsecond to created_at and updated_at column
        // TODO: Delete file in storage while deleted or detact the asset from product
        return $schema
            ->components([
                Section::make(__("products.information"))->schema([
                    Flex::make([
                        TextInput::make('name')->label(__("products.name"))->required(),
                        Select::make("category_id")
                            ->relationship("categories", "name")
                            ->multiple()
                            ->preload()
                            ->required()
                            ->label(__("products.categories.name"))
                            ->required(),
                    ])->columnSpanFull()->from("sm"),
                    Flex::make([
                        TextInput::make('price')
                            ->mask(RawJs::make('$money($input)'))
                            ->stripCharacters(',')
                            ->prefix("Rp")
                            ->numeric()
                            ->integer()
                            ->minValue(0)
                            ->reactive()
                            ->required()
                            ->label(__("products.price")),
                        TextInput::make('discount_percentage')
                            ->default(0)
                            ->suffix("%")
                            ->integer()
                            ->numeric()
                            ->minValue(0)
                            ->maxValue(100)
                            ->label(__("products.discount_percentage")),
                    ])->columnSpanFull(),
                    Textarea::make('description')
                        ->required()
                        ->columnSpanFull()
                        ->rows(5)
                        ->label(__("products.description")),
                    KeyValue::make("attributes")
                        ->keyLabel(__("products.specification_name"))
                        ->valueLabel(__("products.specification_value"))
                        ->addActionLabel(__("products.add_specification"))
                        ->reorderable()
                        ->columnSpanFull()
                        ->label(__("products.specifications")),
                ])->columnSpanFull(),
                Section::make(__("products.media_urls.title"))->schema([
                    FileUpload::make('media_urls.primary_image')
                        ->directory('product/primary')
                        ->acceptedFileTypes(['image/png', 'image/jpg', 'image/jpeg'])
                        ->formatStateUsing(fn($state) => $state ? str_replace(url('storage') . '/', '', $state) : null)
                        ->dehydrateStateUsing(fn($state) => $state ? Storage::url($state) : null)
                        ->required()
                        ->image()
                        ->imageEditor()
                        ->label(__("products.media_urls.primary_image")),
                    FileUpload::make('media_urls.catalog_image')
                        ->reorderable()
                        ->appendFiles(true)
                        ->panelLayout(layout: 'grid')
                        ->directory('product/catalog')
                        ->acceptedFileTypes(['image/png', 'image/jpg', 'image/jpeg'])
                        ->multiple()
                        ->formatStateUsing(function ($state) {
                            if (is_array($state)) {
                                return collect($state)->map(fn($path) => str_replace(url('storage') . '/', '', $path))->toArray();
                            }

                            return $state ? str_replace(url('storage') . '/', '', $state) : null;
                        })
                        ->dehydrateStateUsing(function ($state) {
                            if (is_array($state)) {
                                return collect($state)->map(fn($path) => Storage::url($path))->toArray();
                            }

                            return $state ? Storage::url($state) : null;
                        })
                        ->required()
                        ->image()
                        ->imageEditor()
                        ->minFiles(3)
                        ->maxFiles(20)
                        ->label(__("products.media_urls.catalog_image")),
                    FileUpload::make('media_urls.model')
                        ->directory('product/3d-model')
                        ->maxSize(10240)
                        ->acceptedFileTypes(['model/gltf-binary'])
                        ->previewable(false)
                        ->formatStateUsing(fn($state) => $state ? str_replace(url('storage') . '/', '', $state) : null)
                        ->dehydrateStateUsing(fn($state) => $state ? Storage::url($state) : null)
                        ->label(__("products.media_urls.model"))
                ])->columnSpanFull(),
            ]);
    }
}
