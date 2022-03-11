package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayWorldBorderWarningReach extends PacketWrapper<WrapperPlayWorldBorderWarningReach> {
    int warningBlocks;

    public WrapperPlayWorldBorderWarningReach(int warningBlocks) {
        super(PacketType.Play.Server.WORLD_BORDER_WARNING_REACH);
        this.warningBlocks = warningBlocks;
    }

    @Override
    public void read() {
        warningBlocks = readVarInt();
    }

    @Override
    public void copy(WrapperPlayWorldBorderWarningReach packet) {
        warningBlocks = packet.warningBlocks;
    }

    @Override
    public void write() {
        writeVarInt(warningBlocks);
    }

    public int getWarningBlocks() {
        return warningBlocks;
    }

    public void setWarningBlocks(int warningBlocks) {
        this.warningBlocks = warningBlocks;
    }
}
