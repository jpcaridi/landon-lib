package com.landonlib.session.impl;

import com.landonlib.dbc.DbConnection;
import com.landonlib.session.UserInfo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserInfoImpl implements UserInfo {
    private String userName;
    private String userId;

    public UserInfoImpl(String userId, String userName) {
        this.userName = userName;
        this.userId = userId;
    }

    public static UserInfo getUserInfo(String userId) {

        String sql = "select * from LML_USER where ID_ = ?";

        DbConnection dbConnection = new DbConnection();
        String newUserID = "";
        String newUserName = "";

        try (Connection connection = dbConnection.getConnection();
             CallableStatement statement = connection.prepareCall(sql);
        ) {
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                newUserID = resultSet.getString(1);
                newUserName = resultSet.getString(2);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new UserInfoImpl(newUserID, newUserName);
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getUserId() {
        return userId;
    }
}
