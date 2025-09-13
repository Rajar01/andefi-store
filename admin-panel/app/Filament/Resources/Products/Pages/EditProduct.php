<?php

namespace App\Filament\Resources\Products\Pages;

use App\Filament\Resources\Products\ProductResource;
use App\Models\Product;
use Filament\Actions\DeleteAction;
use Filament\Actions\ViewAction;
use Filament\Resources\Pages\EditRecord;
use Illuminate\Support\Facades\Http;

class EditProduct extends EditRecord
{
    protected static string $resource = ProductResource::class;

    protected function getHeaderActions(): array
    {
        return [
            ViewAction::make(),
            DeleteAction::make()
                ->action(function (Product $record): void {
                    $record->delete();
                    $record->stock()->delete();
                }),
        ];
    }

    public function getRelationManagers(): array
    {
        return [];
    }

    protected function mutateFormDataBeforeSave(array $data): array
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

        if ($response->successful()) {
            $data["embedding"] = $response->json()[0];
        }

        return $data;
    }
}
