package io.github.retrooper.packetevents.packetwrappers.out.animation;

import io.github.retrooper.packetevents.enums.minecraft.EntityAnimationType;
import io.github.retrooper.packetevents.packetwrappers.Sendable;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class WrappedPacketOutAnimation extends WrappedPacket implements Sendable {
    private static Class<?> animationClass, nmsEntityClass;
    private static Constructor<?> animationConstructor;

    static {
        try {
            animationClass = NMSUtils.getNMSClass("PacketPlayOutAnimation");
            nmsEntityClass = NMSUtils.getNMSClass("Entity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            animationConstructor = animationClass.getConstructor(nmsEntityClass, int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private Entity entity;
    private int entityID;
    private EntityAnimationType type;

    public WrappedPacketOutAnimation(final Object packet) {
        super(packet);
    }

    public WrappedPacketOutAnimation(final Entity target, final EntityAnimationType type) {
        super(null);
        this.entityID = target.getEntityId();
        this.entity = target;
        this.type = type;
    }

    @Override
    protected void setup() {
        try {
            //a - ID
            //b - TYPE
            this.entityID = Reflection.getField(animationClass, int.class, 0).getInt(packet);
            this.entity = NMSUtils.getEntityById(this.entityID);
            this.type = EntityAnimationType.get(Reflection.getField(animationClass, int.class, 1).getInt(packet));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public Entity getEntity() {
        return this.entity;
    }

    public int getEntityId() {
        return this.entityID;
    }

    public EntityAnimationType getAnimationType() {
        return type;
    }

    @Override
    public Object asNMSPacket() {
        final Object nmsEntity = NMSUtils.getNMSEntity(this.entity);
        final int index = type.getIndex();
        try {
            return animationConstructor.newInstance(nmsEntity, index);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
