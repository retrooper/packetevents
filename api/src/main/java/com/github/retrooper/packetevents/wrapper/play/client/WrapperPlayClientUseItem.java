package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientUseItem extends PacketWrapper<WrapperPlayClientUseItem> {
    InteractionHand hand;

    public WrapperPlayClientUseItem(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientUseItem(InteractionHand hand) {
        super(PacketType.Play.Client.USE_ITEM);
        this.hand = hand;
    }

    @Override
    public void readData() {
        hand = InteractionHand.getById(readVarInt());
    }

    @Override
    public void readData(WrapperPlayClientUseItem packet) {
        this.hand = packet.hand;
    }

    @Override
    public void writeData() {
        writeVarInt(hand.getId());
    }

    public InteractionHand getHand() {
        return hand;
    }

    public void setHand(InteractionHand hand) {
        this.hand = hand;
    }
}
