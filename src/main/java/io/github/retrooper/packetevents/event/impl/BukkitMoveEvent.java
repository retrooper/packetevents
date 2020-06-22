package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.event.PacketEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public final class BukkitMoveEvent extends PacketEvent {
    private final Player player;
    private final Location to, from;

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
}
