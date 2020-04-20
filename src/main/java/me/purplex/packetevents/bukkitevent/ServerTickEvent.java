package me.purplex.packetevents.bukkitevent;

import me.purplex.packetevents.events.PacketEvent;


public class ServerTickEvent extends PacketEvent {

    private long timestamp;
    public ServerTickEvent(long timestamp) {
        this.timestamp = timestamp;
    }



    public long getTimestamp() {
        return timestamp;
    }

}
