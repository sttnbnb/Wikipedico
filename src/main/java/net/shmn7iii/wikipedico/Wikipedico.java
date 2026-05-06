package net.shmn7iii.wikipedico;

import net.shmn7iii.wikipedico.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Wikipedico extends JavaPlugin {
    public Wikipedico plugin;
    public String version = Wikipedico.class.getPackage().getImplementationVersion();

    @Override
    public void onEnable() {
        plugin = this;

        // register command/event
        CommandHandler commandHandler = new CommandHandler();
        getCommand("wikipedico").setExecutor(commandHandler);
        getCommand("wikipedico").setTabCompleter(commandHandler);
        getServer().getPluginManager().registerEvents(new EventHandler(this), this);

        // create battle world
        World battleWorld = WorldUtils.generateBattleWorld();

        // set gamerule
        WorldUtils.setGameRule(plugin.getServer().getWorld("world"));
        WorldUtils.setGameRule(battleWorld);

        // generate wgame instance
        WGame wgame = WGame.getInstance();
        wgame.setWorld(battleWorld);

        // create wplayer
        for (Player player : Bukkit.getOnlinePlayers()) {
            WPlayer.registerWPlayer(player);
        }

        // config
        saveDefaultConfig();

        getLogger().info("Hello!");
        getLogger().info("Current version is v" + version);
    }

    @Override
    public void onDisable() {
        // return to default world
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.spigot().respawn();
            p.teleport(new Location(plugin.getServer().getWorld("world"), getConfig().getInt("lobbyLocationX"), getConfig().getInt("lobbyLocationY"), getConfig().getInt("lobbyLocationZ")));
        }

        // delete battle world
        WorldUtils.deleteBattleWorld();
    }
}
