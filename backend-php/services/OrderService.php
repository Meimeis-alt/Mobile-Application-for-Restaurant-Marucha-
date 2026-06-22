<?php

declare(strict_types=1);

require_once __DIR__ . '/../models/Order.php';
require_once __DIR__ . '/../models/User.php';
require_once __DIR__ . '/../models/Address.php';
require_once __DIR__ . '/../models/Cart.php';

class OrderService
{
    private Order $orderModel;
    private User $userModel;
    private Address $addressModel;
    private Cart $cartModel;

    public function __construct()
    {
        $this->orderModel = new Order();
        $this->userModel = new User();
        $this->addressModel = new Address();
        $this->cartModel = new Cart();
    }

    public function createOrder(int $userId, array $data): array
    {
        $user = $this->userModel->findById($userId);

        if (!$user) {
            return [
                'success' => false,
                'status' => 404,
                'message' => 'User not found'
            ];
        }

        $errors = [];

        if (!isset($data['id_direccion'])) {
            $errors['id_direccion'] = 'The field "id_direccion" is required.';
        }

        if (!isset($data['id_metodo_pago'])) {
            $errors['id_metodo_pago'] = 'The field "id_metodo_pago" is required.';
        }

        if (!empty($errors)) {
            return [
                'success' => false,
                'status' => 422,
                'message' => 'Validation errors',
                'data' => $errors
            ];
        }

        $addressId = (int)$data['id_direccion'];
        $paymentMethodId = (int)$data['id_metodo_pago'];
        $observations = isset($data['observaciones']) ? trim((string)$data['observaciones']) : null;

        $address = $this->addressModel->findById($addressId);

        if (!$address || (int)$address['id_usuario'] !== $userId) {
            return [
                'success' => false,
                'status' => 404,
                'message' => 'Address not found for this user'
            ];
        }

        $paymentMethod = $this->orderModel->findPaymentMethodById($paymentMethodId);

        if (!$paymentMethod) {
            return [
                'success' => false,
                'status' => 404,
                'message' => 'Payment method not found'
            ];
        }

        $initialStatus = $this->orderModel->findOrderStatusById(1);

        if (!$initialStatus) {
            return [
                'success' => false,
                'status' => 500,
                'message' => 'Initial order status not found'
            ];
        }

        $cart = $this->cartModel->findActiveCartByUserId($userId);

        if (!$cart) {
            return [
                'success' => false,
                'status' => 400,
                'message' => 'Cart is empty'
            ];
        }

        $cartId = (int)$cart['id_carrito'];
        $cartItems = $this->cartModel->getCartItems($cartId);

        if (empty($cartItems)) {
            return [
                'success' => false,
                'status' => 400,
                'message' => 'Cart is empty'
            ];
        }

        $subtotal = 0;
        foreach ($cartItems as $item) {
            $subtotal += (float)$item['subtotal'];
        }

        $total = $subtotal;
        $orderNumber = $this->orderModel->generateOrderNumber();

        $orderId = $this->orderModel->createOrder([
            'id_usuario'        => $userId,
            'id_direccion'      => $addressId,
            'id_estado_pedido'  => 1,
            'id_metodo_pago'    => $paymentMethodId,
            'numero_pedido'     => $orderNumber,
            'subtotal'          => $subtotal,
            'total'             => $total,
            'observaciones'     => $observations
        ]);

        if (!$orderId) {
            return [
                'success' => false,
                'status' => 500,
                'message' => 'Failed to create order'
            ];
        }

        foreach ($cartItems as $item) {
            $this->orderModel->createOrderDetail([
                'id_pedido'       => $orderId,
                'id_platillo'     => (int)$item['id_platillo'],
                'cantidad'        => (int)$item['cantidad'],
                'precio_unitario' => (float)$item['precio_unitario'],
                'subtotal'        => (float)$item['subtotal']
            ]);
        }

        $this->orderModel->createOrderStatusHistory([
            'id_pedido'        => $orderId,
            'id_estado_pedido' => 1,
            'comentario'       => 'Pedido creado'
        ]);

        $this->cartModel->clearCartItems($cartId);
        $this->cartModel->markCartAsCompleted($cartId);

        $order = $this->orderModel->getOrderById($orderId);
        $details = $this->orderModel->getOrderDetails($orderId);

        return [
            'success' => true,
            'status' => 201,
            'message' => 'Order created successfully',
            'data' => [
                'order'   => $order,
                'details' => $details
            ]
        ];
    }

    public function getOrdersByUserId(int $userId): array
    {
        $user = $this->userModel->findById($userId);

        if (!$user) {
            return [
                'success' => false,
                'status' => 404,
                'message' => 'User not found'
            ];
        }

        $orders = $this->orderModel->getOrdersByUserId($userId);

        return [
            'success' => true,
            'status' => 200,
            'message' => 'User orders retrieved successfully',
            'data' => $orders
        ];
    }

    public function getOrderById(int $orderId): array
    {
        $order = $this->orderModel->getOrderById($orderId);

        if (!$order) {
            return [
                'success' => false,
                'status' => 404,
                'message' => 'Order not found'
            ];
        }

        $address = $this->orderModel->findAddressById((int)$order['id_direccion']);
        $details = $this->orderModel->getOrderDetails($orderId);

        return [
            'success' => true,
            'status' => 200,
            'message' => 'Order retrieved successfully',
            'data' => [
                'order'   => $order,
                'address' => $address,
                'details' => $details
            ]
        ];
    }
}