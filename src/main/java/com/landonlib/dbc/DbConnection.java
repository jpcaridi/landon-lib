package com.landonlib.dbc;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DbConnection {
    private static final String _databaseHost = "199.250.205.20";
    private static final String _databaseUrl = "johncaridi.com";
    private static final String _databaseName = "johnca14_landon_music_lib";
    private static final String _userName = "johnca14_jpcaridi";
    private static final String _password = "n1rvanafan11";
    private static final Integer _port = 3306;

    private final MysqlDataSource dataSourceInstance;

    public DbConnection () {
        dataSourceInstance = new MysqlDataSource();
        dataSourceInstance.setUser(_userName);
        dataSourceInstance.setPassword(_password);
        dataSourceInstance.setServerName(_databaseUrl);
        //dataSourceInstance.setUrl(_databaseUrl);
        dataSourceInstance.setDatabaseName(_databaseName);
        dataSourceInstance.setPort(_port);
    }

    public Connection getConnection() throws SQLException {
        dataSourceInstance.setServerTimezone("UTC");
        dataSourceInstance.setNoAccessToProcedureBodies(true);
        return dataSourceInstance.getConnection();
    }
}
