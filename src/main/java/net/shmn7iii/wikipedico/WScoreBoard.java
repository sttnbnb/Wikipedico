package net.shmn7iii.wikipedico;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class WScoreBoard {
    public ScoreboardManager manager;
    public Scoreboard scoreboard;
    public Objective scoreBoardObjective;

    public WScoreBoard(Player player) {
        this.manager = Bukkit.getScoreboardManager();
        this.scoreboard = this.manager.getNewScoreboard();
        this.scoreBoardObjective = this.scoreboard.registerNewObjective("wikipedico", Criteria.DUMMY, ChatColor.AQUA + "--- Wikipedico ---");
        this.scoreBoardObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        player.setScoreboard(this.scoreboard);
    }
    
    // ボードの登録
    public void register(Player player) {
        setPlayerName(player);
        upsertGameStatus();
    }

    // ボードの破棄
    public void unregister() {
        this.scoreBoardObjective.unregister();
    }

    public void setPlayerName(Player player) {
        int sortI = 1;

        this.addTitleScore(sortI, "PLAYER");
        this.addValueScore(sortI, player.getName());
        this.addDividerScore(sortI);
    }

    public void upsertGameStatus() {
        WGame wGame = WGame.getInstance();
        int sortI = 2;

        this.addTitleScore(sortI, "GAME STATUS");

        String valueString = "";
        switch (wGame.status) {
            case PREPARE:
                valueString = "準備中";
                break;

            case PROGRESS:
                valueString = "ゲーム中";
                break;

            case END:
                valueString = "終了";
                break;
        }

        // old 持たせるのめんどいし三つしかないでこれで
        this.removeValueScore("準備中");
        this.removeValueScore("ゲーム中");
        this.removeValueScore("終了");

        this.addValueScore(sortI, valueString);
        this.addDividerScore(sortI);
    }

    public void upsertKillCount(Integer oldKillCount, Integer newKillCount) {
        int sortI = 3;

        this.addTitleScore(sortI, "KILL COUNT");
        this.removeValueScore(oldKillCount.toString());
        this.addValueScore(sortI, newKillCount.toString());
        this.addDividerScore(sortI);
    }

    private void addTitleScore(int sortI, String content) {
        String string = ChatColor.GREEN + content.toUpperCase();
        Score title = this.scoreBoardObjective.getScore(string);
        title.setScore(sortI * -10 - 1);
    }

    private void addValueScore(int sortI, String content) {
        String string = ChatColor.GRAY + " - " + ChatColor.RESET + content;
        Score score = this.scoreBoardObjective.getScore(string);
        score.setScore(sortI * -10 - 2);
    }

    private void removeValueScore(String content) {
        String string = ChatColor.GRAY + " - " + ChatColor.RESET + content;
        this.scoreboard.resetScores(string);
    }

    private void addDividerScore(int sortI) {
        String string = StringUtils.repeat(" ", sortI);
        Score score = this.scoreBoardObjective.getScore(string);
        score.setScore(sortI * -10 - 3);
    }
}
