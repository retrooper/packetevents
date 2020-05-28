package me.purplex.packetevents.event.impl;

import me.purplex.packetevents.event.PacketEvent;
import org.bukkit.entity.Player;

public class PlayerInjectEvent extends PacketEvent {
    private final Player player;
    public PlayerInjectEvent(final Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}
