package de.pangaea.xref.datasource;

import de.pangaea.xref.config.AppConfig;
import de.pangaea.xref.model.EnaXrefMapper;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public class DatabaseManager {

    private final Jdbi jdbi;

    public DatabaseManager(AppConfig config) {
        String url = config.getDbUrl();
        String user = config.getDbUser();
        String password = config.getDbPassword();

        this.jdbi = Jdbi.create(url, user, password)
                .installPlugin(new PostgresPlugin())
                .installPlugin(new SqlObjectPlugin())
                .registerRowMapper(new EnaXrefMapper());
    }

    public Jdbi getJdbi() {
        return jdbi;
    }
}