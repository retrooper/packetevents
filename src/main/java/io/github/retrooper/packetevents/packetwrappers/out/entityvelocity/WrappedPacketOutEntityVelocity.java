package io.github.retrooper.packetevents.packetwrappers.out.entityvelocity;

import io.github.retrooper.packetevents.enums.ServerVersion;
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

    public WrappedPacketOutEntityVelocity(final Object packet) {
        super(packet);
    }

    public WrappedPacketOutEntityVelocity(final Entity entity, final double velocityX, final double velocityY, final double velocityZ) {
        super(null);
        this.entityId = entity.getEntityId();
        this.entity = entity;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
    }

    @Override
    protected void setup() {
        try {
            int i = 0;
            final int entityId = fields[0].getInt(packet);

            final int x = fields[1].getInt(packet);
            final int y = fields[2].getInt(packet);
            final int z = fields[3].getInt(packet);

            this.entityId = entityId;
            this.entity = NMSUtils.getEntityById(this.entityId);

            this.velocityX = x / 8000.0D;
            this.velocityY = y / 8000.0D;
            this.velocityZ = z / 8000.0D;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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
        if (version.isLowerThan(ServerVersion.v_1_14)) {
            try {
                return velocityConstructor.newInstance(entityId, velocityX, velocityY, velocityZ);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            try {
                return velocityConstructor.newInstance(entityId, vec3dConstructor.newInstance(velocityX, velocityY, velocityZ));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private static Constructor<?> velocityConstructor, vec3dConstructor;
    private static Class<?> velocityClass, vec3dClass;

    private static Field[] fields = new Field[4];


    /**
     * Velocity Constructor parameter types:
     * 1.7.10->1.13.2 use int, double, double, double style,
     * 1.14+ use int, Vec3D style
     */
    static {
        try {

            velocityClass = NMSUtils.getNMSClass("PacketPlayOutEntityVelocity");
            if (version.isHigherThan(ServerVersion.v_1_13_2)) {
                vec3dClass = NMSUtils.getNMSClass("Vec3D");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            if (version.isHigherThan(ServerVersion.v_1_13_2)) {
                velocityConstructor = velocityClass.getConstructor(int.class, vec3dClass);
                //vec3d constructor
                vec3dConstructor = vec3dClass.getConstructor(double.class, double.class, double.class);
            } else {
                velocityConstructor = velocityClass.getConstructor(int.class, double.class, double.class, double.class);
            }
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
