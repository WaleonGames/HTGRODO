package pl.htgmc.htgrodo.audit;

import org.bukkit.Bukkit;
import pl.htgmc.htgrodo.Main;
import pl.htgmc.htgrodo.utils.FileUtil;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLogger {

    private final File auditFile;

    public AuditLogger() {
        File folder = new File(Main.get().getDataFolder(), "logs");
        if (!folder.exists()) folder.mkdirs();

        this.auditFile = new File(folder, "audit.json");
    }

    /**
     * Zapis pojedynczego zdarzenia audytu.
     * Format JSON – jedno zdarzenie na linię.
     */
    public void log(String user, String action, String detail) {
        try {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String json = "{"
                    + "\"time\":\"" + timestamp + "\","
                    + "\"user\":\"" + escape(user) + "\","
                    + "\"action\":\"" + escape(action) + "\","
                    + "\"detail\":\"" + escape(detail) + "\""
                    + "}";

            FileUtil.append(auditFile, json);

        } catch (Exception e) {
            Bukkit.getLogger().warning("[HTGRODO] Błąd przy zapisie audytu: " + e.getMessage());
        }
    }

    /**
     * Zabezpieczenie przed napotkaniem cudzysłowów itp.
     */
    private String escape(String text) {
        return text
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");
    }
}
