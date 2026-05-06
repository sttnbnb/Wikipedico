package net.shmn7iii.wikipedico.command.sub;

import net.shmn7iii.wikipedico.Wikipedico;
import net.shmn7iii.wikipedico.command.SubCommand;
import net.shmn7iii.wikipedico.game.GameStatus;
import org.bukkit.command.CommandSender;

public class EndSubCommand implements SubCommand {

    private final Wikipedico plugin;

    public EndSubCommand(Wikipedico plugin) {
        this.plugin = plugin;
    }

    @Override public String name() { return "end"; }
    @Override public String permission() { return "wikipedico.admin"; }
    @Override public String usage() { return "/wiki end"; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (plugin.getGameStatus() == GameStatus.LOBBY) {
            sender.sendMessage("[Wikipedico] ゲームが進行中ではありません。");
            return true;
        }
        plugin.getGameManager().endGame();
        return true;
    }
}
