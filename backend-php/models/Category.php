<?php

declare(strict_types=1);

require_once __DIR__ . '/../config/database.php';

class Category
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
                id_categoria,
                nombre,
                descripcion,
                activo,
                fecha_creacion
            FROM categoria
            WHERE activo = 1
            ORDER BY nombre ASC
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->execute();

        return $stmt->fetchAll();
    }
}