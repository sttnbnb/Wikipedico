package net.shmn7iii.wikipedico.command.sub;

import net.shmn7iii.wikipedico.command.SubCommand;
import org.bukkit.command.CommandSender;

public class StartSubCommand implements SubCommand {
    @Override public String name() { return "start"; }
    @Override public String permission() { return "wikipedico.admin"; }
    @Override public String usage() { return "/wiki start"; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage("[Wikipedico] start: not yet implemented");
        return true;
    }
}
