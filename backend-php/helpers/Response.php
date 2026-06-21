<?php

declare(strict_types=1);

class Response
{
    public static function json(bool $success, string $message, mixed $data = null, int $statusCode = 200): void
    {
        http_response_code($statusCode);

        $response = [
            'success' => $success,
            'message' => $message
        ];

        if ($data !== null) {
            $response['data'] = $data;
        }

        echo json_encode($response, JSON_UNESCAPED_UNICODE);
        exit;
    }

    public static function success(string $message, mixed $data = null, int $statusCode = 200): void
    {
        self::json(true, $message, $data, $statusCode);
    }

    public static function error(string $message, int $statusCode = 500, mixed $data = null): void
    {
        self::json(false, $message, $data, $statusCode);
    }
}