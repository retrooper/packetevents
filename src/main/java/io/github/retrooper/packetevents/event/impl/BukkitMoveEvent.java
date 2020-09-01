package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.event.CancellableEvent;
import io.github.retrooper.packetevents.event.PacketEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

//Do not use this, I plan to remove this event.
//If you wish you may recreate this class yourself and use it.
@Deprecated
public final class BukkitMoveEvent extends PacketEvent implements CancellableEvent {
    private final Player player;
    private final Location to, from;
    private boolean cancelled;

    public BukkitMoveEvent(final Player player, final Location to, final Location from) {
        this.player = player;
        this.to = to;
        this.from = from;
    }

    public BukkitMoveEvent(final PlayerMoveEvent e) {
        this.player = e.getPlayer();
        this.to = e.getTo();
        this.from = e.getFrom();
    }

    public Player getPlayer() {
        return player;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(final boolean val) {
        this.cancelled = val;
    }

    @Override
    public void cancel() {
        setCancelled(true);
    }
}
