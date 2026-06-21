<?php

declare(strict_types=1);

class Validator
{
    public static function validateRequired(array $data, array $requiredFields): array
    {
        $errors = [];

        foreach ($requiredFields as $field) {
            if (!isset($data[$field]) || trim((string)$data[$field]) === '') {
                $errors[$field] = "The field '{$field}' is required.";
            }
        }

        return $errors;
    }

    public static function validateEmail(string $email): ?string
    {
        if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
            return 'Invalid email format.';
        }

        return null;
    }

    public static function validateMinLength(string $value, int $minLength, string $fieldName): ?string
    {
        if (mb_strlen(trim($value)) < $minLength) {
            return "The field '{$fieldName}' must be at least {$minLength} characters long.";
        }

        return null;
    }
}