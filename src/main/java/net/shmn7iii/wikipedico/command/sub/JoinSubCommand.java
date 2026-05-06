package net.shmn7iii.wikipedico.command.sub;

import net.shmn7iii.wikipedico.Wikipedico;
import net.shmn7iii.wikipedico.command.SubCommand;
import net.shmn7iii.wikipedico.game.GameStatus;
import net.shmn7iii.wikipedico.team.TeamColor;
import net.shmn7iii.wikipedico.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JoinSubCommand implements SubCommand {

    private final Wikipedico plugin;

    public JoinSubCommand(Wikipedico plugin) {
        this.plugin = plugin;
    }

    @Override public String name() { return "join"; }
    @Override public String permission() { return "wikipedico.user"; }
    @Override public String usage() { return "/wiki join <team> [player]"; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (plugin.getGameStatus() != GameStatus.LOBBY) {
            sender.sendMessage("[Wikipedico] チームへの参加はロビー中のみ可能です。");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage("[Wikipedico] 使い方: " + usage());
            return true;
        }

        TeamColor color = TeamColor.fromId(args[1]);
        if (color == null) {
            sender.sendMessage("[Wikipedico] 無効なチーム名です。利用可能: " +
                Arrays.stream(TeamColor.values()).map(TeamColor::getId).collect(Collectors.joining(", ")));
            return true;
        }

        Player target;
        if (args.length >= 3) {
            if (!sender.hasPermission("wikipedico.admin")) {
                sender.sendMessage("[Wikipedico] 他のプレイヤーを参加させる権限がありません。");
                return true;
            }
            target = Bukkit.getPlayer(args[2]);
            if (target == null) {
                sender.sendMessage("[Wikipedico] プレイヤー " + args[2] + " が見つかりません。");
                return true;
            }
        } else if (sender instanceof Player p) {
            target = p;
        } else {
            sender.sendMessage("[Wikipedico] コンソールからはプレイヤー名を指定してください。");
            return true;
        }

        TeamManager.JoinResult result = plugin.getTeamManager().join(target, color);
        switch (result) {
            case SUCCESS -> {
                target.sendMessage("[Wikipedico] " + color.getChatColor() + color.getDisplayName() + "チーム§r に参加しました。");
                if (!target.equals(sender)) {
                    sender.sendMessage("[Wikipedico] " + target.getName() + " を " + color.getDisplayName() + "チームに参加させました。");
                }
            }
            case TEAM_FULL -> sender.sendMessage("[Wikipedico] " + color.getDisplayName() + "チームは満員です。");
            case NOT_REGISTERED -> sender.sendMessage("[Wikipedico] プレイヤーが登録されていません。");
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return Arrays.stream(TeamColor.values())
                .map(TeamColor::getId)
                .filter(id -> id.startsWith(args[1].toLowerCase()))
                .collect(Collectors.toList());
        }
        return List.of();
    }
}
