package com.landonlib.session.impl;

import com.landonlib.session.Encryption;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptionImpl implements Encryption {

    private final String KEY = "Bar12345Bar12345";
    private final Cipher ecipher;
    private final Cipher dcipher;

    public EncryptionImpl() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        SecretKey key = new SecretKeySpec(KEY.getBytes(), "AES");
        ecipher = Cipher.getInstance("AES");
        dcipher = Cipher.getInstance("AES");
        ecipher.init(Cipher.ENCRYPT_MODE, key);
        dcipher.init(Cipher.DECRYPT_MODE, key);
    }

    @Override
    public String encryptString(String string) {
        String encryptedString = "";

        byte[] utf8;
        try {
            utf8 = string.getBytes("UTF8");
            byte[] enc = ecipher.doFinal(utf8);
            encryptedString = Base64.getEncoder().encodeToString(enc);

        } catch (UnsupportedEncodingException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return encryptedString;
    }

    @Override
    public String decryptString(String string) {
        String decryptedString = "";

        byte[] dec = Base64.getDecoder().decode(string);
        byte[] utf8 = new byte[0];        // Decode using utf-8
        try {
            utf8 = dcipher.doFinal(dec);
            decryptedString = new String(utf8, "UTF8");
        } catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return decryptedString;
    }
}
