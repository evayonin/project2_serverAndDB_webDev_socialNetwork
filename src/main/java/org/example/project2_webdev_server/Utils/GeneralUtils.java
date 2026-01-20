package org.example.project2_webdev_server.Utils;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class GeneralUtils {

    public static String hash(String username, String password) {
        String source = username + password;

        try {
            byte[] digest = MessageDigest
                    .getInstance("SHA-256")
                    .digest(source.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(digest);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
