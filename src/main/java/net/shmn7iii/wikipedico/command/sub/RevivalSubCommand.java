package net.shmn7iii.wikipedico.command.sub;

import net.shmn7iii.wikipedico.command.SubCommand;
import org.bukkit.command.CommandSender;

public class RevivalSubCommand implements SubCommand {
    @Override public String name() { return "revival"; }
    @Override public String permission() { return "wikipedico.admin"; }
    @Override public String usage() { return "/wiki revival <player>"; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage("[Wikipedico] revival: not yet implemented");
        return true;
    }
}
