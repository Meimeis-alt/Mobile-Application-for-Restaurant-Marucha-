USE marucha_db;

-- =========================================================
-- MARUCHA - BUSINESS TRIGGERS
-- =========================================================
-- Este archivo agrega triggers de negocio para reforzar la
-- integridad del sistema.
--
-- Triggers incluidos:
--
-- DIRECCIONES
-- 1) Solo una dirección principal por usuario (INSERT)
-- 2) Solo una dirección principal por usuario (UPDATE)
--
-- CARRITO DETALLE
-- 3) Recalcular subtotal automáticamente antes de INSERT
-- 4) Recalcular subtotal automáticamente antes de UPDATE
--
-- DETALLE PEDIDO
-- 5) Recalcular subtotal automáticamente antes de INSERT
-- 6) Recalcular subtotal automáticamente antes de UPDATE
--
-- CARRITO
-- 7) Impedir más de un carrito activo por usuario (INSERT)
-- 8) Impedir más de un carrito activo por usuario (UPDATE)
-- =========================================================

DELIMITER $$

-- =========================================================
-- 1) DIRECCIÓN PRINCIPAL ÚNICA - INSERT
-- =========================================================
DROP TRIGGER IF EXISTS trg_direccion_usuario_bi_unica_principal $$

CREATE TRIGGER trg_direccion_usuario_bi_unica_principal
BEFORE INSERT ON direccion_usuario
FOR EACH ROW
BEGIN
    IF NEW.es_principal = 1 THEN
        UPDATE direccion_usuario
        SET es_principal = 0
        WHERE id_usuario = NEW.id_usuario;
    END IF;
END $$

-- =========================================================
-- 2) DIRECCIÓN PRINCIPAL ÚNICA - UPDATE
-- =========================================================
DROP TRIGGER IF EXISTS trg_direccion_usuario_bu_unica_principal $$

CREATE TRIGGER trg_direccion_usuario_bu_unica_principal
BEFORE UPDATE ON direccion_usuario
FOR EACH ROW
BEGIN
    IF NEW.es_principal = 1 THEN
        UPDATE direccion_usuario
        SET es_principal = 0
        WHERE id_usuario = NEW.id_usuario
          AND id_direccion <> OLD.id_direccion;
    END IF;
END $$

-- =========================================================
-- 3) RECALCULAR SUBTOTAL EN CARRITO_DETALLE - INSERT
-- =========================================================
DROP TRIGGER IF EXISTS trg_carrito_detalle_bi_subtotal $$

CREATE TRIGGER trg_carrito_detalle_bi_subtotal
BEFORE INSERT ON carrito_detalle
FOR EACH ROW
BEGIN
    SET NEW.subtotal = ROUND(NEW.cantidad * NEW.precio_unitario, 2);
END $$

-- =========================================================
-- 4) RECALCULAR SUBTOTAL EN CARRITO_DETALLE - UPDATE
-- =========================================================
DROP TRIGGER IF EXISTS trg_carrito_detalle_bu_subtotal $$

CREATE TRIGGER trg_carrito_detalle_bu_subtotal
BEFORE UPDATE ON carrito_detalle
FOR EACH ROW
BEGIN
    SET NEW.subtotal = ROUND(NEW.cantidad * NEW.precio_unitario, 2);
END $$

-- =========================================================
-- 5) RECALCULAR SUBTOTAL EN DETALLE_PEDIDO - INSERT
-- =========================================================
DROP TRIGGER IF EXISTS trg_detalle_pedido_bi_subtotal $$

CREATE TRIGGER trg_detalle_pedido_bi_subtotal
BEFORE INSERT ON detalle_pedido
FOR EACH ROW
BEGIN
    SET NEW.subtotal = ROUND(NEW.cantidad * NEW.precio_unitario, 2);
END $$

-- =========================================================
-- 6) RECALCULAR SUBTOTAL EN DETALLE_PEDIDO - UPDATE
-- =========================================================
DROP TRIGGER IF EXISTS trg_detalle_pedido_bu_subtotal $$

CREATE TRIGGER trg_detalle_pedido_bu_subtotal
BEFORE UPDATE ON detalle_pedido
FOR EACH ROW
BEGIN
    SET NEW.subtotal = ROUND(NEW.cantidad * NEW.precio_unitario, 2);
END $$

-- =========================================================
-- 7) IMPEDIR MÁS DE UN CARRITO ACTIVO POR USUARIO - INSERT
-- =========================================================
DROP TRIGGER IF EXISTS trg_carrito_bi_unico_activo $$

CREATE TRIGGER trg_carrito_bi_unico_activo
BEFORE INSERT ON carrito
FOR EACH ROW
BEGIN
    IF NEW.estado = 'activo' AND EXISTS (
        SELECT 1
        FROM carrito
        WHERE id_usuario = NEW.id_usuario
          AND estado = 'activo'
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El usuario ya tiene un carrito activo';
    END IF;
END $$

-- =========================================================
-- 8) IMPEDIR MÁS DE UN CARRITO ACTIVO POR USUARIO - UPDATE
-- =========================================================
DROP TRIGGER IF EXISTS trg_carrito_bu_unico_activo $$

CREATE TRIGGER trg_carrito_bu_unico_activo
BEFORE UPDATE ON carrito
FOR EACH ROW
BEGIN
    IF NEW.estado = 'activo' AND EXISTS (
        SELECT 1
        FROM carrito
        WHERE id_usuario = NEW.id_usuario
          AND estado = 'activo'
          AND id_carrito <> OLD.id_carrito
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El usuario ya tiene un carrito activo';
    END IF;
END $$

DELIMITER ;