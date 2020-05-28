package me.purplex.packetevents.event.impl;

import me.purplex.packetevents.event.PacketEvent;
import org.bukkit.entity.Player;

public class PlayerUninjectEvent extends PacketEvent {
    private final Player player;

    public PlayerUninjectEvent(final Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
