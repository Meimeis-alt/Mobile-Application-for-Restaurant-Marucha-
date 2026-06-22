<?php

declare(strict_types=1);

require_once __DIR__ . '/../services/AdminOrderService.php';
require_once __DIR__ . '/../helpers/Response.php';

class AdminOrderController
{
    private AdminOrderService $adminOrderService;

    public function __construct()
    {
        $this->adminOrderService = new AdminOrderService();
    }

    public function index(): void
    {
        try {
            $result = $this->adminOrderService->getAllOrders();

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
                'Error retrieving orders',
                500,
                ['error' => $e->getMessage()]
            );
        }
    }

    public function show(int $orderId): void
    {
        try {
            $result = $this->adminOrderService->getOrderById($orderId);

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
    public function updateStatus(int $orderId): void
{
    try {
        $requestData = json_decode(file_get_contents('php://input'), true);

        if (!is_array($requestData)) {
            Response::error('Invalid JSON body', 400);
        }

        $result = $this->adminOrderService->updateOrderStatus($orderId, $requestData);

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
            'Error updating order status',
            500,
            ['error' => $e->getMessage()]
        );
    }
}
}