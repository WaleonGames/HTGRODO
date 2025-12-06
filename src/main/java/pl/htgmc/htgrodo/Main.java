package pl.htgmc.htgrodo;

import org.bukkit.plugin.java.JavaPlugin;

import pl.htgmc.htgrodo.censor.ChatFilter;
import pl.htgmc.htgrodo.censor.UserInputFilter;
import pl.htgmc.htgrodo.privacy.DataMasker;
import pl.htgmc.htgrodo.privacy.PrivacyPolicyEngine;
import pl.htgmc.htgrodo.audit.AuditLogger;
import pl.htgmc.htgrodo.listeners.ChatListener;
import pl.htgmc.htgrodo.listeners.CommandListener;
import pl.htgmc.htgrodo.api.RodoAPI;
import pl.htgmc.htgrodo.commands.RodoCommand;

public class Main extends JavaPlugin {

    private static Main instance;

    private ChatFilter chatFilter;
    private UserInputFilter inputFilter;
    private DataMasker masker;
    private PrivacyPolicyEngine policy;
    private AuditLogger audit;
    private RodoAPI api;

    // ===============================
    // Globalne ON/OFF filtracji
    // ===============================
    private boolean filteringEnabled = true;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        // Wczytaj status filtracji z config.yml
        this.filteringEnabled = getConfig().getBoolean("filters.enabled", true);

        // Inicjalizacja modułów
        this.masker = new DataMasker();
        this.chatFilter = new ChatFilter();
        this.inputFilter = new UserInputFilter();
        this.policy = new PrivacyPolicyEngine();
        this.audit = new AuditLogger();
        this.api = new RodoAPI(masker, chatFilter, inputFilter, policy, audit);

        // Rejestr listenerów
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new CommandListener(), this);

        // Rejestracja komendy /rodo
        getCommand("rodo").setExecutor(new RodoCommand());

        getLogger().info("[HTGRODO] Plugin uruchomiony!");
        getLogger().info("[HTGRODO] Filtry globalne: " + (filteringEnabled ? "Włączone" : "Wyłączone"));
    }

    @Override
    public void onDisable() {
        getLogger().info("[HTGRODO] Plugin wyłączony.");
    }

    public static Main get() {
        return instance;
    }

    public RodoAPI api() {
        return api;
    }

    // ===============================
    // SYSTEM WŁĄCZ / WYŁĄCZ FILTRY
    // ===============================

    public boolean isFilteringEnabled() {
        return filteringEnabled;
    }

    public void setFilteringEnabled(boolean value) {
        this.filteringEnabled = value;

        // zapisanie do configu
        getConfig().set("filters.enabled", value);
        saveConfig();

        getLogger().info("[HTGRODO] Globalna filtracja: " + (value ? "Włączona" : "Wyłączona"));
    }
}
