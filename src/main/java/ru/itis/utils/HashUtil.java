package ru.itis.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    private HashUtil() {
    }

    public static String getHash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.update(password.getBytes(StandardCharsets.UTF_8));
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder resultHash = new StringBuilder();

            for (byte b : hash) {
                resultHash.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }

            return resultHash.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
