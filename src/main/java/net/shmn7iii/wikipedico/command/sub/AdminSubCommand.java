package net.shmn7iii.wikipedico.command.sub;

import net.shmn7iii.wikipedico.Wikipedico;
import net.shmn7iii.wikipedico.command.SubCommand;
import net.shmn7iii.wikipedico.player.PlayerStatus;
import net.shmn7iii.wikipedico.player.WPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AdminSubCommand implements SubCommand {

    private final Wikipedico plugin;

    public AdminSubCommand(Wikipedico plugin) {
        this.plugin = plugin;
    }

    @Override public String name() { return "admin"; }
    @Override public String permission() { return "wikipedico.admin"; }
    @Override public String usage() { return "/wiki admin <player>"; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
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
        if (wp == null) {
            sender.sendMessage("[Wikipedico] プレイヤーが登録されていません。");
            return true;
        }

        if (wp.getStatus() == PlayerStatus.ADMIN) {
            wp.setStatus(PlayerStatus.SPECTATOR);
            sender.sendMessage("[Wikipedico] " + target.getName() + " のADMINステータスを解除しました。");
        } else {
            wp.setStatus(PlayerStatus.ADMIN);
            sender.sendMessage("[Wikipedico] " + target.getName() + " をADMINに設定しました。");
        }
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
