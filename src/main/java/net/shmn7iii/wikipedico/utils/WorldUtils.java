package net.shmn7iii.wikipedico.utils;

import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class WorldUtils {
    public static World generateBattleWorld() {
        WorldCreator wc = new WorldCreator("world_battle");
        wc.environment(World.Environment.NORMAL);
        wc.type(WorldType.NORMAL);
        World world = wc.createWorld();

        return world;
    }

    public static void deleteBattleWorld() {
        World oldWorld = Bukkit.getServer().getWorld("world_battle");
        if (oldWorld != null) {
            Bukkit.getServer().unloadWorld(oldWorld, false);
            File file = oldWorld.getWorldFolder();
            try {
                FileUtils.deleteDirectory(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public static void spawnChests(Chunk chunk) {
        // 3チャンクに一つ
        if (!(chunk.getX() % 3 == 0 && chunk.getZ() % 3 == 0)) return;

        World world = chunk.getWorld();

        int x = (chunk.getX() << 4) + ThreadLocalRandom.current().nextInt(16);
        int z = (chunk.getZ() << 4) + ThreadLocalRandom.current().nextInt(16);
        int y = world.getHighestBlockYAt(x, z);

        Block block = world.getBlockAt(x, y, z);
        block.getRelative(BlockFace.UP).setType(Material.CHEST);
    }

    public static void setGameRule(World world) {
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        world.setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, false);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.FALL_DAMAGE, false);
        world.setGameRule(GameRule.KEEP_INVENTORY, true);
        world.setGameRule(GameRule.SPAWN_RADIUS, 1);
        world.setStorm(false);
        world.setTime(6000);
    }
}
