/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.retrooper.packetevents.util.reflection;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectionObject implements ReflectionObjectReader, ReflectionObjectWriter {
    private static final Map<Class<?>, Map<Class<?>, Field[]>> FIELD_CACHE = new ConcurrentHashMap<>();
    private static final Field[] EMPTY_FIELD_ARRAY = new Field[0];
    protected final Object object;
    private final Class<?> clazz;

    public ReflectionObject() {
        object = null;
        clazz = null;
    }

    public ReflectionObject(Object object) {
        this.object = object;
        this.clazz = object.getClass();
    }

    public ReflectionObject(Object object, Class<?> clazz) {
        this.object = object;
        this.clazz = clazz;
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
        return read(index, String[].class); // JavaImpact: Can we be sure that returning the original array is okay? retrooper: Yes
    }

    @Override
    public String readString(int index) {
        return read(index, String.class);
    }

    @Override
    public Object readAnyObject(int index) {
        try {
            Field f = clazz.getDeclaredFields()[index];
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }
            try {
                return f.get(object);
            } catch (IllegalAccessException | NullPointerException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalStateException("PacketEvents failed to find any field indexed " + index + " in the " + clazz.getSimpleName() + " class!");
        }
        return null;
    }

    @Override
    public <T> T readObject(int index, Class<? extends T> type) {
        return read(index, type);
    }

    @Override
    public <T> T[] readObjectArray(int index, Class<? extends T> type) {
        return (T[]) read(0, Array.newInstance(type, 0).getClass());
    }

    @Override
    public Enum<?> readEnumConstant(int index, Class<? extends Enum<?>> type) {
        return read(index, type);
    }

    @SuppressWarnings("unchecked")
    public <T> T read(int index, Class<? extends T> type) {
        try {
            Field field = getField(type, index);
            return (T) field.get(object);
        } catch (IllegalAccessException | NullPointerException | ArrayIndexOutOfBoundsException e) {
            throw new IllegalStateException("PacketEvents failed to find a " + type.getSimpleName() + " indexed " + index + " by its type in the " + clazz.getName() + " class!");
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

    @Override
    public void writeBooleanArray(int index, boolean[] array) {
        write(boolean[].class, index, array);
    }

    @Override
    public void writeByteArray(int index, byte[] value) {
        write(byte[].class, index, value);
    }

    @Override
    public void writeShortArray(int index, short[] value) {
        write(short[].class, index, value);
    }

    @Override
    public void writeIntArray(int index, int[] value) {
        write(int[].class, index, value);
    }

    @Override
    public void writeLongArray(int index, long[] value) {
        write(long[].class, index, value);
    }

    @Override
    public void writeFloatArray(int index, float[] value) {
        write(float[].class, index, value);
    }

    @Override
    public void writeDoubleArray(int index, double[] value) {
        write(double[].class, index, value);
    }

    @Override
    public void writeStringArray(int index, String[] value) {
        write(String[].class, index, value);
    }

    @Override
    public void writeAnyObject(int index, Object value) {
        try {
            Field f = clazz.getDeclaredFields()[index];
            f.set(object, value);
        } catch (Exception e) {
            throw new IllegalStateException("PacketEvents failed to find any field indexed " + index + " in the " + clazz.getSimpleName() + " class!");
        }
    }

    @Override
    public void writeEnumConstant(int index, Enum<?> enumConstant) {
        try {
            write(enumConstant.getClass(), index, enumConstant);
        } catch (IllegalStateException ex) {
            write(enumConstant.getDeclaringClass(), index, enumConstant);
        }
    }

    public void write(Class<?> type, int index, Object value) throws IllegalStateException {
        Field field = getField(type, index);
        if (field == null) {
            throw new IllegalStateException("PacketEvents failed to find a " + type.getSimpleName() + " indexed " + index + " by its type in the " + clazz.getName() + " class!");
        }
        try {
            field.set(object, value);
        } catch (IllegalAccessException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public <T> List<T> readList(int index) {
        return read(index, List.class);
    }

    public void writeList(int index, List<?> list) {
        write(List.class, index, list);
    }

    private Field getField(Class<?> type, int index) {
        Map<Class<?>, Field[]> cached = FIELD_CACHE.computeIfAbsent(clazz, k -> new ConcurrentHashMap<>());
        Field[] fields = cached.computeIfAbsent(type, typeClass -> getFields(typeClass, clazz.getDeclaredFields()));
        if (fields.length >= index + 1) {
            return fields[index];
        } else {
            throw new IllegalStateException("PacketEvents failed to find a " + type.getSimpleName() + " indexed " + index + " by its type in the " + clazz.getName() + " class!");
        }
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
}
