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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientUseItem extends PacketWrapper<WrapperPlayClientUseItem> {

    private InteractionHand hand;
    private int sequence;
    private float yaw; // yRot
    private float pitch; // xRot

    public WrapperPlayClientUseItem(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientUseItem(InteractionHand hand) {
        this(hand, 0);
    }

    public WrapperPlayClientUseItem(InteractionHand hand, int sequence) {
        this(hand, sequence, 0f, 0f);
    }

    public WrapperPlayClientUseItem(InteractionHand hand, int sequence, float yaw, float pitch) {
        super(PacketType.Play.Client.USE_ITEM);
        this.hand = hand;
        this.sequence = sequence;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public void read() {
        this.hand = InteractionHand.getById(this.readVarInt());
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            this.sequence = this.readVarInt();
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21)) {
                this.yaw = this.readFloat();
                this.pitch = this.readFloat();
            }
        }
    }

    @Override
    public void write() {
        this.writeVarInt(this.hand.getId());
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            this.writeVarInt(this.sequence);
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21)) {
                this.writeFloat(this.yaw);
                this.writeFloat(this.pitch);
            }
        }
    }

    @Override
    public void copy(WrapperPlayClientUseItem packet) {
        this.hand = packet.hand;
        this.sequence = packet.sequence;
        this.yaw = packet.yaw;
        this.pitch = packet.pitch;
    }

    public InteractionHand getHand() {
        return this.hand;
    }

    public void setHand(InteractionHand hand) {
        this.hand = hand;
    }

    public int getSequence() {
        return this.sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
