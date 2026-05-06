package net.shmn7iii.wikipedico.game;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameContext {

    private final Map<UUID, Integer> kills = new HashMap<>();

    public void addKill(UUID uuid) {
        kills.merge(uuid, 1, Integer::sum);
    }

    public int getKills(UUID uuid) {
        return kills.getOrDefault(uuid, 0);
    }

    public Map<UUID, Integer> getAllKills() {
        return kills;
    }

    public void reset() {
        kills.clear();
    }
}
