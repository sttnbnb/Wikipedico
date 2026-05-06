package net.shmn7iii.wikipedico.listener;

import net.shmn7iii.wikipedico.Wikipedico;
import net.shmn7iii.wikipedico.game.GameStatus;
import net.shmn7iii.wikipedico.player.WPlayer;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLifecycleListener implements Listener {

    private final Wikipedico plugin;

    public PlayerLifecycleListener(Wikipedico plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        WPlayer wp = plugin.getPlayerManager().register(player);

        var lobbySpawn = plugin.getConfigManager().lobbySpawn();
        if (lobbySpawn != null) {
            player.teleport(lobbySpawn);
        }
        player.setGameMode(GameMode.ADVENTURE);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        var player = event.getPlayer();
        var gameStatus = plugin.getGameStatus();

        plugin.getTeamManager().leave(player);

        if (gameStatus == GameStatus.PLAYING) {
            var wp = plugin.getPlayerManager().get(player);
            if (wp != null && wp.getStatus() == net.shmn7iii.wikipedico.player.PlayerStatus.ALIVE) {
                plugin.getGameManager().onDeath(player, null);
            }
        }

        plugin.getPlayerManager().unregister(player);
    }
}
