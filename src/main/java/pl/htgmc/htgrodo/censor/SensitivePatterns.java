package pl.htgmc.htgrodo.censor;

import java.util.regex.Pattern;

public class SensitivePatterns {

    // =====================================================
    // TELEFON (PL)
    // =====================================================
    public static final Pattern PHONE = Pattern.compile(
            "(?<![A-Za-z0-9_])" +
                    "(?:\\+?48\\s*)?" +
                    "[4-9]\\d{2}[\\s-]?\\d{3}[\\s-]?\\d{3}" +
                    "(?![A-Za-z0-9_])"
    );

    // =====================================================
    // EMAIL
    // =====================================================
    public static final Pattern EMAIL = Pattern.compile(
            "\\b[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}\\b"
    );

    // =====================================================
    // PESEL – kandydat (11 cyfr)
    // =====================================================
    public static final Pattern PESEL = Pattern.compile(
            "(?<![A-Za-z0-9_])\\d{11}(?![A-Za-z0-9_])"
    );

    // =====================================================
    // IPv4 – kandydat (anty wersje MC)
    // =====================================================
    public static final Pattern IP = Pattern.compile(
            "(?<![A-Za-z0-9_])" +
                    "(?:\\d{1,3}\\.){3}\\d{1,3}" +
                    "(?![A-Za-z0-9_])"
    );

    // =====================================================
    // ADRES PL – wymagany numer
    // =====================================================
    public static final Pattern ADDRESS = Pattern.compile(
            "(?iu)(^|\\s)" +
                    "(" +
                    "ul\\.?|ulica|ulicy|" +
                    "al\\.?|aleja|alei|" +
                    "pl\\.?|plac|placu|" +
                    "os\\.?|osiedle|osiedlu" +
                    ")" +
                    "\\s+" +
                    "(?:\\d{1,2}\\s+)?" +
                    "[a-ząćęłńóśźż]{3,}" +
                    "(?:\\s+[a-ząćęłńóśźż]{2,}|\\s+II|\\s+III|\\s+IV)*" +
                    "\\s+" +
                    "\\d{1,4}[a-z]?(?:/\\d{1,4})?"
    );
}
