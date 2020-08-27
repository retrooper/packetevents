/**
 * Copyright (c) 2020 retrooper
 */
package io.github.retrooper.packetevents.packetwrappers.out.updatehealth;

import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.Sendable;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class WrappedPacketOutUpdateHealth extends WrappedPacket implements Sendable {
    private static Class<?> packetClass;
    private static Constructor<?> packetConstructor;
    private float health, foodSaturation;
    private int food;
    public WrappedPacketOutUpdateHealth(final Object packet) {
        super(packet);
    }

    /**
     * See https://wiki.vg/Protocol#Update_Health
     *
     * @param health 0 or less = dead, 20 = full HP
     * @param food 0–20
     * @param foodSaturation Seems to vary from 0.0 to 5.0 in integer increments
     */
    public WrappedPacketOutUpdateHealth(final float health, final int food, final float foodSaturation) {
        super();
        this.health = health;
        this.food = food;
        this.foodSaturation = foodSaturation;
    }

    public static void load() {
        packetClass = PacketTypeClasses.Server.UPDATE_HEALTH;

        try {
            packetConstructor = packetClass.getConstructor(float.class, int.class, float.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setup() {
        try {
            this.health = Reflection.getField(packetClass, float.class, 0).getFloat(packet);
            this.foodSaturation = Reflection.getField(packetClass, float.class, 0).getFloat(packet);
            this.food = Reflection.getField(packetClass, int.class, 0).getInt(packet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public float getHealth() {
        return health;
    }

    public float getFoodSaturation() {
        return foodSaturation;
    }

    public int getFood() {
        return food;
    }

    @Override
    public Object asNMSPacket() {
        try {
            return packetConstructor.newInstance(health, food, foodSaturation);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
