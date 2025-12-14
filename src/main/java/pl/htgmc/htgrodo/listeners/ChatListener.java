package pl.htgmc.htgrodo.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import pl.htgmc.htgrodo.HTGRODO;
import pl.htgmc.htgrodo.api.RodoAPI;
import pl.htgmc.htgrodo.censor.UserInputFilter;
import pl.htgmc.htgrodo.censor.ChatFilter;
import pl.htgmc.htgrodo.users.UserDataManager;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        // 0. Globalne filtry wyłączone
        if (!HTGRODO.get().isFilteringEnabled()) return;

        // 1. Ochrona użytkownika wyłączona
        if (!UserDataManager.isProtectionEnabled(event.getPlayer().getName())) return;

        String original = event.getMessage();

        RodoAPI rodo = HTGRODO.get().api();
        UserInputFilter inputFilter = rodo.getInputFilter();
        ChatFilter chatFilter = rodo.getChatFilter();

        boolean sensitive = inputFilter.containsSensitiveData(original);
        String sanitized = original;

        // 2. Dane wysokiego ryzyka (PESEL, tel, email)
        if (sensitive) {
            sanitized = inputFilter.sanitize(original);

            rodo.logAudit(
                    event.getPlayer().getName(),
                    "CHAT_SENSITIVE_BLOCK",
                    "Wiadomość zawierała dane osobowe (wysokiego ryzyka)"
            );
        }

        // 🔥 3. MASKOWANIE ULIC – NOWA LOGIKA (ANTY FALSE-POSITIVE)
        sanitized = rodo.maskStreet(sanitized);

        // 4. Cenzura końcowa (***)
        sanitized = chatFilter.filterOutgoingMessage(sanitized);

        event.setMessage(sanitized);
    }
}
