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


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public final class Reflection {

    //FIELDS
    @Deprecated
    public static Field[] getFields(Class<?> cls) {
        Field[] declaredFields = cls.getDeclaredFields();
        for (Field f : declaredFields) {
            f.setAccessible(true);
        }
        return declaredFields;
    }

    @Deprecated
    public static Field getField(final Class<?> cls, final String name) {
        for (final Field f : getFields(cls)) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        return null;
    }

    @Deprecated
    public static Field getField(final Class<?> cls, final Class<?> dataType, final int index) {
        int currentIndex = 0;
        for (final Field f : getFields(cls)) {
            if (f.getType().equals(dataType)) {
                if (currentIndex++ == index) {
                    return f;
                }
            }
        }
        return null;
    }

    @Deprecated
    public static Field getField(final Class<?> cls, final int index) {
        return getFields(cls)[index];
    }

    //METHODS
    public static Method[] getMethods(final Class<?> cls) {
        Method[] declaredMethods = cls.getDeclaredMethods();
        for (Method m : declaredMethods) {
            m.setAccessible(true);
        }
        return declaredMethods;
    }

    public static Method getMethod(final Class<?> cls, final int index, final Class<?>... params) {
        int currentIndex = 0;
        for (final Method m : getMethods(cls)) {
            if (Arrays.equals(m.getParameterTypes(), params) && index == currentIndex++) {
                return m;
            }
        }
        return null;
    }

    public static Method getMethod(Class<?> cls, Class<?> returning, int index, Class<?>... params) {
        int currentIndex = 0;
        for (Method m : getMethods(cls)) {
            if (Arrays.equals(m.getParameterTypes(), params) && m.getReturnType().equals(returning) && index == currentIndex++) {
                return m;
            }
        }
        return null;
    }

    public static Method getMethod(final Class<?> cls, final String name, Class<?> returning, Class<?>... params) {
        for (final Method m : getMethods(cls)) {
            if (m.getName().equals(name) && Arrays.equals(m.getParameterTypes(), params) && m.getReturnType().equals(returning)) {
                return m;
            }
        }
        return null;
    }

    public static Method getMethod(final Class<?> cls, final String name, final int index) {
        int currentIndex = 0;
        for (final Method m : getMethods(cls)) {
            if (m.getName().equals(name) && index == currentIndex++) {
                return m;
            }
        }
        return null;
    }

    public static Method getMethod(final Class<?> cls, final Class<?> returning, final int index) {
        int currentIndex = 0;
        for (final Method m : getMethods(cls)) {
            if (returning.equals(m.getReturnType()) && index == currentIndex++) {
                return m;
            }
        }
        return null;
    }

    public static Method getMethod(final Class<?> cls, final String name, final Class<?> returning) {
        for (final Method m : getMethods(cls)) {
            if (m.getName().equals(name) && m.getReturnType().equals(returning)) {
                return m;
            }
        }
        return null;
    }
}
