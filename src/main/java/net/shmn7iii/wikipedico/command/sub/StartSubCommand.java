package net.shmn7iii.wikipedico.command.sub;

import net.shmn7iii.wikipedico.Wikipedico;
import net.shmn7iii.wikipedico.command.SubCommand;
import org.bukkit.command.CommandSender;

public class StartSubCommand implements SubCommand {

    private final Wikipedico plugin;

    public StartSubCommand(Wikipedico plugin) {
        this.plugin = plugin;
    }

    @Override public String name() { return "start"; }
    @Override public String permission() { return "wikipedico.admin"; }
    @Override public String usage() { return "/wiki start"; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        boolean ok = plugin.getGameManager().startGame();
        if (!ok) {
            sender.sendMessage("[Wikipedico] ゲームはロビー状態の時のみ開始できます。");
        }
        return true;
    }
}
