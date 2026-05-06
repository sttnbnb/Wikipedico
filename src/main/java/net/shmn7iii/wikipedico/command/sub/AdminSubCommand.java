package net.shmn7iii.wikipedico.command.sub;

import net.shmn7iii.wikipedico.command.SubCommand;
import org.bukkit.command.CommandSender;

public class AdminSubCommand implements SubCommand {
    @Override public String name() { return "admin"; }
    @Override public String permission() { return "wikipedico.admin"; }
    @Override public String usage() { return "/wiki admin <player>"; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage("[Wikipedico] admin: not yet implemented");
        return true;
    }
}
