USE marucha_db;

-- ============================================
-- ROLES
-- ============================================
INSERT INTO rol (nombre, descripcion)
VALUES
('cliente', 'Usuario cliente de la aplicación'),
('admin', 'Administrador del sistema');

-- ============================================
-- MÉTODOS DE PAGO
-- ============================================
INSERT INTO metodo_pago (nombre, descripcion, activo)
VALUES
('Yape', 'Pago mediante Yape', 1),
('Efectivo', 'Pago en efectivo al recoger o recibir el pedido', 1);

-- ============================================
-- ESTADOS DE PEDIDO
-- ============================================
INSERT INTO estado_pedido (nombre, descripcion)
VALUES
('pendiente', 'Pedido registrado y pendiente de revisión'),
('confirmado', 'Pedido confirmado por el restaurante'),
('en preparación', 'El pedido se encuentra en preparación'),
('listo', 'El pedido está listo para ser entregado o recogido'),
('entregado', 'El pedido fue entregado o recogido por el cliente'),
('cancelado', 'El pedido fue cancelado');