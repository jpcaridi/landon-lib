package com.landonlib.session;

public interface UserSession {

    UserSession createInstance();

    UserInfo getUserInfo();
    String getToken();
    boolean isValid();

    boolean changePassword(String currentPassword, String newPassword);

    UserSession getSession(String userId, String token);
}
