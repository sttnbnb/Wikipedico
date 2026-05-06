package net.shmn7iii.wikipedico.worldborder;

import net.shmn7iii.wikipedico.Wikipedico;
import net.shmn7iii.wikipedico.config.BorderStage;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class WorldBorderController {

    private final Wikipedico plugin;
    private final List<BukkitTask> tasks = new ArrayList<>();

    public WorldBorderController(Wikipedico plugin) {
        this.plugin = plugin;
    }

    public void start() {
        List<BorderStage> stages = plugin.getConfigManager().borderStages();
        if (stages.isEmpty()) return;

        long delayTicks = plugin.getConfigManager().borderStartDelaySeconds() * 20L;
        long[] cursor = {delayTicks};

        for (BorderStage stage : stages) {
            final double range = stage.range();
            final long timeSec = stage.timeSeconds();
            long fireTick = cursor[0];

            BukkitTask task = plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                World world = Bukkit.getWorlds().get(0);
                if (world == null) return;
                WorldBorder border = world.getWorldBorder();
                border.setSize(range, timeSec);
                Bukkit.broadcastMessage("§6>Border §rワールドボーダーが §e" + (int) range + " §rブロックに変化しています。");
            }, fireTick);

            tasks.add(task);
            cursor[0] += timeSec * 20L;
        }
    }

    public void stop() {
        for (BukkitTask task : tasks) {
            task.cancel();
        }
        tasks.clear();

        World world = Bukkit.getWorlds().isEmpty() ? null : Bukkit.getWorlds().get(0);
        if (world != null) {
            world.getWorldBorder().reset();
        }
    }
}
