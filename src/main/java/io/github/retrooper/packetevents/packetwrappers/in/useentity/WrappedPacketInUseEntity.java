/**
 * Copyright (c) 2020 retrooper
 */
package io.github.retrooper.packetevents.packetwrappers.in.useentity;

import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;
import org.bukkit.entity.Entity;

public final class WrappedPacketInUseEntity extends WrappedPacket {
    private static Class<?> useEntityClass;
    private static Class<?> enumEntityUseActionClass;
    private int entityId;
    private EntityUseAction action;
    public WrappedPacketInUseEntity(final Object packet) {
        super(packet);
    }

    public static void load() {
        useEntityClass = PacketTypeClasses.Client.USE_ENTITY;

        try {
            enumEntityUseActionClass = NMSUtils.getNMSClass("EnumEntityUseAction");
        } catch (ClassNotFoundException e) {
            //That is fine, it is probably a subclass
            enumEntityUseActionClass = Reflection.getSubClass(useEntityClass, "EnumEntityUseAction");
        }
    }

    @Override
    protected void setup() {
        try {
            this.entityId = Reflection.getField(useEntityClass, int.class, 0).getInt(packet);
            final Object useActionEnum = Reflection.getField(useEntityClass, enumEntityUseActionClass, 0).get(packet);
            this.action = EntityUseAction.valueOf(useActionEnum.toString());
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

    public EntityUseAction getAction() {
        return action;
    }

    public enum EntityUseAction {
        INTERACT, INTERACT_AT, ATTACK, INVALID
    }
}
