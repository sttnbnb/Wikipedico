package net.shmn7iii.wikipedico.controlpanel;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ControlPanelItem {
    public ItemStack itemStack;
    
    public ControlPanelItem(Material material, String displayName) {
        this.itemStack = new ItemStack(material);

        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        this.itemStack.setItemMeta(itemMeta);
    }

    public static ItemStack StartSword() {
        return new ControlPanelItem(Material.IRON_SWORD, "GAME START").itemStack;
    }

    public static ItemStack EndBarrier() {
        return new ControlPanelItem(Material.BARRIER, "GAME END").itemStack;
    }
}
