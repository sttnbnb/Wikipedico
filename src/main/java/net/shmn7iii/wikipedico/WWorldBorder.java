package net.shmn7iii.wikipedico;

import org.bukkit.World;
import org.bukkit.WorldBorder;

import java.util.ArrayList;
import java.util.Random;

public class WWorldBorder {
    private double borderRadiusMax = 200;
    private double borderRadiusMin = 20;
    private long borderTimeSecond = 300;
    private double borderDamageAmount = 1;

    public WWorldBorder() {
    }

    /**
     * ワールドボーダーを設定
     * 生存者のうちからランダムで一名をピックし中心とする
     *
     * @param world         対象ワールド
     * @param aliveWPlayers 生存者リスト
     */
    public void startWorldBorder(World world, ArrayList<WPlayer> aliveWPlayers) {
        WorldBorder wb = world.getWorldBorder();

        resetWorldBorder(world);

        int random = new Random().nextInt(aliveWPlayers.size());
        wb.setCenter(aliveWPlayers.get(random).player.getLocation());
        wb.setSize(borderRadiusMin, borderTimeSecond);
        wb.setDamageAmount(borderDamageAmount);
    }

    /**
     * ワールドボーダーをリセット
     *
     * @param world 対象ワールド
     */
    public void resetWorldBorder(World world) {
        world.getWorldBorder().setSize(borderRadiusMax, 0);
    }
}
