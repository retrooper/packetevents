/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2016-2021 retrooper and contributors
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

package io.github.retrooper.packetevents.packetwrappers.play.out.setcooldown;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Constructor;
import java.util.IdentityHashMap;
import java.util.Map;

//TODO TEST
public class WrappedPacketOutSetCooldown extends WrappedPacket implements SendableWrapper {
    private static final Map<Short, EntityType> ID_MAP = new IdentityHashMap<>();
    private static Constructor<?> packetConstructor;
    private EntityType type;
    private int cooldownTicks;

    public WrappedPacketOutSetCooldown(EntityType type, int cooldownTicks) {
        this.type = type;
        this.cooldownTicks = cooldownTicks;
    }

    @Override
    protected void load() {
        for (EntityType type : EntityType.values()) {
            ID_MAP.put(type.getTypeId(), type);
        }
        try {
            packetConstructor = PacketTypeClasses.Play.Server.SET_COOLDOWN.getConstructor(NMSUtils.nmsItemClass, int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public EntityType getType() {
        if (packet != null) {
            Object nmsItem = readObject(0, NMSUtils.nmsItemClass);
            short nmsItemID = (short) NMSUtils.getNMSItemId(nmsItem);
            return ID_MAP.get(nmsItemID);
        } else {
            return type;
        }
    }

    public void setType(EntityType type) {
        if (packet != null) {
            Object nmsItem = NMSUtils.getNMSItemById(type.getTypeId());
            write(NMSUtils.nmsItemClass, 0, nmsItem);
        } else {
            this.type = type;
        }
    }

    public int getCooldownTicks() {
        if (packet != null) {
            return readInt(0);
        } else {
            return cooldownTicks;
        }
    }

    public void setCooldownTicks(int cooldownTicks) {
        if (packet != null) {
            writeInt(0, cooldownTicks);
        } else {
            this.cooldownTicks = cooldownTicks;
        }
    }

    @Override
    public boolean isSupported() {
        return version.isNewerThan(ServerVersion.v_1_8_8);
    }

    @Override
    public Object asNMSPacket() throws Exception {
        Object nmsItem = NMSUtils.getNMSItemById(getType().getTypeId());
        return packetConstructor.newInstance(nmsItem, getCooldownTicks());
    }
}
