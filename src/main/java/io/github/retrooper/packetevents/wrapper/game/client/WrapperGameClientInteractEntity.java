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

import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.manager.player.Hand;
import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufAbstract;
import io.github.retrooper.packetevents.utils.vector.Vector3f;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Optional;

public class WrapperGameClientInteractEntity extends PacketWrapper {
    public enum Type {
        INTERACT, ATTACK, INTERACT_AT;
        public static final Type[] VALUES = values();
    }
    private final int entityID;
    private final Type type;
    private final Optional<Vector3f> target;
    private final Optional<Hand> hand;
    private final Optional<Boolean> sneaking;
    public WrapperGameClientInteractEntity(ClientVersion clientVersion, ByteBufAbstract byteBuf) {
        super(clientVersion, byteBuf);
        if (clientVersion.isOlderThan(ClientVersion.v_1_8)) {
            this.entityID = readInt();
            int typeIndex = readByte() % Type.VALUES.length;
            this.type = Type.VALUES[typeIndex];
            this.target = Optional.empty();
            this.hand= Optional.empty();
            this.sneaking = Optional.empty();
        }
        else {
            this.entityID = readVarInt();
            int typeIndex = readVarInt();
            this.type = Type.VALUES[typeIndex];

            if (type == Type.INTERACT_AT) {
                float x = readFloat();
                float y = readFloat();
                float z = readFloat();
                this.target = Optional.of(new Vector3f(x, y, z));
            }
            else {
                this.target = Optional.empty();
            }

            if (clientVersion.isNewerThan(ClientVersion.v_1_8) && (type == Type.INTERACT || type == Type.INTERACT_AT)) {
                int handIndex = readVarInt();
                this.hand = Optional.of(Hand.VALUES[handIndex]);
            }
            else {
                this.hand = Optional.empty();
            }

            if (clientVersion.isNewerThanOrEquals(ClientVersion.v_1_16)) {
                this.sneaking = Optional.of(readBoolean());
            }
            else {
                this.sneaking = Optional.empty();
            }
        }
    }

    public int getEntityID() {
        return entityID;
    }

    public Type getType() {
        return type;
    }

    public Optional<Vector3f> getTarget() {
        return target;
    }

    public Optional<Hand> getHand() {
        return hand;
    }

    public Optional<Boolean> isSneaking() {
        return sneaking;
    }
}
