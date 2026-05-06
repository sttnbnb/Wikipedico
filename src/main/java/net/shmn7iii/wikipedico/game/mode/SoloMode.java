package net.shmn7iii.wikipedico.game.mode;

import net.shmn7iii.wikipedico.Wikipedico;
import net.shmn7iii.wikipedico.game.GameContext;
import net.shmn7iii.wikipedico.player.PlayerStatus;
import net.shmn7iii.wikipedico.player.WPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

public class SoloMode implements GameMode {

    @Override
    public String getId() { return "solo"; }

    @Override
    public String getDisplayName() { return "個人戦"; }

    @Override
    public void onDeath(Player victim, Player killer, Wikipedico plugin, GameContext context) {
        if (killer != null) {
            context.addKill(killer.getUniqueId());
            WPlayer killerWp = plugin.getPlayerManager().get(killer);
            if (killerWp != null) killerWp.addKill();
            Bukkit.broadcastMessage("§b" + killer.getName() + " §r✈► §c" + victim.getName());
        } else {
            Bukkit.broadcastMessage("§c" + victim.getName() + " §rが死亡しました。");
        }
    }

    @Override
    public Optional<String> checkVictory(Wikipedico plugin) {
        long alive = plugin.getPlayerManager().getAll().stream()
            .filter(wp -> wp.getStatus() == PlayerStatus.ALIVE)
            .count();
        if (alive > 1) return Optional.empty();

        if (alive == 1) {
            WPlayer winner = plugin.getPlayerManager().getAll().stream()
                .filter(wp -> wp.getStatus() == PlayerStatus.ALIVE)
                .findFirst().orElse(null);
            if (winner != null) {
                Player p = Bukkit.getPlayer(winner.getUuid());
                String name = p != null ? p.getName() : winner.getUuid().toString().substring(0, 8);
                return Optional.of("§e" + name + " §r§lの勝利！");
            }
        }
        return Optional.of("§7引き分け");
    }
}
