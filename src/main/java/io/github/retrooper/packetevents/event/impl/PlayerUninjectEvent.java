/**
 * Copyright (c) 2020 retrooper
 */
package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.event.PacketEvent;
import org.bukkit.entity.Player;

public final class PlayerUninjectEvent extends PacketEvent {
    private final Player player;
    private final boolean sync;
    private boolean cancelled;

    @Deprecated
    public PlayerUninjectEvent(final Player player, @Deprecated final boolean sync) {
        this.player = player;
        this.sync = sync;
    }

    public PlayerUninjectEvent(final Player player) {
        this.player = player;
        this.sync = true;
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

    /**
     * Whether the uninjection was called synchronously or asynchronously.
     * The event is still called synchronously.
     * By default all uninjections are called aynchronously in this build.
     *
     * @return If the uninjection was done async or sync
     * @deprecated because it is unneeded
     */
    @Deprecated
    public boolean isSync() {
        return sync;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isAsyncByDefault() {
        return false;
    }
}
