package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerAttachEntity extends PacketWrapper<WrapperPlayServerAttachEntity> {
    int attachedId;
    int holdingId;
    boolean leash = true;

    public WrapperPlayServerAttachEntity(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerAttachEntity(int attachedId, int holdingId, boolean leash) {
        super(PacketType.Play.Server.ATTACH_ENTITY);
        this.attachedId = attachedId;
        this.holdingId = holdingId;
        this.leash = leash;
    }

    @Override
    public void readData() {
        this.attachedId = readInt();
        this.holdingId = readInt();
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_8_8)) {
            // YES, vanilla uses == 1 and not != 0
            this.leash = readUnsignedByte() == 1;
        }
    }

    @Override
    public void writeData() {
        writeInt(attachedId);
        writeInt(holdingId);
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_8_8)) {
            writeByte(leash ? 1 : 0);
        }
    }

    @Override
    public void readData(WrapperPlayServerAttachEntity wrapper) {
        this.attachedId = wrapper.attachedId;
        this.holdingId = wrapper.holdingId;
        this.leash = wrapper.leash;
    }

    /**
     * @return entity being leashed or the passenger
     */
    public int getAttachedId() {
        return attachedId;
    }

    /**
     * @param attachedId entity being leashed or the passenger
     */
    public void setAttachedId(int attachedId) {
        this.attachedId = attachedId;
    }

    /**
     * @return entity holding the leash or the vehicle
     */
    public int getHoldingId() {
        return holdingId;
    }

    /**
     * @param holdingId entity holding the leash or the vehicle
     */
    public void setHoldingId(int holdingId) {
        this.holdingId = holdingId;
    }

    /**
     * @return true if leashing instead of mounting
     * always true for 1.9 and above, due to SetPassengers replacing this packet
     *
     * @see WrapperPlayServerSetPassengers
     */
    public boolean isLeash() {
        return leash;
    }

    /**
     * Only affects 1.8 and below servers
     * @param leash whether packet indicates leashing instead of mounting
     */
    public void setLeash(boolean leash) {
        this.leash = leash;
    }
}
