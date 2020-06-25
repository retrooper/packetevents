package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.event.PacketEvent;
import org.bukkit.entity.Player;

/**
 * Please use {@link PlayerInjectEvent}
 */
@Deprecated
public final class PostPlayerInjectEvent extends PacketEvent {
    private final Player player;

    public PostPlayerInjectEvent(final Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
