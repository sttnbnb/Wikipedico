package net.shmn7iii.wikipedico;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WPlayer {
    public Player player;
    public Integer killCount;
    public PlayerStateEnum state;
    public RoleEnum role;
    public WScoreBoard wScoreBoard;

    /**
     * コンストラクタ
     * 直接は呼べず registerPlayer を経由する
     */
    private WPlayer(Player _player) {
        this.player = _player;
        this.state = PlayerStateEnum.SPECTATE;
        this.role = RoleEnum.PLAYER;
        this.killCount = 0;
        this.wScoreBoard = new WScoreBoard(this.player);
        this.wScoreBoard.register(_player);
    }

    public static void registerWPlayer(Player player) {
        WGame wgame = WGame.getInstance();
        WPlayer wplayer = new WPlayer(player);

        switch (wgame.status) {
            case PREPARE:
                wplayer.makeAlive();
                break;
            case PROGRESS:
            case END:
                wplayer.makeSpectator();
                break;
        }

        wgame.playerMap.put(player, wplayer);
    }

    public static void removeWPlayer(Player player) {
        WGame wgame = WGame.getInstance();
        WPlayer wPLayer = wgame.playerMap.get(player);

        wPLayer.dead();

        wgame.playerMap.remove(player);
    }

    /**
     * ロールはステータスに影響を与えない
     */
    public void makeAdmin() {
        this.role = RoleEnum.ADMIN;
        this.player.setGameMode(GameMode.CREATIVE);
        this.player.getInventory().setItemInMainHand(new ItemStack(Material.NETHER_STAR));
    }

    public void makeAlive() {
        this.state = PlayerStateEnum.ALIVE;
        this.player.setGameMode(GameMode.ADVENTURE);
    }

    public void makeDead() {
        this.state = PlayerStateEnum.DEAD;
        this.player.setGameMode(GameMode.SPECTATOR);
    }

    public void makeSpectator() {
        this.state = PlayerStateEnum.SPECTATE;
        this.player.setGameMode(GameMode.SPECTATOR);
    }

    /**
     * Player kills someone.
     */
    public void kill() {
        int oldKillCount = this.killCount;
        this.killCount++;
        this.wScoreBoard.upsertKillCount(oldKillCount, this.killCount);
    }

    /**
     * Player has killed by someone.
     */
    public void dead() {
        this.makeDead();
    }

    /**
     * Player has revived by someone.
     */
    public void revived() {
        this.makeAlive();
    }

    public enum PlayerStateEnum {
        ALIVE,    // 生存中
        DEAD,     // 死亡
        SPECTATE  // 観戦
    }

    public enum RoleEnum {
        ADMIN,    // 運営
        PLAYER    // 一般
    }
}
