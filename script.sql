-- ===================================================================
-- Tarea 4 - Programación I
-- Base de datos: tarea4_db
-- Motor: MySQL 8
-- ===================================================================

CREATE DATABASE IF NOT EXISTS tarea4_db
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE tarea4_db;

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    idUser INT AUTO_INCREMENT PRIMARY KEY,
    UserName VARCHAR(140) NOT NULL UNIQUE,
    Nombre VARCHAR(140) NOT NULL,
    Apellido VARCHAR(140) NOT NULL,
    Telefono VARCHAR(140) NOT NULL,
    Email VARCHAR(140) NOT NULL UNIQUE,
    Password VARCHAR(140) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL DEFAULT NULL
) ENGINE=InnoDB;

-- Password almacena el hash en formato salt:hash.
-- deleted_at se usa para el borrado lógico.
