-- Crear base de datos
CREATE DATABASE IF NOT EXISTS mobilestav;
USE mobilestav;

-- Crear tabla usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    email VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    nombre VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_admin BOOLEAN NOT NULL DEFAULT FALSE
);

-- Crear tabla libros
CREATE TABLE IF NOT EXISTS libros (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(255) NOT NULL,
    paginas INT NOT NULL,
    descripcion LONGTEXT,
    imagen_uri LONGTEXT,
    precio DECIMAL(10, 2) NOT NULL
);

-- Crear tabla carro
CREATE TABLE IF NOT EXISTS carro (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    libro_id BIGINT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    FOREIGN KEY (email) REFERENCES usuarios(email) ON DELETE CASCADE,
    FOREIGN KEY (libro_id) REFERENCES libros(id) ON DELETE CASCADE
);

-- Insertar usuario admin (contraseña: admin123)
-- Password encoded con BCrypt: $2a$10$8.UnVuG9HHgffUZ0jNjikusSVcSr6/ (admin123)
INSERT INTO usuarios (email, username, nombre, password, is_admin) VALUES 
('admin@mobilestav.com', 'admin', 'Administrador', '$2a$10$8.UnVuG9HHgffUZ0jNjikusSVcSr60FxVK8KT2l0b5.3AmP36DK/i', TRUE)
ON DUPLICATE KEY UPDATE is_admin = TRUE;

-- Insertar usuario de prueba
-- Password encoded con BCrypt: $2a$10$8.UnVuG9HHgffUZ0jNjikusSVcSr60 (user123)
INSERT INTO usuarios (email, username, nombre, password, is_admin) VALUES 
('usuario@example.com', 'usuario', 'Usuario de Prueba', '$2a$10$8.UnVuG9HHgffUZ0jNjikusSVcSr60FxVK8KT2l0b5.3AmP36DK/i', FALSE)
ON DUPLICATE KEY UPDATE is_admin = FALSE;

-- Insertar libros de ejemplo
INSERT INTO libros (titulo, autor, paginas, descripcion, precio) VALUES
('El Quijote', 'Miguel de Cervantes', 1072, 'La historia del ingenioso hidalgo Don Quijote', 15.99),
('1984', 'George Orwell', 328, 'Novela distópica sobre un régimen totalitario', 12.99),
('Cien años de soledad', 'Gabriel García Márquez', 496, 'Épica familiar en el pueblo ficticio de Macondo', 14.99);
