<?php

namespace App\Filament\Pages;

use Filament\Auth\Pages\Login;
use Filament\Schemas\Schema;
use Illuminate\Contracts\Support\Htmlable;

class LoginCustom extends Login
{
    public function getHeading(): string|Htmlable
    {
        return __('panel.heading');
    }

    public function hasLogo(): bool
    {
        return false;
    }

    public function form(Schema $schema): Schema
    {
        return $schema
            ->components([
                $this->getEmailFormComponent(),
                $this->getPasswordFormComponent()
            ]);
    }
}
