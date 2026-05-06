package net.shmn7iii.wikipedico;

import net.shmn7iii.wikipedico.command.WikipedicoCommand;
import net.shmn7iii.wikipedico.command.sub.*;
import net.shmn7iii.wikipedico.config.ConfigManager;
import net.shmn7iii.wikipedico.game.GameManager;
import net.shmn7iii.wikipedico.game.GameStatus;
import net.shmn7iii.wikipedico.listener.PlayerCombatListener;
import net.shmn7iii.wikipedico.listener.PlayerLifecycleListener;
import net.shmn7iii.wikipedico.player.PlayerManager;
import net.shmn7iii.wikipedico.team.TeamManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Wikipedico extends JavaPlugin {

    private static Wikipedico instance;

    private ConfigManager configManager;
    private PlayerManager playerManager;
    private TeamManager teamManager;
    private GameManager gameManager;
    private GameStatus gameStatus = GameStatus.LOBBY;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        configManager = new ConfigManager(this);
        playerManager = new PlayerManager();
        teamManager = new TeamManager(configManager, playerManager);
        gameManager = new GameManager(this);

        WikipedicoCommand cmd = new WikipedicoCommand();
        cmd.register(new StartSubCommand(this));
        cmd.register(new EndSubCommand(this));
        cmd.register(new JoinSubCommand(this));
        cmd.register(new AdminSubCommand());
        cmd.register(new RevivalSubCommand());
        cmd.register(new ReloadSubCommand(this));

        Objects.requireNonNull(getCommand("wikipedico")).setExecutor(cmd);
        Objects.requireNonNull(getCommand("wikipedico")).setTabCompleter(cmd);

        getServer().getPluginManager().registerEvents(new PlayerLifecycleListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerCombatListener(this), this);

        getLogger().info("Wikipedico v" + getDescription().getVersion() + " enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Wikipedico disabled.");
    }

    public static Wikipedico getInstance() { return instance; }

    public ConfigManager getConfigManager() { return configManager; }
    public PlayerManager getPlayerManager() { return playerManager; }
    public TeamManager getTeamManager() { return teamManager; }
    public GameManager getGameManager() { return gameManager; }

    public GameStatus getGameStatus() { return gameStatus; }
    public void setGameStatus(GameStatus status) { this.gameStatus = status; }
}
