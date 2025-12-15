package pl.htgmc.htgrodo.censor;

import java.util.regex.Matcher;

public class ChatFilter {

    public String filterOutgoingMessage(String msg) {
        if (msg == null || msg.isEmpty()) return "";

        String result = msg;

        // proste maskowanie
        result = maskSimple(result, SensitivePatterns.PHONE);
        result = maskSimple(result, SensitivePatterns.EMAIL);

        // PESEL – logika specjalna
        result = maskPesel(result);

        // adres – silent
        result = maskAddress(result);

        // IP - logika specjalna
        result = maskIp(result);

        return result;
    }

    // =========================
    // Proste maskowanie ***
    // =========================
    private String maskSimple(String text, java.util.regex.Pattern pattern) {
        Matcher m = pattern.matcher(text);
        StringBuffer sb = new StringBuffer();

        while (m.find()) {
            // zachowujemy spacing z tekstu
            m.appendReplacement(sb, "***");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    // =========================
    // PESEL – fake vs real
    // =========================
    private String maskPesel(String text) {
        Matcher m = SensitivePatterns.PESEL.matcher(text);
        StringBuffer sb = new StringBuffer();

        while (m.find()) {
            String pesel = m.group();

            if (PeselValidator.isValid(pesel)) {
                // prawdziwy PESEL → ukrywamy
                m.appendReplacement(sb, "***");
            } else {
                // fake PESEL → oznaczamy wizualnie
                m.appendReplacement(sb, "~~" + pesel + "~~");
            }
        }

        m.appendTail(sb);
        return sb.toString();
    }

    // =========================
    // Maskowanie adresu – CICHY TRYB
    // =========================
    private String maskAddress(String text) {
        Matcher m = SensitivePatterns.ADDRESS.matcher(text);
        StringBuffer sb = new StringBuffer();

        while (m.find()) {
            // oddajemy spację / początek linii
            m.appendReplacement(sb, m.group(1) + "***");
        }

        m.appendTail(sb);
        return sb.toString();
    }

    private String maskIp(String text) {
        var m = SensitivePatterns.IP.matcher(text);
        StringBuffer sb = new StringBuffer();

        while (m.find()) {
            String ip = m.group();
            IpType type = IpValidator.detectType(ip);

            if (type == IpType.PUBLIC || type == IpType.PRIVATE) {
                m.appendReplacement(sb, IpValidator.maskPartial(ip));
            } else {
                m.appendReplacement(sb, ip);
            }
        }

        m.appendTail(sb);
        return sb.toString();
    }
}
