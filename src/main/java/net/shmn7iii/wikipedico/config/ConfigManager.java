package net.shmn7iii.wikipedico.config;

import net.shmn7iii.wikipedico.Wikipedico;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private final Wikipedico plugin;

    public ConfigManager(Wikipedico plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        plugin.reloadConfig();
    }

    private FileConfiguration cfg() {
        return plugin.getConfig();
    }

    public int teamMaxPlayer() {
        return cfg().getInt("game.teamMaxPlayer", 3);
    }

    public int preparationTime() {
        return cfg().getInt("game.preparationTime", 30);
    }

    public int countDownTime() {
        return cfg().getInt("game.countDownTime", 5);
    }

    public int killRankingTimes() {
        return cfg().getInt("game.killRankingTimes", 3);
    }

    public int borderStartDelaySeconds() {
        return cfg().getInt("worldBorder.startDelaySeconds", 15);
    }

    public List<BorderStage> borderStages() {
        List<BorderStage> stages = new ArrayList<>();
        List<?> list = cfg().getList("worldBorder.stages");
        if (list == null) return stages;
        for (Object obj : list) {
            if (obj instanceof java.util.Map<?, ?> raw) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) raw;
                double range = ((Number) map.getOrDefault("range", 1000)).doubleValue();
                long time = ((Number) map.getOrDefault("time", 60)).longValue();
                stages.add(new BorderStage(range, time));
            }
        }
        return stages;
    }

    public Location lobbySpawn() {
        return readLocation("locations.lobbySpawn");
    }

    public Location deathSpawn() {
        return readLocation("locations.deathSpawn");
    }

    public Location skySpawn() {
        return readLocation("locations.skySpawn");
    }

    private Location readLocation(String path) {
        ConfigurationSection sec = cfg().getConfigurationSection(path);
        String worldName = cfg().getString("locations.world", "world");
        World world = Bukkit.getWorld(worldName);
        if (sec == null || world == null) {
            return world != null ? world.getSpawnLocation() : null;
        }
        double x = sec.getDouble("x", 0);
        double y = sec.getDouble("y", 64);
        double z = sec.getDouble("z", 0);
        float yaw = (float) sec.getDouble("yaw", 0);
        float pitch = (float) sec.getDouble("pitch", 0);
        return new Location(world, x, y, z, yaw, pitch);
    }
}
