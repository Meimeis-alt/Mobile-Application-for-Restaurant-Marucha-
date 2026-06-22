<?php

declare(strict_types=1);

require_once __DIR__ . '/../services/UserService.php';
require_once __DIR__ . '/../helpers/Response.php';

class UserController
{
    private UserService $userService;

    public function __construct()
    {
        $this->userService = new UserService();
    }

    public function show(int $userId): void
    {
        try {
            $result = $this->userService->getById($userId);

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
                'Error retrieving user',
                500,
                ['error' => $e->getMessage()]
            );
        }
    }

    public function update(int $userId): void
    {
        try {
            $requestData = json_decode(file_get_contents('php://input'), true);

            if (!is_array($requestData)) {
                Response::error('Invalid JSON body', 400);
            }

            $result = $this->userService->update($userId, $requestData);

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
                'Error updating user',
                500,
                ['error' => $e->getMessage()]
            );
        }
    }
}