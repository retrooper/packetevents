/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.packetwrappers;

import io.github.retrooper.packetevents.exceptions.WrapperFieldNotFoundException;
import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.in.blockdig.WrappedPacketInBlockDig;
import io.github.retrooper.packetevents.packetwrappers.in.blockplace.WrappedPacketInBlockPlace;
import io.github.retrooper.packetevents.packetwrappers.in.clientcommand.WrappedPacketInClientCommand;
import io.github.retrooper.packetevents.packetwrappers.in.custompayload.WrappedPacketInCustomPayload;
import io.github.retrooper.packetevents.packetwrappers.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.in.keepalive.WrappedPacketInKeepAlive;
import io.github.retrooper.packetevents.packetwrappers.in.settings.WrappedPacketInSettings;
import io.github.retrooper.packetevents.packetwrappers.in.updatesign.WrappedPacketInUpdateSign;
import io.github.retrooper.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
import io.github.retrooper.packetevents.packetwrappers.in.windowclick.WrappedPacketInWindowClick;
import io.github.retrooper.packetevents.packetwrappers.out.abilities.WrappedPacketOutAbilities;
import io.github.retrooper.packetevents.packetwrappers.out.animation.WrappedPacketOutAnimation;
import io.github.retrooper.packetevents.packetwrappers.out.chat.WrappedPacketOutChat;
import io.github.retrooper.packetevents.packetwrappers.out.custompayload.WrappedPacketOutCustomPayload;
import io.github.retrooper.packetevents.packetwrappers.out.entity.WrappedPacketOutEntity;
import io.github.retrooper.packetevents.packetwrappers.out.entityvelocity.WrappedPacketOutEntityVelocity;
import io.github.retrooper.packetevents.packetwrappers.out.gamestatechange.WrappedPacketOutGameStateChange;
import io.github.retrooper.packetevents.packetwrappers.out.keepalive.WrappedPacketOutKeepAlive;
import io.github.retrooper.packetevents.packetwrappers.out.kickdisconnect.WrappedPacketOutKickDisconnect;
import io.github.retrooper.packetevents.packetwrappers.out.position.WrappedPacketOutPosition;
import io.github.retrooper.packetevents.packetwrappers.out.transaction.WrappedPacketOutTransaction;
import io.github.retrooper.packetevents.packetwrappers.out.updatehealth.WrappedPacketOutUpdateHealth;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WrappedPacket implements WrapperPacketReader {
    private static final HashMap<Class<?>, HashMap<Class<?>, Field[]>> fieldCache = new HashMap<>();
    public static ServerVersion version;
    protected final Player player;
    protected Object packet;
    private Class<?> packetClass;

    public WrappedPacket() {
        this(null, null, null);
    }

    public WrappedPacket(final Object packet) {
        this(packet, packet.getClass());
    }

    public WrappedPacket(final Object packet, final Class<?> packetClass) {
        this(null, packet, packetClass);
    }

    public WrappedPacket(final Player player, final Object packet) {
        this(player, packet, packet.getClass());
    }

    public WrappedPacket(final Player player, final Object packet, Class<?> packetClass) {
        if (packet == null) {
            this.player = null;
            return;
        }
        if (packet.getClass().getSuperclass().equals(PacketTypeClasses.Client.FLYING)) {
            packetClass = PacketTypeClasses.Client.FLYING;
        } else if (packet.getClass().getSuperclass().equals(PacketTypeClasses.Server.ENTITY)) {
            packetClass = PacketTypeClasses.Server.ENTITY;
        }
        this.packetClass = packetClass;


        if (!fieldCache.containsKey(packetClass)) {
            final Field[] declaredFields = packetClass.getDeclaredFields();
            List<Field> boolFields = new ArrayList<>();
            List<Field> byteFields = new ArrayList<>();
            List<Field> shortFields = new ArrayList<>();
            List<Field> intFields = new ArrayList<>();
            List<Field> longFields = new ArrayList<>();
            List<Field> floatFields = new ArrayList<>();
            List<Field> doubleFields = new ArrayList<>();
            List<Field> stringFields = new ArrayList<>();

            List<Field> boolArrayFields = new ArrayList<>();
            List<Field> byteArrayFields = new ArrayList<>();
            List<Field> shortArrayFields = new ArrayList<>();
            List<Field> intArrayFields = new ArrayList<>();
            List<Field> longArrayFields = new ArrayList<>();
            List<Field> floatArrayFields = new ArrayList<>();
            List<Field> doubleArrayFields = new ArrayList<>();
            List<Field> stringArrayFields = new ArrayList<>();

            for (Field f : declaredFields) {
                f.setAccessible(true);
                if (f.getType().equals(boolean.class)) {
                    boolFields.add(f);
                } else if (f.getType().equals(byte.class)) {
                    byteFields.add(f);
                } else if (f.getType().equals(short.class)) {
                    shortFields.add(f);
                } else if (f.getType().equals(int.class)) {
                    intFields.add(f);
                } else if (f.getType().equals(long.class)) {
                    longFields.add(f);
                } else if (f.getType().equals(float.class)) {
                    floatFields.add(f);
                } else if (f.getType().equals(double.class)) {
                    doubleFields.add(f);
                } else if (f.getType().equals(String.class)) {
                    stringFields.add(f);
                } else if (f.getType().equals(boolean[].class)) {
                    boolArrayFields.add(f);
                } else if (f.getType().equals(byte[].class)) {
                    byteArrayFields.add(f);
                } else if (f.getType().equals(short[].class)) {
                    shortArrayFields.add(f);
                } else if (f.getType().equals(int[].class)) {
                    intArrayFields.add(f);
                } else if (f.getType().equals(long[].class)) {
                    longArrayFields.add(f);
                } else if (f.getType().equals(float[].class)) {
                    floatArrayFields.add(f);
                } else if (f.getType().equals(double[].class)) {
                    doubleArrayFields.add(f);
                } else if (f.getType().equals(String[].class)) {
                    stringArrayFields.add(f);
                }
            }

            HashMap<Class<?>, Field[]> map = new HashMap<>();
            if (!boolFields.isEmpty()) {
                map.put(boolean.class, boolFields.toArray(new Field[0]));
            }

            if (!byteFields.isEmpty()) {
                map.put(byte.class, byteFields.toArray(new Field[0]));
            }

            if (!shortFields.isEmpty()) {
                map.put(short.class, shortFields.toArray(new Field[0]));
            }

            if (!intFields.isEmpty()) {
                map.put(int.class, intFields.toArray(new Field[0]));
            }

            if (!longFields.isEmpty()) {
                map.put(long.class, longFields.toArray(new Field[0]));
            }

            if (!floatFields.isEmpty()) {
                map.put(float.class, floatFields.toArray(new Field[0]));
            }

            if (!doubleFields.isEmpty()) {
                map.put(double.class, doubleFields.toArray(new Field[0]));
            }

            if (!stringFields.isEmpty()) {
                map.put(String.class, stringFields.toArray(new Field[0]));
            }

            if (!boolArrayFields.isEmpty()) {
                map.put(boolean[].class, boolArrayFields.toArray(new Field[0]));
            }

            if (!byteArrayFields.isEmpty()) {
                map.put(byte[].class, byteArrayFields.toArray(new Field[0]));
            }

            if (!shortArrayFields.isEmpty()) {
                map.put(short[].class, shortArrayFields.toArray(new Field[0]));
            }

            if (!intArrayFields.isEmpty()) {
                map.put(int[].class, intArrayFields.toArray(new Field[0]));
            }

            if (!longArrayFields.isEmpty()) {
                map.put(long[].class, longArrayFields.toArray(new Field[0]));
            }

            if (!floatArrayFields.isEmpty()) {
                map.put(float[].class, floatArrayFields.toArray(new Field[0]));
            }

            if (!doubleArrayFields.isEmpty()) {
                map.put(double[].class, doubleArrayFields.toArray(new Field[0]));
            }

            if (!stringArrayFields.isEmpty()) {
                map.put(String[].class, stringArrayFields.toArray(new Field[0]));
            }
            fieldCache.put(packetClass, map);
        }
        this.player = player;
        this.packet = packet;
        setup();
    }

    public static void loadAllWrappers() {
        //SERVER BOUND
        WrappedPacketInBlockDig.load();
        WrappedPacketInBlockPlace.load();
        WrappedPacketInClientCommand.load();
        WrappedPacketInCustomPayload.load();
        WrappedPacketInEntityAction.load();
        WrappedPacketInKeepAlive.load();
        WrappedPacketInSettings.load();
        WrappedPacketInUseEntity.load();
        WrappedPacketInUpdateSign.load();
        WrappedPacketInWindowClick.load();

        //CLIENTBOUND
        WrappedPacketOutAbilities.load();
        WrappedPacketOutAnimation.load();
        WrappedPacketOutChat.load();
        WrappedPacketOutEntity.load();
        WrappedPacketOutEntityVelocity.load();
        WrappedPacketOutKeepAlive.load();
        WrappedPacketOutKickDisconnect.load();
        WrappedPacketOutPosition.load();
        WrappedPacketOutTransaction.load();
        WrappedPacketOutUpdateHealth.load();
        WrappedPacketOutGameStateChange.load();
        WrappedPacketOutCustomPayload.load();
    }

    protected void setup() {

    }

    @Override
    public boolean readBoolean(int index) {
        if (fieldCache.containsKey(packetClass)) {
            try {
                return fieldCache.get(packetClass).get(boolean.class)[index].getBoolean(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        throw new WrapperFieldNotFoundException(packetClass, boolean.class, index);
    }

    @Override
    public byte readByte(int index) {
        if (fieldCache.containsKey(packetClass)) {
            try {
                return fieldCache.get(packetClass).get(byte.class)[index].getByte(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        throw new WrapperFieldNotFoundException(packetClass, byte.class, index);
    }

    @Override
    public short readShort(int index) {
        if (fieldCache.containsKey(packetClass)) {
            try {
                return fieldCache.get(packetClass).get(short.class)[index].getShort(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        throw new WrapperFieldNotFoundException(packetClass, short.class, index);
    }

    @Override
    public int readInt(int index) {
        if (fieldCache.containsKey(packetClass)) {
            try {
                return fieldCache.get(packetClass).get(int.class)[index].getInt(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        throw new WrapperFieldNotFoundException(packetClass, int.class, index);
    }

    @Override
    public long readLong(int index) {
        if (fieldCache.containsKey(packetClass)) {
            try {
                return fieldCache.get(packetClass).get(long.class)[index].getLong(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        throw new WrapperFieldNotFoundException(packetClass, long.class, index);
    }

    @Override
    public float readFloat(int index) {
        if (fieldCache.containsKey(packetClass)) {
            try {
                return fieldCache.get(packetClass).get(float.class)[index].getFloat(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        throw new WrapperFieldNotFoundException(packetClass, float.class, index);
    }

    @Override
    public double readDouble(int index) {
        if (fieldCache.containsKey(packetClass)) {
            try {
                return fieldCache.get(packetClass).get(double.class)[index].getDouble(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        throw new WrapperFieldNotFoundException(packetClass, double.class, index);
    }

    @Override
    public boolean[] readBooleanArray(int index) {
        if (fieldCache.containsKey(packetClass)) {
            try {
                return (boolean[]) fieldCache.get(packetClass).get(boolean[].class)[index].get(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        int currentIndex = 0;
        for (Field f : packetClass.getDeclaredFields()) {
            f.setAccessible(true);
            if (boolean[].class.isAssignableFrom(f.getType())) {
                if (index == currentIndex++) {
                    try {
                        return (boolean[]) f.get(packet);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        throw new WrapperFieldNotFoundException(packetClass, boolean[].class, index);
    }

    @Override
    public byte[] readByteArray(int index) {
        if (fieldCache.containsKey(packetClass)) {
            try {
                return (byte[]) fieldCache.get(packetClass).get(byte[].class)[index].get(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        int currentIndex = 0;
        for (Field f : packetClass.getDeclaredFields()) {
            f.setAccessible(true);
            if (byte[].class.isAssignableFrom(f.getType())) {
                if (index == currentIndex++) {
                    try {
                        return (byte[]) f.get(packet);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        throw new WrapperFieldNotFoundException(packetClass, byte[].class, index);
    }

    @Override
    public short[] readShortArray(int index) {
        if (fieldCache.containsKey(packetClass)) {
            try {
                return (short[]) fieldCache.get(packetClass).get(short[].class)[index].get(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        int currentIndex = 0;
        for (Field f : packetClass.getDeclaredFields()) {
            f.setAccessible(true);
            if (short[].class.isAssignableFrom(f.getType())) {
                if (index == currentIndex++) {
                    try {
                        return (short[]) f.get(packet);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        throw new WrapperFieldNotFoundException(packetClass, short[].class, index);
    }

    @Override
    public int[] readIntArray(int index) {
        if (fieldCache.containsKey(packetClass)) {
            try {
                return (int[]) fieldCache.get(packetClass).get(int[].class)[index].get(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        int currentIndex = 0;
        for (Field f : packetClass.getDeclaredFields()) {
            f.setAccessible(true);
            if (int[].class.isAssignableFrom(f.getType())) {
                if (index == currentIndex++) {
                    try {
                        return (int[]) f.get(packet);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        throw new WrapperFieldNotFoundException(packetClass, int[].class, index);
    }

    @Override
    public long[] readLongArray(int index) {
        if (fieldCache.containsKey(packetClass)) {
            try {
                return (long[]) fieldCache.get(packetClass).get(long[].class)[index].get(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        int currentIndex = 0;
        for (Field f : packetClass.getDeclaredFields()) {
            f.setAccessible(true);
            if (long[].class.isAssignableFrom(f.getType())) {
                if (index == currentIndex++) {
                    try {
                        return (long[]) f.get(packet);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        throw new WrapperFieldNotFoundException(packetClass, long[].class, index);
    }

    @Override
    public float[] readFloatArray(int index) {
        if (fieldCache.containsKey(packetClass)) {
            try {
                return (float[]) fieldCache.get(packetClass).get(float[].class)[index].get(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        int currentIndex = 0;
        for (Field f : packetClass.getDeclaredFields()) {
            f.setAccessible(true);
            if (float.class.isAssignableFrom(f.getType())) {
                if (index == currentIndex++) {
                    try {
                        return (float[]) f.get(packet);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        throw new WrapperFieldNotFoundException(packetClass, float[].class, index);
    }

    @Override
    public double[] readDoubleArray(int index) {
        if (fieldCache.containsKey(packetClass)) {
            try {
                return (double[]) fieldCache.get(packetClass).get(double[].class)[index].get(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        int currentIndex = 0;
        for (Field f : packetClass.getDeclaredFields()) {
            f.setAccessible(true);
            if (double[].class.isAssignableFrom(f.getType())) {
                if (index == currentIndex++) {
                    try {
                        return (double[]) f.get(packet);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        throw new WrapperFieldNotFoundException(packetClass, double[].class, index);
    }

    @Override
    public String[] readStringArray(int index) {
        if (fieldCache.containsKey(packetClass)) {
            try {
                Object[] array = (Object[]) fieldCache.get(packetClass).get(String[].class)[index].get(packet);
                int len = array.length;
                String[] stringArray = new String[len];
                for (int i = 0; i < len; i++) {
                    stringArray[i] = array[i].toString();
                }
                return stringArray;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        throw new WrapperFieldNotFoundException(packetClass, String[].class, index);
    }

    @Override
    public String readString(int index) {
        if (fieldCache.containsKey(packetClass)) {
            try {
                Field[] fields = fieldCache.get(packetClass).get(String.class);
                Object obj = fields[index].get(packet);
                return obj.toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        throw new WrapperFieldNotFoundException(packetClass, String.class, index);
    }

    public Object readAnyObject(int index) {
        try {
            try {
                return packetClass.getDeclaredFields()[index].get(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new WrapperFieldNotFoundException("PacketEvents failed to find any field indexed " + index + " in the " + ClassUtil.getClassSimpleName(packetClass) + " class!");
        }
        return null;
    }

    public Object readObject(int index, Class<?> type) {
        if (fieldCache.containsKey(packetClass)) {
            HashMap<Class<?>, Field[]> map = fieldCache.get(packetClass);
            if (!map.containsKey(type)) {
                List<Field> typeFields = new ArrayList<>();
                for (Field f : packetClass.getDeclaredFields()) {
                    f.setAccessible(true);
                    if (f.getType().equals(type)) {
                        typeFields.add(f);
                    }
                }
                if (!typeFields.isEmpty()) {
                    map.put(type, typeFields.toArray(new Field[0]));
                }
            }

        }
        try {
            return fieldCache.get(packetClass).get(type)[index].get(packet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new WrapperFieldNotFoundException(packetClass, type, index);
    }

    public Object[] readObjectArray(int index, Class<?> type) {
        return (Object[]) readObject(index, type);
    }
}
