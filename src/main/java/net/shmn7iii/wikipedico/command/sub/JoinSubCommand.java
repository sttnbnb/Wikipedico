package net.shmn7iii.wikipedico.command.sub;

import net.shmn7iii.wikipedico.command.SubCommand;
import org.bukkit.command.CommandSender;

public class JoinSubCommand implements SubCommand {
    @Override public String name() { return "join"; }
    @Override public String permission() { return "wikipedico.user"; }
    @Override public String usage() { return "/wiki join <team> [player]"; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage("[Wikipedico] join: not yet implemented");
        return true;
    }
}
