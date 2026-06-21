<?php

declare(strict_types=1);

require_once __DIR__ . '/../models/Category.php';
require_once __DIR__ . '/../helpers/Response.php';

class CategoryController
{
    public function index(): void
    {
        try {
            $categoryModel = new Category();
            $categories = $categoryModel->getAll();

            Response::success(
                'Categories retrieved successfully',
                $categories
            );
        } catch (Throwable $e) {
            Response::error(
                'Error retrieving categories',
                500,
                ['error' => $e->getMessage()]
            );
        }
    }
}