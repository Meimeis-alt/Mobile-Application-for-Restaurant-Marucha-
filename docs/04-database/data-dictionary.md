# Data Dictionary – Marucha Mobile App

This document defines the tables, fields, and purpose of the database entities used in **Marucha Mobile App**.

---

# 1. Table: `rol`

| Field | Type | Null | Key | Description |
|---|---|---|---|---|
| id_rol | INT | No | PK | Unique identifier of the role |
| nombre | VARCHAR(30) | No | UNIQUE | Role name (`cliente`, `admin`) |
| descripcion | VARCHAR(100) | Yes |  | Optional role description |

---

# 2. Table: `usuario`

| Field | Type | Null | Key | Description |
|---|---|---|---|---|
| id_usuario | INT | No | PK | Unique identifier of the user |
| id_rol | INT | No | FK | User role |
| username | VARCHAR(50) | No | UNIQUE | Username |
| nombre | VARCHAR(100) | No |  | First name |
| apellido | VARCHAR(100) | No |  | Last name |
| email | VARCHAR(120) | No | UNIQUE | Email address |
| password_hash | VARCHAR(255) | Yes |  | Password hash for local authentication |
| telefono | VARCHAR(20) | Yes |  | Phone number |
| fecha_nacimiento | DATE | Yes |  | Birth date |
| foto_perfil | VARCHAR(255) | Yes |  | Profile image path or URL |
| auth_provider | ENUM('local','google') | No |  | Authentication provider |
| google_id | VARCHAR(255) | Yes | UNIQUE | Google account identifier |
| estado | TINYINT(1) | No |  | User status: 1 active / 0 inactive |
| fecha_registro | TIMESTAMP | No |  | Creation date |
| fecha_actualizacion | TIMESTAMP | Yes |  | Last update date |

---

# 3. Table: `direccion_usuario`

| Field | Type | Null | Key | Description |
|---|---|---|---|---|
| id_direccion | INT | No | PK | Unique identifier of the address |
| id_usuario | INT | No | FK | Owner user |
| alias | VARCHAR(50) | Yes |  | Address alias (e.g. Casa, Trabajo) |
| direccion_texto | VARCHAR(255) | No |  | Address or reference text |
| referencia | VARCHAR(255) | Yes |  | Additional reference |
| latitud | DECIMAL(10,7) | Yes |  | Latitude |
| longitud | DECIMAL(10,7) | Yes |  | Longitude |
| es_principal | TINYINT(1) | No |  | Indicates if this is the main address |
| fecha_registro | TIMESTAMP | No |  | Creation date |

---

# 4. Table: `categoria`

| Field | Type | Null | Key | Description |
|---|---|---|---|---|
| id_categoria | INT | No | PK | Unique identifier of the category |
| nombre | VARCHAR(100) | No | UNIQUE | Category name |
| descripcion | VARCHAR(255) | Yes |  | Category description |
| activo | TINYINT(1) | No |  | 1 active / 0 inactive |
| fecha_creacion | TIMESTAMP | No |  | Creation date |

---

# 5. Table: `platillo`

| Field | Type | Null | Key | Description |
|---|---|---|---|---|
| id_platillo | INT | No | PK | Unique identifier of the platillo |
| id_categoria | INT | No | FK | Related category |
| nombre | VARCHAR(150) | No |  | Platillo name |
| descripcion | TEXT | Yes |  | Platillo description |
| precio | DECIMAL(10,2) | No |  | Platillo price |
| imagen_url | VARCHAR(255) | Yes |  | Platillo image path or URL |
| disponible | TINYINT(1) | No |  | 1 available / 0 unavailable |
| fecha_creacion | TIMESTAMP | No |  | Creation date |
| fecha_actualizacion | TIMESTAMP | Yes |  | Last update date |

---

# 6. Table: `carrito`

| Field | Type | Null | Key | Description |
|---|---|---|---|---|
| id_carrito | INT | No | PK | Unique identifier of the cart |
| id_usuario | INT | No | FK | Owner user |
| estado | ENUM('activo','convertido','abandonado') | No |  | Cart state |
| fecha_creacion | TIMESTAMP | No |  | Creation date |
| fecha_actualizacion | TIMESTAMP | Yes |  | Last update date |

---

# 7. Table: `carrito_detalle`

| Field | Type | Null | Key | Description |
|---|---|---|---|---|
| id_carrito_detalle | INT | No | PK | Unique identifier of the cart detail |
| id_carrito | INT | No | FK | Related cart |
| id_platillo | INT | No | FK | Added platillo |
| cantidad | INT | No |  | Quantity |
| precio_unitario | DECIMAL(10,2) | No |  | Unit price |
| subtotal | DECIMAL(10,2) | No |  | Calculated subtotal |

---

# 8. Table: `metodo_pago`

| Field | Type | Null | Key | Description |
|---|---|---|---|---|
| id_metodo_pago | INT | No | PK | Unique identifier of the payment method |
| nombre | VARCHAR(50) | No | UNIQUE | Payment method name |
| descripcion | VARCHAR(100) | Yes |  | Optional description |
| activo | TINYINT(1) | No |  | 1 active / 0 inactive |

---

# 9. Table: `estado_pedido`

| Field | Type | Null | Key | Description |
|---|---|---|---|---|
| id_estado_pedido | INT | No | PK | Unique identifier of the order state |
| nombre | VARCHAR(50) | No | UNIQUE | Order state name |
| descripcion | VARCHAR(100) | Yes |  | Optional description |

---

# 10. Table: `pedido`

| Field | Type | Null | Key | Description |
|---|---|---|---|---|
| id_pedido | INT | No | PK | Unique identifier of the order |
| id_usuario | INT | No | FK | Customer who placed the order |
| id_direccion | INT | Yes | FK | Selected address/reference |
| id_estado_pedido | INT | No | FK | Current order state |
| id_metodo_pago | INT | No | FK | Selected payment method |
| numero_pedido | VARCHAR(20) | No | UNIQUE | Visible order code |
| subtotal | DECIMAL(10,2) | No |  | Order subtotal |
| total | DECIMAL(10,2) | No |  | Final order total |
| observaciones | VARCHAR(255) | Yes |  | Additional order notes |
| fecha_pedido | TIMESTAMP | No |  | Order creation date |
| fecha_actualizacion | TIMESTAMP | Yes |  | Last update date |

---

# 11. Table: `detalle_pedido`

| Field | Type | Null | Key | Description |
|---|---|---|---|---|
| id_detalle_pedido | INT | No | PK | Unique identifier of the order detail |
| id_pedido | INT | No | FK | Related order |
| id_platillo | INT | No | FK | Ordered platillo |
| cantidad | INT | No |  | Quantity ordered |
| precio_unitario | DECIMAL(10,2) | No |  | Unit price at purchase time |
| subtotal | DECIMAL(10,2) | No |  | Detail subtotal |

---

# 12. Table: `pedido_historial_estado`

| Field | Type | Null | Key | Description |
|---|---|---|---|---|
| id_historial | INT | No | PK | Unique identifier of the history record |
| id_pedido | INT | No | FK | Related order |
| id_estado_pedido | INT | No | FK | Registered order state |
| comentario | VARCHAR(255) | Yes |  | Optional comment |
| fecha_cambio | TIMESTAMP | No |  | State change date |

---

# 13. Relationship Summary

- `rol` 1:N `usuario`
- `usuario` 1:N `direccion_usuario`
- `categoria` 1:N `platillo`
- `usuario` 1:N `carrito`
- `carrito` 1:N `carrito_detalle`
- `platillo` 1:N `carrito_detalle`
- `usuario` 1:N `pedido`
- `direccion_usuario` 1:N `pedido`
- `metodo_pago` 1:N `pedido`
- `estado_pedido` 1:N `pedido`
- `pedido` 1:N `detalle_pedido`
- `platillo` 1:N `detalle_pedido`
- `pedido` 1:N `pedido_historial_estado`
- `estado_pedido` 1:N `pedido_historial_estado`