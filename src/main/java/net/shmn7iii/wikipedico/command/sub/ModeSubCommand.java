package net.shmn7iii.wikipedico.command.sub;

import net.shmn7iii.wikipedico.Wikipedico;
import net.shmn7iii.wikipedico.command.SubCommand;
import net.shmn7iii.wikipedico.game.GameStatus;
import net.shmn7iii.wikipedico.game.mode.SoloMode;
import net.shmn7iii.wikipedico.game.mode.TeamMode;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ModeSubCommand implements SubCommand {

    private final Wikipedico plugin;

    public ModeSubCommand(Wikipedico plugin) {
        this.plugin = plugin;
    }

    @Override public String name() { return "mode"; }
    @Override public String permission() { return "wikipedico.admin"; }
    @Override public String usage() { return "/wiki mode <team|solo>"; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            String current = plugin.getGameManager().getMode().getDisplayName();
            sender.sendMessage("[Wikipedico] 現在のモード: " + current + "  使い方: " + usage());
            return true;
        }

        if (plugin.getGameStatus() != GameStatus.LOBBY) {
            sender.sendMessage("[Wikipedico] モード変更はロビー中のみ可能です。");
            return true;
        }

        boolean ok = switch (args[1].toLowerCase()) {
            case "team" -> plugin.getGameManager().setMode(new TeamMode());
            case "solo" -> plugin.getGameManager().setMode(new SoloMode());
            default -> {
                sender.sendMessage("[Wikipedico] 不明なモード: " + args[1] + "  利用可能: team, solo");
                yield false;
            }
        };

        if (ok) {
            sender.sendMessage("[Wikipedico] モードを §e" + plugin.getGameManager().getMode().getDisplayName() + " §rに変更しました。");
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return List.of("team", "solo").stream()
                .filter(s -> s.startsWith(args[1].toLowerCase()))
                .toList();
        }
        return List.of();
    }
}
