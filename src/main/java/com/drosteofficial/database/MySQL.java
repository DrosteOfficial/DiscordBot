package com.drosteofficial.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class MySQL extends Database {

    private HikariDataSource dataSource;
    private final Map<String , String> credentials;

    public MySQL(Map<String, String> credentials) {
        this.credentials = credentials;
    }

    @Override
    public void init() {
        HikariConfig config = new HikariConfig();
        String link = credentials.get("jdbcUrl");
        config.setJdbcUrl(
                 link.replace("{host}", credentials.get("host"))
                .replace("{port}", credentials.get("port"))
                .replace("{database}", credentials.get("database")));
        config.setUsername(credentials.get("username"));
        config.setPassword(credentials.get("password"));
        config.setMaximumPoolSize(Integer.parseInt(credentials.get("pool-size")));
        config.setConnectionTimeout(30000);
        this.dataSource = new HikariDataSource(config);
        dataSource.getConnectionTestQuery();

    }

    @Override
    public Connection GetConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
