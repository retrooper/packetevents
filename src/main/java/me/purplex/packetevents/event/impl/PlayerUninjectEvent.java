package me.purplex.packetevents.event.impl;

import me.purplex.packetevents.event.PacketEvent;
import org.bukkit.entity.Player;

public class PlayerUninjectEvent extends PacketEvent {
    private final Player player;
    private boolean cancelled;
    public PlayerUninjectEvent(final Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setCancelled(final boolean val) {
        cancelled = val;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
