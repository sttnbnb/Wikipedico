package net.shmn7iii.wikipedico;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandHandler implements CommandExecutor, TabCompleter {
    public static String senderErrorMessage = "Can't execute this from server console!";
    public static ArrayList<String> SUBCOMMANDS = new ArrayList<>(Arrays.asList("help", "admin"));

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("wikipedico")) {
            if (args[0].equalsIgnoreCase("help")) {
                sender.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "===[Wikipedico - CommandHelp]==============");

                return true;
            } else if (args[0].equalsIgnoreCase("admin")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(senderErrorMessage);
                    return true;
                }

                WPlayer wplayer = WGame.getInstance().playerMap.get((Player) sender);
                wplayer.makeAdmin();

                return true;
            } else {
                return false;
            }
        }
        return false;
    }


    /*
    参考: https://www.spigotmc.org/threads/tab-complete.160308/
    */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();
        StringUtil.copyPartialMatches(args[0], SUBCOMMANDS, completions);
        Collections.sort(completions);

        return completions;
    }
}
