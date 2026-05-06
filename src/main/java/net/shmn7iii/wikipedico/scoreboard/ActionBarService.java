package net.shmn7iii.wikipedico.scoreboard;

import net.shmn7iii.wikipedico.Wikipedico;
import net.shmn7iii.wikipedico.game.GameStatus;
import net.shmn7iii.wikipedico.player.PlayerStatus;
import net.shmn7iii.wikipedico.player.WPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class ActionBarService {

    private final Wikipedico plugin;

    public ActionBarService(Wikipedico plugin) {
        this.plugin = plugin;
    }

    public void update() {
        GameStatus status = plugin.getGameStatus();
        if (status != GameStatus.PLAYING) return;

        int survivors = (int) plugin.getPlayerManager().getAll().stream()
            .filter(wp -> wp.getStatus() == PlayerStatus.ALIVE)
            .count();
        int aliveTeams = plugin.getTeamManager().aliveTeams().size();

        Component bar = Component.text("生存: ", NamedTextColor.GRAY)
            .append(Component.text(survivors + "人", NamedTextColor.GREEN))
            .append(Component.text("  |  ", NamedTextColor.DARK_GRAY))
            .append(Component.text("残チーム: ", NamedTextColor.GRAY))
            .append(Component.text(aliveTeams + "", NamedTextColor.YELLOW));

        for (Player p : plugin.getServer().getOnlinePlayers()) {
            WPlayer wp = plugin.getPlayerManager().get(p);
            if (wp == null) continue;
            if (wp.getStatus() == PlayerStatus.ALIVE || wp.getStatus() == PlayerStatus.SPECTATOR) {
                p.sendActionBar(bar);
            }
        }
    }
}
