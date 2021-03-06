package com.dp1wms.dao.impl;

import com.dp1wms.controller.MainController;
import com.dp1wms.dao.RepositoryProforma;
import com.dp1wms.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RepositoryProformaImpl implements RepositoryProforma {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MainController mainController;



    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean registrarProforma(Proforma proforma){
        String sqlProforma = "INSERT INTO proforma (idempleado, idcliente, observaciones, total, idempleadoauditado)" +
                " VALUES (?, ?, ?, ?, ?) RETURNING idproforma";
        try{
            int idProforma = jdbcTemplate.queryForObject(sqlProforma, new Object[]{
                    proforma.getIdEmpleado(),
                    proforma.getCliente().getIdCliente(),
                    proforma.getObservaciones(),
                    proforma.getTotal(),
                    mainController.getEmpleado().getIdempleado()}, (rs, i)->{
                return new Integer(rs.getInt("idproforma"));
            });
            proforma.setIdProforma(idProforma);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        String sqlDetalleProforma = "INSERT INTO detalleproforma (idproforma, idproducto, cantidad, preciounitario, descuento, subtotal)" +
                " VALUES (" + String.valueOf(proforma.getIdProforma()) + ", ?, ?, ?, ?, ?)";
        try{
            for(DetalleProforma dp: proforma.getDetallesProforma()){
                jdbcTemplate.update(sqlDetalleProforma, new Object[]{
                        dp.getProducto().getIdProducto(),
                        dp.getCantidad(),
                        dp.getProducto().getPrecio(),
                        dp.getDescuento(),
                        dp.getSubtotal()
                });
            }
        } catch(Exception e){
            e.printStackTrace();
            throw e;
        }

        return true;
    }

    @Override
    public List<Proforma> buscarProformas(String campoCliente, String datoCliente, String codigoProforma,
                                   Timestamp fechaDesde, Timestamp fechaHasta){
        String sql = "SELECT p.idproforma, p.idcliente, c.razonsocial, c.numdoc, c.telefono, c.direccion, c.email, " +
                "to_char(p.fechacreacion, 'DD/MM/YYYY') as fechacreacion, p.total " +
                "FROM proforma as p INNER JOIN cliente as c ON p.idcliente = c.idcliente " +
                "WHERE c." + campoCliente + " LIKE  ? AND p.idproforma::varchar LIKE ? " +
                "AND date_trunc('day',p.fechacreacion) >= ? AND date_trunc('day',p.fechacreacion) <= ?";
        datoCliente = "%" + datoCliente + "%";
        codigoProforma = "%" + codigoProforma + "%";

        try{
            List<Proforma> proformas = jdbcTemplate.query(sql,
                    new Object[]{datoCliente, codigoProforma,
                    fechaDesde, fechaHasta}, (res, i)->{
                Proforma p = new Proforma();
                p.setIdCliente(res.getLong("idcliente"));
                p.setIdProforma(res.getInt("idproforma"));
                Cliente c = new Cliente();
                c.setIdCliente(res.getLong("idcliente"));
                c.setRazonSocial(res.getString("razonsocial"));
                c.setNumDoc(res.getString("numdoc"));
                c.setTelefono(res.getString("telefono"));
                c.setDireccion(res.getString("direccion"));
                c.setEmail(res.getString("email"));
                p.setCliente(c);
                p.setFechaCreacion(res.getString("fechacreacion"));
                p.setTotal(res.getFloat("total"));
                return p;
                    });
            return proformas;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<DetalleProforma> obtenerDetallesDeProforma(int idProforma){
        String sql = "SELECT dp.iddetalleproforma, dp.idproducto, dp.cantidad, p.codigo, p.nombreproducto," +
                " p.precio, p.peso " +
                "FROM detalleproforma as dp INNER JOIN producto as p ON dp.idproducto = p.idproducto " +
                "WHERE dp.idproforma = ?";
        try{
            List<DetalleProforma> detalles = this.jdbcTemplate.query(sql, new Object[]{idProforma},
                    (res, i)->{
                DetalleProforma dp =  new DetalleProforma();
                dp.setIdDetalleProforma(res.getInt("iddetalleproforma"));
                Producto p = new Producto();
                p.setCodigo(res.getString("codigo"));
                p.setPrecio(res.getFloat("precio"));
                p.setIdProducto(res.getInt("idproducto"));
                p.setNombreProducto(res.getString("nombreproducto"));
                p.setPeso(res.getFloat("peso"));
                dp.setProducto(p);
                dp.setCantidad(res.getInt("cantidad"));
                return dp;
                    });
            return new ArrayList<DetalleProforma>(detalles);
        }catch(Exception e){
            e.printStackTrace();
            return  null;
        }
    }
}
