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
import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Mojang name: ClientboundAnimatePacket
 */
public class WrapperPlayServerEntityAnimation extends PacketWrapper<WrapperPlayServerEntityAnimation> {
    private int entityID;
    private EntityAnimationType type;

    public WrapperPlayServerEntityAnimation(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerEntityAnimation(int entityID, EntityAnimationType type) {
        super(PacketType.Play.Server.ENTITY_ANIMATION);
        this.entityID = entityID;
        this.type = type;
    }

    @Override
    public void read() {
        entityID = readVarInt();
        this.type = EntityAnimationType.getById(this.serverVersion, this.readUnsignedByte());
    }

    @Override
    public void write() {
        writeVarInt(entityID);
        this.writeByte(this.type.getId(this.serverVersion));
    }

    @Override
    public void copy(WrapperPlayServerEntityAnimation wrapper) {
        entityID = wrapper.entityID;
        type = wrapper.type;
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityId(int entityID) {
        this.entityID = entityID;
    }

    public EntityAnimationType getType() {
        return type;
    }

    public void setType(EntityAnimationType type) {
        this.type = type;
    }

    public enum EntityAnimationType {

        /**
         * @versions -26.2
         */
        @ApiStatus.Obsolete
        SWING_MAIN_ARM(0, -1),
        /**
         * @versions -1.19.3
         */
        @ApiStatus.Obsolete
        HURT(1, -1),
        WAKE_UP(2, 0),
        /**
         * @versions -26.2
         */
        @ApiStatus.Obsolete
        SWING_OFF_HAND(3, -1),
        CRITICAL_HIT(4, 1),
        MAGIC_CRITICAL_HIT(5, 2),
        ;

        private static final EntityAnimationType[] LEGACY_VALUES = Arrays.stream(values())
                .filter(type -> type.legacyId != -1)
                .sorted(Comparator.comparingInt(type -> type.legacyId))
                .toArray(EntityAnimationType[]::new);
        private static final EntityAnimationType[] VALUES = Arrays.stream(values())
                .filter(type -> type.id != -1)
                .sorted(Comparator.comparingInt(type -> type.id))
                .toArray(EntityAnimationType[]::new);

        private final int legacyId;
        private final int id;

        EntityAnimationType(int legacyId, int id) {
            this.legacyId = legacyId;
            this.id = id;
        }

        public static EntityAnimationType getById(ServerVersion version, int id) {
            return (version.isNewerThanOrEquals(ServerVersion.V_26_3) ? VALUES : LEGACY_VALUES)[id];
        }

        @Deprecated
        public static EntityAnimationType getById(int id) {
            return values()[id];
        }

        public int getId(ServerVersion version) {
            int id = version.isNewerThanOrEquals(ServerVersion.V_26_3) ? this.id : this.legacyId;
            if (id < 0) {
                throw new IllegalStateException(this + " has no id on " + version);
            }
            return id;
        }
    }
}
