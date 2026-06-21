<?php

declare(strict_types=1);

class Database
{
    private string $host = '127.0.0.1';
    private string $port = '3306';
    private string $dbName = 'marucha_db';
    private string $username = 'root';
    private string $password = '';
    private string $charset = 'utf8mb4';

    private ?PDO $connection = null;

    public function connect(): PDO
    {
        if ($this->connection !== null) {
            return $this->connection;
        }

        $dsn = "mysql:host={$this->host};port={$this->port};dbname={$this->dbName};charset={$this->charset}";

        try {
            $this->connection = new PDO($dsn, $this->username, $this->password, [
                PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
                PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
                PDO::ATTR_EMULATE_PREPARES   => false,
            ]);

            return $this->connection;
        } catch (PDOException $e) {
            http_response_code(500);

            echo json_encode([
                'success' => false,
                'message' => 'Database connection error',
                'error'   => $e->getMessage()
            ], JSON_UNESCAPED_UNICODE);

            exit;
        }
    }
}