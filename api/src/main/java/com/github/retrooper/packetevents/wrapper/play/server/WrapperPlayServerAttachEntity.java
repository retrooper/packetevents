/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerAttachEntity extends PacketWrapper<WrapperPlayServerAttachEntity> {
    private int attachedId;
    private int holdingId;
    private boolean leash;

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
    public void read() {
        this.attachedId = readInt();
        this.holdingId = readInt();
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_8_8)) {
            // YES, vanilla uses == 1 and not != 0
            this.leash = readUnsignedByte() == 1;
        } else {
            this.leash = true;
        }
    }

    @Override
    public void write() {
        writeInt(attachedId);
        writeInt(holdingId);
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_8_8)) {
            writeByte(leash ? 1 : 0);
        }
    }

    @Override
    public void copy(WrapperPlayServerAttachEntity wrapper) {
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
