package net.shmn7iii.wikipedico.command.sub;

import net.shmn7iii.wikipedico.Wikipedico;
import net.shmn7iii.wikipedico.command.SubCommand;
import net.shmn7iii.wikipedico.game.GameStatus;
import net.shmn7iii.wikipedico.player.PlayerStatus;
import net.shmn7iii.wikipedico.player.WPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class RevivalSubCommand implements SubCommand {

    private final Wikipedico plugin;

    public RevivalSubCommand(Wikipedico plugin) {
        this.plugin = plugin;
    }

    @Override public String name() { return "revival"; }
    @Override public String permission() { return "wikipedico.admin"; }
    @Override public String usage() { return "/wiki revival <player>"; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (plugin.getGameStatus() != GameStatus.PLAYING) {
            sender.sendMessage("[Wikipedico] ゲームが進行中ではありません。");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage("[Wikipedico] 使い方: " + usage());
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("[Wikipedico] プレイヤーが見つかりません: " + args[0]);
            return true;
        }

        WPlayer wp = plugin.getPlayerManager().get(target);
        if (wp == null || wp.getStatus() != PlayerStatus.DEAD) {
            sender.sendMessage("[Wikipedico] " + target.getName() + " はDEAD状態ではありません。");
            return true;
        }

        wp.setStatus(PlayerStatus.ALIVE);
        target.setGameMode(GameMode.SURVIVAL);
        Location sky = plugin.getConfigManager().skySpawn();
        if (sky != null) target.teleport(sky);

        Bukkit.broadcastMessage("§a>Game §r" + target.getName() + " §rが復活しました！");
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                .toList();
        }
        return List.of();
    }
}
