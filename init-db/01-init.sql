-- Crear la base de datos si no existe
CREATE DATABASE inventario_db WITH OWNER postgres;

-- Conectarse a la base de datos
\c inventario_db;

-- Asegurar que el esquema público tenga los permisos correctos
ALTER SCHEMA public OWNER TO postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public;