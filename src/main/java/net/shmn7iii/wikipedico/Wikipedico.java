package net.shmn7iii.wikipedico;

import org.bukkit.plugin.java.JavaPlugin;

public final class Wikipedico extends JavaPlugin {

    private static Wikipedico instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getLogger().info("Wikipedico v" + getDescription().getVersion() + " enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Wikipedico disabled.");
    }

    public static Wikipedico getInstance() {
        return instance;
    }
}
