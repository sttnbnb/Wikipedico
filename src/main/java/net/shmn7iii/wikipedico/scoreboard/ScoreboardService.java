package net.shmn7iii.wikipedico.scoreboard;

import net.shmn7iii.wikipedico.Wikipedico;
import net.shmn7iii.wikipedico.game.GameStatus;
import net.shmn7iii.wikipedico.player.WPlayer;
import net.shmn7iii.wikipedico.team.TeamColor;
import net.shmn7iii.wikipedico.team.TeamManager;
import net.shmn7iii.wikipedico.team.WTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

public class ScoreboardService {

    private final Wikipedico plugin;
    private final Map<UUID, Scoreboard> boards = new HashMap<>();

    public ScoreboardService(Wikipedico plugin) {
        this.plugin = plugin;
    }

    public void update() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Scoreboard board = boards.computeIfAbsent(player.getUniqueId(), k -> {
                Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
                player.setScoreboard(sb);
                return sb;
            });
            renderBoard(board, player);
        }

        // 退出プレイヤーのボードを削除
        boards.keySet().removeIf(uuid -> Bukkit.getPlayer(uuid) == null);
    }

    private void renderBoard(Scoreboard board, Player player) {
        GameStatus status = plugin.getGameStatus();

        // 既存 objective を削除して再生成
        Objective old = board.getObjective("wiki_sidebar");
        if (old != null) old.unregister();

        Objective obj = board.registerNewObjective("wiki_sidebar", Criteria.DUMMY, "§6§lWikipedico");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        List<String> lines = buildLines(status, player);
        int score = lines.size();
        Set<String> used = new HashSet<>();
        for (String line : lines) {
            // スコアボードは同じ文字列を重複登録できないため末尾にゼロ幅文字を足す
            String key = dedup(line, used);
            used.add(key);
            obj.getScore(key).setScore(score--);
        }
    }

    private List<String> buildLines(GameStatus status, Player player) {
        List<String> lines = new ArrayList<>();
        TeamManager tm = plugin.getTeamManager();

        switch (status) {
            case LOBBY -> {
                lines.add("§7チームに参加してください");
                lines.add("§8-----------");
                for (TeamColor color : TeamColor.values()) {
                    WTeam team = tm.getTeam(color.getId());
                    if (team == null) continue;
                    int size = team.size();
                    int max = plugin.getConfigManager().teamMaxPlayer();
                    lines.add(color.getChatColor() + color.getDisplayName() + " §f" + size + "/" + max);
                }
            }
            case PREPARING -> {
                lines.add("§e準備中...");
                lines.add("§8-----------");
                lines.add("§7間もなく開始します");
            }
            case PLAYING -> {
                int survivors = (int) plugin.getPlayerManager().getAll().stream()
                    .filter(wp -> wp.getStatus() == net.shmn7iii.wikipedico.player.PlayerStatus.ALIVE)
                    .count();
                int aliveTeams = tm.aliveTeams().size();
                lines.add("§a生存者");
                lines.add("§f" + survivors + "人");
                lines.add("§8-----------");
                lines.add("§a残チーム");
                lines.add("§f" + aliveTeams + "チーム");
                WPlayer wp = plugin.getPlayerManager().get(player);
                if (wp != null) {
                    lines.add("§8-----------");
                    lines.add("§eKill: §f" + wp.getKills());
                }
            }
            case ENDING -> {
                lines.add("§cゲーム終了");
            }
        }
        return lines;
    }

    private String dedup(String line, Set<String> used) {
        String candidate = line;
        int i = 0;
        while (used.contains(candidate)) {
            candidate = line + ChatColor.RESET.toString().repeat(++i);
        }
        return candidate;
    }

    public void removeBoard(UUID uuid) {
        boards.remove(uuid);
    }
}
