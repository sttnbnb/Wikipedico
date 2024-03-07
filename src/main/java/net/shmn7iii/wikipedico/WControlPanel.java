package net.shmn7iii.wikipedico;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class WControlPanel {
    public Inventory controlPanelInventory = Bukkit.createInventory(null,18, "Control Panel");
    public ItemStack[] controlPanelInventoryItemStacks = {
            new WControlPanelItem(Material.IRON_SWORD, "GAME START").itemStack,
            new WControlPanelItem(Material.IRON_SWORD, "DEBUG").itemStack,
            new WControlPanelItem(Material.BARRIER, "GAME END").itemStack,
    };

    // Singleton
    private static WControlPanel wControlPanel = new WControlPanel();
    private WControlPanel() {
        controlPanelInventory.setContents(controlPanelInventoryItemStacks);
    }
    public static WControlPanel getInstance() {
        return wControlPanel;
    }

    // usage: WControlPanel.getInstance().open(player)
    public void open(Player _player) {
        _player.openInventory(this.controlPanelInventory);
    }

    public void clickItemEvent(InventoryClickEvent e) {
        if (!Arrays.asList(this.controlPanelInventoryItemStacks).contains(e.getCurrentItem())) { return; }

        e.setCancelled(true);
        e.getWhoClicked().closeInventory();

        switch (e.getCurrentItem().getItemMeta().getDisplayName()) {
            case "GAME START":
                WGame.getInstance().prepare(e.getWhoClicked().getWorld());
                break;
            case "GAME END":
                WGame.getInstance().end(e.getWhoClicked().getName());
                break;
            case "DEBUG":
                WorldCreator wc = new WorldCreator("debug");

                wc.environment(World.Environment.NORMAL);
                wc.type(WorldType.NORMAL);

                wc.createWorld();

                break;
        }
    }
}
