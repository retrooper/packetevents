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
import io.github.retrooper.packetevents.packetwrappers.login.in.custompayload.WrappedPacketLoginInCustomPayload;
import io.github.retrooper.packetevents.packetwrappers.login.out.custompayload.WrappedPacketLoginOutCustomPayload;
import io.github.retrooper.packetevents.packetwrappers.login.out.setcompression.WrappedPacketLoginOutSetCompression;
import io.github.retrooper.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig;
import io.github.retrooper.packetevents.packetwrappers.play.in.blockplace.WrappedPacketInBlockPlace;
import io.github.retrooper.packetevents.packetwrappers.play.in.clientcommand.WrappedPacketInClientCommand;
import io.github.retrooper.packetevents.packetwrappers.play.in.custompayload.WrappedPacketInCustomPayload;
import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.play.in.keepalive.WrappedPacketInKeepAlive;
import io.github.retrooper.packetevents.packetwrappers.play.in.settings.WrappedPacketInSettings;
import io.github.retrooper.packetevents.packetwrappers.play.in.updatesign.WrappedPacketInUpdateSign;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import io.github.retrooper.packetevents.packetwrappers.play.in.windowclick.WrappedPacketInWindowClick;
import io.github.retrooper.packetevents.packetwrappers.play.out.abilities.WrappedPacketOutAbilities;
import io.github.retrooper.packetevents.packetwrappers.play.out.animation.WrappedPacketOutAnimation;
import io.github.retrooper.packetevents.packetwrappers.play.out.chat.WrappedPacketOutChat;
import io.github.retrooper.packetevents.packetwrappers.play.out.custompayload.WrappedPacketOutCustomPayload;
import io.github.retrooper.packetevents.packetwrappers.play.out.entity.WrappedPacketOutEntity;
import io.github.retrooper.packetevents.packetwrappers.play.out.entitystatus.WrappedPacketOutEntityStatus;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityteleport.WrappedPacketOutEntityTeleport;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityvelocity.WrappedPacketOutEntityVelocity;
import io.github.retrooper.packetevents.packetwrappers.play.out.experience.WrappedPacketOutExperience;
import io.github.retrooper.packetevents.packetwrappers.play.out.explosion.WrappedPacketOutExplosion;
import io.github.retrooper.packetevents.packetwrappers.play.out.gamestatechange.WrappedPacketOutGameStateChange;
import io.github.retrooper.packetevents.packetwrappers.play.out.helditemslot.WrappedPacketOutHeldItemSlot;
import io.github.retrooper.packetevents.packetwrappers.play.out.keepalive.WrappedPacketOutKeepAlive;
import io.github.retrooper.packetevents.packetwrappers.play.out.kickdisconnect.WrappedPacketOutKickDisconnect;
import io.github.retrooper.packetevents.packetwrappers.play.out.position.WrappedPacketOutPosition;
import io.github.retrooper.packetevents.packetwrappers.play.out.transaction.WrappedPacketOutTransaction;
import io.github.retrooper.packetevents.packetwrappers.play.out.updatehealth.WrappedPacketOutUpdateHealth;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.lang.reflect.Field;
import java.util.*;

public class WrappedPacket implements WrapperPacketReader, WrapperPacketWriter {
    private static final Map<Class<?>, Map<Class<?>, Field[]>> FIELD_CACHE = new HashMap<>();
    private static final Field[] EMPTY_FIELD_ARRAY = new Field[0];
    public static ServerVersion version;
    protected Object packet;
    private Class<?> packetClass;

    public WrappedPacket() {

    }

    public WrappedPacket(final Object packet) {
        this(packet, packet.getClass());
    }

    public WrappedPacket(final Object packet, Class<?> packetClass) {
        if (packetClass.getSuperclass().equals(PacketTypeClasses.Client.FLYING)) {
            packetClass = PacketTypeClasses.Client.FLYING;
        } else if (packetClass.getSuperclass().equals(PacketTypeClasses.Server.ENTITY)) {
            packetClass = PacketTypeClasses.Server.ENTITY;
        }
        this.packetClass = packetClass;
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
        Field field = getField(boolean.class, index);
        try {
            return field.getBoolean(packet);
        } catch (IllegalAccessException | NullPointerException e) {
            throw new WrapperFieldNotFoundException(packetClass, boolean.class, index);
        }
    }

    @Override
    public byte readByte(int index) {
        Field field = getField(byte.class, index);
        try {
            return field.getByte(packet);
        } catch (IllegalAccessException | NullPointerException e) {
            throw new WrapperFieldNotFoundException(packetClass, byte.class, index);
        }
    }

    @Override
    public short readShort(int index) {
        Field field = getField(short.class, index);
        try {
            return field.getShort(packet);
        } catch (IllegalAccessException | NullPointerException e) {
            throw new WrapperFieldNotFoundException(packetClass, boolean.class, index);
        }
    }

    @Override
    public int readInt(int index) {
        Field field = getField(int.class, index);
        try {
            return field.getInt(packet);
        } catch (IllegalAccessException | NullPointerException e) {
            throw new WrapperFieldNotFoundException(packetClass, int.class, index);
        }
    }

    @Override
    public long readLong(int index) {
        Field field = getField(long.class, index);
        try {
            return field.getLong(packet);
        } catch (IllegalAccessException | NullPointerException e) {
            throw new WrapperFieldNotFoundException(packetClass, long.class, index);
        }
    }

    @Override
    public float readFloat(int index) {
        Field field = getField(float.class, index);
        try {
            return field.getFloat(packet);
        } catch (IllegalAccessException | NullPointerException e) {
            throw new WrapperFieldNotFoundException(packetClass, float.class, index);
        }
    }

    @Override
    public double readDouble(int index) {
        Field field = getField(double.class, index);
        try {
            return field.getDouble(packet);
        } catch (IllegalAccessException | NullPointerException e) {
            throw new WrapperFieldNotFoundException(packetClass, double.class, index);
        }
    }

    @Override
    public boolean[] readBooleanArray(int index) {
        return read(index, boolean[].class);
    }

    @Override
    public byte[] readByteArray(int index) {
        return read(index, byte[].class);
    }

    @Override
    public short[] readShortArray(int index) {
       return read(index, short[].class);
    }

    @Override
    public int[] readIntArray(int index) {
        return read(index, int[].class);
    }

    @Override
    public long[] readLongArray(int index) {
        return read(index, long[].class);
    }

    @Override
    public float[] readFloatArray(int index) {
        return read(index, float[].class);
    }

    @Override
    public double[] readDoubleArray(int index) {
        return read(index, double[].class);
    }

    @Override
    public String[] readStringArray(int index) {
        Field field = getField(String[].class, 0);
        try {
            Object[] array = (Object[]) field.get(packet);
            int len = array.length;
            String[] stringArray = new String[len];
            for (int i = 0; i < len; i++) {
                stringArray[i] = array[i].toString();
            }
            return stringArray;
        } catch (IllegalAccessException | NullPointerException e) {
            throw new WrapperFieldNotFoundException(packetClass, String[].class, index);
        }
    }

    @Override
    public String readString(int index) {
       return read(index, String.class);
    }

    public Object readAnyObject(int index) {
        try {
            Field f = packetClass.getDeclaredFields()[index];
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }
            try {
                return f.get(packet);
            } catch (IllegalAccessException | NullPointerException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new WrapperFieldNotFoundException("PacketEvents failed to find any field indexed " + index + " in the " + ClassUtil.getClassSimpleName(packetClass) + " class!");
        }
        return null;
    }

    public Object readObject(int index, Class<?> type) {
        Field field = getField(type, index);
        try {
            return field.get(packet);
        } catch (IllegalAccessException | NullPointerException e) {
            throw new WrapperFieldNotFoundException(packetClass, type, index);
        }
    }

    private <T> T read(int index, Class<? extends T> type) {
        Field field = getField(type, index);
        try {
            return (T) field.get(packet);
        } catch (IllegalAccessException | NullPointerException e) {
            throw new WrapperFieldNotFoundException(packetClass, type, index);
        }
    }

    @Override
    public void writeBoolean(int index, boolean value) {
        Field field = getField(boolean.class, index);
        if (field == null) {
            throw new WrapperFieldNotFoundException(packetClass, boolean.class, index);
        }
        try {
            field.setBoolean(packet, value);
        } catch (IllegalAccessException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeByte(int index, byte value) {
        Field field = getField(byte.class, index);
        if (field == null) {
            throw new WrapperFieldNotFoundException(packetClass, byte.class, index);
        }
        try {
            field.setByte(packet, value);
        } catch (IllegalAccessException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeShort(int index, short value) {
        Field field = getField(short.class, index);
        if (field == null) {
            throw new WrapperFieldNotFoundException(packetClass, short.class, index);
        }
        try {
            field.setShort(packet, value);
        } catch (IllegalAccessException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeInt(int index, int value) {
        Field field = getField(int.class, index);
        if (field == null) {
            throw new WrapperFieldNotFoundException(packetClass, int.class, index);
        }
        try {
            field.setInt(packet, value);
        } catch (IllegalAccessException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeLong(int index, long value) {
        Field field = getField(long.class, index);
        if (field == null) {
            throw new WrapperFieldNotFoundException(packetClass, long.class, index);
        }
        try {
            field.setLong(packet, value);
        } catch (IllegalAccessException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeFloat(int index, float value) {
        Field field = getField(float.class, index);
        if (field == null) {
            throw new WrapperFieldNotFoundException(packetClass, float.class, index);
        }
        try {
            field.setFloat(packet, value);
        } catch (IllegalAccessException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeDouble(int index, double value) {
        Field field = getField(double.class, index);
        if (field == null) {
            throw new WrapperFieldNotFoundException(packetClass, double.class, index);
        }
        try {
            field.setDouble(packet, value);
        } catch (IllegalAccessException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeString(int index, String value) {
        Field field = getField(String.class, index);
        if (field == null) {
            throw new WrapperFieldNotFoundException(packetClass, String.class, index);
        }
        try {
            field.set(packet, value);
        } catch (IllegalAccessException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeObject(int index, Object object) {
        Field field = getField(object.getClass(), index);
        if (field == null) {
            throw new WrapperFieldNotFoundException(packetClass, object.getClass(), index);
        }
        try {
            field.set(packet, object);
        } catch (IllegalAccessException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private Field getField(Class<?> type, int index) {
        Map<Class<?>, Field[]> cached = FIELD_CACHE.computeIfAbsent(packetClass, k -> new HashMap<>());
        Field[] fields = cached.get(type);
        if (fields == null) {
            fields = getFields(type, packetClass.getDeclaredFields());
            cached.put(type, fields);
        }
        return fields[index];
    }

    private Field[] getFields(Class<?> type, Field[] fields) {
        List<Field> ret = new ArrayList<>();
        for (Field field : fields) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            if (field.getType() == type) {
                ret.add(field);
            }
        }
        return ret.toArray(EMPTY_FIELD_ARRAY);
    }

    //Does the server version support reading/sending this packet?
    public boolean isSupported() {
        return true;
    }
}
