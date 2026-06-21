<?php

declare(strict_types=1);

require_once __DIR__ . '/../models/User.php';
require_once __DIR__ . '/../helpers/Validator.php';

class AuthService
{
    private User $userModel;

    public function __construct()
    {
        $this->userModel = new User();
    }

    public function register(array $data): array
    {
        // 1. Validar campos obligatorios
        $errors = Validator::validateRequired($data, [
            'username',
            'nombre',
            'apellido',
            'email',
            'password'
        ]);

        // 2. Validar email
        if (isset($data['email'])) {
            $emailError = Validator::validateEmail($data['email']);
            if ($emailError !== null) {
                $errors['email'] = $emailError;
            }
        }

        // 3. Validar longitud mínima de password
        if (isset($data['password'])) {
            $passwordError = Validator::validateMinLength($data['password'], 8, 'password');
            if ($passwordError !== null) {
                $errors['password'] = $passwordError;
            }
        }

        if (!empty($errors)) {
            return [
                'success' => false,
                'status'  => 422,
                'message' => 'Validation errors',
                'data'    => $errors
            ];
        }

        // 4. Verificar si email ya existe
        $existingUserByEmail = $this->userModel->findByEmail($data['email']);
        if ($existingUserByEmail) {
            return [
                'success' => false,
                'status'  => 409,
                'message' => 'Email is already registered'
            ];
        }

        // 5. Verificar si username ya existe
        $existingUserByUsername = $this->userModel->findByUsername($data['username']);
        if ($existingUserByUsername) {
            return [
                'success' => false,
                'status'  => 409,
                'message' => 'Username is already registered'
            ];
        }

        // 6. Crear hash de contraseña
        $passwordHash = password_hash($data['password'], PASSWORD_DEFAULT);

        // 7. Preparar datos del usuario
        $userData = [
            'id_rol'        => 1, // cliente por defecto
            'username'      => trim($data['username']),
            'nombre'        => trim($data['nombre']),
            'apellido'      => trim($data['apellido']),
            'email'         => trim($data['email']),
            'password_hash' => $passwordHash,
            'telefono'      => $data['telefono'] ?? null,
            'auth_provider' => 'local',
            'estado'        => 1
        ];

        // 8. Guardar usuario
        $newUserId = $this->userModel->create($userData);

        return [
            'success' => true,
            'status'  => 201,
            'message' => 'User registered successfully',
            'data'    => [
                'id_usuario' => $newUserId,
                'username'   => $userData['username'],
                'nombre'     => $userData['nombre'],
                'apellido'   => $userData['apellido'],
                'email'      => $userData['email'],
                'telefono'   => $userData['telefono']
            ]
        ];
    }

    public function login(array $data): array
    {
        // 1. Validar campos obligatorios
        $errors = Validator::validateRequired($data, [
            'email',
            'password'
        ]);

        // 2. Validar formato email
        if (isset($data['email'])) {
            $emailError = Validator::validateEmail($data['email']);
            if ($emailError !== null) {
                $errors['email'] = $emailError;
            }
        }

        if (!empty($errors)) {
            return [
                'success' => false,
                'status'  => 422,
                'message' => 'Validation errors',
                'data'    => $errors
            ];
        }

        // 3. Buscar usuario por email
        $user = $this->userModel->findByEmail(trim($data['email']));

        if (!$user) {
            return [
                'success' => false,
                'status'  => 401,
                'message' => 'Invalid credentials'
            ];
        }

        // 4. Verificar si el usuario está activo
        if ((int)$user['estado'] !== 1) {
            return [
                'success' => false,
                'status'  => 403,
                'message' => 'User account is inactive'
            ];
        }

        // 5. Verificar contraseña
        if (!password_verify($data['password'], $user['password_hash'])) {
            return [
                'success' => false,
                'status'  => 401,
                'message' => 'Invalid credentials'
            ];
        }

        // 6. Retornar datos del usuario autenticado
        return [
            'success' => true,
            'status'  => 200,
            'message' => 'Login successful',
            'data'    => [
                'user' => [
                    'id_usuario'    => $user['id_usuario'],
                    'id_rol'        => $user['id_rol'],
                    'username'      => $user['username'],
                    'nombre'        => $user['nombre'],
                    'apellido'      => $user['apellido'],
                    'email'         => $user['email'],
                    'telefono'      => $user['telefono'],
                    'foto_perfil'   => $user['foto_perfil'],
                    'auth_provider' => $user['auth_provider'],
                    'estado'        => $user['estado']
                ]
            ]
        ];
    }
}