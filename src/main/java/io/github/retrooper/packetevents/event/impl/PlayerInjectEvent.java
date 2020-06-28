package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.enums.ClientVersion;
import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.utils.api.VersionLookupUtils;
import org.bukkit.entity.Player;

public final class PlayerInjectEvent extends PacketEvent {
    private final Player player;
    private final ClientVersion clientVersion;
    private boolean cancelled;

    public PlayerInjectEvent(final Player player) {
        this.player = player;
        this.clientVersion = ClientVersion.fromProtocolVersion(VersionLookupUtils.getProtocolVersion(player));
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

    public ClientVersion getClientVersion() {
        return clientVersion;
    }

    public Player getPlayer() {
        return player;
    }
}
