USE marucha_db;

-- ============================================
-- FOREIGN KEYS
-- ============================================

-- usuario -> rol
ALTER TABLE usuario
ADD CONSTRAINT fk_usuario_rol
FOREIGN KEY (id_rol) REFERENCES rol(id_rol);

-- direccion_usuario -> usuario
ALTER TABLE direccion_usuario
ADD CONSTRAINT fk_direccion_usuario
FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario);

-- platillo -> categoria
ALTER TABLE platillo
ADD CONSTRAINT fk_platillo_categoria
FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria);

-- carrito -> usuario
ALTER TABLE carrito
ADD CONSTRAINT fk_carrito_usuario
FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario);

-- carrito_detalle -> carrito
ALTER TABLE carrito_detalle
ADD CONSTRAINT fk_carrito_detalle_carrito
FOREIGN KEY (id_carrito) REFERENCES carrito(id_carrito);

-- carrito_detalle -> platillo
ALTER TABLE carrito_detalle
ADD CONSTRAINT fk_carrito_detalle_platillo
FOREIGN KEY (id_platillo) REFERENCES platillo(id_platillo);

-- pedido -> usuario
ALTER TABLE pedido
ADD CONSTRAINT fk_pedido_usuario
FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario);

-- pedido -> direccion_usuario
ALTER TABLE pedido
ADD CONSTRAINT fk_pedido_direccion
FOREIGN KEY (id_direccion) REFERENCES direccion_usuario(id_direccion);

-- pedido -> estado_pedido
ALTER TABLE pedido
ADD CONSTRAINT fk_pedido_estado
FOREIGN KEY (id_estado_pedido) REFERENCES estado_pedido(id_estado_pedido);

-- pedido -> metodo_pago
ALTER TABLE pedido
ADD CONSTRAINT fk_pedido_metodo_pago
FOREIGN KEY (id_metodo_pago) REFERENCES metodo_pago(id_metodo_pago);

-- detalle_pedido -> pedido
ALTER TABLE detalle_pedido
ADD CONSTRAINT fk_detalle_pedido_pedido
FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido);

-- detalle_pedido -> platillo
ALTER TABLE detalle_pedido
ADD CONSTRAINT fk_detalle_pedido_platillo
FOREIGN KEY (id_platillo) REFERENCES platillo(id_platillo);

-- pedido_historial_estado -> pedido
ALTER TABLE pedido_historial_estado
ADD CONSTRAINT fk_historial_pedido
FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido);

-- pedido_historial_estado -> estado_pedido
ALTER TABLE pedido_historial_estado
ADD CONSTRAINT fk_historial_estado
FOREIGN KEY (id_estado_pedido) REFERENCES estado_pedido(id_estado_pedido);

-- ============================================
-- CHECKS LÓGICOS
-- ============================================

ALTER TABLE carrito_detalle
ADD CONSTRAINT chk_carrito_detalle_cantidad
CHECK (cantidad > 0);

ALTER TABLE carrito_detalle
ADD CONSTRAINT chk_carrito_detalle_precio
CHECK (precio_unitario >= 0);

ALTER TABLE carrito_detalle
ADD CONSTRAINT chk_carrito_detalle_subtotal
CHECK (subtotal >= 0);

ALTER TABLE platillo
ADD CONSTRAINT chk_platillo_precio
CHECK (precio >= 0);

ALTER TABLE pedido
ADD CONSTRAINT chk_pedido_subtotal
CHECK (subtotal >= 0);

ALTER TABLE pedido
ADD CONSTRAINT chk_pedido_total
CHECK (total >= 0);

ALTER TABLE detalle_pedido
ADD CONSTRAINT chk_detalle_pedido_cantidad
CHECK (cantidad > 0);

ALTER TABLE detalle_pedido
ADD CONSTRAINT chk_detalle_pedido_precio
CHECK (precio_unitario >= 0);

ALTER TABLE detalle_pedido
ADD CONSTRAINT chk_detalle_pedido_subtotal
CHECK (subtotal >= 0);

-- ============================================
-- ÍNDICES RECOMENDADOS
-- ============================================

CREATE INDEX idx_usuario_rol ON usuario(id_rol);
CREATE INDEX idx_direccion_usuario_usuario ON direccion_usuario(id_usuario);
CREATE INDEX idx_platillo_categoria ON platillo(id_categoria);
CREATE INDEX idx_carrito_usuario ON carrito(id_usuario);
CREATE INDEX idx_carrito_detalle_carrito ON carrito_detalle(id_carrito);
CREATE INDEX idx_carrito_detalle_platillo ON carrito_detalle(id_platillo);
CREATE INDEX idx_pedido_usuario ON pedido(id_usuario);
CREATE INDEX idx_pedido_direccion ON pedido(id_direccion);
CREATE INDEX idx_pedido_estado ON pedido(id_estado_pedido);
CREATE INDEX idx_pedido_metodo_pago ON pedido(id_metodo_pago);
CREATE INDEX idx_detalle_pedido_pedido ON detalle_pedido(id_pedido);
CREATE INDEX idx_detalle_pedido_platillo ON detalle_pedido(id_platillo);
CREATE INDEX idx_historial_pedido ON pedido_historial_estado(id_pedido);
CREATE INDEX idx_historial_estado ON pedido_historial_estado(id_estado_pedido);