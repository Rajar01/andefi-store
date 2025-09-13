<?php

namespace App\Filament\Resources\Products\Pages;

use App\Filament\Resources\Products\ProductResource;
use App\Models\Stock;
use Filament\Resources\Pages\CreateRecord;
use Illuminate\Support\Facades\Http;
use Illuminate\Support\Str;

class CreateProduct extends CreateRecord
{
    protected static string $resource = ProductResource::class;

    public function getRelationManagers(): array
    {
        return [];
    }

    protected function mutateFormDataBeforeCreate(array $data): array
    {
        $input = sprintf(
            "Product name: %s, product description: %s",
            $data["name"],
            $data["description"]
        );

        // Call text embedding API
        $response = Http::post(env("TEI_EMBEDDING_API_URL"), [
            "inputs" => $input,
        ]);

        // Create an initial stock record for the new product with default quantities
        $stock = Stock::create([
            'available_quantity' => 0,
            'quantity_on_hand' => 0,
            'reserved_quantity' => 0,
            'sold_quantity' => 0,
        ]);

        $data["id"] = Str::uuid7();
        $data["embedding"] = $response->successful() ? $response->json()[0] : array_fill(0, 1024, 0);
        $data["stock_id"] = $stock->id;

        return $data;
    }
}
