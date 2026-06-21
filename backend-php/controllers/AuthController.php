<?php

declare(strict_types=1);

require_once __DIR__ . '/../services/AuthService.php';
require_once __DIR__ . '/../helpers/Response.php';

class AuthController
{
    private AuthService $authService;

    public function __construct()
    {
        $this->authService = new AuthService();
    }

    public function register(): void
    {
        try {
            $requestData = json_decode(file_get_contents('php://input'), true);

            if (!is_array($requestData)) {
                Response::error('Invalid JSON body', 400);
            }

            $result = $this->authService->register($requestData);

            if (!$result['success']) {
                Response::error(
                    $result['message'],
                    $result['status'],
                    $result['data'] ?? null
                );
            }

            Response::success(
                $result['message'],
                $result['data'],
                $result['status']
            );
        } catch (Throwable $e) {
            Response::error(
                'Error during user registration',
                500,
                ['error' => $e->getMessage()]
            );
        }
    }

    public function login(): void
    {
        try {
            $requestData = json_decode(file_get_contents('php://input'), true);

            if (!is_array($requestData)) {
                Response::error('Invalid JSON body', 400);
            }

            $result = $this->authService->login($requestData);

            if (!$result['success']) {
                Response::error(
                    $result['message'],
                    $result['status'],
                    $result['data'] ?? null
                );
            }

            Response::success(
                $result['message'],
                $result['data'],
                $result['status']
            );
        } catch (Throwable $e) {
            Response::error(
                'Error during login',
                500,
                ['error' => $e->getMessage()]
            );
        }
    }
}