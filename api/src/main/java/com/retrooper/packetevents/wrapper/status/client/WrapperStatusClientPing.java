package com.retrooper.packetevents.wrapper.status.client;

import com.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.retrooper.packetevents.protocol.packettype.PacketType;
import com.retrooper.packetevents.protocol.data.player.ClientVersion;
import com.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperStatusClientPing extends PacketWrapper<WrapperStatusClientPing> {
    private long time;

    public WrapperStatusClientPing(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperStatusClientPing(long time) {
        super(PacketType.Status.Client.PING.getID(), ClientVersion.UNKNOWN);
        this.time = time;
    }

    @Override
    public void readData() {
        this.time = readLong();
    }

    @Override
    public void readData(WrapperStatusClientPing wrapper) {
        this.time = wrapper.time;
    }

    @Override
    public void writeData() {
        writeLong(time);
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
