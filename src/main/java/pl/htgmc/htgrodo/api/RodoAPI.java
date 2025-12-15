package pl.htgmc.htgrodo.api;

import org.bukkit.entity.Player;

import pl.htgmc.htgrodo.censor.ChatFilter;
import pl.htgmc.htgrodo.censor.UserInputFilter;
import pl.htgmc.htgrodo.privacy.PrivacyPolicyEngine;
import pl.htgmc.htgrodo.audit.AuditLogger;

public class RodoAPI {

    private final ChatFilter chatFilter;
    private final UserInputFilter inputFilter;
    private final PrivacyPolicyEngine policy;
    private final AuditLogger audit;

    public RodoAPI(ChatFilter chatFilter,
                   UserInputFilter inputFilter,
                   PrivacyPolicyEngine policy,
                   AuditLogger audit) {

        this.chatFilter = chatFilter;
        this.inputFilter = inputFilter;
        this.policy = policy;
        this.audit = audit;
    }

    // ============================================================
    //  DOSTĘP DO MODUŁÓW
    // ============================================================

    public ChatFilter getChatFilter() {
        return chatFilter;
    }

    public UserInputFilter getInputFilter() {
        return inputFilter;
    }

    public PrivacyPolicyEngine getPolicy() {
        return policy;
    }

    // ============================================================
    //  FILTROWANIE TREŚCI
    // ============================================================

    /** Dane wysokiego ryzyka (telefon, email, PESEL) */
    public String filterUserInput(String message) {
        return inputFilter.sanitize(message);
    }

    /** Wizualna cenzura czatu */
    public String filterChat(String message) {
        return chatFilter.filterOutgoingMessage(message);
    }

    public boolean containsSensitive(String message) {
        return inputFilter.containsSensitiveData(message);
    }

    // ============================================================
    //  POLITYKA DOSTĘPU
    // ============================================================

    public boolean canSee(Player player, DataType type) {
        return policy.canSee(player, type);
    }

    public boolean hasBypass(Player player) {
        return policy.hasBypass(player);
    }

    // ============================================================
    //  AUDYT
    // ============================================================

    public void logAudit(String user, String action, String detail) {
        audit.log(user, action, detail);
    }
}
