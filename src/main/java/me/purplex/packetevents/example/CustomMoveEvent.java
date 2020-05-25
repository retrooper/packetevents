package me.purplex.packetevents.example;

import me.purplex.packetevents.event.PacketEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Example packetevent
 * Make sure to initiate the super timestamp variable,
 * if you want to use the getTimestamp() function
 */
public class CustomMoveEvent extends PacketEvent {
    private final Player player;
    private Location to, from;
    private boolean cancelled;

    public CustomMoveEvent(final Player player, final Location to, final Location from) {
        this.player = player;
        this.to = to;
        this.from = from;
        this.cancelled = false;

        //IMPORTANT to initialize the timestamp variable
        this.timestamp = (System.nanoTime() / 1000000);
    }

    @Override
    public long getTimestamp() {
        return super.getTimestamp();
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getTo() {
        return to;
    }

    public Location getFrom() {
        return from;
    }

    public void setTo(Location loc) {
        this.to = loc;
    }

    public void setFrom(Location loc) {
        this.from = from;
    }

}
