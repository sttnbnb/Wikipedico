package net.shmn7iii.wikipedico.command;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand {
    String name();
    String permission();
    String usage();
    boolean execute(CommandSender sender, String[] args);
    default List<String> tabComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
