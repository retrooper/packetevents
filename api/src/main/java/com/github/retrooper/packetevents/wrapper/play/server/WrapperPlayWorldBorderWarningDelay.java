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
    public void readData() {
        delay = readVarLong();
    }

    @Override
    public void readData(WrapperPlayWorldBorderWarningDelay packet) {
        delay = packet.delay;
    }

    @Override
    public void writeData() {
        writeVarLong(delay);
    }
}
