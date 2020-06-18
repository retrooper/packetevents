package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.event.PacketEvent;
import org.bukkit.entity.Player;

public final class PlayerInjectEvent extends PacketEvent {
    private final Player player;
    private boolean cancelled;

    public PlayerInjectEvent(final Player player) {
        this.player = player;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void cancel() {
        setCancelled(true);
    }

    public Player getPlayer() {
        return player;
    }
}
