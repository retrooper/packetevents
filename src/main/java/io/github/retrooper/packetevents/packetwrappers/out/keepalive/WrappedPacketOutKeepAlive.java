/**
 * Copyright (c) 2020 retrooper
 */
package io.github.retrooper.packetevents.packetwrappers.out.keepalive;

import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.Sendable;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class WrappedPacketOutKeepAlive extends WrappedPacket implements Sendable {
    private static Class<?> packetClass;
    private static Constructor<?> keepAliveConstructor;
    private static boolean integerMode;
    private long id;

    public WrappedPacketOutKeepAlive(Object packet) {
        super(packet);
    }

    public WrappedPacketOutKeepAlive(long id) {
        super();
    }

    public static void load() {
        packetClass = PacketTypeClasses.Server.KEEP_ALIVE;
        integerMode = Reflection.getField(packetClass, int.class, 0) != null;

        if (integerMode) {
            try {
                keepAliveConstructor = packetClass.getConstructor(int.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else {
            try {
                keepAliveConstructor = packetClass.getConstructor(long.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void setup() {
        if (!integerMode) {
            try {
                this.id = Reflection.getField(packetClass, long.class, 0).getLong(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            try {
                this.id = Reflection.getField(packetClass, int.class, 0).getInt(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * You can cast this long to an integer if you are using 1.7.10->1.12.2!
     * In 1.13.2->1.16.2 a long is sent
     *
     * @return Keep Alive ID
     */
    public long getId() {
        return id;
    }

    @Override
    public Object asNMSPacket() {
        if (integerMode) {
            try {
                return keepAliveConstructor.newInstance((int) id);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            try {
                return keepAliveConstructor.newInstance(id);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
