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

package io.github.retrooper.packetevents.wrapper.game.client;

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.manager.player.Hand;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.utils.vector.Vector3f;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Optional;

/**
 * This packet is sent from the client to the server when the client attacks or right-clicks another entity (a player, minecart, etc).
 * The vanilla server discards this packet if the entity being attacked is not within a 4-unit radius of the player's position.
 * Please note that this packet is NOT sent whenever the client middle-clicks, the {@link WrapperGameClientCreativeInventoryAction} packet is sent instead.
 */
public class WrapperGameClientInteractEntity extends PacketWrapper<WrapperGameClientInteractEntity> {
    private int entityID;
    private Type type;
    private Optional<Vector3f> target;
    private Optional<Hand> hand;
    private Optional<Boolean> sneaking;

    public WrapperGameClientInteractEntity(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperGameClientInteractEntity(ClientVersion clientVersion, int entityID, Type type, Optional<Vector3f> target, Optional<Hand> hand, Optional<Boolean> sneaking) {
        super(PacketType.Game.Client.INTERACT_ENTITY.getPacketID(clientVersion), clientVersion);
        this.entityID = entityID;
        this.type = type;
        this.target = target;
        this.hand = hand;
        this.sneaking = sneaking;
    }

    @Override
    public void readData() {
        if (clientVersion.isOlderThan(ClientVersion.v_1_8)) {
            this.entityID = readInt();
            byte typeIndex = readByte();
            this.type = Type.VALUES[typeIndex];
            this.target = Optional.empty();
            this.hand = Optional.empty();
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

            if (clientVersion.isNewerThan(ClientVersion.v_1_8) && (type == Type.INTERACT || type == Type.INTERACT_AT)) {
                int handIndex = readVarInt();
                this.hand = Optional.of(Hand.VALUES[handIndex]);
            } else {
                this.hand = Optional.empty();
            }

            if (clientVersion.isNewerThanOrEquals(ClientVersion.v_1_16)) {
                this.sneaking = Optional.of(readBoolean());
            } else {
                this.sneaking = Optional.empty();
            }
        }
    }

    @Override
    public void readData(WrapperGameClientInteractEntity wrapper) {
        this.entityID = wrapper.entityID;
        this.type = wrapper.type;
        this.target = wrapper.target;
        this.hand = wrapper.hand;
        this.sneaking = wrapper.sneaking;
    }

    @Override
    public void writeData() {
        if (clientVersion.isOlderThan(ClientVersion.v_1_8)) {
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

            if (clientVersion.isNewerThan(ClientVersion.v_1_8) && (type == Type.INTERACT || type == Type.INTERACT_AT)) {
                Hand handValue = hand.orElse(Hand.MAIN_HAND);
                writeVarInt(handValue.ordinal());
            }

            if (clientVersion.isNewerThanOrEquals(ClientVersion.v_1_16)) {
                writeBoolean(sneaking.orElse(false));
            }
        }
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Optional<Vector3f> getTarget() {
        return target;
    }

    public void setTarget(Optional<Vector3f> target) {
        this.target = target;
    }

    public Optional<Hand> getHand() {
        return hand;
    }

    public void setHand(Optional<Hand> hand) {
        this.hand = hand;
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
