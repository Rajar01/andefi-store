<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsToMany;
use Illuminate\Database\Eloquent\Relations\HasMany;
use Illuminate\Database\Eloquent\Relations\HasOne;

class Product extends Model
{
    use HasUuids;

    protected $keyType = 'string';
    public $incrementing = false;

    protected $guarded = [];

    protected $casts = [
        'attributes' => 'array',
        'media_urls' => 'array',
        'embedding' => 'array',
    ];

    protected static function booted()
    {
        static::deleting(function ($product) {
            $product->categories()->detach();
        });
    }

    public function categories(): BelongsToMany
    {
        return $this->belongsToMany(Category::class, "product_category");
    }

    public function stock(): HasOne
    {
        return $this->hasOne(Stock::class, 'id', 'stock_id');
    }

    public function reviews(): HasMany
    {
        return $this->hasMany(Review::class, 'product_id', 'id');
    }
}
