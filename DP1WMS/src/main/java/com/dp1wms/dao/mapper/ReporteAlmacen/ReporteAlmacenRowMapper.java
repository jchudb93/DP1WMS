package com.dp1wms.dao.mapper.ReporteAlmacen;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReporteAlmacenRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        ReporteAlmacenResultSetExtractor extractor = new ReporteAlmacenResultSetExtractor();
        return extractor.extractData(resultSet);

    }

}
