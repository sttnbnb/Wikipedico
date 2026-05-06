package net.shmn7iii.wikipedico.scoreboard;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.shmn7iii.wikipedico.Wikipedico;
import net.shmn7iii.wikipedico.game.GameStatus;
import net.shmn7iii.wikipedico.player.PlayerStatus;
import net.shmn7iii.wikipedico.player.WPlayer;
import org.bukkit.entity.Player;

public class ActionBarService {

    private final Wikipedico plugin;

    public ActionBarService(Wikipedico plugin) {
        this.plugin = plugin;
    }

    public void update() {
        if (plugin.getGameStatus() != GameStatus.PLAYING) return;

        int survivors = (int) plugin.getPlayerManager().getAll().stream()
            .filter(wp -> wp.getStatus() == PlayerStatus.ALIVE)
            .count();
        int aliveTeams = plugin.getTeamManager().aliveTeams().size();

        String text = "§a生存: §f" + survivors + "人  §8|  §e残チーム: §f" + aliveTeams;

        for (Player p : plugin.getServer().getOnlinePlayers()) {
            WPlayer wp = plugin.getPlayerManager().get(p);
            if (wp == null) continue;
            if (wp.getStatus() == PlayerStatus.ALIVE || wp.getStatus() == PlayerStatus.SPECTATOR) {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(text));
            }
        }
    }
}
