package net.shmn7iii.wikipedico.team;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;

public enum TeamColor {
    RED("red", "赤", ChatColor.RED, NamedTextColor.RED),
    BLUE("blue", "青", ChatColor.BLUE, NamedTextColor.BLUE),
    YELLOW("yellow", "黄", ChatColor.YELLOW, NamedTextColor.YELLOW),
    GREEN("green", "緑", ChatColor.GREEN, NamedTextColor.GREEN),
    ORANGE("orange", "橙", ChatColor.GOLD, NamedTextColor.GOLD),
    PURPLE("purple", "紫", ChatColor.DARK_PURPLE, NamedTextColor.DARK_PURPLE),
    BLACK("black", "黒", ChatColor.DARK_GRAY, NamedTextColor.DARK_GRAY);

    private final String id;
    private final String displayName;
    private final ChatColor chatColor;
    private final NamedTextColor textColor;

    TeamColor(String id, String displayName, ChatColor chatColor, NamedTextColor textColor) {
        this.id = id;
        this.displayName = displayName;
        this.chatColor = chatColor;
        this.textColor = textColor;
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public ChatColor getChatColor() { return chatColor; }
    public NamedTextColor getTextColor() { return textColor; }

    public static TeamColor fromId(String id) {
        for (TeamColor c : values()) {
            if (c.id.equalsIgnoreCase(id)) return c;
        }
        return null;
    }
}
