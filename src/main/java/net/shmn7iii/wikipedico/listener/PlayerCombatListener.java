package net.shmn7iii.wikipedico.listener;

import net.shmn7iii.wikipedico.Wikipedico;
import net.shmn7iii.wikipedico.game.GameStatus;
import net.shmn7iii.wikipedico.player.PlayerStatus;
import net.shmn7iii.wikipedico.player.WPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerCombatListener implements Listener {

    private final Wikipedico plugin;

    public PlayerCombatListener(Wikipedico plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (plugin.getGameStatus() != GameStatus.PLAYING) return;

        Player victim = event.getEntity();
        Player killer = event.getEntity().getKiller();

        event.setDeathMessage(null);
        plugin.getGameManager().onDeath(victim, killer);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        WPlayer wp = plugin.getPlayerManager().get(player);
        if (wp == null) return;

        if (wp.getStatus() == PlayerStatus.DEAD) {
            var deathSpawn = plugin.getConfigManager().deathSpawn();
            if (deathSpawn != null) event.setRespawnLocation(deathSpawn);

            plugin.getServer().getScheduler().runTask(plugin, () ->
                player.setGameMode(GameMode.SPECTATOR)
            );
        }
    }
}
