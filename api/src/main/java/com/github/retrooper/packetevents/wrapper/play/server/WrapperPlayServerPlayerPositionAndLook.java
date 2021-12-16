package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.teleport.RelativeFlags;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * Teleporting a player directly with packets will cause issues on most server implementations and is discouraged!
 */
public class WrapperPlayServerPlayerPositionAndLook extends PacketWrapper<WrapperPlayServerPlayerPositionAndLook> {
    double x;
    double y;
    double z;
    float yaw;
    float pitch;
    int flags;
    int teleportId;
    boolean dismountVehicle = false;

    public WrapperPlayServerPlayerPositionAndLook(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerPlayerPositionAndLook(double x, double y, double z, float yaw, float pitch,
                                                  int flags, int teleportId, boolean dismountVehicle) {
        super(PacketType.Play.Server.PLAYER_POSITION_AND_LOOK);
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.flags = flags;
        this.teleportId = teleportId;
        this.dismountVehicle = dismountVehicle;
    }

    @Override
    public void readData() {
        this.x = readDouble();
        this.y = readDouble();
        this.z = readDouble();
        this.yaw = readFloat();
        this.pitch = readFloat();
        this.flags = readUnsignedByte();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            this.teleportId = readVarInt();
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            this.dismountVehicle = readBoolean();
        }
    }

    @Override
    public void writeData() {
        writeDouble(x);
        writeDouble(y);
        writeDouble(z);
        writeFloat(yaw);
        writeFloat(pitch);
        writeByte(flags);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            writeVarInt(teleportId);
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            writeBoolean(dismountVehicle);
        }
    }

    @Override
    public void readData(WrapperPlayServerPlayerPositionAndLook wrapper) {
        this.x = wrapper.x;
        this.y = wrapper.y;
        this.z = wrapper.z;
        this.yaw = wrapper.yaw;
        this.pitch = wrapper.pitch;
        this.flags = wrapper.flags;
        this.teleportId = wrapper.teleportId;
        this.dismountVehicle = wrapper.dismountVehicle;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
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

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public boolean isRelativeFlag(RelativeFlags flag) {
        return flag.isSet(flags);
    }

    public void setRelative(RelativeFlags flag, boolean relative) {
        flags = flag.set(relative, flags);
    }

    public int getTeleportId() {
        return teleportId;
    }

    public void setTeleportId(int teleportId) {
        this.teleportId = teleportId;
    }

    public boolean isDismountVehicle() {
        return dismountVehicle;
    }

    public void setDismountVehicle(boolean dismountVehicle) {
        this.dismountVehicle = dismountVehicle;
    }
}