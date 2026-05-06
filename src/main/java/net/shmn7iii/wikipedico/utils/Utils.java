package net.shmn7iii.wikipedico.utils;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Random;

public class Utils {
    public static Location getRandomLocation(World world, int minX, int maxX, int minZ, int maxZ) {
        Random random = new Random();
        
        int x = random.nextInt(maxX - (minX) + 1) + (minX);
        int z = random.nextInt(maxZ - (minZ) + 1) + (minZ);
        int y = world.getHighestBlockAt(x, z).getY();

        return new Location(world, x, y, z);
    }
}
