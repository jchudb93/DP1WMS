﻿CREATE EXTENSION PGCRYPTO;


--Estados de pedido
INSERT INTO estadopedido (descripcion) VALUES ('En espera');
INSERT INTO estadopedido (descripcion) VALUES ('Atendido');
INSERT INTO estadopedido (descripcion) VALUES ('Rechazado');
INSERT INTO estadopedido (descripcion) VALUES ('Cancelado por cliente');
INSERT INTO estadopedido (descripcion) VALUES ('Despachando envios');

--Secciones
--Administracion
INSERT INTO seccion (descripcion) VALUES ('Administración de Roles');
INSERT INTO seccion (descripcion) VALUES ('Administración de Usuarios');
INSERT INTO seccion (descripcion) VALUES ('Administración de Clientes');
INSERT INTO seccion (descripcion) VALUES ('Administración de Categorías');
INSERT INTO seccion (descripcion) VALUES ('Administración de Productos');
INSERT INTO seccion (descripcion) VALUES ('Administración de Ubicaciones');
--Almacenes
INSERT INTO seccion (descripcion) VALUES ('Creación y Modificación');
INSERT INTO seccion (descripcion) VALUES ('Transferencias Internas');
INSERT INTO seccion (descripcion) VALUES ('Movimientos - Ingreso/Salida Producto');
INSERT INTO seccion (descripcion) VALUES ('Movimientos - Crear Lote');
INSERT INTO seccion (descripcion) VALUES ('Despacho de Envios');
INSERT INTO seccion (descripcion) VALUES ('Generación de Rutas');

--Ventas
INSERT INTO seccion (descripcion) VALUES ('Admninistración de Condiciones Comerciales');
INSERT INTO seccion (descripcion) VALUES ('Generación de Proforma');
INSERT INTO seccion (descripcion) VALUES ('Consultas de Pedidos');
INSERT INTO seccion (descripcion) VALUES ('Generación de Pedidos');
INSERT INTO seccion (descripcion) VALUES ('Administración de Guia de Remisión');
INSERT INTO seccion (descripcion) VALUES ('Administración de Comprobantes de Pago');
INSERT INTO seccion (descripcion) VALUES ('Registrar Pedido Devolución');
INSERT INTO seccion (descripcion) VALUES ('Generación de Nota de Crétido');

--Reporte
INSERT INTO seccion (descripcion) VALUES ('Reporte Kardex');
INSERT INTO seccion (descripcion) VALUES ('Reporte de Almacén');
INSERT INTO seccion (descripcion) VALUES ('Reporte de Auditoria');

--Configuracion
INSERT INTO seccion (descripcion) VALUES ('Configuracíón del Sistema');

--Master User
--tipo empleado
INSERT INTO tipoempleado (descripcion) VALUES ('Master');
-- usuario
INSERT INTO usuario (nombreusuario, password) VALUES ('master', crypt('master', gen_salt('md5')));
-- empleado
INSERT INTO empleado (idusuario, nombre, apellidos, idtipoempleado, numdoc) VALUES (1, 'Master', '', 1, '314159');
--permisos
INSERT INTO tipoempleadoxseccion (idtipoempleado, idseccion, idempleadoauditado)
VALUES (1,1,1);
INSERT INTO tipoempleadoxseccion (idtipoempleado, idseccion, idempleadoauditado)
VALUES (1,2,1);
INSERT INTO tipoempleadoxseccion (idtipoempleado, idseccion, idempleadoauditado)
VALUES (1,3,1);
INSERT INTO tipoempleadoxseccion (idtipoempleado, idseccion, idempleadoauditado)
VALUES (1,4,1);
INSERT INTO tipoempleadoxseccion (idtipoempleado, idseccion, idempleadoauditado)
VALUES (1,5,1);
INSERT INTO tipoempleadoxseccion (idtipoempleado, idseccion, idempleadoauditado)
VALUES (1,6,1);
INSERT INTO tipoempleadoxseccion (idtipoempleado, idseccion, idempleadoauditado)
VALUES (1,7,1);
INSERT INTO tipoempleadoxseccion (idtipoempleado, idseccion, idempleadoauditado)
VALUES (1,8,1);
INSERT INTO tipoempleadoxseccion (idtipoempleado, idseccion, idempleadoauditado)
VALUES (1,9,1);
INSERT INTO tipoempleadoxseccion (idtipoempleado, idseccion, idempleadoauditado)
VALUES (1,10,1);
INSERT INTO tipoempleadoxseccion (idtipoempleado, idseccion, idempleadoauditado)
VALUES (1,11,1);
INSERT INTO tipoempleadoxseccion (idtipoempleado, idseccion, idempleadoauditado)
VALUES (1,12,1);
INSERT INTO tipoempleadoxseccion (idtipoempleado, idseccion, idempleadoauditado)
VALUES (1,13,1);
INSERT INTO tipoempleadoxseccion (idtipoempleado, idseccion, idempleadoauditado)
VALUES (1,14,1);
INSERT INTO tipoempleadoxseccion (idtipoempleado, idseccion, idempleadoauditado)
VALUES (1,15,1);
INSERT INTO tipoempleadoxseccion (idtipoempleado, idseccion, idempleadoauditado)
VALUES (1,16,1);
INSERT INTO tipoempleadoxseccion (idtipoempleado, idseccion, idempleadoauditado)
VALUES (1,17,1);
INSERT INTO tipoempleadoxseccion (idtipoempleado, idseccion, idempleadoauditado)
VALUES (1,18,1);
INSERT INTO tipoempleadoxseccion (idtipoempleado, idseccion, idempleadoauditado)
VALUES (1,19,1);
INSERT INTO tipoempleadoxseccion (idtipoempleado, idseccion, idempleadoauditado)
VALUES (1,20,1);
INSERT INTO tipoempleadoxseccion (idtipoempleado, idseccion, idempleadoauditado)
VALUES (1,21,1);
INSERT INTO tipoempleadoxseccion (idtipoempleado, idseccion, idempleadoauditado)
VALUES (1,22,1);
INSERT INTO tipoempleadoxseccion (idtipoempleado, idseccion, idempleadoauditado)
VALUES (1,23,1);
INSERT INTO tipoempleadoxseccion (idtipoempleado, idseccion, idempleadoauditado)
VALUES (1,24,1);

-- Tipo comprobante de pago
INSERT INTO TipoComprobante (descripcion) VALUES ('Boleta');
INSERT INTO TipoComprobante (descripcion) VALUES ('Factura');


-- Tipo de movimiento
INSERT INTO TipoMovimiento (descripcion, esingreso) VALUES ('Ingreso de Lote', true);
INSERT INTO TipoMovimiento (descripcion, esingreso) VALUES ('Despacho', false);
INSERT INTO TipoMovimiento (descripcion) VALUES ('Ubicacion de Lote');
INSERT INTO TipoMovimiento (descripcion) VALUES ('Reubicacion de Lote');
INSERT INTO TipoMovimiento (descripcion, esingreso) VALUES ('Robo', false);
INSERT INTO TipoMovimiento (descripcion, esingreso) VALUES ('Pérdida', false);
INSERT INTO TipoMovimiento (descripcion, esingreso) VALUES ('Ingreso', true);
INSERT INTO TipoMovimiento (descripcion, esingreso) VALUES ('Salida', false);
INSERT INTO TipoMovimiento (descripcion, esingreso) VALUES ('Incorporación de Producto', true);
INSERT INTO TipoMovimiento (descripcion, esingreso) VALUES ('Devolución', false);
