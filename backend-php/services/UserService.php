<?php

declare(strict_types=1);

require_once __DIR__ . '/../models/User.php';
require_once __DIR__ . '/../helpers/Validator.php';

class UserService
{
    private User $userModel;

    public function __construct()
    {
        $this->userModel = new User();
    }

    public function getById(int $userId): array
    {
        $user = $this->userModel->findById($userId);

        if (!$user) {
            return [
                'success' => false,
                'status'  => 404,
                'message' => 'User not found'
            ];
        }

        return [
            'success' => true,
            'status'  => 200,
            'message' => 'User retrieved successfully',
            'data'    => $user
        ];
    }

    public function update(int $userId, array $data): array
    {
        $user = $this->userModel->findById($userId);

        if (!$user) {
            return [
                'success' => false,
                'status'  => 404,
                'message' => 'User not found'
            ];
        }

        $errors = [];

        if (!isset($data['nombre']) || trim((string)$data['nombre']) === '') {
            $errors['nombre'] = 'The field "nombre" is required.';
        }

        if (!isset($data['apellido']) || trim((string)$data['apellido']) === '') {
            $errors['apellido'] = 'The field "apellido" is required.';
        }

        if (!isset($data['telefono']) || trim((string)$data['telefono']) === '') {
            $errors['telefono'] = 'The field "telefono" is required.';
        }

        if (!empty($errors)) {
            return [
                'success' => false,
                'status'  => 422,
                'message' => 'Validation errors',
                'data'    => $errors
            ];
        }

        $payload = [
            'nombre'   => trim((string)$data['nombre']),
            'apellido' => trim((string)$data['apellido']),
            'telefono' => trim((string)$data['telefono'])
        ];

        $updated = $this->userModel->update($userId, $payload);

        if (!$updated) {
            return [
                'success' => false,
                'status'  => 500,
                'message' => 'Failed to update user'
            ];
        }

        $updatedUser = $this->userModel->findById($userId);

        return [
            'success' => true,
            'status'  => 200,
            'message' => 'User updated successfully',
            'data'    => $updatedUser
        ];
    }
}