/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.packetwrappers.play.out.updateattributes;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.attributesnapshot.AttributeSnapshotWrapper;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//TODO test the wrapper ;-;
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
    public Object asNMSPacket() {
        List<AttributeSnapshotWrapper> properties = getProperties();
        List<Object> nmsProperties = new ArrayList<>(properties.size());
        for (AttributeSnapshotWrapper property : properties) {
            nmsProperties.add(property.getNMSPacket().getRawNMSPacket());
        }
        try {
            return packetConstructor.newInstance(getEntityId(), nmsProperties);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
