<?php

declare(strict_types=1);

require_once __DIR__ . '/../models/Order.php';

class AdminOrderService
{
    private Order $orderModel;

    public function __construct()
    {
        $this->orderModel = new Order();
    }

    public function getAllOrders(): array
    {
        $orders = $this->orderModel->getAllOrders();

        return [
            'success' => true,
            'status'  => 200,
            'message' => 'Orders retrieved successfully',
            'data'    => $orders
        ];
    }

    public function getOrderById(int $orderId): array
    {
        $order = $this->orderModel->getAdminOrderById($orderId);

        if (!$order) {
            return [
                'success' => false,
                'status'  => 404,
                'message' => 'Order not found'
            ];
        }

        $address = $this->orderModel->findAddressById((int)$order['id_direccion']);
        $details = $this->orderModel->getOrderDetails($orderId);

        return [
            'success' => true,
            'status'  => 200,
            'message' => 'Order retrieved successfully',
            'data'    => [
                'order'   => $order,
                'address' => $address,
                'details' => $details
            ]
        ];
    }

    public function updateOrderStatus(int $orderId, array $data): array
    {
        $order = $this->orderModel->getAdminOrderById($orderId);

        if (!$order) {
            return [
                'success' => false,
                'status'  => 404,
                'message' => 'Order not found'
            ];
        }

        if (!isset($data['id_estado_pedido'])) {
            return [
                'success' => false,
                'status'  => 422,
                'message' => 'Validation errors',
                'data'    => [
                    'id_estado_pedido' => 'The field "id_estado_pedido" is required.'
                ]
            ];
        }

        $statusId = (int)$data['id_estado_pedido'];
        $comment = isset($data['comentario']) ? trim((string)$data['comentario']) : null;

        $status = $this->orderModel->findOrderStatusById($statusId);

        if (!$status) {
            return [
                'success' => false,
                'status'  => 404,
                'message' => 'Order status not found'
            ];
        }

        $updated = $this->orderModel->updateOrderStatus($orderId, $statusId);

        if (!$updated) {
            return [
                'success' => false,
                'status'  => 500,
                'message' => 'Failed to update order status'
            ];
        }

        $this->orderModel->createOrderStatusHistory([
            'id_pedido'        => $orderId,
            'id_estado_pedido' => $statusId,
            'comentario'       => $comment
        ]);

        $updatedOrder = $this->orderModel->getAdminOrderById($orderId);
        $address = $this->orderModel->findAddressById((int)$updatedOrder['id_direccion']);
        $details = $this->orderModel->getOrderDetails($orderId);

        return [
            'success' => true,
            'status'  => 200,
            'message' => 'Order status updated successfully',
            'data'    => [
                'order'   => $updatedOrder,
                'address' => $address,
                'details' => $details
            ]
        ];
    }
}