<?php

declare(strict_types=1);

require_once __DIR__ . '/../config/database.php';

class Address
{
    private PDO $connection;

    public function __construct()
    {
        $database = new Database();
        $this->connection = $database->connect();
    }

    public function getByUserId(int $userId): array
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
            WHERE id_usuario = :id_usuario
            ORDER BY es_principal DESC, id_direccion DESC
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':id_usuario', $userId, PDO::PARAM_INT);
        $stmt->execute();

        return $stmt->fetchAll();
    }

    public function create(array $data): int
    {
        $sql = "
            INSERT INTO direccion_usuario (
                id_usuario,
                alias,
                direccion_texto,
                referencia,
                latitud,
                longitud,
                es_principal
            ) VALUES (
                :id_usuario,
                :alias,
                :direccion_texto,
                :referencia,
                :latitud,
                :longitud,
                :es_principal
            )
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':id_usuario', $data['id_usuario'], PDO::PARAM_INT);
        $stmt->bindValue(':alias', $data['alias']);
        $stmt->bindValue(':direccion_texto', $data['direccion_texto']);
        $stmt->bindValue(':referencia', $data['referencia']);
        $stmt->bindValue(':latitud', $data['latitud']);
        $stmt->bindValue(':longitud', $data['longitud']);
        $stmt->bindValue(':es_principal', $data['es_principal'], PDO::PARAM_INT);

        $stmt->execute();

        return (int) $this->connection->lastInsertId();
    }

    public function findById(int $addressId): array|false
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
    public function update(int $addressId, array $data): bool
{
    $sql = "
        UPDATE direccion_usuario
        SET
            alias = :alias,
            direccion_texto = :direccion_texto,
            referencia = :referencia,
            latitud = :latitud,
            longitud = :longitud,
            es_principal = :es_principal
        WHERE id_direccion = :id_direccion
    ";

    $stmt = $this->connection->prepare($sql);
    $stmt->bindValue(':alias', $data['alias']);
    $stmt->bindValue(':direccion_texto', $data['direccion_texto']);
    $stmt->bindValue(':referencia', $data['referencia']);
    $stmt->bindValue(':latitud', $data['latitud']);
    $stmt->bindValue(':longitud', $data['longitud']);
    $stmt->bindValue(':es_principal', $data['es_principal'], PDO::PARAM_INT);
    $stmt->bindValue(':id_direccion', $addressId, PDO::PARAM_INT);

    return $stmt->execute();
}

    public function delete(int $addressId): bool
    {
        $sql = "DELETE FROM direccion_usuario WHERE id_direccion = :id_direccion";
    
        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':id_direccion', $addressId, PDO::PARAM_INT);
    
        return $stmt->execute();
    }
    
    public function clearPrimaryByUserId(int $userId): bool
    {
        $sql = "
            UPDATE direccion_usuario
            SET es_principal = 0
            WHERE id_usuario = :id_usuario
        ";
    
        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':id_usuario', $userId, PDO::PARAM_INT);
    
        return $stmt->execute();
    }
}