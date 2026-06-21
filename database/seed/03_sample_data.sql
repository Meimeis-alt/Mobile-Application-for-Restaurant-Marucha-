USE marucha_db;

-- ============================================
-- CATEGORÍAS DE EJEMPLO
-- ============================================
INSERT INTO categoria (nombre, descripcion, activo)
VALUES
('Entradas', 'Platillos de entrada', 1),
('Segundos', 'Platos principales o de fondo', 1),
('Bebidas', 'Bebidas frías y calientes', 1),
('Postres', 'Postres y dulces', 1);

-- ============================================
-- PLATILLOS DE EJEMPLO
-- ============================================
INSERT INTO platillo (
    id_categoria,
    nombre,
    descripcion,
    precio,
    imagen_url,
    disponible
)
VALUES
(1, 'Tequeños', 'Tequeños rellenos de queso acompañados de salsa.', 12.00, NULL, 1),
(1, 'Papa a la Huancaína', 'Papas bañadas en crema huancaína con huevo y aceituna.', 14.00, NULL, 1),

(2, 'Lomo Saltado', 'Lomo saltado con papas fritas y arroz.', 22.00, NULL, 1),
(2, 'Arroz con Pollo', 'Arroz verde acompañado de presa de pollo y salsa criolla.', 18.00, NULL, 1),
(2, 'Ají de Gallina', 'Ají de gallina acompañado de arroz y papa sancochada.', 19.50, NULL, 1),

(3, 'Chicha Morada', 'Bebida tradicional peruana.', 6.00, NULL, 1),
(3, 'Limonada', 'Limonada fresca natural.', 5.50, NULL, 1),
(3, 'Inca Kola Personal', 'Gaseosa Inca Kola personal.', 4.50, NULL, 1),

(4, 'Torta de Chocolate', 'Porción de torta de chocolate.', 9.00, NULL, 1),
(4, 'Suspiro a la Limeña', 'Postre tradicional peruano.', 8.50, NULL, 1);