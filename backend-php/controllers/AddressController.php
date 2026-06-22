<?php

declare(strict_types=1);

require_once __DIR__ . '/../services/AddressService.php';
require_once __DIR__ . '/../helpers/Response.php';

class AddressController
{
    private AddressService $addressService;

    public function __construct()
    {
        $this->addressService = new AddressService();
    }

    public function indexByUser(int $userId): void
    {
        try {
            $result = $this->addressService->getUserAddresses($userId);

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
                'Error retrieving user addresses',
                500,
                ['error' => $e->getMessage()]
            );
        }
    }

    public function store(int $userId): void
    {
        try {
            $requestData = json_decode(file_get_contents('php://input'), true);

            if (!is_array($requestData)) {
                Response::error('Invalid JSON body', 400);
            }

            $result = $this->addressService->createAddress($userId, $requestData);

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
                'Error creating address',
                500,
                ['error' => $e->getMessage()]
            );
        }
    }

    public function update(int $addressId): void
    {
        try {
            $requestData = json_decode(file_get_contents('php://input'), true);

            if (!is_array($requestData)) {
                Response::error('Invalid JSON body', 400);
            }

            $result = $this->addressService->updateAddress($addressId, $requestData);

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
                'Error updating address',
                500,
                ['error' => $e->getMessage()]
            );
        }
    }

    public function delete(int $addressId): void
    {
        try {
            $result = $this->addressService->deleteAddress($addressId);

            if (!$result['success']) {
                Response::error(
                    $result['message'],
                    $result['status'],
                    $result['data'] ?? null
                );
            }

            Response::success(
                $result['message'],
                $result['data'] ?? null,
                $result['status']
            );
        } catch (Throwable $e) {
            Response::error(
                'Error deleting address',
                500,
                ['error' => $e->getMessage()]
            );
        }
    }
}