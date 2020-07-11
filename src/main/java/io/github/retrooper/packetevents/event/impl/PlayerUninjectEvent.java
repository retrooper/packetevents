package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.event.PacketEvent;
import org.bukkit.entity.Player;

public final class PlayerUninjectEvent extends PacketEvent {
    private final Player player;
    private boolean cancelled;
    private final boolean sync;

    public PlayerUninjectEvent(final Player player, final boolean sync) {
        this.player = player;
        this.sync = sync;
    }

    public void cancel() {
        setCancelled(true);
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean val) {
        this.cancelled = val;
    }

    public boolean isSync() {
        return sync;
    }

    public Player getPlayer() {
        return player;
    }
}
