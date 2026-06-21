# Marucha Backend PHP

## Descripción
`backend-php` es el módulo backend de **Marucha**, una aplicación móvil para la gestión y pedidos de un restaurante.  
En esta primera base del backend se implementó la configuración inicial del proyecto en PHP, la conexión a MySQL, el sistema de respuestas JSON, y los primeros endpoints de consulta para categorías y productos.

Esta versión corresponde a la base funcional del backend desarrollada en la rama:

- `feature/php-backend-base`

y consolidada como:

- `v0.3.0`

---

## Tecnologías utilizadas
- **PHP 8**
- **MySQL / MariaDB**
- **PDO** para la conexión a base de datos
- **Apache + XAMPP** para entorno local
- Arquitectura básica por capas:
  - `config`
  - `models`
  - `controllers`
  - `routes`
  - `helpers`

---

## Estructura del módulo

```bash
backend-php/
├─ config/
│  ├─ config.php
│  └─ database.php
├─ controllers/
│  ├─ AuthController.php
│  ├─ CategoryController.php
│  ├─ OrderController.php
│  ├─ ProductController.php
│  └─ UserController.php
├─ helpers/
│  ├─ Response.php
│  └─ Validator.php
├─ middleware/
├─ models/
│  ├─ Category.php
│  ├─ Order.php
│  ├─ OrderDetail.php
│  ├─ Product.php
│  └─ User.php
├─ public/
│  └─ index.php
├─ routes/
│  └─ api.php
├─ services/
│  ├─ AuthService.php
│  ├─ OrderService.php
│  └─ ProductService.php
├─ tests/
├─ uploads/
└─ README.md