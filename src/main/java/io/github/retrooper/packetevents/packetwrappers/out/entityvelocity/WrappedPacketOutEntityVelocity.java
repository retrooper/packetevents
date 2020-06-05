package io.github.retrooper.packetevents.packetwrappers.out.entityvelocity;

import io.github.retrooper.packetevents.packetwrappers.Sendable;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.utils.NMSUtils;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class WrappedPacketOutEntityVelocity extends WrappedPacket implements Sendable {
    private Entity entity;
    private int entityId;
    private double velocityX, velocityY, velocityZ;

    private final boolean sending;

    public WrappedPacketOutEntityVelocity(final Object packet) {
        super(packet);
        sending = false;
    }

    public WrappedPacketOutEntityVelocity(final Entity entity, final double velocityX, final double velocityY, final double velocityZ) {
        super(null);
        this.sending = true;
        this.entityId = entity.getEntityId();
        this.entity = entity;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
    }

    @Override
    protected void setup() throws Exception {
        final int entityId = fields[0].getInt(packet);

        final int x = fields[1].getInt(packet);
        final int y = fields[2].getInt(packet);
        final int z = fields[3].getInt(packet);

        this.entityId = entityId;
        this.entity = NMSUtils.getEntityById(this.entityId);

        this.velocityX = x / 8000.0D;
        this.velocityY = y / 8000.0D;
        this.velocityZ = z / 8000.0D;
    }

    public final int getEntityId() {
        return entityId;
    }

    public final Entity getEntity() {
        return entity;
    }

    public final double getVelocityX() {
        return velocityX;
    }

    public final double getVelocityY() {
        return velocityY;
    }

    public final double getVelocityZ() {
        return velocityZ;
    }

    @Override
    public Object asNMSPacket() {
        if(!sending) {
            throw new IllegalStateException("You cannot convert a non sendable wrapped packet to an nms packet!");
        }

        try {
            return velocityConstructor.newInstance(entityId, velocityX, velocityY, velocityZ);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static Constructor<?> velocityConstructor;
    private static Class<?> velocityClass;

    private static Field[] fields = new Field[4];

    static {
        try {
            velocityClass = NMSUtils.getNMSClass("PacketPlayOutEntityVelocity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            velocityConstructor = velocityClass.getConstructor(int.class, double.class, double.class, double.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            //entity id
            fields[0] = velocityClass.getDeclaredField("a");
            //x
            fields[1] = velocityClass.getDeclaredField("b");
            //y
            fields[2] = velocityClass.getDeclaredField("c");
            //z
            fields[3] = velocityClass.getDeclaredField("d");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        for (final Field f : fields) {
            f.setAccessible(true);
        }


    }

}
