package pl.htgmc.htgrodo.users;

import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import pl.htgmc.htgrodo.Main;

public class UserDataManager {

    public static boolean isProtectionEnabled(String name) {
        File file = new File(Main.get().getDataFolder(), "users/" + name + ".yml");

        if (!file.exists()) {
            return true; // domyślnie ochrona włączona
        }

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        return cfg.getBoolean("protection", true);
    }

    public static void setProtection(String name, boolean enabled) {
        try {
            File folder = new File(Main.get().getDataFolder(), "users");
            if (!folder.exists()) folder.mkdirs();

            File file = new File(folder, name + ".yml");
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

            cfg.set("protection", enabled);
            cfg.save(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
