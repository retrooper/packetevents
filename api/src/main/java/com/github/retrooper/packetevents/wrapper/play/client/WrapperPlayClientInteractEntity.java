/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.Hand;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Optional;

/**
 * This packet is sent from the client to the server when the client attacks or right-clicks another entity (a player, minecart, etc).
 * The vanilla server discards this packet if the entity being attacked is not within a 4-unit radius of the player's position.
 * Please note that this packet is NOT sent whenever the client middle-clicks, the {@link WrapperPlayClientCreativeInventoryAction} packet is sent instead.
 */
public class WrapperPlayClientInteractEntity extends PacketWrapper<WrapperPlayClientInteractEntity> {
    private int entityID;
    private Type type;
    private Optional<Vector3f> target;
    private Hand hand;
    private Optional<Boolean> sneaking;

    public WrapperPlayClientInteractEntity(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientInteractEntity(int entityID, Type type, Hand hand, Optional<Vector3f> target, Optional<Boolean> sneaking) {
        super(PacketType.Play.Client.INTERACT_ENTITY);
        this.entityID = entityID;
        this.type = type;
        this.hand = hand;
        this.target = target;
        this.sneaking = sneaking;
    }

    @Override
    public void readData() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            this.entityID = readInt();
            byte typeIndex = readByte();
            this.type = Type.VALUES[typeIndex];
            this.target = Optional.empty();
            this.hand = Hand.RIGHT;
            this.sneaking = Optional.empty();
        } else {
            this.entityID = readVarInt();
            int typeIndex = readVarInt();
            this.type = Type.VALUES[typeIndex];
            if (type == Type.INTERACT_AT) {
                float x = readFloat();
                float y = readFloat();
                float z = readFloat();
                this.target = Optional.of(new Vector3f(x, y, z));
            } else {
                this.target = Optional.empty();
            }

            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9) && (type == Type.INTERACT || type == Type.INTERACT_AT)) {
                int handID = readVarInt();
                this.hand = Hand.getByLegacyId(handID);
            } else {
                this.hand = Hand.RIGHT;
            }

            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
                this.sneaking = Optional.of(readBoolean());
            } else {
                this.sneaking = Optional.empty();
            }
        }
    }

    @Override
    public void readData(WrapperPlayClientInteractEntity wrapper) {
        this.entityID = wrapper.entityID;
        this.type = wrapper.type;
        this.target = wrapper.target;
        this.hand = wrapper.hand;
        this.sneaking = wrapper.sneaking;
    }

    @Override
    public void writeData() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            writeInt(entityID);
            writeByte(type.ordinal());
        } else {
            writeVarInt(entityID);
            writeVarInt(type.ordinal());
            if (type == Type.INTERACT_AT) {
                Vector3f targetVec = target.orElse(new Vector3f(0.0F, 0.0F, 0.0F));
                writeFloat(targetVec.x);
                writeFloat(targetVec.y);
                writeFloat(targetVec.z);
            }

            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9) && (type == Type.INTERACT || type == Type.INTERACT_AT)) {
                writeVarInt(hand.getLegacyId());
            }

            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
                writeBoolean(sneaking.orElse(false));
            }
        }
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityId(int entityID) {
        this.entityID = entityID;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public Optional<Vector3f> getTarget() {
        return target;
    }

    public void setTarget(Optional<Vector3f> target) {
        this.target = target;
    }

    public Optional<Boolean> isSneaking() {
        return sneaking;
    }

    public void setSneaking(Optional<Boolean> sneaking) {
        this.sneaking = sneaking;
    }

    public enum Type {
        INTERACT, ATTACK, INTERACT_AT;
        public static final Type[] VALUES = values();
    }
}
