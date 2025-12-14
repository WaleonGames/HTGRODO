package pl.htgmc.htgrodo.api;

import org.bukkit.entity.Player;

import pl.htgmc.htgrodo.censor.ChatFilter;
import pl.htgmc.htgrodo.censor.UserInputFilter;
import pl.htgmc.htgrodo.privacy.PrivacyPolicyEngine;
import pl.htgmc.htgrodo.audit.AuditLogger;

public class RodoAPI {

    private final DataMasker masker;
    private final ChatFilter chatFilter;
    private final UserInputFilter inputFilter;
    private final PrivacyPolicyEngine policy;
    private final AuditLogger audit;

    public RodoAPI(DataMasker masker,
                   ChatFilter chatFilter,
                   UserInputFilter inputFilter,
                   PrivacyPolicyEngine policy,
                   AuditLogger audit) {

        this.masker = masker;
        this.chatFilter = chatFilter;
        this.inputFilter = inputFilter;
        this.policy = policy;
        this.audit = audit;
    }

    // ============================================================
    //  DOSTĘP DO MODUŁÓW SYSTEMU RODO
    // ============================================================
    public DataMasker getMasker() { return masker; }
    public ChatFilter getChatFilter() { return chatFilter; }
    public UserInputFilter getInputFilter() { return inputFilter; }
    public PrivacyPolicyEngine getPolicy() { return policy; }


    // ============================================================
    //  MASKOWANIE DANYCH – niskopoziomowe API
    // ============================================================

    public String maskIP(String ip) { return masker.maskIP(ip); }

    public String maskUUID(String uuid) { return masker.maskUUID(uuid); }

    public String maskNick(String nick) { return masker.maskNick(nick); }

    /** 🔥 NOWE – maskowanie adresów ulic */
    public String maskStreet(String text) { return masker.maskStreet(text); }

    /** 🔥 NOWE – maskowanie całego tekstu we wszystkich kategoriach */
    public String maskAll(String text) {
        if (text == null) return "";
        text = masker.maskStreet(text);
        text = chatFilter.filterOutgoingMessage(text);
        text = inputFilter.sanitize(text);
        return text;
    }


    // ============================================================
    //  FILTROWANIE TREŚCI – wysokopoziomowe API
    // ============================================================

    /** Filtrowanie danych **wrażliwych** (PESEL, IP, email itd.) */
    public String filterUserInput(String message) {
        return inputFilter.sanitize(message);
    }

    /** Filtrowanie danych **niewrażliwych** (np. ulice) – używane do czatu */
    public String filterChat(String message) {
        return chatFilter.filterOutgoingMessage(message);
    }

    /** 🔥 NOWE – połączone filtrowanie z opcjonalnym logowaniem audytu */
    public String filterWithAudit(String message, String username) {
        boolean sensitive = containsSensitive(message);
        String sanitized = sensitive ? filterUserInput(message) : filterChat(message);

        if (sensitive) {
            logAudit(username, "AUTO_FILTER", "Wiadomość została automatycznie zanonimizowana");
        }
        return sanitized;
    }


    // ============================================================
    //  DETEKCJA DANYCH
    // ============================================================

    public boolean containsSensitive(String message) {
        return inputFilter.containsSensitiveData(message);
    }

    /** 🔥 NOWE – wykrywanie konkretnych danych */
    public boolean containsEmail(String text) {
        return inputFilter.containsEmail(text);
    }

    public boolean containsPhone(String text) {
        return inputFilter.containsPhone(text);
    }

    public boolean containsPesel(String text) {
        return inputFilter.containsPesel(text);
    }

    public boolean containsStreet(String text) {
        return masker.containsStreet(text);
    }


    // ============================================================
    //  POLITYKA DOSTĘPU (UPRAWNIENIA)
    // ============================================================
    public boolean canSee(Player player, DataType type) {
        return policy.canSee(player, type);
    }

    public boolean hasBypass(Player player) {
        return policy.hasBypass(player);
    }


    // ============================================================
    //  AUDYT SYSTEMOWY
    // ============================================================
    public void logAudit(String user, String action, String detail) {
        audit.log(user, action, detail);
    }
}
