package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientFlying<T extends WrapperPlayClientFlying> extends PacketWrapper<T> {
    private boolean onGround;

    public WrapperPlayClientFlying(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientFlying(boolean onGround) {
        super(PacketType.Play.Client.PLAYER_FLYING);
        this.onGround = onGround;
    }

    public WrapperPlayClientFlying(PacketTypeCommon type, boolean onGround) {
        super(type);
        this.onGround = onGround;
    }

    //TODO Rethink, should this be somewhere else?
    public static boolean isInstanceOfFlying(PacketTypeCommon type) {
        return type == PacketType.Play.Client.PLAYER_FLYING
                || type == PacketType.Play.Client.PLAYER_POSITION
                || type == PacketType.Play.Client.PLAYER_ROTATION
                || type == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION;
    }


    @Override
    public void readData() {
        onGround = readBoolean();
    }

    @Override
    public void readData(WrapperPlayClientFlying wrapper) {
        onGround = wrapper.onGround;
    }

    @Override
    public void writeData() {
        writeBoolean(onGround);
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
