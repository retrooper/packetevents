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
import io.github.retrooper.packetevents.packetwrappers.login.in.custompayload.WrappedPacketLoginInCustomPayload;
import io.github.retrooper.packetevents.packetwrappers.login.out.custompayload.WrappedPacketLoginOutCustomPayload;
import io.github.retrooper.packetevents.packetwrappers.login.out.setcompression.WrappedPacketLoginOutSetCompression;
import io.github.retrooper.packetevents.packetwrappers.out.abilities.WrappedPacketOutAbilities;
import io.github.retrooper.packetevents.packetwrappers.out.animation.WrappedPacketOutAnimation;
import io.github.retrooper.packetevents.packetwrappers.out.chat.WrappedPacketOutChat;
import io.github.retrooper.packetevents.packetwrappers.out.custompayload.WrappedPacketOutCustomPayload;
import io.github.retrooper.packetevents.packetwrappers.out.entity.WrappedPacketOutEntity;
import io.github.retrooper.packetevents.packetwrappers.out.entitystatus.WrappedPacketOutEntityStatus;
import io.github.retrooper.packetevents.packetwrappers.out.entityteleport.WrappedPacketOutEntityTeleport;
import io.github.retrooper.packetevents.packetwrappers.out.entityvelocity.WrappedPacketOutEntityVelocity;
import io.github.retrooper.packetevents.packetwrappers.out.experience.WrappedPacketOutExperience;
import io.github.retrooper.packetevents.packetwrappers.out.explosion.WrappedPacketOutExplosion;
import io.github.retrooper.packetevents.packetwrappers.out.gamestatechange.WrappedPacketOutGameStateChange;
import io.github.retrooper.packetevents.packetwrappers.out.helditemslot.WrappedPacketOutHeldItemSlot;
import io.github.retrooper.packetevents.packetwrappers.out.keepalive.WrappedPacketOutKeepAlive;
import io.github.retrooper.packetevents.packetwrappers.out.kickdisconnect.WrappedPacketOutKickDisconnect;
import io.github.retrooper.packetevents.packetwrappers.out.position.WrappedPacketOutPosition;
import io.github.retrooper.packetevents.packetwrappers.out.transaction.WrappedPacketOutTransaction;
import io.github.retrooper.packetevents.packetwrappers.out.updatehealth.WrappedPacketOutUpdateHealth;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WrappedPacket implements WrapperPacketReader, WrapperPacketWriter {
    private static final Map<Class<?>, Map<Class<?>, Field[]>> FIELD_CACHE = new HashMap<>();
    public static ServerVersion version;
    protected Object packet;
    private Class<?> packetClass;

    public WrappedPacket() {

    }

    public WrappedPacket(final Object packet) {
        this(packet, packet.getClass());
    }

    public WrappedPacket(final Object packet, Class<?> packetClass) {
        if (packet.getClass().getSuperclass().equals(PacketTypeClasses.Client.FLYING)) {
            packetClass = PacketTypeClasses.Client.FLYING;
        } else if (packet.getClass().getSuperclass().equals(PacketTypeClasses.Server.ENTITY)) {
            packetClass = PacketTypeClasses.Server.ENTITY;
        }
        this.packetClass = packetClass;


        if (!FIELD_CACHE.containsKey(packetClass)) {
            final Field[] declaredFields = packetClass.getDeclaredFields();
            for (Field f : declaredFields) {
                f.setAccessible(true);
            }
            List<Field> boolFields = getFields(boolean.class, declaredFields);
            List<Field> byteFields = getFields(byte.class, declaredFields);
            List<Field> shortFields = getFields(short.class, declaredFields);
            List<Field> intFields = getFields(int.class, declaredFields);
            List<Field> longFields = getFields(long.class, declaredFields);
            List<Field> floatFields = getFields(float.class, declaredFields);
            List<Field> doubleFields = getFields(double.class, declaredFields);
            List<Field> stringFields = getFields(String.class, declaredFields);

            List<Field> boolArrayFields = getFields(boolean[].class, declaredFields);
            List<Field> byteArrayFields = getFields(byte[].class, declaredFields);
            List<Field> shortArrayFields = getFields(short[].class, declaredFields);
            List<Field> intArrayFields = getFields(int[].class, declaredFields);
            List<Field> longArrayFields = getFields(long[].class, declaredFields);
            List<Field> floatArrayFields = getFields(float[].class, declaredFields);
            List<Field> doubleArrayFields = getFields(double[].class, declaredFields);
            List<Field> stringArrayFields = getFields(String[].class, declaredFields);


            Field[] tmp = new Field[0];

            Map<Class<?>, Field[]> map = new HashMap<>();

            map.put(boolean.class, boolFields.toArray(tmp));
            map.put(byte.class, byteFields.toArray(tmp));
            map.put(short.class, shortFields.toArray(tmp));
            map.put(int.class, intFields.toArray(tmp));
            map.put(long.class, longFields.toArray(tmp));
            map.put(float.class, floatFields.toArray(tmp));
            map.put(double.class, doubleFields.toArray(tmp));

            map.put(String.class, stringFields.toArray(tmp));
            map.put(boolean[].class, boolArrayFields.toArray(tmp));
            map.put(byte[].class, byteArrayFields.toArray(tmp));
            map.put(short[].class, shortArrayFields.toArray(tmp));
            map.put(int[].class, intArrayFields.toArray(tmp));
            map.put(long[].class, longArrayFields.toArray(tmp));
            map.put(float[].class, floatArrayFields.toArray(tmp));
            map.put(double[].class, doubleArrayFields.toArray(tmp));
            map.put(String[].class, stringArrayFields.toArray(tmp));
            FIELD_CACHE.put(packetClass, map);
        }
        this.packet = packet;
        setup();
    }

    public static void loadAllWrappers() {
        //LOGIN SERVER BOUND
        WrappedPacketLoginInCustomPayload.load();

        //LOGIN CLIENT BOUND
        WrappedPacketLoginOutCustomPayload.load();
        WrappedPacketLoginOutSetCompression.load();

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
        WrappedPacketOutEntityTeleport.load();
        WrappedPacketOutKeepAlive.load();
        WrappedPacketOutKickDisconnect.load();
        WrappedPacketOutPosition.load();
        WrappedPacketOutTransaction.load();
        WrappedPacketOutUpdateHealth.load();
        WrappedPacketOutGameStateChange.load();
        WrappedPacketOutCustomPayload.load();
        WrappedPacketOutExplosion.load();
        WrappedPacketOutEntityStatus.load();
        WrappedPacketOutExperience.load();
        WrappedPacketOutHeldItemSlot.load();
    }

    protected void setup() {

    }

    @Override
    public boolean readBoolean(int index) {
        return (boolean) read(boolean.class, index);
    }

    @Override
    public byte readByte(int index) {
        return (byte) read(byte.class, index);
    }

    @Override
    public short readShort(int index) {
        return (short) read(short.class, index);
    }

    @Override
    public int readInt(int index) {
        return (int) read(int.class, index);
    }

    @Override
    public long readLong(int index) {
        return (long) read(long.class, index);
    }

    @Override
    public float readFloat(int index) {
        return (float) read(float.class, index);
    }

    @Override
    public double readDouble(int index) {
        return (double) read(double.class, index);
    }

    @Override
    public boolean[] readBooleanArray(int index) {
        Map<Class<?>, Field[]> cached = FIELD_CACHE.get(packetClass);
        if (cached != null) {
            try {
                return (boolean[]) cached.get(boolean[].class)[index].get(packet);
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
        Map<Class<?>, Field[]> cached = FIELD_CACHE.get(packetClass);
        if (cached != null) {
            try {
                return (byte[]) cached.get(byte[].class)[index].get(packet);
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
        Map<Class<?>, Field[]> cached = FIELD_CACHE.get(packetClass);
        if (cached != null) {
            try {
                return (short[]) cached.get(short[].class)[index].get(packet);
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
        Map<Class<?>, Field[]> cached = FIELD_CACHE.get(packetClass);
        if (cached != null) {
            try {
                return (int[]) cached.get(int[].class)[index].get(packet);
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
        Map<Class<?>, Field[]> cached = FIELD_CACHE.get(packetClass);
        if (cached != null) {
            try {
                return (long[]) cached.get(long[].class)[index].get(packet);
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
        Map<Class<?>, Field[]> cached = FIELD_CACHE.get(packetClass);
        if (cached != null) {
            try {
                return (float[]) cached.get(float[].class)[index].get(packet);
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
        Map<Class<?>, Field[]> cached = FIELD_CACHE.get(packetClass);
        if (cached != null) {
            try {
                return (double[]) cached.get(double[].class)[index].get(packet);
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
        Map<Class<?>, Field[]> cached = FIELD_CACHE.get(packetClass);
        if (cached != null) {
            try {
                Object[] array = (Object[]) cached.get(String[].class)[index].get(packet);
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
        return (String) read(String.class, index);
    }

    public Object readAnyObject(int index) {
        try {
            Field f = packetClass.getDeclaredFields()[index];
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }
            try {
                return f.get(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new WrapperFieldNotFoundException("PacketEvents failed to find any field indexed " + index + " in the " + ClassUtil.getClassSimpleName(packetClass) + " class!");
        }
        return null;
    }

    public Object readObject(int index, Class<?> type) {
        Map<Class<?>, Field[]> cached = FIELD_CACHE.get(packetClass);
        if (cached != null) {
            Field[] cachedFields = cached.get(type);
            if (cachedFields == null) {
                List<Field> typeFields = new ArrayList<>();
                for (Field f : packetClass.getDeclaredFields()) {
                    f.setAccessible(true);
                    if (f.getType().equals(type)) {
                        typeFields.add(f);
                    }
                }
                if (!typeFields.isEmpty()) {
                    cached.put(type, typeFields.toArray(new Field[0]));
                    cachedFields = cached.get(type);
                } else {
                    throw new WrapperFieldNotFoundException("The class you are trying to read fields from does not contain any fields!");
                }
            }
            try {
                return cachedFields[index].get(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        throw new WrapperFieldNotFoundException(packetClass, type, index);
    }

    public boolean doesObjectExist(int index, Class<?> type) {
        Map<Class<?>, Field[]> cached = FIELD_CACHE.get(packetClass);
        if (cached != null) {
            Field[] cachedFields = cached.get(type);
            if (cachedFields == null) {
                List<Field> typeFields = new ArrayList<>();
                for (Field f : packetClass.getDeclaredFields()) {
                    f.setAccessible(true);
                    if (f.getType().equals(type)) {
                        typeFields.add(f);
                    }
                }
                if (!typeFields.isEmpty()) {
                    cached.put(type, typeFields.toArray(new Field[0]));
                    cachedFields = cached.get(type);
                } else {
                    return false;
                }
            }
            if (cachedFields == null) {
                return false;
            } else return index <= cachedFields.length + 1;
        }
        return false;
    }

    public Object[] readObjectArray(int index, Class<?> type) {
        if (type.equals(String.class)) {
            return readStringArray(index);
        }
        try {
            return (Object[]) readObject(index, getArrayClass(type));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("PacketEvents failed to find the array class of type " + type.getName());
        }
    }

    @Override
    public void writeBoolean(int index, boolean value) {
        write(boolean.class, index, value);
    }

    @Override
    public void writeByte(int index, byte value) {
        write(byte.class, index, value);
    }

    @Override
    public void writeShort(int index, short value) {
        write(short.class, index, value);
    }

    @Override
    public void writeInt(int index, int value) {
        write(int.class, index, value);
    }

    @Override
    public void writeLong(int index, long value) {
        write(long.class, index, value);
    }

    @Override
    public void writeFloat(int index, float value) {
        write(float.class, index, value);
    }

    @Override
    public void writeDouble(int index, double value) {
        write(double.class, index, value);
    }

    @Override
    public void writeString(int index, String value) {
        write(String.class, index, value);
    }

    @Override
    public void writeObject(int index, Object object) {
        write(object.getClass(), index, object);
    }

    private void write(Class<?> type, int index, Object value) throws WrapperFieldNotFoundException {
        Field field = getField(type, index);
        if (field == null) {
            throw new WrapperFieldNotFoundException(packetClass, type, index);
        }
        try {
            field.set(packet, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Object read(Class<?> type, int index) throws WrapperFieldNotFoundException {
        Field field = getField(type, index);
        if (field == null) {
            throw new WrapperFieldNotFoundException(packetClass, type, index);
        }
        try {
            return field.get(packet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Field getField(Class<?> type, int index) {
        Map<Class<?>, Field[]> cached = FIELD_CACHE.get(packetClass);
        if (cached == null) {
            return null;
        }
        Field[] fields = cached.get(type);
        if (fields != null) {
            return fields[index];
        }
        return null;
    }

    private List<Field> getFields(Class<?> type, Field[] fields) {
        List<Field> ret = new ArrayList<>();
        for (Field field : fields) {
            if (field.getType() == type) {
                ret.add(field);
            }
        }
        return ret;
    }

    private Class<?> getArrayClass(Class<?> componentType) throws ClassNotFoundException {
        ClassLoader classLoader = componentType.getClassLoader();
        String name;
        if (componentType.isArray()) {
            // just add a leading "["
            name = "[" + componentType.getName();
        } else if (componentType == boolean.class) {
            name = "[Z";
        } else if (componentType == byte.class) {
            name = "[B";
        } else if (componentType == char.class) {
            name = "[C";
        } else if (componentType == double.class) {
            name = "[D";
        } else if (componentType == float.class) {
            name = "[F";
        } else if (componentType == int.class) {
            name = "[I";
        } else if (componentType == long.class) {
            name = "[J";
        } else if (componentType == short.class) {
            name = "[S";
        } else {
            // must be an object non-array class
            name = "[L" + componentType.getName() + ";";
        }
        return classLoader != null ? classLoader.loadClass(name) : Class.forName(name);
    }
}
