<?php

declare(strict_types=1);

require_once __DIR__ . '/../services/OrderService.php';
require_once __DIR__ . '/../helpers/Response.php';

class OrderController
{
    private OrderService $orderService;

    public function __construct()
    {
        $this->orderService = new OrderService();
    }

    public function store(int $userId): void
{
    try {
        $requestData = json_decode(file_get_contents('php://input'), true);

        if (!is_array($requestData)) {
            Response::error('Invalid JSON body', 400);
        }

        $result = $this->orderService->createOrder($userId, $requestData);

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
            'Error creating order',
            500,
            [
                'error' => $e->getMessage(),
                'file'  => $e->getFile(),
                'line'  => $e->getLine()
            ]
        );
    }
}

    public function indexByUser(int $userId): void
    {
        try {
            $result = $this->orderService->getOrdersByUserId($userId);

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
                'Error retrieving user orders',
                500,
                ['error' => $e->getMessage()]
            );
        }
    }

    public function show(int $orderId): void
    {
        try {
            $result = $this->orderService->getOrderById($orderId);

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
                'Error retrieving order',
                500,
                ['error' => $e->getMessage()]
            );
        }
    }
}