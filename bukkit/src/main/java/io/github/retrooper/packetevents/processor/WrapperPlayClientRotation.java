package io.github.retrooper.packetevents.processor;

import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;

public class WrapperPlayClientRotation extends WrapperPlayClientFlying<WrapperPlayClientRotation> {
    private float yaw;
    private float pitch;
    public WrapperPlayClientRotation(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientRotation(float yaw, float pitch, boolean onGround) {
        super(PacketType.Play.Client.PLAYER_ROTATION, onGround);
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public void readData() {
        yaw = readFloat();
        pitch = readFloat();
        super.readData();
    }

    @Override
    public void readData(WrapperPlayClientRotation wrapper) {
        yaw = wrapper.yaw;
        pitch = wrapper.pitch;
        super.readData(wrapper);
    }

    @Override
    public void writeData() {
        writeFloat(yaw);
        writeFloat(pitch);
        super.writeData();
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
