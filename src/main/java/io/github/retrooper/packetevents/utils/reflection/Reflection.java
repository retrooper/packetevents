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

package io.github.retrooper.packetevents.utils.reflection;


import io.github.retrooper.packetevents.PacketEvents;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Reflection {
    private static Field MODIFIER_FIELD;

    //FIELDS
    public static Field[] getFields(Class<?> cls) {
        Field[] declaredFields = cls.getDeclaredFields();
        for (Field f : declaredFields) {
            f.setAccessible(true);
        }
        return declaredFields;
    }

    public static Field getField(final Class<?> cls, final String name) {
        for (final Field f : getFields(cls)) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        if (cls.getSuperclass() != null) {
            return getField(cls.getSuperclass(), name);
        }
        return null;
    }

    public static Field getField(final Class<?> cls, final Class<?> dataType, final int index) {
        if (dataType == null || cls == null) {
            return null;
        }
        int currentIndex = 0;
        for (final Field f : getFields(cls)) {
            if (dataType.isAssignableFrom(f.getType())) {
                if (currentIndex++ == index) {
                    return f;
                }
            }
        }
        if (cls.getSuperclass() != null) {
            return getField(cls.getSuperclass(), dataType, index);
        }
        return null;
    }

    public static Field getField(final Class<?> cls, final int index) {
        try {
            return getFields(cls)[index];
        } catch (Exception ex) {
            if (cls.getSuperclass() != null) {
                return getFields(cls.getSuperclass())[index];
            }
        }
        return null;
    }

    //METHODS
    public static List<Method> getMethods(Class<?> cls, String name, Class<?>... params) {
        List<Method> methods = new ArrayList<>();
        for (Method m : cls.getDeclaredMethods()) {
            if ((params == null || Arrays.equals(m.getParameterTypes(), params)) && name.equals(m.getName())) {
                m.setAccessible(true);
                methods.add(m);
            }
        }
        return methods;
    }

    public static Method getMethod(final Class<?> cls, final int index, final Class<?>... params) {
        int currentIndex = 0;
        for (final Method m : cls.getDeclaredMethods()) {
            if ((params == null || Arrays.equals(m.getParameterTypes(), params)) && index == currentIndex++) {
                m.setAccessible(true);
                return m;
            }
        }
        if (cls.getSuperclass() != null) {
            return getMethod(cls.getSuperclass(), index, params);
        }
        return null;
    }

    public static Method getMethod(Class<?> cls, Class<?> returning, int index, Class<?>... params) {
        int currentIndex = 0;
        for (Method m : cls.getDeclaredMethods()) {
            if (Arrays.equals(m.getParameterTypes(), params)
                    && (returning == null || m.getReturnType().equals(returning))
                    && index == currentIndex++) {
                m.setAccessible(true);
                return m;
            }
        }
        if (cls.getSuperclass() != null) {
            return getMethod(cls.getSuperclass(), null, index, params);
        }
        return null;
    }

    public static Method getMethod(final Class<?> cls, final String name, Class<?> returning, Class<?>... params) {
        for (final Method m : cls.getDeclaredMethods()) {
            if (m.getName().equals(name)
                    && Arrays.equals(m.getParameterTypes(), params) &&
                    (returning == null || m.getReturnType().equals(returning))) {
                m.setAccessible(true);
                return m;
            }
        }
        if (cls.getSuperclass() != null) {
            return getMethod(cls.getSuperclass(), name, null, params);
        }
        return null;
    }

    public static Method getMethod(final Class<?> cls, final String name, final int index) {
        if (cls == null) {
            return null;
        }
        int currentIndex = 0;
        for (final Method m : cls.getDeclaredMethods()) {
            if (m.getName().equals(name) && index == currentIndex++) {
                m.setAccessible(true);
                return m;
            }
        }
        if (cls.getSuperclass() != null) {
            return getMethod(cls.getSuperclass(), name, index);
        }
        return null;
    }

    public static Method getMethod(final Class<?> cls, final Class<?> returning, final int index) {
        if (cls == null) {
            return null;
        }
        int currentIndex = 0;
        for (final Method m : cls.getDeclaredMethods()) {
            if ((returning == null || m.getReturnType().equals(returning)) && index == currentIndex++) {
                m.setAccessible(true);
                return m;
            }
        }
        if (cls.getSuperclass() != null) {
            return getMethod(cls.getSuperclass(), returning, index);
        }
        return null;
    }

    public static Method getMethodCheckContainsString(Class<?> cls, String nameContainsThisStr, Class<?> returning) {
        if (cls == null) {
            return null;
        }
        for (Method m : cls.getDeclaredMethods()) {
            if (m.getName().contains(nameContainsThisStr) && (returning == null || m.getReturnType().equals(returning))) {
                m.setAccessible(true);
                return m;
            }
        }
        if (cls.getSuperclass() != null) {
            return getMethodCheckContainsString(cls.getSuperclass(), nameContainsThisStr, returning);
        }
        return null;
    }

    public static Method getMethod(final Class<?> cls, final String name, final Class<?> returning) {
        if (cls == null) {
            return null;
        }
        for (final Method m : cls.getDeclaredMethods()) {
            if (m.getName().equals(name)
                    && (returning == null || m.getReturnType().equals(returning))) {
                m.setAccessible(true);
                return m;
            }
        }
        if (cls.getSuperclass() != null) {
            return getMethod(cls.getSuperclass(), name, returning);
        }
        return null;
    }

    public static void updateFinalField(Field field, Object instance, Object value) {
        try {
            field.setAccessible(true);
            if (MODIFIER_FIELD == null) {
                MODIFIER_FIELD = getModifiersField();

            }MODIFIER_FIELD.setAccessible(true);
            MODIFIER_FIELD.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(instance, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void updateStaticFinalField(Field field, Object value) {
        try {
            field.setAccessible(true);
            if (MODIFIER_FIELD == null) {
                MODIFIER_FIELD = getModifiersField();
                MODIFIER_FIELD.setAccessible(true);
            }
            MODIFIER_FIELD.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static Field getModifiersField() throws NoSuchFieldException {
        try {
            //This should work on Java 8 - 11
            return Field.class.getDeclaredField("modifiers");
        } catch (Exception e) {
            //Java 12 -> Java 15
            try {
                Method getDeclaredFields0 = Class.class.getDeclaredMethod("getDeclaredFields0", boolean.class);
                getDeclaredFields0.setAccessible(true);
                Field[] fields = (Field[]) getDeclaredFields0.invoke(Field.class, false);
                for (Field field : fields) {
                    if ("modifiers".equals(field.getName())) {
                        field.setAccessible(true);
                        return field;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            PacketEvents.get().getPlugin().getLogger().severe("PacketEvents failed to access modifiers field! You are most likely on Java 16. Java 16 support is still under development.");
            throw e;
        }
    }
}
