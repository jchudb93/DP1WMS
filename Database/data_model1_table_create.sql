CREATE TABLE public.rack (
	idrack serial NOT NULL,
	idarea integer NOT NULL,
	poscioninicial point NOT NULL,
	posicionfinal point NOT NULL,
	altura integer NOT NULL,
	longitudcajon integer NOT NULL,
	codigo varchar(255) NOT NULL,
	idempleadoauditado integer,
	PRIMARY KEY (idrack)
);

CREATE INDEX ON public.rack
	(idarea);


CREATE TABLE public.ubicacion (
	idlote integer NOT NULL,
	idproducto integer NOT NULL,
	idcajon integer NOT NULL,
	cantidad integer NOT NULL,
	PRIMARY KEY (idlote, idproducto, idcajon)
);


CREATE TABLE public.producto (
	idproducto serial NOT NULL,
	nombreproducto varchar(255) NOT NULL,
	peso real NOT NULL,
	fechavencimiento timestamp without time zone,
	descripcion varchar(255),
	precio real NOT NULL,
	stock integer NOT NULL DEFAULT 0,
	idcategoria integer NOT NULL,
	codigo varchar(255) NOT NULL,
	fechacreacion timestamp without time zone NOT NULL default now(),
	activo boolean NOT NULL,
	idempleadoauditado integer,
	PRIMARY KEY (idproducto)
);

ALTER TABLE public.producto
	ADD UNIQUE (codigo);

CREATE INDEX ON public.producto
	(idcategoria);


CREATE TABLE public.movimiento (
	idmovimiento serial NOT NULL,
	totalproductos integer NOT NULL,
	observaciones varchar(255),
	fechamovimiento timestamp without time zone NOT NULL,
	idtipomovimiento integer NOT NULL,
	fechacreacion timestamp without time zone NOT NULL default now(),
	idempleadoauditado integer,
	PRIMARY KEY (idmovimiento)
);

CREATE INDEX ON public.movimiento
	(idtipomovimiento);


CREATE TABLE public.lote (
	idlote serial NOT NULL,
	idproducto integer NOT NULL,
	fechalote timestamp without time zone NOT NULL,
	fechaentrada timestamp without time zone NOT NULL,
	stockparical integer NOT NULL,
	fechacreacion timestamp without time zone NOT NULL default now(),
	idempleadoauditado integer,
	PRIMARY KEY (idlote, idproducto)
);


CREATE TABLE public.tipomovimiento (
	idtipomovimiento serial NOT NULL,
	descripcion varchar(255) NOT NULL,
	esingreso boolean NOT NULL,
	PRIMARY KEY (idtipomovimiento)
);


CREATE TABLE public.motivotraslado (
	idmotivotraslado serial NOT NULL,
	descripcion varchar(255) NOT NULL,
	PRIMARY KEY (idmotivotraslado)
);


CREATE TABLE public.empleado (
	idempleado serial NOT NULL,
	idusuario integer NOT NULL,
	numdoc varchar(255) NOT NULL,
	nombre varchar(255) NOT NULL,
	apellidos varchar(255) NOT NULL,
	email varchar(255),
	idtipoempleado integer NOT NULL,
	fechacreacion timestamp without time zone NOT NULL default now(),
	activo boolean NOT NULL,
	idempleadoauditado integer,
	PRIMARY KEY (idempleado, idusuario)
);

ALTER TABLE public.empleado
	ADD UNIQUE (numdoc);

CREATE INDEX ON public.empleado
	(idtipoempleado);


CREATE TABLE public.tipoempleado (
	idtipoempleado serial NOT NULL,
	descripcion varchar(255) NOT NULL,
	activo boolean NOT NULL,
	idempleadoauditado integer,
	PRIMARY KEY (idtipoempleado)
);


CREATE TABLE public.usuario (
	idusuario serial NOT NULL,
	nombreusuario varchar(255) NOT NULL,
	password varchar(255) NOT NULL,
	fechacreacion timestamp without time zone NOT NULL default now(),
	PRIMARY KEY (idusuario)
);

ALTER TABLE public.usuario
	ADD UNIQUE (nombreusuario);


CREATE TABLE public.comprobantepago (
	idcomprobante serial NOT NULL,
	idtipocomprobante integer NOT NULL,
	idcliente integer NOT NULL,
	totalsinigv real NOT NULL,
	igv real NOT NULL,
	total real NOT NULL,
	idestadocomprobante integer NOT NULL,
	fechacreacion timestamp without time zone NOT NULL default now(),
	activo boolean NOT NULL,
	fechamodificacion timestamp without time zone NOT NULL,
	idempleadoauditado integer,
	PRIMARY KEY (idcomprobante)
);

CREATE INDEX ON public.comprobantepago
	(idtipocomprobante);
CREATE INDEX ON public.comprobantepago
	(idcliente);
CREATE INDEX ON public.comprobantepago
	(idestadocomprobante);
CREATE INDEX ON public.comprobantepago
	(idempleadoauditado);


CREATE TABLE public.condicion (
	condicion serial NOT NULL,
	tipodescuento varchar(255) NOT NULL,
	idproductogenerador integer,
	idcategoriaprodgen integer,
	cantprodgen integer NOT NULL,
	idproductodescuento integer,
	idcategoriaproddesc integer,
	cantproddesc integer NOT NULL,
	valordescuento real NOT NULL,
	fechainicio timestamp without time zone NOT NULL,
	fechafin timestamp without time zone NOT NULL,
	descripcion varchar(255) NOT NULL,
	idempleadoauditado integer,
	prioridad integer NOT NULL,
	PRIMARY KEY (idcondicion)
);


CREATE TABLE public.cliente (
	idcliente serial NOT NULL,
	numdoc varchar(255) NOT NULL,
	razonsocial varchar(255) NOT NULL,
	telefono varchar(255) NOT NULL,
	direccion varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	fechacreacion timestamp without time zone NOT NULL default now(),
	idempleadoauditado integer,
	PRIMARY KEY (idcliente)
);

ALTER TABLE public.cliente
	ADD UNIQUE (numdoc);


CREATE TABLE public.guiaremision (
	idguiaremision serial NOT NULL,
	idmotivotraslado integer NOT NULL,
	fechaemision timestamp without time zone NOT NULL,
	fechavencimiento timestamp without time zone NOT NULL,
	fechapartida timestamp without time zone NOT NULL,
	observaciones varchar(255),
	puntopartida varchar(255) NOT NULL,
	puntollegada varchar(255) NOT NULL,
	idtransportista integer NOT NULL,
	numeroplaca varchar(255) NOT NULL,
	pesototal real NOT NULL,
	fechacreacion timestamp without time zone NOT NULL default now(),
	idenvio integer NOT NULL,
	idempleadoauditado integer,
	PRIMARY KEY (idguiaremision)
);

CREATE INDEX ON public.guiaremision
	(idmotivotraslado);
CREATE INDEX ON public.guiaremision
	(idtransportista);
CREATE INDEX ON public.guiaremision
	(idenvio);


CREATE TABLE public.estadocomprobante (
	idestadocomprobante serial NOT NULL,
	descripcion varchar(255) NOT NULL,
	PRIMARY KEY (idestadocomprobante)
);


CREATE TABLE public.detallecomprobante (
	iddetallecomprobante serial NOT NULL,
	idcomprobante integer NOT NULL,
	idproducto integer NOT NULL,
	cantidad integer NOT NULL,
	subtotal  NOT NULL,
	PRIMARY KEY (iddetallecomprobante, idcomprobante, idproducto)
);


CREATE TABLE public.detalleguia (
	iddetalleguia serial NOT NULL,
	idguiaremision integer NOT NULL,
	idproducto integer NOT NULL,
	cantidad integer NOT NULL,
	observaciones varchar(255),
	idestadodetalleremision integer NOT NULL,
	peso real NOT NULL,
	PRIMARY KEY (iddetalleguia)
);

CREATE INDEX ON public.detalleguia
	(idguiaremision);
CREATE INDEX ON public.detalleguia
	(idproducto);


CREATE TABLE public.detallemovimiento (
	iddetallemovimiento serial NOT NULL,
	idmovimiento integer NOT NULL,
	idproducto integer NOT NULL,
	idlote integer NOT NULL,
	cantidad integer NOT NULL,
	idcajon integer,
	PRIMARY KEY (iddetallemovimiento)
);

CREATE INDEX ON public.detallemovimiento
	(idmovimiento);
CREATE INDEX ON public.detallemovimiento
	(idproducto);
CREATE INDEX ON public.detallemovimiento
	(idlote);


CREATE TABLE public.tipocliente (
	idtipocliente serial NOT NULL,
	descripcion varchar(255) NOT NULL,
	PRIMARY KEY (idtipocliente)
);


CREATE TABLE public.tipocomprobante (
	idtipocomprobante serial NOT NULL,
	descripcion varchar(255) NOT NULL,
	PRIMARY KEY (idtipocomprobante)
);


CREATE TABLE public.estadodetalleremision (
	idestadodetalleremision serial NOT NULL,
	descripcion varchar(255) NOT NULL,
	PRIMARY KEY (idestadodetalleremision)
);


CREATE TABLE public.proforma (
	idproforma serial NOT NULL,
	idempleado integer NOT NULL,
	idcliente integer NOT NULL,
	total real NOT NULL,
	observaciones varchar(255),
	fechacreacion timestamp without time zone NOT NULL default now(),
	idempleadoauditado integer,
	PRIMARY KEY (idproforma)
);

CREATE INDEX ON public.proforma
	(idempleado);
CREATE INDEX ON public.proforma
	(idcliente);


CREATE TABLE public.detalleproforma (
	iddetalleproforma serial NOT NULL,
	idproforma integer NOT NULL,
	idproducto integer NOT NULL,
	cantidad integer NOT NULL,
	preciounitario real NOT NULL,
	descuento real NOT NULL,
	subtotal real NOT NULL,
	PRIMARY KEY (iddetalleproforma)
);

CREATE INDEX ON public.detalleproforma
	(idproforma);
CREATE INDEX ON public.detalleproforma
	(idproducto);


CREATE TABLE public.ubicacion (
	dasdfasdfasdfasdfas  NOT NULL
);


CREATE TABLE public.cajon (
	idcajon serial NOT NULL,
	idrack integer NOT NULL,
	posicion point NOT NULL,
	PRIMARY KEY (idcajon)
);

CREATE INDEX ON public.cajon
	(idrack);


CREATE TABLE public.envio (
	idenvio serial NOT NULL,
	fechaenvio timestamp without time zone NOT NULL,
	destino varchar(255) NOT NULL,
	costoflete real NOT NULL,
	idpedido integer NOT NULL,
	realizado boolean NOT NULL,
	PRIMARY KEY (idenvio)
);

CREATE INDEX ON public.envio
	(idpedido);


CREATE TABLE public.detalleenvio (
	iddetalleenvio serial NOT NULL,
	idenvio integer NOT NULL,
	idproducto integer NOT NULL,
	cantidad integer NOT NULL,
	PRIMARY KEY (iddetalleenvio)
);

CREATE INDEX ON public.detalleenvio
	(idenvio);
CREATE INDEX ON public.detalleenvio
	(idproducto);


CREATE TABLE public.categoriaproducto (
	idcategoria serial NOT NULL,
	descripcion varchar(255) NOT NULL,
	PRIMARY KEY (idcategoria)
);


CREATE TABLE public.pedido (
	idpedido serial NOT NULL,
	fechaenvio timestamp without time zone NOT NULL,
	destino varchar(255) NOT NULL,
	idestadopedido integer NOT NULL,
	esdevolucion boolean NOT NULL,
	fechacreacion timestamp without time zone NOT NULL default now(),
	observaciones varchar(255),
	idempleadoauditado integer,
	PRIMARY KEY (idpedido)
);

CREATE INDEX ON public.pedido
	(idestadopedido);


CREATE TABLE public.detallepedido (
	iddetallepedido serial NOT NULL,
	idproducto integer NOT NULL,
	idpedido integer NOT NULL,
	cantidad integer NOT NULL,
	preciounitario integer NOT NULL,
	descuento real NOT NULL,
	subtotal real NOT NULL,
	PRIMARY KEY (iddetallepedido)
);

CREATE INDEX ON public.detallepedido
	(idproducto);
CREATE INDEX ON public.detallepedido
	(idpedido);


CREATE TABLE public.estadopedido (
	idestadopedido serial NOT NULL,
	descripcion varchar(255) NOT NULL,
	PRIMARY KEY (idestadopedido)
);


CREATE TABLE public.almacen (
	idalmacen serial NOT NULL,
	direccion varchar(255),
	largo integer NOT NULL,
	ancho integer NOT NULL,
	idempleadoauditado integer,
	PRIMARY KEY (idalmacen)
);


CREATE TABLE public.area (
	idarea serial NOT NULL,
	idalmacen integer NOT NULL,
	posicioninicial point NOT NULL,
	posicionfinal point NOT NULL,
	codigo varchar(10) NOT NULL,
	idempleadoauditado integer,
	PRIMARY KEY (idarea)
);

CREATE INDEX ON public.area
	(idalmacen);


CREATE TABLE public.seccion (
	idseccion serial NOT NULL,
	descripcion varchar(255) NOT NULL,
	PRIMARY KEY (idseccion)
);


CREATE TABLE public.tipoempleadoxseccion (
	idtipoempleado integer NOT NULL,
	idseccion integer NOT NULL,
	idempleadoauditado integer,
	PRIMARY KEY (idtipoempleado, idseccion)
);


CREATE TABLE public.ruta (
	idruta serial NOT NULL,
	idmovimiento integer NOT NULL,
	camino JSON NOT NULL,
	idempleadoauditado integer,
	PRIMARY KEY (idruta)
);

CREATE INDEX ON public.ruta
	(idmovimiento);


CREATE TABLE public.entity1 (
);


CREATE TABLE public.movimientoxenvio (
	idmovimiento integer NOT NULL,
	idenvio integer NOT NULL,
	PRIMARY KEY (idmovimiento, idenvio)
);


CREATE TABLE public.notacredito (
	idnotadecredito serial NOT NULL,
	idcliente integer NOT NULL,
	idcomprobante integer NOT NULL,
	idempleadoauditado integer,
	PRIMARY KEY (idnotadecredito)
);

CREATE INDEX ON public.notacredito
	(idcliente);
CREATE INDEX ON public.notacredito
	(idcomprobante);


CREATE TABLE public.detallenotacredito (
	iddetallenotacredito integer NOT NULL
);


CREATE TABLE public.auditoria (
	idauditoria serial NOT NULL,
	idempleado integer NOT NULL,
	tabla varchar(255) NOT NULL,
	accion varchar(255) NOT NULL,
	fechacreacion timestamp without time zone NOT NULL default now(),
	idpk integer NOT NULL,
	PRIMARY KEY (idauditoria)
);


create table auditoria (
	idauditoria serial not null, 
	tabla text not null, idempleado integer not null, 
	fecha timestamp without time zone not null default now(), 
	accion text not null check (accion in ('I', 'D', 'U')), 
	dataoriginal text, 
	datanueva text, 
	query text) 
with (fillfactor=100);

CREATE INDEX ON public.auditoria (idempleado);

CREATE INDEX ON public.auditoria (fecha);

CREATE INDEX ON public.auditoria (accion);


ALTER TABLE public.rack ADD CONSTRAINT FK_rack__idarea FOREIGN KEY (idarea) REFERENCES public.area(idarea);
ALTER TABLE public.ubicacion ADD CONSTRAINT FK_ubicacion__idlote FOREIGN KEY (idlote) REFERENCES public.lote(idlote);
ALTER TABLE public.ubicacion ADD CONSTRAINT FK_ubicacion__idproducto FOREIGN KEY (idproducto) REFERENCES public.lote(idproducto);
ALTER TABLE public.ubicacion ADD CONSTRAINT FK_ubicacion__idcajon FOREIGN KEY (idcajon) REFERENCES public.cajon(idcajon);
ALTER TABLE public.producto ADD CONSTRAINT FK_producto__idcategoria FOREIGN KEY (idcategoria) REFERENCES public.categoriaproducto(idcategoria);
ALTER TABLE public.movimiento ADD CONSTRAINT FK_movimiento__idtipomovimiento FOREIGN KEY (idtipomovimiento) REFERENCES public.tipomovimiento(idtipomovimiento);
ALTER TABLE public.lote ADD CONSTRAINT FK_lote__idproducto FOREIGN KEY (idproducto) REFERENCES public.producto(idproducto);
ALTER TABLE public.empleado ADD CONSTRAINT FK_empleado__idusuario FOREIGN KEY (idusuario) REFERENCES public.usuario(idusuario);
ALTER TABLE public.empleado ADD CONSTRAINT FK_empleado__idtipoempleado FOREIGN KEY (idtipoempleado) REFERENCES public.tipoempleado(idtipoempleado);
ALTER TABLE public.comprobantepago ADD CONSTRAINT FK_comprobantepago__idtipocomprobante FOREIGN KEY (idtipocomprobante) REFERENCES public.tipocomprobante(idtipocomprobante);
ALTER TABLE public.comprobantepago ADD CONSTRAINT FK_comprobantepago__idcliente FOREIGN KEY (idcliente) REFERENCES public.cliente(idcliente);
ALTER TABLE public.comprobantepago ADD CONSTRAINT FK_comprobantepago__idestadocomprobante FOREIGN KEY (idestadocomprobante) REFERENCES public.estadocomprobante(idestadocomprobante);
ALTER TABLE public.comprobantepago ADD CONSTRAINT FK_comprobantepago__idempleadoauditado FOREIGN KEY (idempleadoauditado) REFERENCES public.empleado(idempleado);
ALTER TABLE public.guiaremision ADD CONSTRAINT FK_guiaremision__idmotivotraslado FOREIGN KEY (idmotivotraslado) REFERENCES public.motivotraslado(idmotivotraslado);
ALTER TABLE public.guiaremision ADD CONSTRAINT FK_guiaremision__idtransportista FOREIGN KEY (idtransportista) REFERENCES public.empleado(idempleado);
ALTER TABLE public.guiaremision ADD CONSTRAINT FK_guiaremision__idenvio FOREIGN KEY (idenvio) REFERENCES public.envio(idenvio);
ALTER TABLE public.detalleguia ADD CONSTRAINT FK_detalleguia__idguiaremision FOREIGN KEY (idguiaremision) REFERENCES public.guiaremision(idguiaremision);
ALTER TABLE public.detalleguia ADD CONSTRAINT FK_detalleguia__idproducto FOREIGN KEY (idproducto) REFERENCES public.producto(idproducto);
ALTER TABLE public.detalleguia ADD CONSTRAINT FK_detalleguia__idestadodetalleremision FOREIGN KEY (idestadodetalleremision) REFERENCES public.estadodetalleremision(idestadodetalleremision);
ALTER TABLE public.detallemovimiento ADD CONSTRAINT FK_detallemovimiento__idmovimiento FOREIGN KEY (idmovimiento) REFERENCES public.movimiento(idmovimiento);
ALTER TABLE public.detallemovimiento ADD CONSTRAINT FK_detallemovimiento__idproducto FOREIGN KEY (idproducto) REFERENCES public.lote(idproducto);
ALTER TABLE public.detallemovimiento ADD CONSTRAINT FK_detallemovimiento__idlote FOREIGN KEY (idlote) REFERENCES public.lote(idlote);
ALTER TABLE public.proforma ADD CONSTRAINT FK_proforma__idempleado FOREIGN KEY (idempleado) REFERENCES public.empleado(idempleado);
ALTER TABLE public.proforma ADD CONSTRAINT FK_proforma__idcliente FOREIGN KEY (idcliente) REFERENCES public.cliente(idcliente);
ALTER TABLE public.detalleproforma ADD CONSTRAINT FK_detalleproforma__idproforma FOREIGN KEY (idproforma) REFERENCES public.proforma(idproforma);
ALTER TABLE public.detalleproforma ADD CONSTRAINT FK_detalleproforma__idproducto FOREIGN KEY (idproducto) REFERENCES public.producto(idproducto);
ALTER TABLE public.cajon ADD CONSTRAINT FK_cajon__idrack FOREIGN KEY (idrack) REFERENCES public.rack(idrack);
ALTER TABLE public.envio ADD CONSTRAINT FK_envio__idpedido FOREIGN KEY (idpedido) REFERENCES public.pedido(idpedido);
ALTER TABLE public.detalleenvio ADD CONSTRAINT FK_detalleenvio__idenvio FOREIGN KEY (idenvio) REFERENCES public.envio(idenvio);
ALTER TABLE public.detalleenvio ADD CONSTRAINT FK_detalleenvio__idproducto FOREIGN KEY (idproducto) REFERENCES public.producto(idproducto);
ALTER TABLE public.pedido ADD CONSTRAINT FK_pedido__idestadopedido FOREIGN KEY (idestadopedido) REFERENCES public.estadopedido(idestadopedido);
ALTER TABLE public.detallepedido ADD CONSTRAINT FK_detallepedido__idproducto FOREIGN KEY (idproducto) REFERENCES public.producto(idproducto);
ALTER TABLE public.detallepedido ADD CONSTRAINT FK_detallepedido__idpedido FOREIGN KEY (idpedido) REFERENCES public.pedido(idpedido);
ALTER TABLE public.area ADD CONSTRAINT FK_area__idalmacen FOREIGN KEY (idalmacen) REFERENCES public.almacen(idalmacen);
ALTER TABLE public.tipoempleadoxseccion ADD CONSTRAINT FK_tipoempleadoxseccion__idtipoempleado FOREIGN KEY (idtipoempleado) REFERENCES public.tipoempleado(idtipoempleado);
ALTER TABLE public.tipoempleadoxseccion ADD CONSTRAINT FK_tipoempleadoxseccion__idseccion FOREIGN KEY (idseccion) REFERENCES public.seccion(idseccion);
ALTER TABLE public.ruta ADD CONSTRAINT FK_ruta__idmovimiento FOREIGN KEY (idmovimiento) REFERENCES public.movimiento(idmovimiento);
ALTER TABLE public.movimientoxenvio ADD CONSTRAINT FK_movimientoxenvio__idmovimiento FOREIGN KEY (idmovimiento) REFERENCES public.movimiento(idmovimiento);
ALTER TABLE public.movimientoxenvio ADD CONSTRAINT FK_movimientoxenvio__idenvio FOREIGN KEY (idenvio) REFERENCES public.envio(idenvio);
ALTER TABLE public.notacredito ADD CONSTRAINT FK_notacredito__idcliente FOREIGN KEY (idcliente) REFERENCES public.cliente(idcliente);
ALTER TABLE public.notacredito ADD CONSTRAINT FK_notacredito__idcomprobante FOREIGN KEY (idcomprobante) REFERENCES public.comprobantepago(idcomprobante);