<?php

declare(strict_types=1);

require_once __DIR__ . '/../helpers/Response.php';
require_once __DIR__ . '/../controllers/CategoryController.php';
require_once __DIR__ . '/../controllers/ProductController.php';

$method = $_SERVER['REQUEST_METHOD'];
$uri = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);

/*
|--------------------------------------------------------------------------
| Normalizar la ruta
|--------------------------------------------------------------------------
| Ejemplo:
| /marucha/backend-php/public/index.php/api/categories
| /backend-php/public/index.php/api/categories
| /api/categories
|
| Queremos quedarnos solo con:
| /api/categories
*/

$scriptName = $_SERVER['SCRIPT_NAME']; 
$basePath = str_replace('\\', '/', dirname($scriptName));

// Quitamos el basePath si está presente
if ($basePath !== '/' && str_starts_with($uri, $basePath)) {
    $uri = substr($uri, strlen($basePath));
}

// Si aún aparece /index.php al inicio, lo quitamos
$uri = preg_replace('#^/index\.php#', '', $uri);

// Si quedó vacío, lo dejamos como /
$uri = $uri ?: '/';

/*
|--------------------------------------------------------------------------
| Routing
|--------------------------------------------------------------------------
*/

if ($method === 'GET' && $uri === '/api/health') {
    Response::success('Marucha API is running', [
        'app' => APP_NAME,
        'version' => APP_VERSION
    ]);
}

if ($method === 'GET' && $uri === '/api/categories') {
    $controller = new CategoryController();
    $controller->index();
}

if ($method === 'GET' && $uri === '/api/products') {
    $controller = new ProductController();
    $controller->index();
}

Response::error('Route not found', 404);