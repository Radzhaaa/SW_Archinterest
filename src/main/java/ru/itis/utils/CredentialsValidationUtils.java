package ru.itis.utils;

public class CredentialsValidationUtils {
    public static boolean isEmailValid(String email) {
        return !email.isBlank()
                && email.length() <= 320
                && email.matches("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
    }

    public static boolean isUsernameValid(String nickname) {
        return !nickname.isBlank()
                && nickname.length() >= 4
                && nickname.length() <= 32
                && nickname.matches("^[a-zA-Z0-9а-яА-Я_\\-]+$");
    }

    public static boolean isPasswordValid(String password) {
        return !password.isBlank()
                && password.length() >= 8
                && password.length() <= 64;
    }
}
