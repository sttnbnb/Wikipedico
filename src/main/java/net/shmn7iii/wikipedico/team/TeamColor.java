package net.shmn7iii.wikipedico.team;

import org.bukkit.ChatColor;

public enum TeamColor {
    RED("red", "赤", ChatColor.RED),
    BLUE("blue", "青", ChatColor.BLUE),
    YELLOW("yellow", "黄", ChatColor.YELLOW),
    GREEN("green", "緑", ChatColor.GREEN),
    ORANGE("orange", "橙", ChatColor.GOLD),
    PURPLE("purple", "紫", ChatColor.DARK_PURPLE),
    BLACK("black", "黒", ChatColor.DARK_GRAY);

    private final String id;
    private final String displayName;
    private final ChatColor chatColor;

    TeamColor(String id, String displayName, ChatColor chatColor) {
        this.id = id;
        this.displayName = displayName;
        this.chatColor = chatColor;
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public ChatColor getChatColor() { return chatColor; }

    public static TeamColor fromId(String id) {
        for (TeamColor c : values()) {
            if (c.id.equalsIgnoreCase(id)) return c;
        }
        return null;
    }
}
