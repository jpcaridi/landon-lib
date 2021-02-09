package com.landonlib.session.impl;

import com.landonlib.dbc.DbConnection;
import com.landonlib.session.SessionFactory;
import com.landonlib.session.UserInfo;
import com.landonlib.session.UserSession;
import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.*;
import java.util.Calendar;
import java.util.UUID;

public class UserSessionImpl implements UserSession {
    private static final String _databaseHost = "johncaridi.com";
    private static final String _databaseName = "johnca14_landon_music_lib";
    private static final String _userName = "johnca14_jpcaridi";
    private static final String _password = "n1rvanafan11";
    private static final Integer _port = 3306;
    private static final int TIME_TO_EXPIRE = (5 * 60000);

    private UserInfo userInfo;
    private String token;

    private UserSessionImpl () {}

    private UserSessionImpl(String token, UserInfo userInfo) {
        this(token);

        this.userInfo = userInfo;
    }
    private UserSessionImpl (String token){
        this.token = token;
    }

    private static UserSession createSession(UserInfo userInfo) {
        String token = UUID.randomUUID().toString();
        UserSessionImpl userSession = null;

        String sql = "{call insert_user_session(?,?,?)}";

        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.getConnection();
             CallableStatement statement = connection.prepareCall(sql);
        ){
            statement.setString(1, token);
            statement.setString(2, userInfo.getUserId());

            Calendar calendar = Calendar.getInstance();
            long t = calendar.getTimeInMillis();
            Date tPlusFive = new Date(t + TIME_TO_EXPIRE);
            Timestamp timestamp = new Timestamp(tPlusFive.getTime());
            statement.setTimestamp(3, timestamp);

            statement.execute();

            userSession = new UserSessionImpl(token);
            userSession.userInfo = userInfo;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userSession;
    }

    public static UserSession logIn(String username, String password) {
        UserSession userSession = null;
        String sql = "{call verify_user(?,?,?)}";

        String encryptedPwd = SessionFactory.getEncryption().encryptString(password);

        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.getConnection();
             CallableStatement statement = connection.prepareCall(sql);
             ){
            statement.setString(1, username);
            statement.setString(2, encryptedPwd);
            statement.registerOutParameter(3, Types.VARCHAR);

            statement.execute();
            String userId = statement.getString(3);

            userSession = createSession(new UserInfoImpl(userId, username));
            System.out.println("User Id:" + userId + " User Name:" + username + " PWD:" + encryptedPwd);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userSession;
    }

    public static UserSession createInstanceStatic () {
        return new UserSessionImpl();
    }

    @Override
    public UserSession createInstance() {
        return createInstanceStatic();
    }

    @Override
    public UserInfo getUserInfo() {
        return userInfo;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public boolean isValid() {
        boolean validSession = false;

        if (token != null && userInfo != null) {
            String sql = "{call verify_user_session(?,?)}";

            DbConnection dbConnection = new DbConnection();
            try (Connection connection = dbConnection.getConnection();
                 CallableStatement statement = connection.prepareCall(sql);
            ) {
                statement.setString(1, token);
                statement.setString(2, userInfo.getUserId());
                statement.registerOutParameter(2, Types.VARCHAR);

                statement.execute();
                String userId = statement.getString(2);

                if (userId != null && !userId.isEmpty()) {
                    extendSession();
                    validSession = true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return validSession;
    }

    private void extendSession() {
        String sql = "{call set_user_session_exipiration(?,?,?)}";

        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.getConnection();
             CallableStatement statement = connection.prepareCall(sql);
        ) {
            statement.setString(1, token);
            statement.setString(2, userInfo.getUserId());

            Calendar calendar = Calendar.getInstance();
            long t = calendar.getTimeInMillis();
            Date tPlusFive = new Date(t + TIME_TO_EXPIRE);
            Timestamp timestamp = new Timestamp(tPlusFive.getTime());
            statement.setTimestamp(3, timestamp);

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean changePassword(String currentPassword, String newPassword) {
        return false;
    }

    @Override
    public UserSession getSession(String userId, String token) {

        if ( this.token != null && userInfo != null) {
            if (this.token.equals(token) && userInfo.getUserId().equals(userId)) {
                /* This is our current session. return it. */
                if (this.isValid())
                    return this;
            }
        }

        /* Check the data store for to see if the token is valid for this user. */
        UserInfo userInfo = UserInfoImpl.getUserInfo(userId);
        if (userInfo != null) {
            UserSession userSession = new UserSessionImpl(token, userInfo);

            if (userSession.isValid()) {
                return userSession;
            }
        }

        return null;
    }
}
