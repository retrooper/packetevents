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


import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Reflection {
	//FIELDS
	public static Field[] getFields(final Class<?> cls) {
		if (cls == null) {
			return new Field[0];
		}
		final Field[] declaredFields = cls.getDeclaredFields();
		for (final Field f : declaredFields) {
			f.setAccessible(true);
		}
		return declaredFields;
	}

	public static Field getField(final Class<?> cls, final String name) {
		if (cls == null) {
			return null;
		}
		try {
			final Field field = cls.getDeclaredField(name);
			field.setAccessible(true);
			return field;
		} catch (NoSuchFieldException e) {
			if (cls.getSuperclass() != null) {
				return getField(cls.getSuperclass(), name);
			}
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

	public static Field getField(final Class<?> cls, final Class<?> dataType, final int index, boolean ignoreStatic) {
		if (dataType == null || cls == null) {
			return null;
		}
		int currentIndex = 0;
		for (final Field f : getFields(cls)) {
			if (dataType.isAssignableFrom(f.getType()) && (!ignoreStatic || !Modifier.isStatic(f.getModifiers()))) {
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
		if (cls == null) {
			return null;
		}
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
	public static Method getMethod(final Class<?> cls, final String name, final Class<?>... params) {
		if (cls == null) {
			return null;
		}
		try {
			final Method m = cls.getDeclaredMethod(name, params);
			m.setAccessible(true);
			return m;
		} catch (NoSuchMethodException e) {
			try {
				final Method m = cls.getMethod(name, params);
				m.setAccessible(true);
				return m;
			} catch (NoSuchMethodException e1) {
				if (cls.getSuperclass() != null) {
					return getMethod(cls.getSuperclass(), name, params);
				}
			}
		}
		return null;
	}

	public static Method getMethod(final Class<?> cls, final int index, final Class<?>... params) {
		if (cls == null) {
			return null;
		}
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

	public static Method getMethod(final Class<?> cls, final Class<?> returning, final int index, final Class<?>... params) {
		if (cls == null) {
			return null;
		}
		int currentIndex = 0;
		for (final Method m : cls.getDeclaredMethods()) {
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

	public static Method getMethodExact(final Class<?> cls, final String name, Class<?> returning, Class<?>... params) {
		if (cls == null) {
			return null;
		}
		for (final Method m : cls.getDeclaredMethods()) {
			if (m.getName().equals(name)
					&& Arrays.equals(m.getParameterTypes(), params) &&
					(returning == null || m.getReturnType().equals(returning))) {
				m.setAccessible(true);
				return m;
			}
		}
		if (cls.getSuperclass() != null) {
			return getMethodExact(cls.getSuperclass(), name, null, params);
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

	public static Method getMethodCheckContainsString(final Class<?> cls, final String nameContainsThisStr, final Class<?> returning) {
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

	public static List<Method> getMethods(final Class<?> cls, final String name, final Class<?> returning) {
		if (cls == null) {
			return null;
		}
		final List<Method> methods = new ArrayList<>();
		for (final Method m : cls.getDeclaredMethods()) {
			if (m.getName().equals(name)
					&& (returning == null || m.getReturnType().equals(returning))) {
				m.setAccessible(true);
				methods.add(m);
			}
		}
		if (cls.getSuperclass() != null) {
			methods.addAll(getMethods(cls.getSuperclass(), name, returning));
		}
		return methods;
	}

	// CLASS
	@Nullable
	public static Class<?> getClassByNameWithoutException(final String name) {
		try {
			return Class.forName(name);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	// CONSTRUCTOR
	public static Constructor<?> getConstructor(final Class<?> cls, final int index) {
		if (cls == null) {
			return null;
		}
		final Constructor<?> constructor = cls.getDeclaredConstructors()[index];
		constructor.setAccessible(true);
		return constructor;
	}

	public static Constructor<?> getConstructor(final Class<?> cls, final Class<?>... params) {
		if (cls == null) {
			return null;
		}
		try {
			final Constructor<?> c = cls.getDeclaredConstructor(params);
			c.setAccessible(true);
			return c;
		} catch (NoSuchMethodException e) {
			try {
				final Constructor<?> c = cls.getConstructor(params);
				c.setAccessible(true);
				return c;
			} catch (NoSuchMethodException e1) {
				return null;
			}
		}
	}
}
