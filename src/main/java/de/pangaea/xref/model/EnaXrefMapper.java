package de.pangaea.xref.model;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EnaXrefMapper implements RowMapper<EnaXref> {
    @Override
    public EnaXref map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new EnaXref(
                rs.getString("insdc"),
                rs.getString("datasetId"),
                rs.getString("citationUrl"),
                rs.getString("citation")
        );
    }
}