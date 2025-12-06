package pl.htgmc.htgrodo.utils;

import org.bukkit.configuration.file.FileConfiguration;
import pl.htgmc.htgrodo.Main;

public class ConfigLoader {

    public static FileConfiguration cfg() {
        return Main.get().getConfig();
    }

    public static String getString(String path) {
        return cfg().getString(path, "");
    }

    public static boolean getBoolean(String path) {
        return cfg().getBoolean(path, false);
    }
}
