package pl.htgmc.htgrodo.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {

    // Możesz zmienić wg. HTGMC Security Standard
    private static final String PEPPER = "HTGMC-RODO-PEPPER-2025";

    /**
     * Tworzy hash SHA-256 tekstu
     */
    public static String sha256(String text) {
        return hash(text, "SHA-256");
    }

    /**
     * Tworzy hash SHA-1 (opcjonalnie, lżejszy niż SHA-256)
     */
    public static String sha1(String text) {
        return hash(text, "SHA-1");
    }

    /**
     * Tworzy hash MD5 (szybki, NIE do wrażliwych danych, tylko skróty)
     */
    public static String md5(String text) {
        return hash(text, "MD5");
    }

    /**
     * HASH Z PEPPEREM – zalecany do czułych danych np. DeviceID
     */
    public static String secureHash(String text) {
        return sha256(text + PEPPER);
    }

    /**
     * Silny wspólny rdzeń, obsługuje wszystkie algorytmy
     */
    private static String hash(String text, String algorithm) {
        if (text == null) text = "";

        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashBytes = digest.digest(text.getBytes(StandardCharsets.UTF_8));

            StringBuilder result = new StringBuilder();
            for (byte b : hashBytes) {
                result.append(String.format("%02x", b));
            }

            return result.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
