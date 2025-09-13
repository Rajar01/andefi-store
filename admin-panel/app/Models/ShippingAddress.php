<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Model;

class ShippingAddress extends Model
{
    use HasUuids;

    protected $keyType = 'string';
    public $incrementing = false;
    protected $guarded = [];
}
