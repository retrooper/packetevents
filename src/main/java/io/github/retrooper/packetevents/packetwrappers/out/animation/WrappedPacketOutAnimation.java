package io.github.retrooper.packetevents.packetwrappers.out.animation;

import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.Sendable;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public final class WrappedPacketOutAnimation extends WrappedPacket implements Sendable {
    private static Class<?> animationClass, nmsEntityClass;
    private static Constructor<?> animationConstructor;

    private static final HashMap<Integer, EntityAnimationType> cachedAnimationIDS = new HashMap<Integer, EntityAnimationType>();
    private static final HashMap<EntityAnimationType, Integer> cachedAnimations = new HashMap<EntityAnimationType, Integer>();
    private Entity entity;
    private int entityID;
    private EntityAnimationType type;
    public WrappedPacketOutAnimation(final Object packet) {
        super(packet);
    }

    public WrappedPacketOutAnimation(final Entity target, final EntityAnimationType type) {
        super();
        this.entityID = target.getEntityId();
        this.entity = target;
        this.type = type;
    }

    public static void load() {
        animationClass = PacketTypeClasses.Server.ANIMATION;
        try {
            nmsEntityClass = NMSUtils.getNMSClass("Entity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            animationConstructor = animationClass.getConstructor(nmsEntityClass, int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        cachedAnimationIDS.put(0, EntityAnimationType.SWING_MAIN_ARM);
        cachedAnimationIDS.put(1, EntityAnimationType.TAKE_DAMAGE);
        cachedAnimationIDS.put(2, EntityAnimationType.LEAVE_BED);
        cachedAnimationIDS.put(3, EntityAnimationType.SWING_OFFHAND);
        cachedAnimationIDS.put(4, EntityAnimationType.CRITICAL_EFFECT);
        cachedAnimationIDS.put(5, EntityAnimationType.MAGIC_CRITICAL_EFFECT);

        cachedAnimations.put(EntityAnimationType.SWING_MAIN_ARM, 0);
        cachedAnimations.put(EntityAnimationType.TAKE_DAMAGE, 1);
        cachedAnimations.put(EntityAnimationType.LEAVE_BED, 2);
        cachedAnimations.put(EntityAnimationType.SWING_OFFHAND, 3);
        cachedAnimations.put(EntityAnimationType.CRITICAL_EFFECT, 4);
        cachedAnimations.put(EntityAnimationType.MAGIC_CRITICAL_EFFECT, 5);
    }

    @Override
    protected void setup() {
        try {
            //a - ID
            //b - TYPE
            this.entityID = Reflection.getField(animationClass, int.class, 0).getInt(packet);
            int animationID = Reflection.getField(animationClass, int.class, 1).getInt(packet);
            this.type = cachedAnimationIDS.get(animationID);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public Entity getEntity() {
        return NMSUtils.getEntityById(this.entityID);
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
        final int index = cachedAnimations.get(type);
        try {
            return animationConstructor.newInstance(nmsEntity, index);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public enum EntityAnimationType {
        SWING_MAIN_ARM, TAKE_DAMAGE, LEAVE_BED, SWING_OFFHAND, CRITICAL_EFFECT, MAGIC_CRITICAL_EFFECT
    }
}
