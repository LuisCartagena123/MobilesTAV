-- Insertar usuario admin (contraseña: admin123)
-- Password BCrypt hash para "admin123"
INSERT INTO usuarios (email, username, nombre, password, is_admin) 
VALUES ('admin@mobilestav.com', 'admin', 'Administrador', '$2a$10$8.UnVuG9HHgffUZ0jNjikusSVcSr60FxVK8KT2l0b5.3AmP36DK/i', TRUE)
ON DUPLICATE KEY UPDATE is_admin = TRUE;

-- Insertar usuario de prueba (contraseña: user123)
INSERT INTO usuarios (email, username, nombre, password, is_admin) 
VALUES ('usuario@example.com', 'usuario', 'Usuario de Prueba', '$2a$10$8.UnVuG9HHgffUZ0jNjikusSVcSr60FxVK8KT2l0b5.3AmP36DK/i', FALSE)
ON DUPLICATE KEY UPDATE is_admin = FALSE;


