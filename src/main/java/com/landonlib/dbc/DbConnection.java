package com.landonlib.dbc;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DbConnection {
    private static final String _databaseHost = "35.185.1.84";
    private static final String _databaseName = "landon_lib_master";
    private static final String _userName = "landon_lib_master";
    private static final String _password = "landon_lib_master";
    private static final Integer _port = 3306;

    private final MysqlDataSource dataSourceInstance;

    public DbConnection () {
        dataSourceInstance = new MysqlDataSource();
        dataSourceInstance.setUser(_userName);
        dataSourceInstance.setPassword(_password);
        dataSourceInstance.setServerName(_databaseHost);
        dataSourceInstance.setDatabaseName(_databaseName);
        dataSourceInstance.setPort(_port);
    }

    public Connection getConnection() throws SQLException {
        dataSourceInstance.setServerTimezone("UTC");
        return dataSourceInstance.getConnection();
    }
}
