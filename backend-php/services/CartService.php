<?php

declare(strict_types=1);

require_once __DIR__ . '/../models/Cart.php';
require_once __DIR__ . '/../models/User.php';
require_once __DIR__ . '/../models/Product.php';
require_once __DIR__ . '/../helpers/Validator.php';

class CartService
{
    private Cart $cartModel;
    private User $userModel;
    private Product $productModel;

    public function __construct()
    {
        $this->cartModel = new Cart();
        $this->userModel = new User();
        $this->productModel = new Product();
    }

    public function getActiveCartByUserId(int $userId): array
    {
        $user = $this->userModel->findById($userId);

        if (!$user) {
            return [
                'success' => false,
                'status'  => 404,
                'message' => 'User not found'
            ];
        }

        $cart = $this->cartModel->findActiveCartByUserId($userId);

        if (!$cart) {
            return [
                'success' => true,
                'status'  => 200,
                'message' => 'Active cart retrieved successfully',
                'data'    => [
                    'cart'  => null,
                    'items' => [],
                    'total' => 0
                ]
            ];
        }

        $items = $this->cartModel->getCartItems((int)$cart['id_carrito']);
        $total = 0;

        foreach ($items as $item) {
            $total += (float) $item['subtotal'];
        }

        return [
            'success' => true,
            'status'  => 200,
            'message' => 'Active cart retrieved successfully',
            'data'    => [
                'cart'  => $cart,
                'items' => $items,
                'total' => $total
            ]
        ];
    }

    public function addItemToCart(int $userId, array $data): array
    {
        $user = $this->userModel->findById($userId);

        if (!$user) {
            return [
                'success' => false,
                'status'  => 404,
                'message' => 'User not found'
            ];
        }

        $errors = Validator::validateRequired($data, [
            'id_platillo',
            'cantidad'
        ]);

        if (!empty($errors)) {
            return [
                'success' => false,
                'status'  => 422,
                'message' => 'Validation errors',
                'data'    => $errors
            ];
        }

        $platilloId = (int) $data['id_platillo'];
        $quantity   = (int) $data['cantidad'];

        if ($quantity <= 0) {
            return [
                'success' => false,
                'status'  => 422,
                'message' => 'Validation errors',
                'data'    => [
                    'cantidad' => 'The field "cantidad" must be greater than 0.'
                ]
            ];
        }

        $product = $this->productModel->findById($platilloId);

        if (!$product) {
            return [
                'success' => false,
                'status'  => 404,
                'message' => 'Product not found'
            ];
        }

        if ((int)$product['disponible'] !== 1) {
            return [
                'success' => false,
                'status'  => 400,
                'message' => 'Product is not available'
            ];
        }

        $cart = $this->cartModel->findActiveCartByUserId($userId);

        if (!$cart) {
            $newCartId = $this->cartModel->createCart($userId);
            $cart = $this->cartModel->findActiveCartByUserId($userId);

            if (!$cart || (int)$cart['id_carrito'] !== $newCartId) {
                return [
                    'success' => false,
                    'status'  => 500,
                    'message' => 'Failed to create cart'
                ];
            }
        }

        $cartId = (int) $cart['id_carrito'];
        $existingItem = $this->cartModel->findCartItemByCartAndPlatillo($cartId, $platilloId);
        $price = (float) $product['precio'];

        if ($existingItem) {
            $newQuantity = (int)$existingItem['cantidad'] + $quantity;
            $newSubtotal = $newQuantity * $price;

            $updated = $this->cartModel->updateCartItem(
                (int)$existingItem['id_carrito_detalle'],
                $newQuantity,
                $newSubtotal
            );

            if (!$updated) {
                return [
                    'success' => false,
                    'status'  => 500,
                    'message' => 'Failed to update cart item'
                ];
            }
        } else {
            $subtotal = $quantity * $price;

            $this->cartModel->createCartItem([
                'id_carrito'     => $cartId,
                'id_platillo'    => $platilloId,
                'cantidad'       => $quantity,
                'precio_unitario'=> $price,
                'subtotal'       => $subtotal
            ]);
        }

        $items = $this->cartModel->getCartItems($cartId);
        $total = 0;

        foreach ($items as $item) {
            $total += (float) $item['subtotal'];
        }

        return [
            'success' => true,
            'status'  => 200,
            'message' => 'Item added to cart successfully',
            'data'    => [
                'cart'  => $cart,
                'items' => $items,
                'total' => $total
            ]
        ];
    }
    public function updateCartItem(int $cartItemId, array $data): array
{
    $cartItem = $this->cartModel->findCartItemById($cartItemId);

    if (!$cartItem) {
        return [
            'success' => false,
            'status'  => 404,
            'message' => 'Cart item not found'
        ];
    }

    $errors = Validator::validateRequired($data, ['cantidad']);

    if (!empty($errors)) {
        return [
            'success' => false,
            'status'  => 422,
            'message' => 'Validation errors',
            'data'    => $errors
        ];
    }

    $quantity = (int) $data['cantidad'];

    if ($quantity <= 0) {
        return [
            'success' => false,
            'status'  => 422,
            'message' => 'Validation errors',
            'data'    => [
                'cantidad' => 'The field "cantidad" must be greater than 0.'
            ]
        ];
    }

    $price = (float) $cartItem['precio_unitario'];
    $subtotal = $quantity * $price;

    $updated = $this->cartModel->updateCartItem($cartItemId, $quantity, $subtotal);

    if (!$updated) {
        return [
            'success' => false,
            'status'  => 500,
            'message' => 'Failed to update cart item'
        ];
    }

    $updatedItem = $this->cartModel->findCartItemById($cartItemId);

    return [
        'success' => true,
        'status'  => 200,
        'message' => 'Cart item updated successfully',
        'data'    => $updatedItem
    ];
}

    public function deleteCartItem(int $cartItemId): array
    {
    $cartItem = $this->cartModel->findCartItemById($cartItemId);

    if (!$cartItem) {
        return [
            'success' => false,
            'status'  => 404,
            'message' => 'Cart item not found'
        ];
    }

    $deleted = $this->cartModel->deleteCartItem($cartItemId);

    if (!$deleted) {
        return [
            'success' => false,
            'status'  => 500,
            'message' => 'Failed to delete cart item'
        ];
    }

    return [
        'success' => true,
        'status'  => 200,
        'message' => 'Cart item deleted successfully'
    ];
}
}