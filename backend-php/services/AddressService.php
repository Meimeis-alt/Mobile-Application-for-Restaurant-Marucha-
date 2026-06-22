<?php

declare(strict_types=1);

require_once __DIR__ . '/../models/Address.php';
require_once __DIR__ . '/../models/User.php';
require_once __DIR__ . '/../helpers/Validator.php';

class AddressService
{
    private Address $addressModel;
    private User $userModel;

    public function __construct()
    {
        $this->addressModel = new Address();
        $this->userModel = new User();
    }

    public function getUserAddresses(int $userId): array
    {
        $user = $this->userModel->findById($userId);

        if (!$user) {
            return [
                'success' => false,
                'status'  => 404,
                'message' => 'User not found'
            ];
        }

        $addresses = $this->addressModel->getByUserId($userId);

        return [
            'success' => true,
            'status'  => 200,
            'message' => 'User addresses retrieved successfully',
            'data'    => $addresses
        ];
    }

    public function createAddress(int $userId, array $data): array
    {
        $user = $this->userModel->findById($userId);

        if (!$user) {
            return [
                'success' => false,
                'status'  => 404,
                'message' => 'User not found'
            ];
        }

        $errors = Validator::validateRequired($data, [
            'direccion_texto'
        ]);

        if (!empty($errors)) {
            return [
                'success' => false,
                'status'  => 422,
                'message' => 'Validation errors',
                'data'    => $errors
            ];
        }

        $addressData = [
            'id_usuario'       => $userId,
            'alias'            => $data['alias'] ?? null,
            'direccion_texto'  => trim($data['direccion_texto']),
            'referencia'       => $data['referencia'] ?? null,
            'latitud'          => $data['latitud'] ?? null,
            'longitud'         => $data['longitud'] ?? null,
            'es_principal'     => isset($data['es_principal']) ? (int)$data['es_principal'] : 0
        ];
        if ($addressData['es_principal'] === 1) {
    $this->addressModel->clearPrimaryByUserId($userId);
}

        $newAddressId = $this->addressModel->create($addressData);
        $newAddress = $this->addressModel->findById($newAddressId);

        return [
            'success' => true,
            'status'  => 201,
            'message' => 'Address created successfully',
            'data'    => $newAddress
        ];
    }
    public function updateAddress(int $addressId, array $data): array
{
    $address = $this->addressModel->findById($addressId);

    if (!$address) {
        return [
            'success' => false,
            'status'  => 404,
            'message' => 'Address not found'
        ];
    }

    $errors = Validator::validateRequired($data, [
        'direccion_texto'
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
        'alias'           => $data['alias'] ?? null,
        'direccion_texto' => trim($data['direccion_texto']),
        'referencia'      => $data['referencia'] ?? null,
        'latitud'         => $data['latitud'] ?? null,
        'longitud'        => $data['longitud'] ?? null,
        'es_principal'    => isset($data['es_principal']) ? (int)$data['es_principal'] : 0
    ];

    // Si esta dirección pasa a ser principal, desmarcar las demás del mismo usuario
    if ($updateData['es_principal'] === 1) {
        $this->addressModel->clearPrimaryByUserId((int)$address['id_usuario']);
    }

    $updated = $this->addressModel->update($addressId, $updateData);

    if (!$updated) {
        return [
            'success' => false,
            'status'  => 500,
            'message' => 'Failed to update address'
        ];
    }

    $updatedAddress = $this->addressModel->findById($addressId);

    return [
        'success' => true,
        'status'  => 200,
        'message' => 'Address updated successfully',
        'data'    => $updatedAddress
    ];
}

    public function deleteAddress(int $addressId): array
    {
        $address = $this->addressModel->findById($addressId);

        if (!$address) {
            return [
                'success' => false,
                'status'  => 404,
                'message' => 'Address not found'
            ];
        }

        $deleted = $this->addressModel->delete($addressId);

        if (!$deleted) {
            return [
                'success' => false,
                'status'  => 500,
                'message' => 'Failed to delete address'
            ];
        }

        return [
            'success' => true,
            'status'  => 200,
            'message' => 'Address deleted successfully'
        ];
    }
}