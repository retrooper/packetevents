package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.EntityPositionData;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.teleport.RelativeFlag;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

/**
 * Teleporting a player directly with packets will cause
 * issues on most server implementations and is discouraged!
 */
public class WrapperPlayServerPlayerPositionAndLook extends PacketWrapper<WrapperPlayServerPlayerPositionAndLook> {

    private int teleportId;
    /**
     * Changed with 1.21.2
     * <p>
     * In versions before 1.21.2, the {@link EntityPositionData#getDeltaMovement()} will always be zero.
     */
    private EntityPositionData values;
    private RelativeFlag relativeFlags;

    /**
     * Removed in 1.19.3
     */
    @ApiStatus.Obsolete
    private boolean dismountVehicle = false;

    public WrapperPlayServerPlayerPositionAndLook(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerPlayerPositionAndLook(
            double x, double y, double z, float yaw, float pitch,
            byte flags, int teleportId, boolean dismountVehicle
    ) {
        this(new Vector3d(x, y, z), yaw, pitch, flags, teleportId, dismountVehicle);
    }

    public WrapperPlayServerPlayerPositionAndLook(
            Vector3d position, float yaw, float pitch,
            byte flags, int teleportId, boolean dismountVehicle
    ) {
        this(position, yaw, pitch, flags, teleportId);
        this.dismountVehicle = dismountVehicle;
    }

    public WrapperPlayServerPlayerPositionAndLook(
            Vector3d position, float yaw, float pitch,
            byte flags, int teleportId
    ) {
        this(teleportId, position, Vector3d.zero(), yaw, pitch, flags);
    }

    public WrapperPlayServerPlayerPositionAndLook(
            int teleportId, Vector3d position, Vector3d deltaMovement,
            float yaw, float pitch, byte flags
    ) {
        this(teleportId, position, deltaMovement, yaw, pitch, null);
        this.relativeFlags = new RelativeFlag(flags);
    }

    public WrapperPlayServerPlayerPositionAndLook(
            int teleportId, Vector3d position, Vector3d deltaMovement,
            float yaw, float pitch, RelativeFlag flags
    ) {
        this(teleportId, new EntityPositionData(position, deltaMovement, yaw, pitch), flags);
    }

    public WrapperPlayServerPlayerPositionAndLook(
            int teleportId, EntityPositionData values, RelativeFlag flags
    ) {
        super(PacketType.Play.Server.PLAYER_POSITION_AND_LOOK);
        this.teleportId = teleportId;
        this.values = values;
        this.relativeFlags = flags;
    }

    @Override
    public void read() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            this.teleportId = this.readVarInt();
            this.values = EntityPositionData.read(this);
            this.relativeFlags = new RelativeFlag(this.readInt());
        } else {
            Vector3d position = Vector3d.read(this);
            float yaw = this.readFloat();
            float pitch = this.readFloat();
            this.values = new EntityPositionData(position, Vector3d.zero(), yaw, pitch);
            this.relativeFlags = new RelativeFlag(this.readUnsignedByte());
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
                this.teleportId = this.readVarInt();
                if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)
                        && this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_19_3)) {
                    this.dismountVehicle = this.readBoolean();
                }
            }
        }
    }

    @Override
    public void write() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            this.writeVarInt(this.teleportId);
            EntityPositionData.write(this, this.values);
            this.writeInt(this.relativeFlags.getFullMask());
        } else {
            Vector3d.write(this, this.values.getPosition());
            this.writeFloat(this.values.getYaw());
            this.writeFloat(this.values.getPitch());
            this.writeByte(this.relativeFlags.getFullMask());
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
                this.writeVarInt(this.teleportId);
                if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)
                        && this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_19_3)) {
                    this.writeBoolean(this.dismountVehicle);
                }
            }
        }
    }

    @Override
    public void copy(WrapperPlayServerPlayerPositionAndLook wrapper) {
        this.teleportId = wrapper.teleportId;
        this.values = wrapper.values;
        this.relativeFlags = wrapper.relativeFlags;
        this.dismountVehicle = wrapper.dismountVehicle;
    }

    public int getTeleportId() {
        return this.teleportId;
    }

    public void setTeleportId(int teleportId) {
        this.teleportId = teleportId;
    }

    public EntityPositionData getValues() {
        return this.values;
    }

    public void setValues(EntityPositionData values) {
        this.values = values;
    }

    public Vector3d getPosition() {
        return this.values.getPosition();
    }

    public void setPosition(Vector3d position) {
        this.values.setPosition(position);
    }

    public double getX() {
        return this.getPosition().getX();
    }

    public void setX(double x) {
        this.setPosition(new Vector3d(x, this.getY(), this.getZ()));
    }

    public double getY() {
        return this.getPosition().getY();
    }

    public void setY(double y) {
        this.setPosition(new Vector3d(this.getX(), y, this.getZ()));
    }

    public double getZ() {
        return this.getPosition().getZ();
    }

    public void setZ(double z) {
        this.setPosition(new Vector3d(this.getX(), this.getY(), z));
    }

    public Vector3d getDeltaMovement() {
        return this.values.getDeltaMovement();
    }

    public void setDeltaMovement(Vector3d deltaMovement) {
        this.values.setDeltaMovement(deltaMovement);
    }

    public float getYaw() {
        return this.values.getYaw();
    }

    public void setYaw(float yaw) {
        this.values.setYaw(yaw);
    }

    public float getPitch() {
        return this.values.getPitch();
    }

    public void setPitch(float pitch) {
        this.values.setPitch(pitch);
    }

    /**
     * @deprecated The mask no longer fits in a byte since 1.21.2
     */
    @Deprecated
    public byte getRelativeMask() {
        return this.relativeFlags.getMask();
    }

    /**
     * @deprecated The mask no longer fits in a byte since 1.21.2
     */
    @Deprecated
    public void setRelativeMask(byte relativeMask) {
        this.relativeFlags = new RelativeFlag(relativeMask);
    }

    public boolean isRelativeFlag(RelativeFlag flag) {
        return this.relativeFlags.has(flag);
    }

    public void setRelative(RelativeFlag flag, boolean relative) {
        this.relativeFlags = this.relativeFlags.set(flag, relative);
    }

    public RelativeFlag getRelativeFlags() {
        return this.relativeFlags;
    }

    public void setRelativeFlags(RelativeFlag flags) {
        this.relativeFlags = flags;
    }

    /**
     * Removed with 1.19.3
     */
    @ApiStatus.Obsolete
    public boolean isDismountVehicle() {
        return this.dismountVehicle;
    }

    /**
     * Removed with 1.19.3
     */
    @ApiStatus.Obsolete
    public void setDismountVehicle(boolean dismountVehicle) {
        this.dismountVehicle = dismountVehicle;
    }
}
