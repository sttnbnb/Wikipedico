package net.shmn7iii.wikipedico.command.sub;

import net.shmn7iii.wikipedico.Wikipedico;
import net.shmn7iii.wikipedico.command.SubCommand;
import org.bukkit.command.CommandSender;

public class ReloadSubCommand implements SubCommand {
    private final Wikipedico plugin;

    public ReloadSubCommand(Wikipedico plugin) {
        this.plugin = plugin;
    }

    @Override public String name() { return "reload"; }
    @Override public String permission() { return "wikipedico.admin"; }
    @Override public String usage() { return "/wiki reload"; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        plugin.getConfigManager().reload();
        sender.sendMessage("[Wikipedico] Config reloaded.");
        return true;
    }
}
