<?php

declare(strict_types=1);

require_once __DIR__ . '/../config/database.php';

class User
{
    private PDO $connection;

    public function __construct()
    {
        $database = new Database();
        $this->connection = $database->connect();
    }

    public function findByEmail(string $email): array|false
    {
        $sql = "
            SELECT 
                id_usuario,
                id_rol,
                username,
                nombre,
                apellido,
                email,
                password_hash,
                telefono,
                fecha_nacimiento,
                foto_perfil,
                auth_provider,
                google_id,
                estado,
                fecha_registro,
                fecha_actualizacion
            FROM usuario
            WHERE email = :email
            LIMIT 1
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':email', $email);
        $stmt->execute();

        return $stmt->fetch();
    }

    public function findByUsername(string $username): array|false
    {
        $sql = "
            SELECT 
                id_usuario,
                id_rol,
                username,
                nombre,
                apellido,
                email,
                password_hash,
                telefono,
                fecha_nacimiento,
                foto_perfil,
                auth_provider,
                google_id,
                estado,
                fecha_registro,
                fecha_actualizacion
            FROM usuario
            WHERE username = :username
            LIMIT 1
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':username', $username);
        $stmt->execute();

        return $stmt->fetch();
    }

    public function create(array $data): int
    {
        $sql = "
            INSERT INTO usuario (
                id_rol,
                username,
                nombre,
                apellido,
                email,
                password_hash,
                telefono,
                auth_provider,
                estado
            ) VALUES (
                :id_rol,
                :username,
                :nombre,
                :apellido,
                :email,
                :password_hash,
                :telefono,
                :auth_provider,
                :estado
            )
        ";

        $stmt = $this->connection->prepare($sql);
        $stmt->bindValue(':id_rol', $data['id_rol'], PDO::PARAM_INT);
        $stmt->bindValue(':username', $data['username']);
        $stmt->bindValue(':nombre', $data['nombre']);
        $stmt->bindValue(':apellido', $data['apellido']);
        $stmt->bindValue(':email', $data['email']);
        $stmt->bindValue(':password_hash', $data['password_hash']);
        $stmt->bindValue(':telefono', $data['telefono']);
        $stmt->bindValue(':auth_provider', $data['auth_provider']);
        $stmt->bindValue(':estado', $data['estado'], PDO::PARAM_INT);

        $stmt->execute();

        return (int) $this->connection->lastInsertId();
    }
}