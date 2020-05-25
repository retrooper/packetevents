package me.purplex.packetevents.event.impl;


import me.purplex.packetevents.event.PacketEvent;
import org.bukkit.entity.Player;

public class PacketReceiveEvent extends PacketEvent {
    private final Player player;
    private final String name;
    private final Object packet;
    private boolean cancelled;

    public PacketReceiveEvent(final Player player, final String packetName, final Object packet) {
        this.player = player;
        this.name = packetName;
        this.packet = packet;
        this.timestamp = (System.nanoTime() / 1000000);
        this.cancelled = false;
    }

    /**
     * Get the player sending the packet
     *
     * @return
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the packet's name (NMS packet class simple name)
     *
     * @return
     */
    public String getPacketName() {
        return this.name;
    }

    /**
     * Get the raw packet object
     *
     * @return packet
     */
    public Object getPacket() {
        return this.packet;
    }

    /**
     * Cancel the packet
     *
     * @param cancelled
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Returns if the packet has been cancelled
     *
     * @return cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }
}
