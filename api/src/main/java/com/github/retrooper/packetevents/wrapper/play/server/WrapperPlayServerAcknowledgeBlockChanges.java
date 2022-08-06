package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerAcknowledgeBlockChanges extends PacketWrapper<WrapperPlayServerAcknowledgeBlockChanges> {
    private int sequence;

    public WrapperPlayServerAcknowledgeBlockChanges(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerAcknowledgeBlockChanges(int sequence) {
        super(PacketType.Play.Server.ACKNOWLEDGE_BLOCK_CHANGES);
        this.sequence = sequence;
    }

    @Override
    public void read() {
        sequence = readVarInt();
    }

    @Override
    public void write() {
        writeVarInt(sequence);
    }

    @Override
    public void copy(WrapperPlayServerAcknowledgeBlockChanges packet) {
        this.sequence = packet.sequence;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
