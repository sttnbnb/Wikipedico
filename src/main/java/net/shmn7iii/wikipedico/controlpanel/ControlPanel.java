package net.shmn7iii.wikipedico.controlpanel;

import net.shmn7iii.wikipedico.WGame;
import net.shmn7iii.wikipedico.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static net.shmn7iii.wikipedico.EventHandler.plugin;

public class ControlPanel {
    // Singleton
    private static ControlPanel controlPanel = new ControlPanel();
    public Inventory controlPanelInventory = Bukkit.createInventory(null, 9, "Control Panel");
    // Map<slot, item>
    public Map<Integer, ItemStack> controlPanelInventoryItemStacks = new HashMap<Integer, ItemStack>() {
        {
            put(3, ControlPanelItem.StartSword());
            put(4, ControlPanelItem.EndBarrier());
            put(5, new ControlPanelItem(Material.WOODEN_PICKAXE, "DEBUG").itemStack);
        }
    };

    private ControlPanel() {
        for (Map.Entry<Integer, ItemStack> entry : controlPanelInventoryItemStacks.entrySet()) {
            Integer slot = entry.getKey();
            ItemStack item = entry.getValue();

            controlPanelInventory.setItem(slot, item);
        }
    }

    public static ControlPanel getInstance() {
        return controlPanel;
    }

    // usage: WControlPanel.getInstance().open(player)
    public void open(Player _player) {
        _player.openInventory(this.controlPanelInventory);
    }

    public void clickItemEvent(InventoryClickEvent e) {
        if (!this.controlPanelInventoryItemStacks.values().contains(e.getCurrentItem())) {
            return;
        }

        e.setCancelled(true);
        e.getWhoClicked().closeInventory();

        switch (e.getCurrentItem().getItemMeta().getDisplayName()) {
            case "GAME START":
                WGame.getInstance().start();
                break;
            case "GAME END":
                WGame.getInstance().end(e.getWhoClicked().getName());
                break;
            case "DEBUG":
                for (Player p : Bukkit.getOnlinePlayers()) {
                    WGame wgame = WGame.getInstance();
                    World world = wgame.world;
                    p.teleport(Utils.getRandomLocation(world, -500, 500, -500, 500));
                }
                break;
            case "DEBUG2":
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.teleport(new Location(plugin.getServer().getWorld("world"), 0, 75, 0));
                }
                break;
        }
    }
}
