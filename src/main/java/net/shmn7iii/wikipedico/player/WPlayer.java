package net.shmn7iii.wikipedico.player;

import java.util.UUID;

public class WPlayer {

    private final UUID uuid;
    private PlayerStatus status;
    private String teamId;
    private boolean joined;
    private int kills;

    public WPlayer(UUID uuid) {
        this.uuid = uuid;
        reset();
    }

    public void reset() {
        this.status = PlayerStatus.SPECTATOR;
        this.teamId = null;
        this.joined = false;
        this.kills = 0;
    }

    public UUID getUuid() { return uuid; }

    public PlayerStatus getStatus() { return status; }
    public void setStatus(PlayerStatus status) { this.status = status; }

    public String getTeamId() { return teamId; }
    public void setTeamId(String teamId) { this.teamId = teamId; }

    public boolean isJoined() { return joined; }
    public void setJoined(boolean joined) { this.joined = joined; }

    public int getKills() { return kills; }
    public void addKill() { this.kills++; }
}
