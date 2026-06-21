<?php

declare(strict_types=1);

require_once __DIR__ . '/../helpers/Response.php';
require_once __DIR__ . '/../controllers/CategoryController.php';
require_once __DIR__ . '/../controllers/ProductController.php';
require_once __DIR__ . '/../controllers/AuthController.php';

$method = $_SERVER['REQUEST_METHOD'];
$uri = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);



$scriptName = $_SERVER['SCRIPT_NAME'];
$basePath = str_replace('\\', '/', dirname($scriptName));

if ($basePath !== '/' && str_starts_with($uri, $basePath)) {
    $uri = substr($uri, strlen($basePath));
}

$uri = preg_replace('#^/index\.php#', '', $uri);
$uri = $uri ?: '/';

/*
|--------------------------------------------------------------------------
| Routing
|--------------------------------------------------------------------------
*/

/*
| Health
*/
if ($method === 'GET' && $uri === '/api/health') {
    Response::success('Marucha API is running', [
        'app' => APP_NAME,
        'version' => APP_VERSION
    ]);
}

/*
| Categories
*/
if ($method === 'GET' && $uri === '/api/categories') {
    $controller = new CategoryController();
    $controller->index();
}

/*
| Products
*/
if ($method === 'GET' && $uri === '/api/products') {
    $controller = new ProductController();
    $controller->index();
}

/*
| Auth - Register
*/
if ($method === 'POST' && $uri === '/api/auth/register') {
    $controller = new AuthController();
    $controller->register();
}

/*
| Auth - Login
*/
if ($method === 'POST' && $uri === '/api/auth/login') {
    $controller = new AuthController();
    $controller->login();
}

Response::error('Route not found', 404);