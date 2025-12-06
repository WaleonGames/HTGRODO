package pl.htgmc.htgrodo.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import pl.htgmc.htgrodo.HTGRODO;
import pl.htgmc.htgrodo.censor.UserInputFilter;
import pl.htgmc.htgrodo.users.UserDataManager;

public class CommandListener implements Listener {

    private final String[] privateCommands = {
            "/msg", "/tell", "/w", "/tpa", "/mail", "/r", "/reply"
    };

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {

        // 0. Globalne filtry wyłączone
        if (!HTGRODO.get().isFilteringEnabled()) {
            return;
        }

        // 1. Ochrona użytkownika wyłączona
        boolean userProtection = UserDataManager.isProtectionEnabled(event.getPlayer().getName());
        if (!userProtection) {
            return;
        }

        String cmd = event.getMessage().toLowerCase();
        UserInputFilter filter = HTGRODO.get().api().getInputFilter();

        // 2. Czy to komenda prywatna
        boolean isPrivate = false;
        for (String c : privateCommands) {
            if (cmd.startsWith(c + " ") || cmd.equals(c)) {
                isPrivate = true;
                break;
            }
        }

        // 3. Filtrowanie zwykłych komend
        if (!isPrivate) {
            if (filter.containsSensitiveData(cmd)) {
                HTGRODO.get().api().logAudit(
                        event.getPlayer().getName(),
                        "COMMAND_SENSITIVE_DATA",
                        "Próba użycia danych osobowych w komendzie: " + event.getMessage()
                );
                event.setMessage(filter.sanitize(cmd));
            }
            return;
        }

        // 4. Filtrowanie komend prywatnych
        String sanitized = filter.sanitize(event.getMessage());

        if (!sanitized.equals(event.getMessage())) {
            HTGRODO.get().api().logAudit(
                    event.getPlayer().getName(),
                    "PRIVATE_MESSAGE_SANITIZED",
                    "Usunięto dane osobowe z komendy: " + event.getMessage()
            );
        }

        event.setMessage(sanitized);
    }
}
