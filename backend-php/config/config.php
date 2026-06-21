<?php

declare(strict_types=1);

/*
|--------------------------------------------------------------------------
| General App Configuration
|--------------------------------------------------------------------------
| Global backend configuration for Marucha API.
*/

define('APP_NAME', 'Marucha API');
define('APP_VERSION', '0.3.0-dev');
define('APP_TIMEZONE', 'America/Lima');

date_default_timezone_set(APP_TIMEZONE);

/*
|--------------------------------------------------------------------------
| Base Headers
|--------------------------------------------------------------------------
| These headers will be used for API responses.
*/

header('Content-Type: application/json; charset=utf-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type, Authorization');

/*
|--------------------------------------------------------------------------
| Handle preflight requests
|--------------------------------------------------------------------------
*/
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}