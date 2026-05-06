package net.shmn7iii.wikipedico.player;

import org.bukkit.entity.Player;

import java.util.*;

public class PlayerManager {

    private final Map<UUID, WPlayer> players = new HashMap<>();

    public WPlayer register(Player player) {
        WPlayer wp = new WPlayer(player.getUniqueId());
        players.put(player.getUniqueId(), wp);
        return wp;
    }

    public void unregister(Player player) {
        players.remove(player.getUniqueId());
    }

    public WPlayer get(Player player) {
        return players.get(player.getUniqueId());
    }

    public WPlayer get(UUID uuid) {
        return players.get(uuid);
    }

    public Collection<WPlayer> getAll() {
        return Collections.unmodifiableCollection(players.values());
    }

    public void resetAll() {
        players.values().forEach(WPlayer::reset);
    }
}
