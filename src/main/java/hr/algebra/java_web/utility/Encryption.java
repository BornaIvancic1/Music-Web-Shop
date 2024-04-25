package hr.algebra.java_web.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Encryption {
    public static String createSalt(int size) {
        byte[] salt = new byte[size];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String generateHash(String input, String salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = (input + salt).getBytes();
        byte[] hash = digest.digest(bytes);
        return Base64.getEncoder().encodeToString(hash);
    }

    public static boolean equals(String plainTextInput, String hashedInput, String salt) throws NoSuchAlgorithmException {
        String newHashedInput = generateHash(plainTextInput, salt);
        return newHashedInput.equals(hashedInput);
    }

    public static boolean validatePassword(String password, String salt, String hashedPassword) throws NoSuchAlgorithmException {
        String newPasswordHash = generateHash(password, salt);
        return newPasswordHash.equals(hashedPassword);
    }
}
