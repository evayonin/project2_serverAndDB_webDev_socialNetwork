package org.example.project2_webdev_server.Utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GeneralUtils {
    public static void main(String[] args) {
        System.out.println(hashPassword("1234" + "abc"));
    }
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = digest.digest(password.getBytes());

            StringBuilder hashString = new StringBuilder();
            for (byte b : hashBytes) {
                hashString.append(String.format("%02x", b));
            }

            return hashString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash error", e);
        }
    }
}