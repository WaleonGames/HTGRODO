package pl.htgmc.htgrodo.censor;

import java.util.regex.Matcher;

public class IpValidator {

    public static IpType detectType(String text) {
        if (text == null || text.isEmpty()) {
            return IpType.NONE;
        }

        Matcher m = SensitivePatterns.IP.matcher(text);
        if (!m.find()) {
            return IpType.NONE;
        }

        String ip = m.group();

        if (!isValidIpv4(ip)) {
            return IpType.NONE;
        }

        if (isPrivate(ip)) {
            return IpType.PRIVATE;
        }

        // Na tym etapie bez API → traktujemy jako PUBLIC
        return IpType.PUBLIC;
    }

    // =====================================================
    // Walidacja IPv4 (0–255)
    // =====================================================
    public static boolean isValidIpv4(String ip) {
        String[] parts = ip.split("\\.");
        if (parts.length != 4) return false;

        for (String p : parts) {
            int v;
            try {
                v = Integer.parseInt(p);
            } catch (NumberFormatException e) {
                return false;
            }
            if (v < 0 || v > 255) return false;
        }
        return true;
    }

    // =====================================================
    // Zakresy prywatne / lokalne
    // =====================================================
    public static boolean isPrivate(String ip) {
        String[] p = ip.split("\\.");
        int a = Integer.parseInt(p[0]);
        int b = Integer.parseInt(p[1]);

        // 10.0.0.0/8
        if (a == 10) return true;

        // 172.16.0.0 – 172.31.255.255
        if (a == 172 && b >= 16 && b <= 31) return true;

        // 192.168.0.0/16
        if (a == 192 && b == 168) return true;

        // loopback
        if (a == 127) return true;

        // APIPA
        if (a == 169 && b == 254) return true;

        return false;
    }

    // =====================================================
    // Maskowanie częściowe IP → A.B.**.**
    // =====================================================
    public static String maskPartial(String ip) {
        if (ip == null) return "***";

        String[] parts = ip.split("\\.");
        if (parts.length != 4) return "***";

        return parts[0] + "." + parts[1] + ".**.**";
    }
}
