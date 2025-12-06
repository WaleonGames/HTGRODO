package pl.htgmc.htgrodo.censor;

import java.util.regex.Pattern;

public class SensitivePatterns {

    // Polski numer telefonu (9 cyfr lub formaty ze spacjami/myślnikami)
    public static final Pattern PHONE = Pattern.compile(
            "\\b(?:\\+?48)?[\\s-]?\\d{3}[\\s-]?\\d{3}[\\s-]?\\d{3}\\b"
    );

    // Email
    public static final Pattern EMAIL = Pattern.compile(
            "\\b[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,6}\\b"
    );

    // Pesel – 11 cyfr
    public static final Pattern PESEL = Pattern.compile(
            "\\b(?:\\d\\s*){11}\\b"
    );

    // Adres – przykładowe formaty: "ul. Kwiatowa 12", "Kwiatowa 12/3"
    public static final Pattern ADDRESS = Pattern.compile(
            "\\b(?:ul\\.?\\s+[A-ZĄĆĘŁŃÓŚŻŹ][a-ząćęłńóśżź]+(?:\\s+[A-ZĄĆĘŁŃÓŚŻŹ][a-ząćęłńóśżź]+)*"
                    + "|[A-ZĄĆĘŁŃÓŚŻŹ][a-ząćęłńóśżź]+(?:\\s+[A-ZĄĆĘŁŃÓŚŻŹ][a-ząćęłńóśżź]+)*\\s+\\d{1,4}(?:/[0-9A-Za-z]{1,4})?)\\b"
    );
}
