package io.github.retrooper.packetevents.packetwrappers.play.out.namedentityspawn;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.play.out.entity.WrappedPacketOutEntity;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.WrappedEntityPlayer;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class WrappedPacketOutNamedEntitySpawn extends WrappedPacket implements SendableWrapper {
    private static final float rotationDividend = 256.0F / 360.0F;
    private static boolean doublesPresent, dataWatcherPresent;
    private static Constructor<?> packetConstructor;
    private static Constructor<?> packetDefaultConstructor;
    private int entityID;
    private UUID uuid;
    private Location location;
    private WrappedEntityPlayer entityPlayer;

    public WrappedPacketOutNamedEntitySpawn(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutNamedEntitySpawn(WrappedEntityPlayer entityPlayer) {
        this.entityID = entityPlayer.getEntityId();
        this.uuid = entityPlayer.getUUID();
        this.entityPlayer = entityPlayer;
    }


    @Override
    protected void load() {
        doublesPresent = Reflection.getField(PacketTypeClasses.Play.Server.NAMED_ENTITY_SPAWN, double.class, 1) != null;
        dataWatcherPresent = Reflection.getField(PacketTypeClasses.Play.Server.NAMED_ENTITY_SPAWN, NMSUtils.dataWatcherClass, 0) != null;
        try {
            packetConstructor = PacketTypeClasses.Play.Server.NAMED_ENTITY_SPAWN.getConstructor(NMSUtils.nmsEntityHumanClass);
            packetDefaultConstructor = PacketTypeClasses.Play.Server.NAMED_ENTITY_SPAWN.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public UUID getUUID() {
        if (packet != null) {
            return readObject(0, UUID.class);
        } else {
            return uuid;
        }
    }

    public void setUUID(UUID uuid) {
        if (packet != null) {
            write(UUID.class, 0, uuid);
        } else {
            this.uuid = uuid;
        }
    }

    public Location getLocation(World world) {
        if (packet != null) {
            double x;
            double y;
            double z;
            if (doublesPresent) {
                x = readDouble(0);
                y = readDouble(1);
                z = readDouble(2);
            } else {
                x = readInt(1) / 32.0D; //TODO fix on 1.16, its doubles
                y = readInt(2) / 32.0D;
                z = readInt(3) / 32.0D;
            }
            float yaw = readByte(0) / rotationDividend;
            float pitch = readByte(1) / rotationDividend;

            return new Location(world, x, y, z, yaw, pitch);
        } else {
            return location;
        }
    }

    public void setLocation(Location location) {
        setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public void setLocation(double x, double y, double z, float yaw, float pitch) {
        if (packet != null) {
            if (doublesPresent) {
                writeDouble(0, x);
                writeDouble(1, y);
                writeDouble(2, z);
            } else {
                writeInt(1, (int) (x * 32.0D));
                writeInt(2, (int) (y * 32.0D));
                writeInt(3, (int) (z * 32.0D));
            }
            writeByte(0, (byte) (yaw * rotationDividend));
            writeByte(1, (byte) (pitch * rotationDividend));
        } else {
            this.location = new Location(null, x, y, z, yaw, pitch);
        }
    }

    public int getEntityId() {
        if (packet != null) {
            return readInt(0);
        } else {
            return entityID;
        }
    }

    public void setEntityId(int entityID) {
        if (packet != null) {
            writeInt(0, entityID);
        } else {
            this.entityID = entityID;
        }
    }

    @Override
    public Object asNMSPacket() {
        try {
            Object packetPlayOutNamedEntitySpawnInstance = null;
            if (packet != null) {
               packetPlayOutNamedEntitySpawnInstance = packetDefaultConstructor.newInstance();
                WrappedPacketOutNamedEntitySpawn wrappedPacketOutNamedEntitySpawn = new WrappedPacketOutNamedEntitySpawn(new NMSPacket(packetPlayOutNamedEntitySpawnInstance));
                wrappedPacketOutNamedEntitySpawn.setEntityId(getEntityId());
                wrappedPacketOutNamedEntitySpawn.setLocation(getLocation(null));
                wrappedPacketOutNamedEntitySpawn.setUUID(getUUID());
                if (dataWatcherPresent) {
                    wrappedPacketOutNamedEntitySpawn.write(NMSUtils.dataWatcherClass, 0, NMSUtils.generateDataWatcher(null));
                }
            }
            else {
                packetPlayOutNamedEntitySpawnInstance = packetConstructor.newInstance(entityPlayer.getRawEntityPlayer());
            }
            return packetPlayOutNamedEntitySpawnInstance;

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
