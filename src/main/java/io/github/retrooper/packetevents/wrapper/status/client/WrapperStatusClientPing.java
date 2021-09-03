package io.github.retrooper.packetevents.wrapper.status.client;

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperStatusClientPing extends PacketWrapper<WrapperStatusClientPing> {
    private long time;

    public WrapperStatusClientPing(PacketReceiveEvent event) {
        super(event);
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
