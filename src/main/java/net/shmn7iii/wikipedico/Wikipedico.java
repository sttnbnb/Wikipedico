package net.shmn7iii.wikipedico;

import net.shmn7iii.wikipedico.command.WikipedicoCommand;
import net.shmn7iii.wikipedico.command.sub.*;
import net.shmn7iii.wikipedico.config.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Wikipedico extends JavaPlugin {

    private static Wikipedico instance;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        configManager = new ConfigManager(this);

        WikipedicoCommand cmd = new WikipedicoCommand();
        cmd.register(new StartSubCommand());
        cmd.register(new EndSubCommand());
        cmd.register(new JoinSubCommand());
        cmd.register(new AdminSubCommand());
        cmd.register(new RevivalSubCommand());
        cmd.register(new ReloadSubCommand(this));

        Objects.requireNonNull(getCommand("wikipedico")).setExecutor(cmd);
        Objects.requireNonNull(getCommand("wikipedico")).setTabCompleter(cmd);

        getLogger().info("Wikipedico v" + getDescription().getVersion() + " enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Wikipedico disabled.");
    }

    public static Wikipedico getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
