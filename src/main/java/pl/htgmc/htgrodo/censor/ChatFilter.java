package pl.htgmc.htgrodo.censor;

public class ChatFilter {

    /**
     * Filtrowanie wiadomości, które już idą na czat.
     * Wszystko, co jest danymi wrażliwymi → ***
     */
    public String filterOutgoingMessage(String msg) {
        if (msg == null) return "";

        String result = msg;

        result = result.replaceAll(SensitivePatterns.PHONE.pattern(), "***");
        result = result.replaceAll(SensitivePatterns.EMAIL.pattern(), "***");
        result = result.replaceAll(SensitivePatterns.PESEL.pattern(), "***");
        result = result.replaceAll(SensitivePatterns.ADDRESS.pattern(), "***");

        return result;
    }
}
