package net.shmn7iii.wikipedico.game;

import net.shmn7iii.wikipedico.Wikipedico;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;

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
            Title title = Title.title(
                Component.text("§e" + remaining),
                Component.text("§7ゲーム開始まで"),
                Title.Times.times(Duration.ofMillis(200), Duration.ofMillis(600), Duration.ofMillis(200))
            );
            plugin.getServer().getOnlinePlayers().forEach(p -> {
                p.showTitle(title);
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            });
        }

        remaining--;
    }
}
