package pl.htgmc.htgrodo.privacy;

import org.bukkit.entity.Player;
import pl.htgmc.htgrodo.api.DataType;

public class PrivacyPolicyEngine {

    /**
     * Sprawdza, czy gracz ma prawo widzieć określone dane.
     */
    public boolean canSee(Player player, DataType type) {
        if (player == null) return false;

        switch (type) {
            case IP:
                return player.hasPermission("htg.rodo.view.ip");

            case UUID:
                return player.hasPermission("htg.rodo.view.uuid");

            case RAW_DATA:
                return player.hasPermission("htg.rodo.view.raw");

            default:
                return false;
        }
    }

    /**
     * Czy gracz omija filtrowanie cenzury?
     */
    public boolean hasBypass(Player player) {
        return player != null && player.hasPermission("htg.rodo.bypass");
    }
}
