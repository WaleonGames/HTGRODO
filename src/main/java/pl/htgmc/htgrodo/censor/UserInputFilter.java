package pl.htgmc.htgrodo.censor;

public class UserInputFilter {

    /**
     * Sprawdza, czy tekst zawiera dane osobowe WYSOKIEGO ryzyka:
     * - numer telefonu
     * - email
     * - PESEL
     *
     * Ulice NIE są tutaj wykrywane → nie są danymi wrażliwymi.
     */
    public boolean containsSensitiveData(String text) {
        if (text == null) return false;

        return SensitivePatterns.PHONE.matcher(text).find()
                || SensitivePatterns.EMAIL.matcher(text).find()
                || SensitivePatterns.PESEL.matcher(text).find();
    }

    /**
     * Usuwa dane osobowe i zastępuje je placeholderem `[ukryto]`
     * Ulice NIE są tu maskowane – robi to DataMasker
     */
    public String sanitize(String text) {
        if (text == null) return "";

        String result = text;

        // Maskowanie danych WYSOKIEGO ryzyka
        result = result.replaceAll(SensitivePatterns.PHONE.pattern(), "[ukryto]");
        result = result.replaceAll(SensitivePatterns.EMAIL.pattern(), "[ukryto]");
        result = result.replaceAll(SensitivePatterns.PESEL.pattern(), "[ukryto]");

        return result;
    }


    // =============================================================
    // Dodatkowe funkcje wykrywania (przydatne dla RodoAPI)
    // =============================================================

    public boolean containsEmail(String text) {
        return text != null && SensitivePatterns.EMAIL.matcher(text).find();
    }

    public boolean containsPhone(String text) {
        return text != null && SensitivePatterns.PHONE.matcher(text).find();
    }

    public boolean containsPesel(String text) {
        return text != null && SensitivePatterns.PESEL.matcher(text).find();
    }
}
