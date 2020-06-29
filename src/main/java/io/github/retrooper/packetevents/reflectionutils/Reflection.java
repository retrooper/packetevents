package io.github.retrooper.packetevents.reflectionutils;


import io.github.retrooper.packetevents.reflectionutils.fielddata.FieldWithIndex;
import io.github.retrooper.packetevents.reflectionutils.fielddata.FieldWithName;
import io.github.retrooper.packetevents.reflectionutils.fielddata.FieldWithTypeAndIndex;
import io.github.retrooper.packetevents.reflectionutils.methoddata.MethodWithIndexAndParams;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author retrooper
 */
public final class Reflection {
    private static final HashMap<Class<?>, Field[]> cachedFields = new HashMap<Class<?>, Field[]>();
    private static final HashMap<Class<?>, Method[]> cachedMethods = new HashMap<Class<?>, Method[]>();
    private static final HashMap<Class<?>, Class<?>[]> cachedSubclasses = new HashMap<Class<?>, Class<?>[]>();


    //FIELDS
    private static final HashMap<FieldWithName, Field> fieldWithNameHashMap = new HashMap<FieldWithName, Field>();
    private static final HashMap<FieldWithTypeAndIndex, Field> fieldWithTypeAndIndexHashMap = new HashMap<FieldWithTypeAndIndex, Field>();
    private static final HashMap<FieldWithIndex, Field> fieldWithIndexHashMap = new HashMap<FieldWithIndex, Field>();
    private static final HashMap<MethodWithIndexAndParams, Method> methodWithIndexAndParamsHashMap = new HashMap<MethodWithIndexAndParams, Method>();

    public static Field[] getFields(final Class<?> cls) {
        if (cachedFields.containsKey(cls)) {
            return cachedFields.get(cls);
        } else {
            final Field[] fields = cls.getDeclaredFields();
            for (final Field f : fields) {
                f.setAccessible(true);
            }
            cachedFields.put(cls, fields);
            return fields;
        }
    }

    public static Field getField(final Class<?> cls, final String name) {
        final FieldWithName fwn = new FieldWithName(cls, name);
        if (!fieldWithNameHashMap.containsKey(fwn)) {
            for (final Field f : getFields(cls)) {
                if (f.getName().equals(name)) {
                    fieldWithNameHashMap.put(fwn, f);
                    return f;
                }
            }
        } else {
            return fieldWithNameHashMap.get(fwn);
        }
        return null;
    }

    public static Field getField(final Class<?> cls, final Class<?> dataType, final int index) {
        final FieldWithTypeAndIndex fwtai = new FieldWithTypeAndIndex(cls, dataType, index);
        if (!fieldWithTypeAndIndexHashMap.containsKey(fwtai)) {
            int i = 0;
            for (final Field f : getFields(cls)) {
                if (f.getType().equals(dataType)) {
                    if (i++ == index) {
                        fieldWithTypeAndIndexHashMap.put(fwtai, f);
                        return f;
                    }
                }
            }
        } else {
            return fieldWithTypeAndIndexHashMap.get(fwtai);
        }
        return null;
    }

    public static Field getField(final Class<?> cls, final int index) {
        final FieldWithIndex fwi = new FieldWithIndex(cls, index);
        if (!fieldWithIndexHashMap.containsKey(fwi)) {
            int i = 0;
            for (final Field f : getFields(cls)) {
                if (index == i++) {
                    fieldWithIndexHashMap.put(fwi, f);
                    return f;
                }
            }
        } else {
            return fieldWithIndexHashMap.get(fwi);
        }
        return null;
    }

    //METHODS
    public static Method[] getMethods(final Class<?> cls) {
        if (cachedMethods.containsKey(cls)) {
            return cachedMethods.get(cls);
        } else {
            final Method[] methods = cls.getDeclaredMethods();
            for (final Method m : methods) {
                m.setAccessible(true);
            }
            cachedMethods.put(cls, methods);
            return methods;
        }
    }

    public static Method getMethod(final Class<?> cls, final int index, final Class<?>... params) {
        final MethodWithIndexAndParams mwiap = new MethodWithIndexAndParams(cls, index, params);
        if (!methodWithIndexAndParamsHashMap.containsKey(mwiap)) {
            int i = 0;
            for (final Method m : getMethods(cls)) {
                if (Arrays.equals(m.getParameterTypes(), params) && index == i++) {
                    return m;
                }
            }
        } else {
            return methodWithIndexAndParamsHashMap.get(mwiap);
        }
        return null;
    }

    //THE REST LATER

    public static Method getMethod(final Class<?> cls, final String name, Class<?> returning, Class<?>... params) {
        for (final Method m : getMethods(cls)) {
            if (m.getName().equals(name) && Arrays.equals(m.getParameterTypes(), params) && m.getReturnType().equals(returning)) {
                return m;
            }
        }
        return null;
    }

    public static Method getMethod(final Class<?> cls, final String name, final int index) {
        int i = 0;
        for (final Method m : getMethods(cls)) {
            if (m.getName().equals(name) && index == i++) {
                return m;
            }
        }
        return null;
    }

    public static Method getMethod(final Class<?> cls, final Class<?> returning, final int index) {
        int i = 0;
        for (final Method m : getMethods(cls)) {
            if (m.getReturnType().equals(returning) && index == i++) {
                return m;
            }
        }
        return null;
    }

    public static Method getMethod(final Class<?> cls, final String name, final Class<?> returning) {
        int i = 0;
        for (final Method m : getMethods(cls)) {
            if (m.getName().equals(name) && m.getReturnType().equals(returning)) {
                return m;
            }
        }
        return null;
    }

    public static Class<?>[] getSubClasses(final Class<?> cls) {
        if (cachedSubclasses.containsKey(cls)) {
            return cachedSubclasses.get(cls);
        } else {
            final Class<?>[] subclasses = cls.getDeclaredClasses();
            cachedSubclasses.put(cls, subclasses);
            return subclasses;
        }
    }

    public static Class<?> getSubClass(final Class<?> cls, final String name) {
        for (final Class<?> subclass : getSubClasses(cls)) {
            if (subclass.getSimpleName().equals(name)) {
                return subclass;
            }
        }
        return null;
    }

    public static Class<?> getSubClass(final Class<?> cls, final int index) {
        final Class<?>[] subclasses = getSubClasses(cls);
        for (int i = 0; i < index + 1; i++) {
            final Class<?> c = subclasses[i];
            if (i == index) {
                return c;
            }
        }
        return null;
    }
}