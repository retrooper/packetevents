package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.event.PacketEvent;
import org.bukkit.entity.Player;

public final class PlayerUninjectEvent extends PacketEvent {
    private final Player player;

    public PlayerUninjectEvent(final Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
