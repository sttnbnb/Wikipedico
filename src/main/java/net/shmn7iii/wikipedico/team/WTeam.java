package net.shmn7iii.wikipedico.team;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WTeam {

    private final TeamColor color;
    private final Set<UUID> members = new HashSet<>();

    public WTeam(TeamColor color) {
        this.color = color;
    }

    public TeamColor getColor() { return color; }
    public String getId() { return color.getId(); }

    public boolean addMember(UUID uuid) {
        return members.add(uuid);
    }

    public boolean removeMember(UUID uuid) {
        return members.remove(uuid);
    }

    public boolean hasMember(UUID uuid) {
        return members.contains(uuid);
    }

    public int size() { return members.size(); }

    public Set<UUID> getMembers() { return Collections.unmodifiableSet(members); }
}
