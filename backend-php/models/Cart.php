<?php

declare(strict_types=1);

require_once __DIR__ . '/../config/database.php';

class Cart
{
    private PDO $connection;

    public function __construct()
    {
        $database = new Database();
        $this->connection = $database->connect();
    }

    public function findActiveCartByUserId(int $userId): array|false
    {
        $sql = "
            SELECT
                id_carrito,
                id_usuario,
                estado,
                fecha_creacion,
                fecha_actualizacion
            FROM carrito
            WHERE id_usuario = :id_usuario
              AND estado = 'activo'
            LIMIT 1
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':id_usuario', $userId, PDO::PARAM_INT);
        $stmt->execute();

        return $stmt->fetch();
    }

    public function createCart(int $userId): int|false
    {
        $sql = "
            INSERT INTO carrito (id_usuario, estado)
            VALUES (:id_usuario, 'activo')
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':id_usuario', $userId, PDO::PARAM_INT);

        if (!$stmt->execute()) {
            return false;
        }

        return (int)$this->connection->lastInsertId();
    }

    public function getCartItems(int $cartId): array
    {
        $sql = "
            SELECT
                cd.id_carrito_detalle,
                cd.id_carrito,
                cd.id_platillo,
                p.nombre AS platillo_nombre,
                p.descripcion AS platillo_descripcion,
                p.imagen_url,
                cd.cantidad,
                cd.precio_unitario,
                cd.subtotal
            FROM carrito_detalle cd
            INNER JOIN platillo p ON p.id_platillo = cd.id_platillo
            WHERE cd.id_carrito = :id_carrito
            ORDER BY cd.id_carrito_detalle ASC
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':id_carrito', $cartId, PDO::PARAM_INT);
        $stmt->execute();

        return $stmt->fetchAll();
    }

    public function findCartItemByCartAndPlatillo(int $cartId, int $productId): array|false
    {
        $sql = "
            SELECT
                id_carrito_detalle,
                id_carrito,
                id_platillo,
                cantidad,
                precio_unitario,
                subtotal
            FROM carrito_detalle
            WHERE id_carrito = :id_carrito
              AND id_platillo = :id_platillo
            LIMIT 1
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':id_carrito', $cartId, PDO::PARAM_INT);
        $stmt->bindValue(':id_platillo', $productId, PDO::PARAM_INT);
        $stmt->execute();

        return $stmt->fetch();
    }

    public function createCartItem(array $data): int|false
    {
        $sql = "
            INSERT INTO carrito_detalle (
                id_carrito,
                id_platillo,
                cantidad,
                precio_unitario,
                subtotal
            ) VALUES (
                :id_carrito,
                :id_platillo,
                :cantidad,
                :precio_unitario,
                :subtotal
            )
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':id_carrito', $data['id_carrito'], PDO::PARAM_INT);
        $stmt->bindValue(':id_platillo', $data['id_platillo'], PDO::PARAM_INT);
        $stmt->bindValue(':cantidad', $data['cantidad'], PDO::PARAM_INT);
        $stmt->bindValue(':precio_unitario', $data['precio_unitario']);
        $stmt->bindValue(':subtotal', $data['subtotal']);

        if (!$stmt->execute()) {
            return false;
        }

        return (int)$this->connection->lastInsertId();
    }

    public function updateCartItem(int $cartDetailId, array $data): bool
    {
        $sql = "
            UPDATE carrito_detalle
            SET
                cantidad = :cantidad,
                precio_unitario = :precio_unitario,
                subtotal = :subtotal
            WHERE id_carrito_detalle = :id_carrito_detalle
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':cantidad', $data['cantidad'], PDO::PARAM_INT);
        $stmt->bindValue(':precio_unitario', $data['precio_unitario']);
        $stmt->bindValue(':subtotal', $data['subtotal']);
        $stmt->bindValue(':id_carrito_detalle', $cartDetailId, PDO::PARAM_INT);

        return $stmt->execute();
    }

    public function findCartItemById(int $cartDetailId): array|false
    {
        $sql = "
            SELECT
                id_carrito_detalle,
                id_carrito,
                id_platillo,
                cantidad,
                precio_unitario,
                subtotal
            FROM carrito_detalle
            WHERE id_carrito_detalle = :id_carrito_detalle
            LIMIT 1
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':id_carrito_detalle', $cartDetailId, PDO::PARAM_INT);
        $stmt->execute();

        return $stmt->fetch();
    }

    public function deleteCartItem(int $cartDetailId): bool
    {
        $sql = "DELETE FROM carrito_detalle WHERE id_carrito_detalle = :id_carrito_detalle";
        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':id_carrito_detalle', $cartDetailId, PDO::PARAM_INT);

        return $stmt->execute();
    }

    public function clearCartItems(int $cartId): bool
    {
        $sql = "DELETE FROM carrito_detalle WHERE id_carrito = :id_carrito";
        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':id_carrito', $cartId, PDO::PARAM_INT);

        return $stmt->execute();
    }

    public function markCartAsCompleted(int $cartId): bool
{
    $sql = "
        UPDATE carrito
        SET estado = 'convertido'
        WHERE id_carrito = :id_carrito
    ";

    $stmt = $this->connection->prepare($sql);
    $stmt->bindValue(':id_carrito', $cartId, PDO::PARAM_INT);

    return $stmt->execute();
}
}