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


import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
        if (cls.getSuperclass() != null) {
            return getField(cls.getSuperclass(), name);
        }
        return null;
    }

    @Deprecated
    public static Field getField(final Class<?> cls, final Class<?> dataType, final int index) {
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

    @Deprecated
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
    public static Method getMethod(final Class<?> cls, final int index, final Class<?>... params) {
        int currentIndex = 0;
        for (final Method m : getDeclaredMethodsInOrder(cls)) {
            if (Arrays.equals(m.getParameterTypes(), params) && index == currentIndex++) {
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
        for (Method m : getDeclaredMethodsInOrder(cls)) {
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
        for (final Method m : getDeclaredMethodsInOrder(cls)) {
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
        int currentIndex = 0;
        for (final Method m : getDeclaredMethodsInOrder(cls)) {
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
        int currentIndex = 0;
        for (final Method m : getDeclaredMethodsInOrder(cls)) {
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

    public static Method getMethod(final Class<?> cls, final String name, final Class<?> returning) {
        for (final Method m : cls.getMethods()) { // no order needed... I don't think (hope not)
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
    
    // KissReflect
    // thx https://github.com/wmacevoy/kiss/blob/master/src/main/java/kiss/util/Reflect.java

    private static class MethodOffset implements Comparable<MethodOffset> {
        MethodOffset(Method _method, int _offset) {
            method = _method;
            offset = _offset;
        }

        @Override
        public int compareTo(MethodOffset target) {
            return offset - target.offset;
        }

        Method method;
        int offset;
    }

    static class ByLength implements Comparator<Method> {

        @Override
        public int compare(Method a, Method b) {
            return b.getName().length() - a.getName().length();
        }
    }

    /**
     * Grok the bytecode to get the declared order
     * <p>
     * This method is slow; please store the method you want as to not use this again.
     */
    public static Method[] getDeclaredMethodsInOrder(Class<?> clazz) {
        Method[] methods = null;
        try {
            String resource = clazz.getName().replace('.', '/') + ".class";

            methods = clazz.getDeclaredMethods();

            InputStream is = clazz.getClassLoader()
              .getResourceAsStream(resource);

            if (is == null) {
                return methods;
            }

            java.util.Arrays.sort(methods, new ByLength());
            ArrayList<byte[]> blocks = new ArrayList<>();
            int length = 0;
            for (; ; ) {
                byte[] block = new byte[16 * 1024];
                int n = is.read(block);
                if (n > 0) {
                    if (n < block.length) {
                        block = java.util.Arrays.copyOf(block, n);
                    }
                    length += block.length;
                    blocks.add(block);
                } else {
                    break;
                }
            }

            byte[] data = new byte[length];
            int offset = 0;
            for (byte[] block : blocks) {
                System.arraycopy(block, 0, data, offset, block.length);
                offset += block.length;
            }

            String sdata = new String(data, StandardCharsets.UTF_8);
            int lnt = sdata.indexOf("LineNumberTable");
            if (lnt != -1) sdata = sdata.substring(lnt + "LineNumberTable".length() + 3);
            int cde = sdata.lastIndexOf("SourceFile");
            if (cde != -1) sdata = sdata.substring(0, cde);

            MethodOffset[] mo = new MethodOffset[methods.length];


            for (int i = 0; i < methods.length; ++i) {
                int pos = -1;
                for (; ; ) {
                    pos = sdata.indexOf(methods[i].getName(), pos);
                    if (pos == -1) break;
                    boolean subset = false;
                    for (int j = 0; j < i; ++j) {
                        if (mo[j].offset >= 0 &&
                          mo[j].offset <= pos &&
                          pos < mo[j].offset + mo[j].method.getName().length()) {
                            subset = true;
                            break;
                        }
                    }
                    if (subset) {
                        pos += methods[i].getName().length();
                    } else {
                        break;
                    }
                }
                mo[i] = new MethodOffset(methods[i], pos);
            }
            java.util.Arrays.sort(mo);
            for (int i = 0; i < mo.length; ++i) {
                methods[i] = mo[i].method;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return methods;
    }

    // todo: getDeclairedFields
}
