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

package io.github.retrooper.packetevents.packetwrappers.play.out.updateattributes;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.attributesnapshot.AttributeSnapshotWrapper;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WrappedPacketOutUpdateAttributes extends WrappedPacketEntityAbstraction implements SendableWrapper {
    private static Constructor<?> packetConstructor;
    private List<AttributeSnapshotWrapper> properties;

    public WrappedPacketOutUpdateAttributes(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutUpdateAttributes(int entityID, List<AttributeSnapshotWrapper> properties) {
        this.entityID = entityID;
        this.properties = properties;
    }

    public WrappedPacketOutUpdateAttributes(Entity entity, List<AttributeSnapshotWrapper> properties) {
        this.entityID = entity.getEntityId();
        this.properties = properties;
    }


    @Override
    protected void load() {
        try {
            packetConstructor = PacketTypeClasses.Play.Server.UPDATE_ATTRIBUTES.getConstructor(int.class, Collection.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public List<AttributeSnapshotWrapper> getProperties() {
        if (packet != null) {
            List<?> list = readObject(0, List.class);
            List<AttributeSnapshotWrapper> attributeSnapshotWrappers = new ArrayList<>(list.size());
            for (Object nmsAttributeSnapshot : list) {
                attributeSnapshotWrappers.add(new AttributeSnapshotWrapper(new NMSPacket(nmsAttributeSnapshot)));
            }
            return attributeSnapshotWrappers;
        } else {
            return properties;
        }
    }

    public void setProperties(List<AttributeSnapshotWrapper> properties) {
        if (packet != null) {
            List<Object> list = new ArrayList<>(properties.size());
            for (AttributeSnapshotWrapper attributeSnapshotWrapper : properties) {
                list.add(attributeSnapshotWrapper.getNMSPacket().getRawNMSPacket());
            }
            writeObject(0, list);
        } else {
            this.properties = properties;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        List<AttributeSnapshotWrapper> properties = getProperties();
        List<Object> nmsProperties = new ArrayList<>(properties.size());
        for (AttributeSnapshotWrapper property : properties) {
            nmsProperties.add(property.getNMSPacket().getRawNMSPacket());
        }
        return packetConstructor.newInstance(getEntityId(), nmsProperties);
    }
}
