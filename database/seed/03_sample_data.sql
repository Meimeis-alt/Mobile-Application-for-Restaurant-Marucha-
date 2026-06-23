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
-- =========================
-- ENTRADAS (id_categoria = 1)
-- =========================
(1, 'Tequeños', 'Tequeños rellenos de queso acompañados de salsa.', 12.00, NULL, 1),
(1, 'Papa a la Huancaína', 'Papas bañadas en crema huancaína con huevo y aceituna.', 14.00, NULL, 1),
(1, 'Causa Limeña', 'Causa rellena de pollo, palta y mayonesa.', 15.50, NULL, 1),
(1, 'Ocopa Arequipeña', 'Papas con salsa de ocopa, huevo y aceituna.', 13.50, NULL, 1),
(1, 'Anticuchos', 'Brochetas de corazón acompañadas de papa y choclo.', 16.00, NULL, 1),
(1, 'Yucas Fritas', 'Porción de yucas fritas con salsa de la casa.', 10.00, NULL, 1),
(1, 'Tamal Criollo', 'Tamal de maíz servido con salsa criolla.', 9.50, NULL, 1),
(1, 'Ensalada Fresca', 'Mix de lechuga, tomate, pepino y zanahoria.', 11.00, NULL, 1),

-- =========================
-- SEGUNDOS (id_categoria = 2)
-- =========================
(2, 'Lomo Saltado', 'Lomo saltado con papas fritas y arroz.', 22.00, NULL, 1),
(2, 'Arroz con Pollo', 'Arroz verde acompañado de presa de pollo y salsa criolla.', 18.00, NULL, 1),
(2, 'Ají de Gallina', 'Ají de gallina acompañado de arroz y papa sancochada.', 19.50, NULL, 1),
(2, 'Arroz Chaufa de Pollo', 'Arroz chaufa salteado con pollo, huevo y cebollita china.', 17.50, NULL, 1),
(2, 'Tallarín Saltado de Carne', 'Tallarines salteados con carne, verduras y sillao.', 20.00, NULL, 1),
(2, 'Milanesa con Papas', 'Milanesa de pollo con papas fritas y ensalada.', 18.50, NULL, 1),
(2, 'Pollo a la Plancha', 'Filete de pollo a la plancha con arroz y ensalada.', 17.00, NULL, 1),
(2, 'Seco de Res', 'Seco de res acompañado de arroz y frejoles.', 21.00, NULL, 1),
(2, 'Tacu Tacu con Lomo', 'Tacu tacu servido con lomo saltado encima.', 24.00, NULL, 1),
(2, 'Chaufa Amazónico', 'Arroz chaufa estilo amazónico con cecina y plátano.', 23.00, NULL, 1),
(2, 'Mostrito', 'Arroz chaufa con presa de pollo broaster y papas fritas.', 21.50, NULL, 1),
(2, 'Pechuga a la Parrilla', 'Pechuga a la parrilla con guarnición del día.', 19.00, NULL, 1),
(2, 'Arroz con Mariscos', 'Arroz sazonado con mariscos y toque criollo.', 25.00, NULL, 1),
(2, 'Chicharrón de Pollo', 'Trozos de pollo crocante con papas fritas y cremas.', 18.00, NULL, 1),
(2, 'Pollo Broaster', 'Presa de pollo broaster con papas y ensalada.', 17.50, NULL, 1),

-- =========================
-- BEBIDAS (id_categoria = 3)
-- =========================
(3, 'Chicha Morada', 'Bebida tradicional peruana.', 6.00, NULL, 1),
(3, 'Limonada', 'Limonada fresca natural.', 5.50, NULL, 1),
(3, 'Inca Kola Personal', 'Gaseosa Inca Kola personal.', 4.50, NULL, 1),
(3, 'Coca Cola Personal', 'Gaseosa Coca Cola personal.', 4.50, NULL, 1),
(3, 'Maracuyá Frozen', 'Bebida helada de maracuyá.', 7.50, NULL, 1),
(3, 'Jugo de Fresa', 'Jugo natural de fresa.', 8.00, NULL, 1),
(3, 'Jugo de Papaya', 'Jugo natural de papaya.', 8.00, NULL, 1),
(3, 'Café Americano', 'Café americano caliente.', 6.50, NULL, 1),
(3, 'Capuccino', 'Café capuccino espumoso.', 8.50, NULL, 1),
(3, 'Agua Mineral', 'Botella de agua mineral personal.', 3.50, NULL, 1),

-- =========================
-- POSTRES (id_categoria = 4)
-- =========================
(4, 'Torta de Chocolate', 'Porción de torta de chocolate.', 9.00, NULL, 1),
(4, 'Suspiro a la Limeña', 'Postre tradicional peruano.', 8.50, NULL, 1),
(4, 'Cheesecake de Fresa', 'Cheesecake cremoso con salsa de fresa.', 10.00, NULL, 1),
(4, 'Arroz con Leche', 'Postre casero de arroz con leche.', 7.00, NULL, 1),
(4, 'Mazamorra Morada', 'Mazamorra morada tradicional.', 7.00, NULL, 1),
(4, 'Pie de Limón', 'Porción de pie de limón.', 9.50, NULL, 1),
(4, 'Helado de Vainilla', 'Copa de helado de vainilla con toppings.', 8.00, NULL, 1),
(4, 'Brownie con Helado', 'Brownie tibio acompañado de helado.', 11.00, NULL, 1);