package io.github.retrooper.packetevents.packetwrappers.play.out.updateattributes;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.attributesnapshot.AttributeSnapshotWrapper;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
//TODO test ;-;
public class WrappedPacketOutUpdateAttributes extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> packetConstructor;
    private Entity entity;
    private int entityID = -1;
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

    @Nullable
    public Entity getEntity() {
        return getEntity(null);
    }


    @Nullable
    public Entity getEntity(@Nullable World world) {
        if (entity == null) {
            entity = NMSUtils.getEntityById(world, getEntityId());
        }
        return entity;
    }



    public void setEntity(Entity entity) {
        setEntityId(entity.getEntityId());
        this.entity = entity;
    }

    public int getEntityId() {
        if (packet != null) {
            return entityID = readInt(0);
        } else {
            return entityID;
        }
    }

    public void setEntityId(int entityID) {
        if (packet != null) {
            writeInt(0, this.entityID = entityID);
        }
        else {
            this.entityID = entityID;
        }
        this.entity = null;
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
