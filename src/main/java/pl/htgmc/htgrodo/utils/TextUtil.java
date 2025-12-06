package pl.htgmc.htgrodo.utils;

public class TextUtil {

    // usuwa wszystkie białe znaki z początku i końca (bez ryzyka null)
    public static String trim(String text) {
        return text == null ? "" : text.trim();
    }

    // usuwa niebezpieczne znaki (np. znaki kontrolne)
    public static String sanitizeControlChars(String text) {
        return text == null ? "" : text.replaceAll("[\\p{Cntrl}&&[^\n\t]]", "");
    }

    // normalizuje duże/małe litery
    public static String normalizeLower(String text) {
        return text == null ? "" : text.toLowerCase();
    }

    // usuwa emoji (często ukrywają dane)
    public static String removeEmoji(String text) {
        if (text == null) return "";
        return text.replaceAll("[\\p{So}\\p{Cn}]", "");
    }

    // pełna sanitizacja (chat + input)
    public static String fullSanitize(String text) {
        text = trim(text);
        text = sanitizeControlChars(text);
        text = removeEmoji(text);
        return text;
    }
}
