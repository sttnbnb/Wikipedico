package net.shmn7iii.wikipedico.command.sub;

import net.shmn7iii.wikipedico.command.SubCommand;
import org.bukkit.command.CommandSender;

public class EndSubCommand implements SubCommand {
    @Override public String name() { return "end"; }
    @Override public String permission() { return "wikipedico.admin"; }
    @Override public String usage() { return "/wiki end"; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage("[Wikipedico] end: not yet implemented");
        return true;
    }
}
