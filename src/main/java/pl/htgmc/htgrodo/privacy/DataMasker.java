package pl.htgmc.htgrodo.privacy;

import pl.htgmc.htgrodo.utils.HashUtils;

public class DataMasker {

    /**
     * Maska IP – np. 192.168.1.xxx
     */
    public String maskIP(String ip) {
        if (ip == null || ip.isEmpty()) return "0.0.0.xxx";
        return ip.replaceAll("\\d+$", "xxx");
    }

    /**
     * Maskowanie UUID – pokazuje tylko 8 znaków
     * 123e4567-xxxx-xxxx-xxxx-xxxxxxxxxxxx
     */
    public String maskUUID(String uuid) {
        if (uuid == null || uuid.isEmpty()) return "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx";

        if (uuid.length() >= 8) {
            return uuid.substring(0, 8) + "-xxxx-xxxx-xxxx-xxxxxxxxxxxx";
        }
        return "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx";
    }

    /**
     * Maska nicku – K***r
     */
    public String maskNick(String nickname) {
        if (nickname == null || nickname.length() < 2) return "**";

        char first = nickname.charAt(0);
        char last = nickname.charAt(nickname.length() - 1);
        return first + "***" + last;
    }

    /**
     * Hashowanie danych wrażliwych (np. DeviceID)
     */
    public String hashSensitive(String value) {
        return HashUtils.secureHash(value);
    }

    /**
     * Pełna anonimizacja — nick + UUID + IP → hash
     */
    public String anonymize(String input) {
        return HashUtils.sha256("HTGRODO" + input);
    }
}
