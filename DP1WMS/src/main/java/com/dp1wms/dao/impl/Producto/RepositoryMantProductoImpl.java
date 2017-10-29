package com.dp1wms.dao.impl.Producto;

import com.dp1wms.dao.IProducto.RepositoryMantProducto;
import com.dp1wms.dao.mapper.Producto.ProductoRowMapper;
import com.dp1wms.model.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.sql.Timestamp;
import java.util.List;
@Repository
public class RepositoryMantProductoImpl implements RepositoryMantProducto {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Producto> selectAllProducto() {
        String sql = "SELECT * from producto order by idproducto";
        return jdbcTemplate.query(sql,new ProductoRowMapper());
    }

    @Override
    public int newIdProducto() {
        String sql = "SELECT MAX(idproducto) FROM producto";
        return jdbcTemplate.queryForObject(sql,new Object[]{},Integer.class)+1;
    }

    @Override
    public void createProducto(Producto producto) {
        String sql = "INSERT INTO producto (idproducto," +
                "nombreproducto," +
                "peso," +
                "fechavencimiento" +
                "descripcion" +
                "precio" +
                "stock" +
                "idcategoria" +
                "codigo" +
                "fechacreacion" +
                "activo) VALUES(default, ?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql,new Object[]{producto.getNombreProducto(),producto.getPeso(),
        producto.getFechaVencimiento(),producto.getFechaVencimiento(),producto.getDescripcion(),
        producto.getPrecio(),
        producto.getStock(),
        producto.getIdCategoria(),
        producto.getCodigo(),
        producto.getFechaCreacion(),
        producto.isActivo()});
    }

    @Override
    public void updateProducto(Producto producto) {
        String sql = "UPDATE producto SET nombreproducto = ?, peso=?,fechavencimiento=?," +
                "descripcion=?,precio=?,stock=?,idcategoria=?,fechacreacion=? WHERE idproducto=?";
        jdbcTemplate.update(sql,new Object[]{producto.getNombreProducto(),producto.getPeso(),
                Timestamp.valueOf(producto.getFechaVencimiento()),producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getIdCategoria(),
                Timestamp.valueOf(producto.getFechaCreacion()),
                producto.getIdProducto()
    });
    }

    @Override
    public void deleteProducto(Producto producto) {
        String sql = "UPDATE producto SET activo = false Where idproducto =?";
        jdbcTemplate.update(sql,new Object[]{producto.getIdProducto()});

    }
}
