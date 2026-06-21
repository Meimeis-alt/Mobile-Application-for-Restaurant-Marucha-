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
}