package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.event.PacketEvent;
import org.bukkit.entity.Player;

public final class PlayerUninjectEvent extends PacketEvent {
    private final Player player;
    private boolean cancelled;

    public PlayerUninjectEvent(final Player player) {
        this.player = player;
    }

    public void setCancelled(final boolean val) {
        this.cancelled = val;
    }

    public void cancel() {
        setCancelled(true);
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public Player getPlayer() {
        return player;
    }
}
