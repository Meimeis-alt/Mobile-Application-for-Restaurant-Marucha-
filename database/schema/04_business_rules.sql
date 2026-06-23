USE marucha_db;

-- =========================================================
-- MARUCHA - BUSINESS RULES / EXTRA CONSTRAINTS
-- =========================================================
-- Este archivo agrega reglas de negocio adicionales que
-- complementan 03_constraints.sql.
--
-- Reglas que cubre:
-- 1) estado de usuario solo 0 o 1
-- 2) es_principal de dirección solo 0 o 1
-- 3) disponible de platillo solo 0 o 1
-- 4) activo de categoría solo 0 o 1
-- 5) activo de método de pago solo 0 o 1
-- 6) carrito_detalle: cantidad máxima por producto = 8
-- 7) detalle_pedido: cantidad máxima por producto = 8
-- 8) pedido.total >= pedido.subtotal
-- 9) evitar platillo duplicado dentro del mismo carrito
-- =========================================================

-- =========================================================
-- 1) CHECKS DE ESTADO / FLAGS
-- =========================================================

ALTER TABLE usuario
ADD CONSTRAINT chk_usuario_estado
CHECK (estado IN (0,1));

ALTER TABLE direccion_usuario
ADD CONSTRAINT chk_direccion_usuario_principal
CHECK (es_principal IN (0,1));

ALTER TABLE categoria
ADD CONSTRAINT chk_categoria_activo
CHECK (activo IN (0,1));

ALTER TABLE platillo
ADD CONSTRAINT chk_platillo_disponible
CHECK (disponible IN (0,1));

ALTER TABLE metodo_pago
ADD CONSTRAINT chk_metodo_pago_activo
CHECK (activo IN (0,1));

-- =========================================================
-- 2) REGLAS DE CANTIDAD MÁXIMA POR PRODUCTO
-- =========================================================
-- Regla de negocio:
-- un usuario no puede pedir más de 8 unidades del mismo platillo
-- por ítem en carrito / pedido.

ALTER TABLE carrito_detalle
ADD CONSTRAINT chk_carrito_detalle_cantidad_max
CHECK (cantidad BETWEEN 1 AND 8);

ALTER TABLE detalle_pedido
ADD CONSTRAINT chk_detalle_pedido_cantidad_max
CHECK (cantidad BETWEEN 1 AND 8);

-- =========================================================
-- 3) REGLA DE TOTAL DE PEDIDO
-- =========================================================
-- El total no debe ser menor que el subtotal.
-- Si luego agregas delivery, descuentos, etc., esto sigue siendo válido.

ALTER TABLE pedido
ADD CONSTRAINT chk_pedido_total_mayor_igual_subtotal
CHECK (total >= subtotal);

-- =========================================================
-- 4) EVITAR PLATILLOS DUPLICADOS DENTRO DEL MISMO CARRITO
-- =========================================================
-- Un mismo carrito no debería tener dos filas separadas
-- para el mismo platillo.

ALTER TABLE carrito_detalle
ADD CONSTRAINT uq_carrito_detalle_carrito_platillo
UNIQUE (id_carrito, id_platillo);

-- =========================================================
-- 5) ÍNDICES EXTRA ÚTILES
-- =========================================================
-- Estos no son obligatorios, pero ayudan a consultas frecuentes.

CREATE INDEX idx_direccion_usuario_principal
ON direccion_usuario(id_usuario, es_principal);

CREATE INDEX idx_carrito_usuario_estado
ON carrito(id_usuario, estado);

CREATE INDEX idx_pedido_usuario_fecha
ON pedido(id_usuario, fecha_pedido);

CREATE INDEX idx_pedido_estado_fecha
ON pedido(id_estado_pedido, fecha_pedido);