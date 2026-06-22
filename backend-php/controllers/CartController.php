<?php

declare(strict_types=1);

require_once __DIR__ . '/../services/CartService.php';
require_once __DIR__ . '/../helpers/Response.php';

class CartController
{
    private CartService $cartService;

    public function __construct()
    {
        $this->cartService = new CartService();
    }

    public function showByUser(int $userId): void
    {
        try {
            $result = $this->cartService->getActiveCartByUserId($userId);

            if (!$result['success']) {
                Response::error(
                    $result['message'],
                    $result['status'],
                    $result['data'] ?? null
                );
            }

            Response::success(
                $result['message'],
                $result['data'],
                $result['status']
            );
        } catch (Throwable $e) {
            Response::error(
                'Error retrieving cart',
                500,
                ['error' => $e->getMessage()]
            );
        }
    }

    public function addItem(int $userId): void
    {
        try {
            $requestData = json_decode(file_get_contents('php://input'), true);

            if (!is_array($requestData)) {
                Response::error('Invalid JSON body', 400);
            }

            $result = $this->cartService->addItemToCart($userId, $requestData);

            if (!$result['success']) {
                Response::error(
                    $result['message'],
                    $result['status'],
                    $result['data'] ?? null
                );
            }

            Response::success(
                $result['message'],
                $result['data'],
                $result['status']
            );
        } catch (Throwable $e) {
            Response::error(
                'Error adding item to cart',
                500,
                ['error' => $e->getMessage()]
            );
        }
    }
    public function updateItem(int $cartItemId): void
{
    try {
        $requestData = json_decode(file_get_contents('php://input'), true);

        if (!is_array($requestData)) {
            Response::error('Invalid JSON body', 400);
        }

        $result = $this->cartService->updateCartItem($cartItemId, $requestData);

        if (!$result['success']) {
            Response::error(
                $result['message'],
                $result['status'],
                $result['data'] ?? null
            );
        }

        Response::success(
            $result['message'],
            $result['data'],
            $result['status']
        );
    } catch (Throwable $e) {
        Response::error(
            'Error updating cart item',
            500,
            ['error' => $e->getMessage()]
        );
    }
}

    public function deleteItem(int $cartItemId): void
    {
        try {
        $result = $this->cartService->deleteCartItem($cartItemId);

        if (!$result['success']) {
            Response::error(
                $result['message'],
                $result['status'],
                $result['data'] ?? null
            );
        }

        Response::success(
            $result['message'],
            null,
            $result['status']
        );
    } catch (Throwable $e) {
        Response::error(
            'Error deleting cart item',
            500,
            ['error' => $e->getMessage()]
        );
    }
}
}