USE marucha_db;

INSERT INTO usuario (
    id_rol,
    username,
    nombre,
    apellido,
    email,
    password_hash,
    telefono,
    fecha_nacimiento,
    foto_perfil,
    auth_provider,
    google_id,
    estado
)
VALUES (
    2,
    'admin_marucha',
    'Admin',
    'Marucha',
    'admin@marucha.com',
    'admin123',
    '999999999',
    '2000-01-01',
    NULL,
    'local',
    NULL,
    1
);