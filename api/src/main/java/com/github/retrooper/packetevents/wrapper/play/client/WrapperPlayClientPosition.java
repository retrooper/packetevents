package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3d;

public class WrapperPlayClientPosition extends WrapperPlayClientFlying<WrapperPlayClientPosition> {
    private Vector3d position;

    public WrapperPlayClientPosition(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientPosition(Vector3d position, boolean onGround) {
        super(PacketType.Play.Client.PLAYER_POSITION, onGround);
        this.position = position;
    }

    @Override
    public void readData() {
        double x = readDouble();
        double y = readDouble();
        if (serverVersion == ServerVersion.v_1_7_10) {
            //Can be ignored, cause stance = (y + 1.62)
            double stance = readDouble();
        }
        double z = readDouble();
        position = new Vector3d(x, y, z);
        super.readData();
    }

    @Override
    public void readData(WrapperPlayClientPosition wrapper) {
        position = wrapper.position;
        super.readData(wrapper);
    }

    @Override
    public void writeData() {
        writeDouble(position.x);
        writeDouble(position.y);
        if (serverVersion == ServerVersion.v_1_7_10) {
            writeDouble(position.y + 1.62);
        }
        writeDouble(position.z);
        super.writeData();
    }

    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }
}
