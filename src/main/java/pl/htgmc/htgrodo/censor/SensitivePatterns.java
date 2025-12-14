package pl.htgmc.htgrodo.censor;

import java.util.regex.Pattern;

public class SensitivePatterns {

    // =========================
    // TELEFON (PL) – 9 cyfr
    // =========================
    public static final Pattern PHONE = Pattern.compile(
            "\\b(?:\\+?48\\s*)?[4-9]\\d{2}[\\s-]?\\d{3}[\\s-]?\\d{3}\\b"
    );

    // =========================
    // EMAIL
    // =========================
    public static final Pattern EMAIL = Pattern.compile(
            "\\b[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,6}\\b"
    );

    // =========================
    // PESEL – 11 cyfr (walidacja później)
    // =========================
    public static final Pattern PESEL = Pattern.compile(
            "\\b\\d{11}\\b"
    );

    // =========================
    // ADRES PL – ulica + numer (OBOWIĄZKOWY)
    // - działa dla: "jasna 12", "ul. jasna 12", "plac 3 Maja 10"
    // - NIE łapie: "jasna sprawa"
    // =========================
    public static final Pattern ADDRESS = Pattern.compile(
            "(?iu)\\b" +
                    "(?:(ul\\.?|ulica|al\\.?|aleja|pl\\.?|plac|os\\.?|osiedle)\\s+)?" +
                    "(?:\\d{1,2}\\s+)?" +                 // 3 Maja, 11 Listopada
                    "[a-ząćęłńóśźż]{3,}" +
                    "(?:\\s+[a-ząćęłńóśźż]{2,})*" +
                    "\\s+\\d{1,4}[a-z]?(?:/\\d{1,4})?" +
                    "\\b"
    );
}
