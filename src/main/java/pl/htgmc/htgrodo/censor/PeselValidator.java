package pl.htgmc.htgrodo.censor;

import java.util.regex.Matcher;

public class PeselValidator {

    // Wagi do sumy kontrolnej
    private static final int[] WEIGHTS = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};

    /**
     * Wykrywa typ PESEL w tekście:
     * - NONE  → brak 11 cyfr
     * - FAKE  → 11 cyfr, ale niepoprawny
     * - REAL  → poprawny PESEL
     */
    public static PeselType detectType(String text) {
        if (text == null || text.isEmpty()) {
            return PeselType.NONE;
        }

        Matcher m = SensitivePatterns.PESEL.matcher(text);
        if (!m.find()) {
            return PeselType.NONE;
        }

        String pesel = m.group();

        return isValid(pesel) ? PeselType.REAL : PeselType.FAKE;
    }

    /**
     * Sprawdza czy PESEL jest poprawny:
     * - 11 cyfr
     * - poprawna data
     * - poprawna suma kontrolna
     */
    public static boolean isValid(String pesel) {
        if (pesel == null || !pesel.matches("\\d{11}")) {
            return false;
        }

        // -------------------------
        // DATA URODZENIA
        // -------------------------
        int year  = Integer.parseInt(pesel.substring(0, 2));
        int month = Integer.parseInt(pesel.substring(2, 4));
        int day   = Integer.parseInt(pesel.substring(4, 6));

        int realMonth = month;
        int century;

        if (month >= 1 && month <= 12) {
            century = 1900;
        } else if (month >= 21 && month <= 32) {
            century = 2000;
            realMonth -= 20;
        } else if (month >= 41 && month <= 52) {
            century = 2100;
            realMonth -= 40;
        } else if (month >= 61 && month <= 72) {
            century = 2200;
            realMonth -= 60;
        } else if (month >= 81 && month <= 92) {
            century = 1800;
            realMonth -= 80;
        } else {
            return false;
        }

        if (!isValidDate(century + year, realMonth, day)) {
            return false;
        }

        // -------------------------
        // SUMA KONTROLNA
        // -------------------------
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (pesel.charAt(i) - '0') * WEIGHTS[i];
        }

        int control = (10 - (sum % 10)) % 10;
        return control == (pesel.charAt(10) - '0');
    }

    /**
     * Prosta walidacja daty (bez LocalDate – szybciej i bez wyjątków)
     */
    private static boolean isValidDate(int year, int month, int day) {
        if (month < 1 || month > 12) return false;
        if (day < 1) return false;

        int[] daysInMonth = {
                31, // I
                isLeapYear(year) ? 29 : 28, // II
                31, // III
                30, // IV
                31, // V
                30, // VI
                31, // VII
                31, // VIII
                30, // IX
                31, // X
                30, // XI
                31  // XII
        };

        return day <= daysInMonth[month - 1];
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}
