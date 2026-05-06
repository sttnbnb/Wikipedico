package net.shmn7iii.wikipedico;

import net.shmn7iii.wikipedico.controlpanel.ControlPanel;
import net.shmn7iii.wikipedico.utils.WorldUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkLoadEvent;

public class EventHandler implements Listener {
    public static Wikipedico plugin;

    public EventHandler(Wikipedico instance) {
        plugin = instance;
    }

    @org.bukkit.event.EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        WPlayer.registerWPlayer(player);
    }

    @org.bukkit.event.EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        WPlayer.removeWPlayer(player);

        WGame wgame = WGame.getInstance();
        wgame.checkGameEnd();
    }

    @org.bukkit.event.EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent e) {
        WGame wgame = WGame.getInstance();
        if (wgame.status != WGame.StatusEnum.PROGRESS) {
            return;
        }

        WPlayer victimWPLayer = wgame.playerMap.get(e.getEntity()); // nullなら？
        victimWPLayer.dead();

        if (e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();
            WPlayer killerWPLayer = wgame.playerMap.get(killer);
            killerWPLayer.kill();
        }

        wgame.checkGameEnd();
    }

    @org.bukkit.event.EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
        // Open control panel
        if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.NETHER_STAR) && (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            ControlPanel.getInstance().open(e.getPlayer());
        }
    }

    @org.bukkit.event.EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        ControlPanel.getInstance().clickItemEvent(e);
    }

    @org.bukkit.event.EventHandler
    public void onChunkLoadEvent(ChunkLoadEvent e) {
        if (!e.isNewChunk()) return;
        if (!e.getChunk().getWorld().getName().equals("world_battle")) return;

        WorldUtils.spawnChests(e.getChunk());
    }
}
