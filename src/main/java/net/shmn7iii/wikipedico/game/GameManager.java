package net.shmn7iii.wikipedico.game;

import net.shmn7iii.wikipedico.Wikipedico;
import net.shmn7iii.wikipedico.player.PlayerStatus;
import net.shmn7iii.wikipedico.player.WPlayer;
import net.shmn7iii.wikipedico.team.WTeam;
import net.shmn7iii.wikipedico.worldborder.WorldBorderController;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.stream.Collectors;

public class GameManager {

    private final Wikipedico plugin;
    private final GameContext context = new GameContext();
    private final WorldBorderController borderController;
    private BukkitTask countdownTask;

    public GameManager(Wikipedico plugin) {
        this.plugin = plugin;
        this.borderController = new WorldBorderController(plugin);
    }

    public boolean startGame() {
        if (plugin.getGameStatus() != GameStatus.LOBBY) return false;

        plugin.setGameStatus(GameStatus.PREPARING);
        int prepTime = plugin.getConfigManager().preparationTime();
        int countDownTime = plugin.getConfigManager().countDownTime();

        countdownTask = new PreparationCountdown(plugin, this, prepTime, countDownTime)
            .runTaskTimer(plugin, 0L, 20L);

        Bukkit.broadcastMessage("§a>Game §rゲームを開始します。準備時間: §e" + prepTime + "秒");
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
                p.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
            } else {
                wp.setStatus(PlayerStatus.SPECTATOR);
                p.setGameMode(GameMode.SPECTATOR);
            }
        }

        plugin.getServer().getOnlinePlayers().forEach(p -> {
            p.sendTitle("§a§lGAME START", "", 10, 30, 10);
        });
        Bukkit.broadcastMessage("§a>Game §rゲーム開始！");
        borderController.start();
    }

    public void endGame() {
        if (plugin.getGameStatus() == GameStatus.LOBBY) return;

        if (countdownTask != null) {
            countdownTask.cancel();
            countdownTask = null;
        }

        plugin.setGameStatus(GameStatus.ENDING);
        borderController.stop();
        Bukkit.broadcastMessage("§c>Game §rゲーム終了！");

        showRanking();

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
            Bukkit.broadcastMessage("§b" + killer.getName() + " §r✈► §c" + victim.getName());
        } else {
            Bukkit.broadcastMessage("§c" + victim.getName() + " §rが死亡しました。");
        }

        checkVictory();
    }

    public void checkVictory() {
        if (plugin.getGameStatus() != GameStatus.PLAYING) return;

        Set<WTeam> alive = plugin.getTeamManager().aliveTeams();
        if (alive.size() <= 1) {
            if (!alive.isEmpty()) {
                WTeam winner = alive.iterator().next();
                Bukkit.broadcastMessage(winner.getColor().getChatColor() + winner.getColor().getDisplayName()
                    + "チーム §r§lの勝利！");
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

        Bukkit.broadcastMessage("§6§l--- キルランキング ---");
        for (int i = 0; i < sorted.size(); i++) {
            var entry = sorted.get(i);
            String name = Optional.ofNullable(Bukkit.getPlayer(entry.getKey()))
                .map(Player::getName)
                .orElse(entry.getKey().toString().substring(0, 8));
            Bukkit.broadcastMessage("§e" + (i + 1) + "位  §f" + name + "  §b" + entry.getValue() + "kill");
        }
        Bukkit.broadcastMessage("§6§l-------------------");
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

        Bukkit.broadcastMessage("§a>Game §rロビーに戻りました。");
    }
}
