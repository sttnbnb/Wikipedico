package net.shmn7iii.wikipedico;

import net.shmn7iii.wikipedico.utils.Utils;
import net.shmn7iii.wikipedico.utils.WorldBorderUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WGame {
    /**
     * Singleton pattern
     * usage: WGame.getInstance()
     */
    private static WGame wGame = new WGame();
    public Map<Player, WPlayer> playerMap = new HashMap<>();
    public StatusEnum status;
    public World world;

    private WGame() {
        this.status = StatusEnum.PREPARE;
        this.world = Bukkit.getServer().getWorld("world_battle");
    }

    public static WGame getInstance() {
        return wGame;
    }

    public void setStatus(StatusEnum _status) {
        this.status = _status;
        for (WPlayer wplayer : playerMap.values()) {
            // update scoreboard
            wplayer.wScoreBoard.upsertGameStatus();
        }
    }

    public void setWorld(World _world) {
        this.world = _world;
    }

    /**
     * 準備フェーズ
     */
    private void preStart() {
        this.status = StatusEnum.PROGRESS;

        // join or reload 時点で全員 ALIVE のはず
        // SURVIVAL or ADVENTURE -> ALIVE
        // CREATIVE or SPECTATOR -> SPECTATE
        for (Map.Entry<Player, WPlayer> entry : playerMap.entrySet()) {
            Player player = entry.getKey();
            WPlayer wplayer = entry.getValue();

            if (player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR)) {
                wplayer.makeSpectator();
            }

            // set scoreboard
            wplayer.wScoreBoard.upsertKillCount(0, wplayer.killCount);
        }

        // 参加者０ならエラー終了
        if (this.getAlivePlayers().isEmpty()) {
            Bukkit.broadcastMessage("error, no players");
            this.end("error");
            return;
        }

        // reset WorldBorder
        WorldBorderUtils.resetWorldBorder(this.world);

    }

    /**
     * ゲーム開始
     * PVP開始・降下開始
     */
    public void start() {
        preStart();

        // teleport players
        for (Player p : Bukkit.getOnlinePlayers()) {
            Location loc = Utils.getRandomLocation(this.world, -500, 500, -500, 500);
            p.teleport(loc);
        }
        
//        WorldBorderUtils.startWorldBorder(this.world, this.getAlivePlayers());

        Bukkit.broadcastMessage("start");

//        Timer timer = new Timer();
//        timer.schedule(worldBorderStartTimerTask, 10000);
    }

    /**
     * ゲーム終了
     *
     * @param winner
     */
    public void end(String winner) {
        status = StatusEnum.END;

        Bukkit.broadcastMessage("end, winner: " + winner);

        WorldBorderUtils.resetWorldBorder(world);
    }

    /**
     * 終了チェック
     *
     * @return 終了条件達成でendさせる
     */
    public void checkGameEnd() {
        ArrayList<WPlayer> alivePlayers = this.getAlivePlayers();

        if (alivePlayers.size() != 1) {
            return;
        }

        this.end(alivePlayers.get(0).player.getDisplayName());
    }

    /**
     * 生存者のリストを返却
     *
     * @return 生存しているWPlayer
     */
    public ArrayList<WPlayer> getAlivePlayers() {
        ArrayList<WPlayer> alivePlayers = new ArrayList<>();
        for (WPlayer wPlayer : this.playerMap.values()) {
            if (wPlayer.state == WPlayer.PlayerStateEnum.ALIVE) {
                alivePlayers.add(wPlayer);
            }
        }

        return alivePlayers;
    }

    /**
     * ゲームステータス
     * PREPARE:  試合前
     * PROGRESS: 試合中
     * END:      試合後
     */
    public enum StatusEnum {
        PREPARE,
        PROGRESS,
        END,
    }
//
//    private TimerTask prepareTimerTask = new TimerTask() {
//        public void run() {
//            start();
//        }
//    };
//
//    private TimerTask worldBorderStartTimerTask = new TimerTask() {
//        public void run() {
//            Bukkit.broadcastMessage("set world border");
//            wWorldBorder.startWorldBorder(world, getAlivePlayers());
//        }
//    };

}
