<?php

declare(strict_types=1);

require_once __DIR__ . '/../models/Order.php';
require_once __DIR__ . '/../models/User.php';
require_once __DIR__ . '/../models/Address.php';
require_once __DIR__ . '/../models/Cart.php';
require_once __DIR__ . '/../models/Product.php';

class OrderService
{
    private const MAX_ITEM_QUANTITY = 8;
    private const INITIAL_ORDER_STATUS_ID = 1;

    private Order $orderModel;
    private User $userModel;
    private Address $addressModel;
    private Cart $cartModel;
    private Product $productModel;

    public function __construct()
    {
        $this->orderModel = new Order();
        $this->userModel = new User();
        $this->addressModel = new Address();
        $this->cartModel = new Cart();
        $this->productModel = new Product();
    }

    public function createOrder(int $userId, array $data): array
    {
        $user = $this->userModel->findById($userId);

        if (!$user) {
            return [
                'success' => false,
                'status'  => 404,
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
                'status'  => 422,
                'message' => 'Validation errors',
                'data'    => $errors
            ];
        }

        $addressId = (int)$data['id_direccion'];
        $paymentMethodId = (int)$data['id_metodo_pago'];
        $observations = isset($data['observaciones'])
            ? trim((string)$data['observaciones'])
            : null;

        $address = $this->addressModel->findById($addressId);

        if (!$address || (int)$address['id_usuario'] !== $userId) {
            return [
                'success' => false,
                'status'  => 404,
                'message' => 'Address not found for this user'
            ];
        }

        $paymentMethod = $this->orderModel->findPaymentMethodById($paymentMethodId);

        if (!$paymentMethod) {
            return [
                'success' => false,
                'status'  => 404,
                'message' => 'Payment method not found'
            ];
        }

        if (isset($paymentMethod['activo']) && (int)$paymentMethod['activo'] !== 1) {
            return [
                'success' => false,
                'status'  => 409,
                'message' => 'Payment method is inactive'
            ];
        }

        $initialStatus = $this->orderModel->findOrderStatusById(self::INITIAL_ORDER_STATUS_ID);

        if (!$initialStatus) {
            return [
                'success' => false,
                'status'  => 500,
                'message' => 'Initial order status not found'
            ];
        }

        $cart = $this->cartModel->findActiveCartByUserId($userId);

        if (!$cart) {
            return [
                'success' => false,
                'status'  => 400,
                'message' => 'Cart is empty'
            ];
        }

        $cartId = (int)$cart['id_carrito'];
        $cartItems = $this->cartModel->getCartItems($cartId);

        if (empty($cartItems)) {
            return [
                'success' => false,
                'status'  => 400,
                'message' => 'Cart is empty'
            ];
        }

        $validatedItems = [];
        $subtotal = 0.0;

        foreach ($cartItems as $item) {
            $productId = (int)$item['id_platillo'];
            $quantity = (int)$item['cantidad'];
            $price = (float)$item['precio_unitario'];

            if ($quantity < 1 || $quantity > self::MAX_ITEM_QUANTITY) {
                return [
                    'success' => false,
                    'status'  => 422,
                    'message' => 'Validation errors',
                    'data'    => [
                        'cantidad' => "Invalid quantity for product ID {$productId}. Allowed range: 1 to 8."
                    ]
                ];
            }

            $product = $this->productModel->findById($productId);

            if (!$product) {
                return [
                    'success' => false,
                    'status'  => 404,
                    'message' => "Product with ID {$productId} not found"
                ];
            }

            if (isset($product['disponible']) && (int)$product['disponible'] !== 1) {
                return [
                    'success' => false,
                    'status'  => 409,
                    'message' => "The product '{$product['nombre']}' is currently unavailable"
                ];
            }

            $currentPrice = (float)$product['precio'];
            if ($currentPrice <= 0) {
                return [
                    'success' => false,
                    'status'  => 409,
                    'message' => "The product '{$product['nombre']}' has an invalid price"
                ];
            }

            $itemSubtotal = round($currentPrice * $quantity, 2);
            $subtotal += $itemSubtotal;

            $validatedItems[] = [
                'id_platillo'     => $productId,
                'cantidad'        => $quantity,
                'precio_unitario' => $currentPrice,
                'subtotal'        => $itemSubtotal
            ];
        }

        $subtotal = round($subtotal, 2);
        $total = $subtotal;
        $orderNumber = $this->orderModel->generateOrderNumber();

        $connection = $this->orderModel->getConnection();

        try {
            $connection->beginTransaction();

            $orderId = $this->orderModel->createOrder([
                'id_usuario'        => $userId,
                'id_direccion'      => $addressId,
                'id_estado_pedido'  => self::INITIAL_ORDER_STATUS_ID,
                'id_metodo_pago'    => $paymentMethodId,
                'numero_pedido'     => $orderNumber,
                'subtotal'          => $subtotal,
                'total'             => $total,
                'observaciones'     => $observations
            ]);

            if (!$orderId) {
                throw new RuntimeException('Failed to create order');
            }

            foreach ($validatedItems as $item) {
                $detailId = $this->orderModel->createOrderDetail([
                    'id_pedido'       => $orderId,
                    'id_platillo'     => $item['id_platillo'],
                    'cantidad'        => $item['cantidad'],
                    'precio_unitario' => $item['precio_unitario'],
                    'subtotal'        => $item['subtotal']
                ]);

                if (!$detailId) {
                    throw new RuntimeException('Failed to create order detail');
                }
            }

            $historyId = $this->orderModel->createOrderStatusHistory([
                'id_pedido'        => $orderId,
                'id_estado_pedido' => self::INITIAL_ORDER_STATUS_ID,
                'comentario'       => 'Pedido creado'
            ]);

            if (!$historyId) {
                throw new RuntimeException('Failed to create order status history');
            }

            $cartCleared = $this->cartModel->clearCartItems($cartId);
            if (!$cartCleared) {
                throw new RuntimeException('Failed to clear cart items');
            }

            $cartClosed = $this->cartModel->markCartAsCompleted($cartId);
            if (!$cartClosed) {
                throw new RuntimeException('Failed to close cart');
            }

            $connection->commit();

            $order = $this->orderModel->getOrderById($orderId);
            $details = $this->orderModel->getOrderDetails($orderId);

            return [
                'success' => true,
                'status'  => 201,
                'message' => 'Order created successfully',
                'data'    => [
                    'order'   => $order,
                    'details' => $details
                ]
            ];
        } catch (Throwable $e) {
            if ($connection->inTransaction()) {
                $connection->rollBack();
            }

            return [
                'success' => false,
                'status'  => 500,
                'message' => 'Failed to create order',
                'data'    => [
                    'error' => $e->getMessage()
                ]
            ];
        }
    }

    public function getOrdersByUserId(int $userId): array
    {
        $user = $this->userModel->findById($userId);

        if (!$user) {
            return [
                'success' => false,
                'status'  => 404,
                'message' => 'User not found'
            ];
        }

        $orders = $this->orderModel->getOrdersByUserId($userId);

        return [
            'success' => true,
            'status'  => 200,
            'message' => 'User orders retrieved successfully',
            'data'    => $orders
        ];
    }

    public function getOrderById(int $orderId): array
    {
        $order = $this->orderModel->getOrderById($orderId);

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
}