package com.landonlib.session;

import com.landonlib.session.impl.EncryptionImpl;
import com.landonlib.session.impl.UserSessionImpl;

import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SessionFactory {

    public static UserSession logIn(String userName, String password) {
        return UserSessionImpl.logIn(userName, password);
    }

    public static UserSession getSession(String userId, String token) {
        return UserSessionImpl.createInstanceStatic().getSession(userId, token);
    }

    public static Encryption getEncryption() {
        try {
            return new EncryptionImpl();
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            return null;
        }
    }
}
