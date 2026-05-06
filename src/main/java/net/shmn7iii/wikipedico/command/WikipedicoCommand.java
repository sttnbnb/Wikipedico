package net.shmn7iii.wikipedico.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WikipedicoCommand implements CommandExecutor, TabCompleter {

    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public void register(SubCommand sub) {
        subCommands.put(sub.name().toLowerCase(), sub);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("[Wikipedico] Usage: /wiki <" + String.join("|", subCommands.keySet()) + ">");
            return true;
        }
        SubCommand sub = subCommands.get(args[0].toLowerCase());
        if (sub == null) {
            sender.sendMessage("[Wikipedico] Unknown subcommand: " + args[0]);
            return true;
        }
        if (!sender.hasPermission(sub.permission())) {
            sender.sendMessage("[Wikipedico] You don't have permission.");
            return true;
        }
        return sub.execute(sender, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            for (SubCommand sub : subCommands.values()) {
                if (sender.hasPermission(sub.permission()) && sub.name().startsWith(args[0].toLowerCase())) {
                    completions.add(sub.name());
                }
            }
            return completions;
        }
        SubCommand sub = subCommands.get(args[0].toLowerCase());
        if (sub != null && sender.hasPermission(sub.permission())) {
            return sub.tabComplete(sender, args);
        }
        return List.of();
    }
}
