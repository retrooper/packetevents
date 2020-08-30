/**
 * Copyright (c) 2020 retrooper
 */
package io.github.retrooper.packetevents.packetwrappers.in.entityaction;

import io.github.retrooper.packetevents.annotations.Nullable;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;
import org.bukkit.entity.Entity;

import java.util.HashMap;

public final class WrappedPacketInEntityAction extends WrappedPacket {
    private static final HashMap<String, PlayerAction> cachedPlayerActionNames = new HashMap<String, PlayerAction>();
    private static Class<?> entityActionClass;
    @Nullable
    private static Class<?> enumPlayerActionClass;
    private static boolean isLowerThan_v_1_8;
    private Entity entity;
    private int entityId;
    private PlayerAction action;
    private int jumpBoost;
    public WrappedPacketInEntityAction(final Object packet) {
        super(packet);
    }

    public static void load() {
        entityActionClass = PacketTypeClasses.Client.ENTITY_ACTION;
        isLowerThan_v_1_8 = version.isLowerThan(ServerVersion.v_1_8);
        if (version.isHigherThan(ServerVersion.v_1_7_10)) {
            enumPlayerActionClass = Reflection.getSubClass(entityActionClass, "EnumPlayerAction");
        }
        //All the already existing values
        for (PlayerAction val : PlayerAction.values()) {
            cachedPlayerActionNames.put(val.name(), val);
        }

        cachedPlayerActionNames.put("PRESS_SHIFT_KEY", PlayerAction.START_SNEAKING);
        cachedPlayerActionNames.put("RELEASE_SHIFT_KEY", PlayerAction.STOP_SNEAKING);
        cachedPlayerActionNames.put("RIDING_JUMP", PlayerAction.START_RIDING_JUMP);
    }

    @Override
    protected void setup() {
        try {
            final int entityId = Reflection.getField(entityActionClass, int.class, 0).getInt(packet);
            final int jumpBoost;
            if (isLowerThan_v_1_8) {
                jumpBoost = Reflection.getField(entityActionClass, int.class, 2).getInt(packet);
            } else {
                jumpBoost = Reflection.getField(entityActionClass, int.class, 1).getInt(packet);
            }

            int animationIndex = -1;

            //1.7.10
            if (isLowerThan_v_1_8) {
                animationIndex = Reflection.getField(entityActionClass, int.class, 1).getInt(packet); // TODO: += if packetdataserializer.version < 16
            } else {
                final Object enumObj = Reflection.getField(entityActionClass, enumPlayerActionClass, 0).get(packet);
                final String enumName = enumObj.toString();
                this.action = cachedPlayerActionNames.get(enumName);
            }


            this.entityId = entityId;
            this.jumpBoost = jumpBoost;
            if (animationIndex != -1) {
                this.action = PlayerAction.values()[animationIndex];
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Entity getEntity() {
        return NMSUtils.getEntityById(this.entityId);
    }

    public int getEntityId() {
        return entityId;
    }

    public PlayerAction getAction() {
        return action;
    }

    public int getJumpBoost() {
        return jumpBoost;
    }

    public enum PlayerAction {
        START_SNEAKING,
        STOP_SNEAKING,
        STOP_SLEEPING,
        START_SPRINTING,
        STOP_SPRINTING,
        START_RIDING_JUMP,
        STOP_RIDING_JUMP,
        OPEN_INVENTORY,
        START_FALL_FLYING
    }

}
