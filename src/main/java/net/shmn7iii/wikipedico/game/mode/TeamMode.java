package net.shmn7iii.wikipedico.game.mode;

import net.shmn7iii.wikipedico.Wikipedico;
import net.shmn7iii.wikipedico.game.GameContext;
import net.shmn7iii.wikipedico.player.WPlayer;
import net.shmn7iii.wikipedico.team.WTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Set;

public class TeamMode implements GameMode {

    @Override
    public String getId() { return "team"; }

    @Override
    public String getDisplayName() { return "チーム戦"; }

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
        Set<WTeam> alive = plugin.getTeamManager().aliveTeams();
        if (alive.size() > 1) return Optional.empty();

        if (!alive.isEmpty()) {
            WTeam winner = alive.iterator().next();
            return Optional.of(winner.getColor().getChatColor() + winner.getColor().getDisplayName()
                + "チーム §r§lの勝利！");
        }
        return Optional.of("§7引き分け");
    }
}
