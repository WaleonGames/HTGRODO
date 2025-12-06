package pl.htgmc.htgrodo.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import pl.htgmc.htgrodo.HTGRODO;
import pl.htgmc.htgrodo.users.UserDataManager;

public class RodoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String name = sender.getName();

        // ===================================================
        // /rodo  → Panel użytkownika
        // ===================================================
        if (args.length == 0) {
            sendUserHelp(sender);
            return true;
        }

        // ===================================================
        // /rodo chron
        // ===================================================
        if (args[0].equalsIgnoreCase("chron")) {

            if (args.length == 1) {
                boolean enabled = UserDataManager.isProtectionEnabled(name);

                sender.sendMessage("§7==== §bTwoja Ochrona RODO §7====");
                sender.sendMessage("§7Status: " + (enabled ? "§aWłączona" : "§cWyłączona"));
                sender.sendMessage("§7Użyj: §e/rodo chron on §7lub §e/rodo chron off");
                return true;
            }

            if (args[1].equalsIgnoreCase("on")) {
                UserDataManager.setProtection(name, true);
                sender.sendMessage("§a[RODO] Ochrona Twoich danych została WŁĄCZONA.");
                return true;
            }

            if (args[1].equalsIgnoreCase("off")) {
                UserDataManager.setProtection(name, false);
                sender.sendMessage("§c[RODO] Ochrona Twoich danych została WYŁĄCZONA.");
                return true;
            }

            sender.sendMessage("§cUżyj: /rodo chron on/off");
            return true;
        }

        // ===================================================
        // /rodo admin ...
        // ===================================================
        if (args[0].equalsIgnoreCase("admin")) {

            if (!sender.hasPermission("htg.rodo.admin")) {
                sender.sendMessage("§c[HTGRODO] Nie masz uprawnień do komend administracyjnych.");
                return true;
            }

            if (args.length == 1) {
                sendAdminHelp(sender);
                return true;
            }

            switch (args[1].toLowerCase()) {

                case "wlacz":
                    HTGRODO.get().setFilteringEnabled(true);
                    sender.sendMessage("§a[HTGRODO] Globalna filtracja została WŁĄCZONA.");
                    return true;

                case "wylacz":
                    HTGRODO.get().setFilteringEnabled(false);
                    sender.sendMessage("§c[HTGRODO] Globalna filtracja została WYŁĄCZONA.");
                    return true;

                case "reload":
                    HTGRODO.get().reloadConfig();
                    sender.sendMessage("§e[HTGRODO] Konfiguracja przeładowana.");
                    return true;

                case "status":
                    sender.sendMessage("§7==== §cHTGRODO — Status Admina §7====");
                    sender.sendMessage("§7Filtry globalne: " + (HTGRODO.get().isFilteringEnabled() ? "§aWłączone" : "§cWyłączone"));
                    return true;

                default:
                    sendAdminHelp(sender);
                    return true;
            }
        }

        // =================================
        // Nieznana akcja
        // =================================
        sender.sendMessage("§cNieznana komenda. Użyj: /rodo lub /rodo admin");
        return true;
    }


    // ===================================================
    // POMOC — użytkownik
    // ===================================================
    private void sendUserHelp(CommandSender s) {
        s.sendMessage("§7==== §bHTGRODO §7====");
        s.sendMessage("§e/rodo chron §7– ustawienia prywatności użytkownika");
        s.sendMessage("§7Aby zobaczyć komendy administratora: §e/rodo admin");
    }


    // ===================================================
    // POMOC — administrator
    // ===================================================
    private void sendAdminHelp(CommandSender s) {
        s.sendMessage("§7==== §cHTGRODO ADMIN §7====");
        s.sendMessage("§e/rodo admin wlacz §7– włącza globalną filtrację");
        s.sendMessage("§e/rodo admin wylacz §7– wyłącza globalną filtrację");
        s.sendMessage("§e/rodo admin reload §7– przeładowuje konfigurację");
        s.sendMessage("§e/rodo admin status §7– status systemu filtracji");
    }
}
