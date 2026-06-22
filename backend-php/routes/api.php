<?php

declare(strict_types=1);

require_once __DIR__ . '/../helpers/Response.php';

require_once __DIR__ . '/../controllers/CategoryController.php';
require_once __DIR__ . '/../controllers/ProductController.php';
require_once __DIR__ . '/../controllers/AuthController.php';
require_once __DIR__ . '/../controllers/UserController.php';
require_once __DIR__ . '/../controllers/AddressController.php';
require_once __DIR__ . '/../controllers/CartController.php';
require_once __DIR__ . '/../controllers/OrderController.php';
require_once __DIR__ . '/../controllers/AdminOrderController.php';

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
| Health
|--------------------------------------------------------------------------
*/
if ($method === 'GET' && $uri === '/api/health') {
    Response::success('Marucha API is running', [
        'app' => APP_NAME,
        'version' => APP_VERSION
    ]);
}

/*
|--------------------------------------------------------------------------
| Categories
|--------------------------------------------------------------------------
*/
if ($method === 'GET' && $uri === '/api/categories') {
    $controller = new CategoryController();
    $controller->index();
}

/*
|--------------------------------------------------------------------------
| Products
|--------------------------------------------------------------------------
*/
if ($method === 'GET' && $uri === '/api/products') {
    $controller = new ProductController();
    $controller->index();
}

/*
|--------------------------------------------------------------------------
| Auth
|--------------------------------------------------------------------------
*/
if ($method === 'POST' && $uri === '/api/auth/register') {
    $controller = new AuthController();
    $controller->register();
}

if ($method === 'POST' && $uri === '/api/auth/login') {
    $controller = new AuthController();
    $controller->login();
}

/*
|--------------------------------------------------------------------------
| Users
|--------------------------------------------------------------------------
*/
if ($method === 'GET' && preg_match('#^/api/users/(\d+)$#', $uri, $matches)) {
    $controller = new UserController();
    $controller->show((int) $matches[1]);
}

if ($method === 'PUT' && preg_match('#^/api/users/(\d+)$#', $uri, $matches)) {
    $controller = new UserController();
    $controller->update((int) $matches[1]);
}

/*
|--------------------------------------------------------------------------
| User Addresses
|--------------------------------------------------------------------------
*/
if ($method === 'GET' && preg_match('#^/api/users/(\d+)/addresses$#', $uri, $matches)) {
    $controller = new AddressController();
    $controller->indexByUser((int) $matches[1]);
}

if ($method === 'POST' && preg_match('#^/api/users/(\d+)/addresses$#', $uri, $matches)) {
    $controller = new AddressController();
    $controller->store((int) $matches[1]);
}

if ($method === 'PUT' && preg_match('#^/api/addresses/(\d+)$#', $uri, $matches)) {
    $controller = new AddressController();
    $controller->update((int) $matches[1]);
}

if ($method === 'DELETE' && preg_match('#^/api/addresses/(\d+)$#', $uri, $matches)) {
    $controller = new AddressController();
    $controller->delete((int) $matches[1]);
}

/*
|--------------------------------------------------------------------------
| Cart
|--------------------------------------------------------------------------
*/
if ($method === 'GET' && preg_match('#^/api/users/(\d+)/cart$#', $uri, $matches)) {
    $controller = new CartController();
    $controller->showByUser((int) $matches[1]);
}

if ($method === 'POST' && preg_match('#^/api/users/(\d+)/cart/items$#', $uri, $matches)) {
    $controller = new CartController();
    $controller->addItem((int) $matches[1]);
}

if ($method === 'PUT' && preg_match('#^/api/cart/items/(\d+)$#', $uri, $matches)) {
    $controller = new CartController();
    $controller->updateItem((int) $matches[1]);
}

if ($method === 'DELETE' && preg_match('#^/api/cart/items/(\d+)$#', $uri, $matches)) {
    $controller = new CartController();
    $controller->deleteItem((int) $matches[1]);
}

/*
|--------------------------------------------------------------------------
| Orders
|--------------------------------------------------------------------------
*/
if ($method === 'POST' && preg_match('#^/api/users/(\d+)/orders$#', $uri, $matches)) {
    $controller = new OrderController();
    $controller->store((int) $matches[1]);
}

if ($method === 'GET' && preg_match('#^/api/users/(\d+)/orders$#', $uri, $matches)) {
    $controller = new OrderController();
    $controller->indexByUser((int) $matches[1]);
}

if ($method === 'GET' && preg_match('#^/api/orders/(\d+)$#', $uri, $matches)) {
    $controller = new OrderController();
    $controller->show((int) $matches[1]);
}

/*
|--------------------------------------------------------------------------
| Admin Orders
|--------------------------------------------------------------------------
*/
if ($method === 'GET' && $uri === '/api/admin/orders') {
    $controller = new AdminOrderController();
    $controller->index();
}

if ($method === 'GET' && preg_match('#^/api/admin/orders/(\d+)$#', $uri, $matches)) {
    $controller = new AdminOrderController();
    $controller->show((int) $matches[1]);
}

if ($method === 'PUT' && preg_match('#^/api/admin/orders/(\d+)/status$#', $uri, $matches)) {
    $controller = new AdminOrderController();
    $controller->updateStatus((int) $matches[1]);
}

Response::error('Route not found', 404);