package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.teleport.RelativeFlag;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Set;

/**
 * Teleporting a player directly with packets will cause issues on most server implementations and is discouraged!
 */
public class WrapperPlayServerPlayerPositionAndLook extends PacketWrapper<WrapperPlayServerPlayerPositionAndLook> {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private byte relativeMask;
    private int teleportId;
    private boolean dismountVehicle = false;

    public WrapperPlayServerPlayerPositionAndLook(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerPlayerPositionAndLook(double x, double y, double z, float yaw, float pitch,
                                                  byte flags, int teleportId, boolean dismountVehicle) {
        super(PacketType.Play.Server.PLAYER_POSITION_AND_LOOK);
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.relativeMask = flags;
        this.teleportId = teleportId;
        this.dismountVehicle = dismountVehicle;
    }

    @Override
    public void read() {
        this.x = readDouble();
        this.y = readDouble();
        this.z = readDouble();
        this.yaw = readFloat();
        this.pitch = readFloat();
        this.relativeMask = readByte();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            this.teleportId = readVarInt();
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            this.dismountVehicle = readBoolean();
        }
    }

    @Override
    public void write() {
        writeDouble(x);
        writeDouble(y);
        writeDouble(z);
        writeFloat(yaw);
        writeFloat(pitch);
        writeByte(relativeMask);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            writeVarInt(teleportId);
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            writeBoolean(dismountVehicle);
        }
    }

    @Override
    public void copy(WrapperPlayServerPlayerPositionAndLook wrapper) {
        this.x = wrapper.x;
        this.y = wrapper.y;
        this.z = wrapper.z;
        this.yaw = wrapper.yaw;
        this.pitch = wrapper.pitch;
        this.relativeMask = wrapper.relativeMask;
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

    public byte getRelativeMask() {
        return relativeMask;
    }

    public void setRelativeMask(byte relativeMask) {
        this.relativeMask = relativeMask;
    }

    public boolean isRelativeFlag(RelativeFlag flag) {
        return flag.isSet(relativeMask);
    }

    public void setRelative(RelativeFlag flag, boolean relative) {
        relativeMask = flag.set(relativeMask, relative);
    }

    public RelativeFlag getRelativeFlags() {
        return new RelativeFlag(relativeMask);
    }

    public void setRelativeFlags(RelativeFlag flags) {
        relativeMask = flags.getMask();
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