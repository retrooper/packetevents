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
import io.github.retrooper.packetevents.packetwrappers.play.in.abilities.WrappedPacketInAbilities;
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
import io.github.retrooper.packetevents.packetwrappers.play.out.closewindow.WrappedPacketOutCloseWindow;
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
import io.github.retrooper.packetevents.packetwrappers.play.out.resourcepacksend.WrappedPacketOutResourcePackSend;
import io.github.retrooper.packetevents.packetwrappers.play.out.transaction.WrappedPacketOutTransaction;
import io.github.retrooper.packetevents.packetwrappers.play.out.updatehealth.WrappedPacketOutUpdateHealth;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (packetClass.getSuperclass().equals(PacketTypeClasses.Play.Client.FLYING)) {
            packetClass = PacketTypeClasses.Play.Client.FLYING;
        } else if (packetClass.getSuperclass().equals(PacketTypeClasses.Play.Server.ENTITY)) {
            packetClass = PacketTypeClasses.Play.Server.ENTITY;
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
        WrappedPacketInAbilities.load();

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
        WrappedPacketOutResourcePackSend.load();
        WrappedPacketOutCloseWindow.load();
    }

    protected void setup() {

    }

    @Override
    public boolean readBoolean(int index) {
        return read(index, boolean.class);
    }

    @Override
    public byte readByte(int index) {
        return read(index, byte.class);
    }

    @Override
    public short readShort(int index) {
        return read(index, short.class);
    }

    @Override
    public int readInt(int index) {
        return read(index, int.class);
    }

    @Override
    public long readLong(int index) {
        return read(index, long.class);
    }

    @Override
    public float readFloat(int index) {
       return read(index, float.class);
    }

    @Override
    public double readDouble(int index) {
       return read(index, double.class);
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
        String[] array = read(index, String[].class);
        int length = array.length;
        String[] copy = new String[length];
        System.arraycopy(array, 0, copy, 0, length);
        return copy;
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
        return read(index, type);
    }

    public <T> T read(int index, Class<? extends T> type) {
        Field field = getField(type, index);
        try {
            return (T) field.get(packet);
        } catch (IllegalAccessException | NullPointerException e) {
            throw new WrapperFieldNotFoundException(packetClass, type, index);
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
    public void writeObject(int index, Object value) {
        write(value.getClass(), index, value);
    }

    public void write(Class<?> type, int index, Object value) throws WrapperFieldNotFoundException {
        Field field = getField(type, index);
        if (field == null) {
            throw new WrapperFieldNotFoundException(packetClass, type, index);
        }
        try {
            field.set(packet, value);
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
            if (field.getType().equals(type)) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
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
