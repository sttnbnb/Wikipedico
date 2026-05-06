package net.shmn7iii.wikipedico.team;

import net.shmn7iii.wikipedico.config.ConfigManager;
import net.shmn7iii.wikipedico.player.PlayerManager;
import net.shmn7iii.wikipedico.player.PlayerStatus;
import net.shmn7iii.wikipedico.player.WPlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamManager {

    private final Map<String, WTeam> teams = new LinkedHashMap<>();
    private final ConfigManager config;
    private final PlayerManager playerManager;

    public TeamManager(ConfigManager config, PlayerManager playerManager) {
        this.config = config;
        this.playerManager = playerManager;
        for (TeamColor color : TeamColor.values()) {
            teams.put(color.getId(), new WTeam(color));
        }
    }

    public JoinResult join(Player player, TeamColor color) {
        WPlayer wp = playerManager.get(player);
        if (wp == null) return JoinResult.NOT_REGISTERED;

        WTeam team = teams.get(color.getId());
        if (team.size() >= config.teamMaxPlayer()) return JoinResult.TEAM_FULL;

        // 現在のチームから離脱
        leave(player);

        team.addMember(player.getUniqueId());
        wp.setTeamId(color.getId());
        wp.setJoined(true);
        return JoinResult.SUCCESS;
    }

    public void leave(Player player) {
        WPlayer wp = playerManager.get(player);
        if (wp == null) return;
        if (wp.getTeamId() != null) {
            WTeam team = teams.get(wp.getTeamId());
            if (team != null) team.removeMember(player.getUniqueId());
        }
        wp.setTeamId(null);
        wp.setJoined(false);
    }

    public WTeam getTeam(String id) {
        return teams.get(id);
    }

    public Collection<WTeam> getAllTeams() {
        return Collections.unmodifiableCollection(teams.values());
    }

    public Set<WTeam> aliveTeams() {
        Set<WTeam> alive = new HashSet<>();
        for (WTeam team : teams.values()) {
            for (UUID uuid : team.getMembers()) {
                WPlayer wp = playerManager.get(uuid);
                if (wp != null && wp.getStatus() == PlayerStatus.ALIVE) {
                    alive.add(team);
                    break;
                }
            }
        }
        return alive;
    }

    public void resetAll() {
        List<WTeam> snapshot = new ArrayList<>(teams.values());
        for (WTeam team : snapshot) {
            team.getMembers().forEach(uuid -> {
                WPlayer wp = playerManager.get(uuid);
                if (wp != null) {
                    wp.setTeamId(null);
                    wp.setJoined(false);
                }
            });
            teams.put(team.getId(), new WTeam(team.getColor()));
        }
    }

    public enum JoinResult {
        SUCCESS, NOT_REGISTERED, TEAM_FULL
    }
}
