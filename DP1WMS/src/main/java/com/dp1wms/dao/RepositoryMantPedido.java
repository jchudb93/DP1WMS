package com.dp1wms.dao;

import com.dp1wms.model.Pedido;
import com.dp1wms.model.Proforma;

import java.sql.Timestamp;
import java.util.List;

public interface RepositoryMantPedido {

    boolean registrarPedido(Pedido pedido);

}