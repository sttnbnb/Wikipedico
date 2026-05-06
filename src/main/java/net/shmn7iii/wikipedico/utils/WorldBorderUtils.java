package net.shmn7iii.wikipedico.utils;

import net.shmn7iii.wikipedico.WPlayer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;

import java.util.ArrayList;
import java.util.Random;

public class WorldBorderUtils {
    private static double borderRadiusMax = 1000;
    private static double borderRadiusMin = 20;
    private static long borderTimeSecond = 300;
    private static double borderDamageAmount = 1;
    
    /**
     * ワールドボーダーを設定
     * 生存者のうちからランダムで一名をピックし中心とする
     *
     * @param world         対象ワールド
     * @param aliveWPlayers 生存者リスト
     */
    public static void startWorldBorder(World world, ArrayList<WPlayer> aliveWPlayers) {
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
    public static void resetWorldBorder(World world) {
        WorldBorder wb = world.getWorldBorder();

        wb.setCenter(new Location(world, 0, 0, 0));
        wb.setSize(borderRadiusMax, 0);
    }
}
