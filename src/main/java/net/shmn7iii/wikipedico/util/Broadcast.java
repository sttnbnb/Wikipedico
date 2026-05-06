package net.shmn7iii.wikipedico.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class Broadcast {

    private Broadcast() {}

    public static void game(String msg) {
        Bukkit.broadcastMessage("§a>Game §r" + msg);
    }

    public static void border(String msg) {
        Bukkit.broadcastMessage("§6>Border §r" + msg);
    }

    public static void error(String msg) {
        Bukkit.broadcastMessage("§c>Error §r" + msg);
    }

    public static void title(String title, String subtitle) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(title, subtitle, 10, 40, 10);
        }
    }
}
