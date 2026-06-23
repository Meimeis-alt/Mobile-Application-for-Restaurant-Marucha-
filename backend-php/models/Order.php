<?php

declare(strict_types=1);

require_once __DIR__ . '/../config/database.php';

class Order
{
    private PDO $connection;

    public function __construct()
    {
        $database = new Database();
        $this->connection = $database->connect();
    }

    public function generateOrderNumber(): string
    {
        return 'MAR-' . date('YmdHis') . '-' . random_int(1, 9);
    }

    public function createOrder(array $data): int|false
    {
        $sql = "
            INSERT INTO pedido (
                id_usuario,
                id_direccion,
                id_estado_pedido,
                id_metodo_pago,
                numero_pedido,
                subtotal,
                total,
                observaciones
            ) VALUES (
                :id_usuario,
                :id_direccion,
                :id_estado_pedido,
                :id_metodo_pago,
                :numero_pedido,
                :subtotal,
                :total,
                :observaciones
            )
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':id_usuario', $data['id_usuario'], PDO::PARAM_INT);
        $stmt->bindValue(':id_direccion', $data['id_direccion'], PDO::PARAM_INT);
        $stmt->bindValue(':id_estado_pedido', $data['id_estado_pedido'], PDO::PARAM_INT);
        $stmt->bindValue(':id_metodo_pago', $data['id_metodo_pago'], PDO::PARAM_INT);
        $stmt->bindValue(':numero_pedido', $data['numero_pedido']);
        $stmt->bindValue(':subtotal', $data['subtotal']);
        $stmt->bindValue(':total', $data['total']);
        $stmt->bindValue(':observaciones', $data['observaciones']);

        if (!$stmt->execute()) {
            return false;
        }

        return (int)$this->connection->lastInsertId();
    }

    public function createOrderDetail(array $data): int|false
    {
        $sql = "
            INSERT INTO detalle_pedido (
                id_pedido,
                id_platillo,
                cantidad,
                precio_unitario,
                subtotal
            ) VALUES (
                :id_pedido,
                :id_platillo,
                :cantidad,
                :precio_unitario,
                :subtotal
            )
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':id_pedido', $data['id_pedido'], PDO::PARAM_INT);
        $stmt->bindValue(':id_platillo', $data['id_platillo'], PDO::PARAM_INT);
        $stmt->bindValue(':cantidad', $data['cantidad'], PDO::PARAM_INT);
        $stmt->bindValue(':precio_unitario', $data['precio_unitario']);
        $stmt->bindValue(':subtotal', $data['subtotal']);

        if (!$stmt->execute()) {
            return false;
        }

        return (int)$this->connection->lastInsertId();
    }

    public function createOrderStatusHistory(array $data): int|false
    {
        $sql = "
            INSERT INTO pedido_historial_estado (
                id_pedido,
                id_estado_pedido,
                comentario
            ) VALUES (
                :id_pedido,
                :id_estado_pedido,
                :comentario
            )
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':id_pedido', $data['id_pedido'], PDO::PARAM_INT);
        $stmt->bindValue(':id_estado_pedido', $data['id_estado_pedido'], PDO::PARAM_INT);
        $stmt->bindValue(':comentario', $data['comentario']);

        if (!$stmt->execute()) {
            return false;
        }

        return (int)$this->connection->lastInsertId();
    }

    public function findPaymentMethodById(int $paymentMethodId): array|false
{
    $sql = "
        SELECT
            id_metodo_pago,
            nombre,
            descripcion,
            activo
        FROM metodo_pago
        WHERE id_metodo_pago = :id_metodo_pago
        LIMIT 1
    ";

    $stmt = $this->connection->prepare($sql);
    $stmt->bindValue(':id_metodo_pago', $paymentMethodId, PDO::PARAM_INT);
    $stmt->execute();

    return $stmt->fetch();
}

    public function findOrderStatusById(int $statusId): array|false
    {
        $sql = "
            SELECT
                id_estado_pedido,
                nombre,
                descripcion
            FROM estado_pedido
            WHERE id_estado_pedido = :id_estado_pedido
            LIMIT 1
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':id_estado_pedido', $statusId, PDO::PARAM_INT);
        $stmt->execute();

        return $stmt->fetch();
    }

    public function getOrdersByUserId(int $userId): array
    {
        $sql = "
            SELECT
                p.id_pedido,
                p.id_usuario,
                p.id_direccion,
                p.id_estado_pedido,
                ep.nombre AS estado_nombre,
                p.id_metodo_pago,
                mp.nombre AS metodo_pago_nombre,
                p.numero_pedido,
                p.subtotal,
                p.total,
                p.observaciones,
                p.fecha_pedido,
                p.fecha_actualizacion
            FROM pedido p
            INNER JOIN estado_pedido ep ON ep.id_estado_pedido = p.id_estado_pedido
            INNER JOIN metodo_pago mp ON mp.id_metodo_pago = p.id_metodo_pago
            WHERE p.id_usuario = :id_usuario
            ORDER BY p.id_pedido DESC
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':id_usuario', $userId, PDO::PARAM_INT);
        $stmt->execute();

        return $stmt->fetchAll();
    }

    public function getOrderById(int $orderId): array|false
    {
        $sql = "
            SELECT
                p.id_pedido,
                p.id_usuario,
                p.id_direccion,
                p.id_estado_pedido,
                ep.nombre AS estado_nombre,
                p.id_metodo_pago,
                mp.nombre AS metodo_pago_nombre,
                p.numero_pedido,
                p.subtotal,
                p.total,
                p.observaciones,
                p.fecha_pedido,
                p.fecha_actualizacion
            FROM pedido p
            INNER JOIN estado_pedido ep ON ep.id_estado_pedido = p.id_estado_pedido
            INNER JOIN metodo_pago mp ON mp.id_metodo_pago = p.id_metodo_pago
            WHERE p.id_pedido = :id_pedido
            LIMIT 1
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':id_pedido', $orderId, PDO::PARAM_INT);
        $stmt->execute();

        return $stmt->fetch();
    }

    public function getOrderDetails(int $orderId): array
    {
        $sql = "
            SELECT
                dp.id_detalle_pedido,
                dp.id_pedido,
                dp.id_platillo,
                p.nombre AS platillo_nombre,
                dp.cantidad,
                dp.precio_unitario,
                dp.subtotal
            FROM detalle_pedido dp
            INNER JOIN platillo p ON p.id_platillo = dp.id_platillo
            WHERE dp.id_pedido = :id_pedido
            ORDER BY dp.id_detalle_pedido ASC
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':id_pedido', $orderId, PDO::PARAM_INT);
        $stmt->execute();

        return $stmt->fetchAll();
    }

    public function findAddressById(int $addressId): array|false
    {
        $sql = "
            SELECT
                id_direccion,
                id_usuario,
                alias,
                direccion_texto,
                referencia,
                latitud,
                longitud,
                es_principal,
                fecha_registro
            FROM direccion_usuario
            WHERE id_direccion = :id_direccion
            LIMIT 1
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':id_direccion', $addressId, PDO::PARAM_INT);
        $stmt->execute();

        return $stmt->fetch();
    }

    public function getAllOrders(): array
    {
        $sql = "
            SELECT
                p.id_pedido,
                p.id_usuario,
                CONCAT(u.nombre, ' ', u.apellido) AS cliente_nombre,
                u.email AS cliente_email,
                p.id_direccion,
                p.id_estado_pedido,
                ep.nombre AS estado_nombre,
                p.id_metodo_pago,
                mp.nombre AS metodo_pago_nombre,
                p.numero_pedido,
                p.subtotal,
                p.total,
                p.observaciones,
                p.fecha_pedido,
                p.fecha_actualizacion
            FROM pedido p
            INNER JOIN usuario u ON u.id_usuario = p.id_usuario
            INNER JOIN estado_pedido ep ON ep.id_estado_pedido = p.id_estado_pedido
            INNER JOIN metodo_pago mp ON mp.id_metodo_pago = p.id_metodo_pago
            ORDER BY p.id_pedido DESC
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->execute();

        return $stmt->fetchAll();
    }

    public function getAdminOrderById(int $orderId): array|false
    {
        $sql = "
            SELECT
                p.id_pedido,
                p.id_usuario,
                CONCAT(u.nombre, ' ', u.apellido) AS cliente_nombre,
                u.email AS cliente_email,
                u.telefono AS cliente_telefono,
                p.id_direccion,
                p.id_estado_pedido,
                ep.nombre AS estado_nombre,
                p.id_metodo_pago,
                mp.nombre AS metodo_pago_nombre,
                p.numero_pedido,
                p.subtotal,
                p.total,
                p.observaciones,
                p.fecha_pedido,
                p.fecha_actualizacion
            FROM pedido p
            INNER JOIN usuario u ON u.id_usuario = p.id_usuario
            INNER JOIN estado_pedido ep ON ep.id_estado_pedido = p.id_estado_pedido
            INNER JOIN metodo_pago mp ON mp.id_metodo_pago = p.id_metodo_pago
            WHERE p.id_pedido = :id_pedido
            LIMIT 1
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':id_pedido', $orderId, PDO::PARAM_INT);
        $stmt->execute();

        return $stmt->fetch();
    }

    public function updateOrderStatus(int $orderId, int $statusId): bool
    {
        $sql = "
            UPDATE pedido
            SET id_estado_pedido = :id_estado_pedido
            WHERE id_pedido = :id_pedido
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':id_estado_pedido', $statusId, PDO::PARAM_INT);
        $stmt->bindValue(':id_pedido', $orderId, PDO::PARAM_INT);

        return $stmt->execute();
    }
    
}