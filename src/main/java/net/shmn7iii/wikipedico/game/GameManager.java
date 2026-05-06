package net.shmn7iii.wikipedico.game;

import net.shmn7iii.wikipedico.Wikipedico;
import net.shmn7iii.wikipedico.player.PlayerStatus;
import net.shmn7iii.wikipedico.player.WPlayer;
import net.shmn7iii.wikipedico.team.WTeam;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class GameManager {

    private final Wikipedico plugin;
    private final GameContext context = new GameContext();
    private BukkitTask countdownTask;

    public GameManager(Wikipedico plugin) {
        this.plugin = plugin;
    }

    public boolean startGame() {
        if (plugin.getGameStatus() != GameStatus.LOBBY) return false;

        plugin.setGameStatus(GameStatus.PREPARING);
        int prepTime = plugin.getConfigManager().preparationTime();
        int countDownTime = plugin.getConfigManager().countDownTime();

        countdownTask = new PreparationCountdown(plugin, this, prepTime, countDownTime)
            .runTaskTimer(plugin, 0L, 20L);

        plugin.getServer().broadcast(Component.text("§a>Game §rゲームを開始します。準備時間: §e" + prepTime + "秒"));
        return true;
    }

    public void onPreparationFinished() {
        plugin.setGameStatus(GameStatus.PLAYING);
        context.reset();

        Location skySpawn = plugin.getConfigManager().skySpawn();

        for (Player p : plugin.getServer().getOnlinePlayers()) {
            WPlayer wp = plugin.getPlayerManager().get(p);
            if (wp == null) continue;

            if (wp.isJoined()) {
                wp.setStatus(PlayerStatus.ALIVE);
                if (skySpawn != null) p.teleport(skySpawn);
                p.setGameMode(GameMode.SURVIVAL);
                p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 100, 4, false, false));
                p.getInventory().clear();
                // エリトラ装備
                p.getInventory().setChestplate(new org.bukkit.inventory.ItemStack(org.bukkit.Material.ELYTRA));
            } else {
                wp.setStatus(PlayerStatus.SPECTATOR);
                p.setGameMode(GameMode.SPECTATOR);
            }
        }

        Title startTitle = Title.title(
            Component.text("§a§lGAME START"),
            Component.empty(),
            Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(1500), Duration.ofMillis(500))
        );
        plugin.getServer().getOnlinePlayers().forEach(p -> {
            p.showTitle(startTitle);
            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 1f);
        });

        plugin.getServer().broadcast(Component.text("§a>Game §rゲーム開始！"));
    }

    public void endGame() {
        if (plugin.getGameStatus() == GameStatus.LOBBY) return;

        if (countdownTask != null) {
            countdownTask.cancel();
            countdownTask = null;
        }

        plugin.setGameStatus(GameStatus.ENDING);
        plugin.getServer().broadcast(Component.text("§c>Game §rゲーム終了！"));

        showRanking();

        // 10秒後にLOBBYへ
        plugin.getServer().getScheduler().runTaskLater(plugin, this::resetToLobby, 200L);
    }

    public void onDeath(Player victim, Player killer) {
        WPlayer wp = plugin.getPlayerManager().get(victim);
        if (wp == null) return;

        wp.setStatus(PlayerStatus.DEAD);

        if (killer != null) {
            context.addKill(killer.getUniqueId());
            WPlayer killerWp = plugin.getPlayerManager().get(killer);
            if (killerWp != null) killerWp.addKill();
            plugin.getServer().broadcast(
                Component.text("§b" + killer.getName() + " §r✈► §c" + victim.getName())
            );
        } else {
            plugin.getServer().broadcast(Component.text("§c" + victim.getName() + " §rが死亡しました。"));
        }

        checkVictory();
    }

    public void checkVictory() {
        if (plugin.getGameStatus() != GameStatus.PLAYING) return;

        Set<WTeam> alive = plugin.getTeamManager().aliveTeams();
        if (alive.size() <= 1) {
            if (!alive.isEmpty()) {
                WTeam winner = alive.iterator().next();
                plugin.getServer().broadcast(
                    Component.text(winner.getColor().getChatColor() + winner.getColor().getDisplayName()
                        + "チーム §r§lの勝利！")
                );
            }
            endGame();
        }
    }

    private void showRanking() {
        int top = plugin.getConfigManager().killRankingTimes();
        List<Map.Entry<UUID, Integer>> sorted = context.getAllKills().entrySet().stream()
            .sorted(Map.Entry.<UUID, Integer>comparingByValue().reversed())
            .limit(top)
            .collect(Collectors.toList());

        plugin.getServer().broadcast(Component.text("§6§l--- キルランキング ---"));
        for (int i = 0; i < sorted.size(); i++) {
            var entry = sorted.get(i);
            String name = Optional.ofNullable(plugin.getServer().getPlayer(entry.getKey()))
                .map(Player::getName)
                .orElse(entry.getKey().toString().substring(0, 8));
            plugin.getServer().broadcast(
                Component.text("§e" + (i + 1) + "位  §f" + name + "  §b" + entry.getValue() + "kill")
            );
        }
        plugin.getServer().broadcast(Component.text("§6§l-------------------"));
    }

    private void resetToLobby() {
        plugin.setGameStatus(GameStatus.LOBBY);
        plugin.getTeamManager().resetAll();
        plugin.getPlayerManager().resetAll();
        context.reset();

        Location lobbySpawn = plugin.getConfigManager().lobbySpawn();
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            p.setGameMode(GameMode.ADVENTURE);
            if (lobbySpawn != null) p.teleport(lobbySpawn);
            p.getActivePotionEffects().forEach(e -> p.removePotionEffect(e.getType()));
        }

        plugin.getServer().broadcast(Component.text("§a>Game §rロビーに戻りました。"));
    }
}
