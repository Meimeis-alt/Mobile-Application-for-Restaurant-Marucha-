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

    public function getUserById(int $id): array
    {
        $user = $this->userModel->findById($id);

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

    public function updateUserProfile(int $id, array $data): array
    {
        $user = $this->userModel->findById($id);

        if (!$user) {
            return [
                'success' => false,
                'status'  => 404,
                'message' => 'User not found'
            ];
        }

        $errors = Validator::validateRequired($data, [
            'nombre',
            'apellido'
        ]);

        if (!empty($errors)) {
            return [
                'success' => false,
                'status'  => 422,
                'message' => 'Validation errors',
                'data'    => $errors
            ];
        }

        $updateData = [
            'nombre'   => trim($data['nombre']),
            'apellido' => trim($data['apellido']),
            'telefono' => $data['telefono'] ?? null
        ];

        $updated = $this->userModel->updateProfile($id, $updateData);

        if (!$updated) {
            return [
                'success' => false,
                'status'  => 500,
                'message' => 'Failed to update user profile'
            ];
        }

        $updatedUser = $this->userModel->findById($id);

        return [
            'success' => true,
            'status'  => 200,
            'message' => 'User profile updated successfully',
            'data'    => $updatedUser
        ];
    }
}