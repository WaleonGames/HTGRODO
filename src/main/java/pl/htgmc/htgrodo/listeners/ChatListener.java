package pl.htgmc.htgrodo.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import pl.htgmc.htgrodo.Main;
import pl.htgmc.htgrodo.censor.UserInputFilter;
import pl.htgmc.htgrodo.censor.ChatFilter;
import pl.htgmc.htgrodo.users.UserDataManager;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        // 0. Globalne filtry wyłączone → nic nie ruszamy
        if (!Main.get().isFilteringEnabled()) {
            return;
        }

        // 1. Ochrona użytkownika wyłączona → NIE FILTRUJEMY jego wiadomości
        boolean userProtection = UserDataManager.isProtectionEnabled(event.getPlayer().getName());
        if (!userProtection) {
            return;
        }

        String original = event.getMessage();
        UserInputFilter inputFilter = Main.get().api().getInputFilter();
        ChatFilter chatFilter = Main.get().api().getChatFilter();

        // 2. Sprawdzenie danych wrażliwych
        boolean sensitive = inputFilter.containsSensitiveData(original);

        String sanitized = original;

        // 3. Filtrowanie wejściowe (usuwamy dane prywatne)
        if (sensitive) {
            sanitized = inputFilter.sanitize(original);

            Main.get().api().logAudit(
                    event.getPlayer().getName(),
                    "CHAT_SENSITIVE_BLOCK",
                    "Wiadomość zawierała dane osobowe"
            );
        }

        // 4. Filtrowanie wyjściowe (cenzura czatu)
        sanitized = chatFilter.filterOutgoingMessage(sanitized);

        event.setMessage(sanitized);
    }
}
