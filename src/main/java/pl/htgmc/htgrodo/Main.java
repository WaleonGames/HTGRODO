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

    private boolean filteringEnabled = true;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;

        printLogo(); // ASCII logo jak LuckPerms

        log("Loading configuration...");
        saveDefaultConfig();
        this.filteringEnabled = getConfig().getBoolean("filters.enabled", true);

        log("Loading modules...");
        this.masker = new DataMasker();
        this.chatFilter = new ChatFilter();
        this.inputFilter = new UserInputFilter();
        this.policy = new PrivacyPolicyEngine();
        this.audit = new AuditLogger();
        this.api = new RodoAPI(masker, chatFilter, inputFilter, policy, audit);

        log("Registering listeners...");
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new CommandListener(), this);

        log("Registering commands...");
        getCommand("rodo").setExecutor(new RodoCommand());

        long took = System.currentTimeMillis() - start;
        log("Successfully enabled. (took " + took + "ms)");
    }

    @Override
    public void onDisable() {
        log("Shutting down...");
        log("Successfully disabled.");
    }

    public static Main get() {
        return instance;
    }

    public RodoAPI api() {
        return api;
    }

    public boolean isFilteringEnabled() {
        return filteringEnabled;
    }

    public void setFilteringEnabled(boolean value) {
        this.filteringEnabled = value;
        getConfig().set("filters.enabled", value);
        saveConfig();
        log("Global filtering state updated: " + (value ? "ENABLED" : "DISABLED"));
    }

    // ===============================
    //  LOG STYL LUCKPERMS
    // ===============================

    private void log(String msg) {
        getLogger().info(msg);
    }

    private void printLogo() {
        log(" ");
        log("     __  __ ______ ____   ____   ___   ____   ___   ");
        log("    |  \\/  |  ____|  _ \\ / __ \\ / _ \\ / __ \\ / _ \\  ");
        log("    | \\  / | |__  | |_) | |  | | | | | |  | | | | | ");
        log("    | |\\/| |  __| |  _ <| |  | | | | | |  | | | | | ");
        log("    | |  | | |____| |_) | |__| | |_| | |__| | |_| | ");
        log("    |_|  |_|______|____/ \\____/ \\___/ \\____/ \\___/  ");
        log(" ");
        log("HTGRODO v1.0 - Privacy & Data Protection Layer");
        log("Running on: Bukkit/Paper");
        log(" ");
    }
}
