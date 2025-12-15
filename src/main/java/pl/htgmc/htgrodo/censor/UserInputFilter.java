package pl.htgmc.htgrodo.censor;

public class UserInputFilter {

    /**
     * Sprawdza, czy tekst zawiera dane osobowe WYSOKIEGO ryzyka:
     * - numer telefonu
     * - email
     * - PRAWDZIWY PESEL (po walidacji)
     *
     * Adresy NIE są tutaj wykrywane.
     */
    public boolean containsSensitiveData(String text) {
        if (text == null) return false;

        if (SensitivePatterns.PHONE.matcher(text).find()) return true;
        if (SensitivePatterns.EMAIL.matcher(text).find()) return true;

        return PeselValidator.detectType(text) == PeselType.REAL;
    }

    /**
     * Usuwa dane osobowe WYSOKIEGO ryzyka i zastępuje je `[ukryto]`.
     * Maskowanie wizualne (*** / ~~fake~~) odbywa się w ChatFilter.
     */
    public String sanitize(String text) {
        if (text == null) return "";

        String result = text;

        result = result.replaceAll(SensitivePatterns.PHONE.pattern(), "[ukryto]");
        result = result.replaceAll(SensitivePatterns.EMAIL.pattern(), "[ukryto]");

        // PESEL: tylko PRAWDZIWY
        if (PeselValidator.detectType(result) == PeselType.REAL) {
            result = result.replaceAll(SensitivePatterns.PESEL.pattern(), "[ukryto]");
        }

        return result;
    }

    // =============================================================
    // Metody pomocnicze (BEZ PESEL!)
    // =============================================================

    public boolean containsEmail(String text) {
        return text != null && SensitivePatterns.EMAIL.matcher(text).find();
    }

    public boolean containsPhone(String text) {
        return text != null && SensitivePatterns.PHONE.matcher(text).find();
    }
}
