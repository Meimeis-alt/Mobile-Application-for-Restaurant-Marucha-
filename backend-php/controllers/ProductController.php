<?php

declare(strict_types=1);

require_once __DIR__ . '/../models/Product.php';
require_once __DIR__ . '/../helpers/Response.php';

class ProductController
{
    public function index(): void
    {
        try {
            $productModel = new Product();
            $products = $productModel->getAll();

            Response::success(
                'Products retrieved successfully',
                $products
            );
        } catch (Throwable $e) {
            Response::error(
                'Error retrieving products',
                500,
                ['error' => $e->getMessage()]
            );
        }
    }
}