<?php

declare(strict_types=1);

require_once __DIR__ . '/../config/database.php';

class Product
{
    private PDO $connection;

    public function __construct()
    {
        $database = new Database();
        $this->connection = $database->connect();
    }

    public function getAll(): array
    {
        $sql = "
            SELECT
                p.id_platillo,
                p.id_categoria,
                c.nombre AS categoria_nombre,
                p.nombre,
                p.descripcion,
                p.precio,
                p.imagen_url,
                p.disponible,
                p.fecha_creacion,
                p.fecha_actualizacion
            FROM platillo p
            INNER JOIN categoria c 
                ON p.id_categoria = c.id_categoria
            WHERE p.disponible = 1
                AND c.activo = 1
            ORDER BY p.nombre ASC
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->execute();

        return $stmt->fetchAll();
    }
    public function findById(int $productId): array|false
{
    $sql = "
        SELECT
            p.id_platillo,
            p.id_categoria,
            c.nombre AS categoria_nombre,
            p.nombre,
            p.descripcion,
            p.precio,
            p.imagen_url
        FROM platillo p
        INNER JOIN categoria c ON c.id_categoria = p.id_categoria
        WHERE p.id_platillo = :id_platillo
        LIMIT 1
    ";

    $stmt = $this->connection->prepare($sql);
    $stmt->bindValue(':id_platillo', $productId, PDO::PARAM_INT);
    $stmt->execute();

    $result = $stmt->fetch(PDO::FETCH_ASSOC);

    return is_array($result) ? $result : false;
}
}