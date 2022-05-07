package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerOpenBook extends PacketWrapper<WrapperPlayServerOpenBook> {
    InteractionHand hand;

    public WrapperPlayServerOpenBook(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerOpenBook(InteractionHand hand) {
        super(PacketType.Play.Server.OPEN_BOOK);
        this.hand = hand;
    }

    @Override
    public void read() {
        hand = InteractionHand.values()[readVarInt()];
    }

    @Override
    public void copy(WrapperPlayServerOpenBook wrapper) {
        hand = wrapper.hand;
    }

    @Override
    public void write() {
        writeVarInt(hand.ordinal());
    }
}
