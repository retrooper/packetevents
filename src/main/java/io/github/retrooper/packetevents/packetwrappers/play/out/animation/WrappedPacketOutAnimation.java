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

package io.github.retrooper.packetevents.packetwrappers.play.out.animation;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;

public final class WrappedPacketOutAnimation extends WrappedPacketEntityAbstraction implements SendableWrapper {
    private static boolean v_1_17;
    private static Constructor<?> animationConstructor;
    private EntityAnimationType type;

    public WrappedPacketOutAnimation(final NMSPacket packet) {
        super(packet, v_1_17 ? 6 : 0);
    }

    public WrappedPacketOutAnimation(final Entity target, final EntityAnimationType type) {
        super(v_1_17 ? 6 : 0);
        this.entityID = target.getEntityId();
        this.entity = target;
        this.type = type;
    }

    public WrappedPacketOutAnimation(final int entityID, final EntityAnimationType type) {
        super(v_1_17 ? 6 : 0);
        this.entityID = entityID;
        this.entity = null;
        this.type = type;
    }

    @Override
    protected void load() {
        v_1_17 = version.isNewerThanOrEquals(ServerVersion.v_1_17);
        try {
            animationConstructor = PacketTypeClasses.Play.Server.ANIMATION.getConstructor(NMSUtils.nmsEntityClass, int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public EntityAnimationType getAnimationType() {
        if (packet != null) {
            byte id = (byte) readInt(v_1_17 ? 7 : 1);
            return EntityAnimationType.values()[id];
        } else {
            return type;
        }
    }

    public void setAnimationType(EntityAnimationType type) {
        if (packet != null) {
            writeInt(v_1_17 ? 7 : 1, type.ordinal());
        } else {
            this.type = type;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        Object nmsEntity = NMSUtils.getNMSEntity(getEntity());
        return animationConstructor.newInstance(nmsEntity, getAnimationType().ordinal());
    }

    public enum EntityAnimationType {
        SWING_MAIN_ARM, TAKE_DAMAGE, LEAVE_BED,
        SWING_OFFHAND, CRITICAL_EFFECT, MAGIC_CRITICAL_EFFECT
    }
}
