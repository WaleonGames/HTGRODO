package pl.htgmc.htgrodo.censor;

public class UserInputFilter {

    /**
     * Sprawdza, czy tekst zawiera dane osobowe
     */
    public boolean containsSensitiveData(String text) {
        if (text == null) return false;

        return SensitivePatterns.PHONE.matcher(text).find()
                || SensitivePatterns.EMAIL.matcher(text).find()
                || SensitivePatterns.PESEL.matcher(text).find()
                || SensitivePatterns.ADDRESS.matcher(text).find();
    }

    /**
     * Usuwa dane osobowe i zastępuje je placeholderem
     */
    public String sanitize(String text) {
        if (text == null) return "";

        String result = text;

        result = result.replaceAll(SensitivePatterns.PHONE.pattern(), "[ukryto]");
        result = result.replaceAll(SensitivePatterns.EMAIL.pattern(), "[ukryto]");
        result = result.replaceAll(SensitivePatterns.PESEL.pattern(), "[ukryto]");
        result = result.replaceAll(SensitivePatterns.ADDRESS.pattern(), "[ukryto]");

        return result;
    }
}
