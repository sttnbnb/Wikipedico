package net.shmn7iii.wikipedico.game;

import net.shmn7iii.wikipedico.Wikipedico;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class PreparationCountdown extends BukkitRunnable {

    private final Wikipedico plugin;
    private final GameManager gameManager;
    private int remaining;
    private final int countDownTime;

    public PreparationCountdown(Wikipedico plugin, GameManager gameManager, int preparationTime, int countDownTime) {
        this.plugin = plugin;
        this.gameManager = gameManager;
        this.remaining = preparationTime;
        this.countDownTime = countDownTime;
    }

    @Override
    public void run() {
        if (remaining <= 0) {
            cancel();
            gameManager.onPreparationFinished();
            return;
        }

        if (remaining <= countDownTime) {
            plugin.getServer().getOnlinePlayers().forEach(p -> {
                p.sendTitle("§e" + remaining, "§7ゲーム開始まで", 4, 12, 4);
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            });
        }

        remaining--;
    }
}
