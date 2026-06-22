<?php

declare(strict_types=1);

require_once __DIR__ . '/../models/Cart.php';
require_once __DIR__ . '/../models/User.php';
require_once __DIR__ . '/../models/Product.php';

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
            $this->cartModel->createCart($userId);
            $cart = $this->cartModel->findActiveCartByUserId($userId);
        }

        $items = $this->cartModel->getCartItems((int)$cart['id_carrito']);

        $total = 0;
        foreach ($items as $item) {
            $total += (float)$item['subtotal'];
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

        $errors = [];

        if (!isset($data['id_platillo'])) {
            $errors['id_platillo'] = 'The field "id_platillo" is required.';
        }

        if (!isset($data['cantidad'])) {
            $errors['cantidad'] = 'The field "cantidad" is required.';
        }

        if (!empty($errors)) {
            return [
                'success' => false,
                'status'  => 422,
                'message' => 'Validation errors',
                'data'    => $errors
            ];
        }

        $productId = (int)$data['id_platillo'];
        $quantity  = (int)$data['cantidad'];

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

        $product = $this->productModel->findById($productId);

        if (!$product) {
            return [
                'success' => false,
                'status'  => 404,
                'message' => 'Product not found'
            ];
        }

        $cart = $this->cartModel->findActiveCartByUserId($userId);

        if (!$cart) {
            $this->cartModel->createCart($userId);
            $cart = $this->cartModel->findActiveCartByUserId($userId);
        }

        $cartId = (int)$cart['id_carrito'];
        $existingItem = $this->cartModel->findCartItemByCartAndPlatillo($cartId, $productId);

        $price = (float)$product['precio'];
        $subtotal = $price * $quantity;

        if ($existingItem) {
            $newQuantity = (int)$existingItem['cantidad'] + $quantity;
            $newSubtotal = $price * $newQuantity;

            $this->cartModel->updateCartItem((int)$existingItem['id_carrito_detalle'], [
                'cantidad'        => $newQuantity,
                'precio_unitario' => $price,
                'subtotal'        => $newSubtotal
            ]);
        } else {
            $this->cartModel->createCartItem([
                'id_carrito'      => $cartId,
                'id_platillo'     => $productId,
                'cantidad'        => $quantity,
                'precio_unitario' => $price,
                'subtotal'        => $subtotal
            ]);
        }

        return $this->getActiveCartByUserId($userId);
    }

    public function updateCartItem(int $cartDetailId, array $data): array
    {
        $item = $this->cartModel->findCartItemById($cartDetailId);

        if (!$item) {
            return [
                'success' => false,
                'status'  => 404,
                'message' => 'Cart item not found'
            ];
        }

        if (!isset($data['cantidad'])) {
            return [
                'success' => false,
                'status'  => 422,
                'message' => 'Validation errors',
                'data'    => [
                    'cantidad' => 'The field "cantidad" is required.'
                ]
            ];
        }

        $quantity = (int)$data['cantidad'];

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

        $subtotal = (float)$item['precio_unitario'] * $quantity;

        $updated = $this->cartModel->updateCartItem($cartDetailId, [
            'cantidad'        => $quantity,
            'precio_unitario' => (float)$item['precio_unitario'],
            'subtotal'        => $subtotal
        ]);

        if (!$updated) {
            return [
                'success' => false,
                'status'  => 500,
                'message' => 'Failed to update cart item'
            ];
        }

        $updatedItem = $this->cartModel->findCartItemById($cartDetailId);

        return [
            'success' => true,
            'status'  => 200,
            'message' => 'Cart item updated successfully',
            'data'    => $updatedItem
        ];
    }

    public function deleteCartItem(int $cartDetailId): array
    {
        $item = $this->cartModel->findCartItemById($cartDetailId);

        if (!$item) {
            return [
                'success' => false,
                'status'  => 404,
                'message' => 'Cart item not found'
            ];
        }

        $deleted = $this->cartModel->deleteCartItem($cartDetailId);

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