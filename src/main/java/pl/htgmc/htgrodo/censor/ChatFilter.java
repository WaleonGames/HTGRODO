package pl.htgmc.htgrodo.censor;

public class ChatFilter {

    /**
     * Filtrowanie wiadomości, które już idą na czat.
     * Wszystkie wykryte dane → ***
     */
    public String filterOutgoingMessage(String msg) {
        if (msg == null) return "";

        String result = msg;

        result = result.replaceAll(SensitivePatterns.PHONE.pattern(), "***");
        result = result.replaceAll(SensitivePatterns.EMAIL.pattern(), "***");
        result = result.replaceAll(SensitivePatterns.PESEL.pattern(), "***");
        result = result.replaceAll(SensitivePatterns.ADDRESS.pattern(), "***"); // ← TU

        return result;
    }
}
