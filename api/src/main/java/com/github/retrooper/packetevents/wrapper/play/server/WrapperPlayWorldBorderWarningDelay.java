package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayWorldBorderWarningDelay extends PacketWrapper<WrapperPlayWorldBorderWarningDelay> {
    long delay;

    public WrapperPlayWorldBorderWarningDelay(long delay) {
        super(PacketType.Play.Server.WORLD_BORDER_WARNING_DELAY);
        this.delay = delay;
    }

    public WrapperPlayWorldBorderWarningDelay(PacketSendEvent event) {
        super(event);
    }

    @Override
    public void read() {
        delay = readVarLong();
    }

    @Override
    public void copy(WrapperPlayWorldBorderWarningDelay packet) {
        delay = packet.delay;
    }

    @Override
    public void write() {
        writeVarLong(delay);
    }
}
