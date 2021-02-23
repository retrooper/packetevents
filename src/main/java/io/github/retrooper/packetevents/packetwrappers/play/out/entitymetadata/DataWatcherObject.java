package io.github.retrooper.packetevents.packetwrappers.play.out.entitymetadata;

import io.github.retrooper.packetevents.utils.nms.NMSUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

/**
 * A data watcher object.
 * This class is only present in 1.9+.
 *
 * @author SteelPhoenix
 */
// TODO:  hashCode, equals, toString
public abstract class DataWatcherObject {

	// Look at this beautiful code
	// That's what you get for not including proper reflection helpers
	// TODO: Move to static initializer
	public static final Class<?> TYPE = ((Supplier<Class<?>>) (() -> {
		try {
			return NMSUtils.getNMSClass("DataWatcherObject");
		} catch (ClassNotFoundException exception) {
			return null;
		}
	})).get();

	private static final Constructor<?> CONSTRUCTOR = isPresent() ? TYPE.getConstructors()[0] : null;

	/**
	 * Get the entry's index.
	 *
	 * @return the index.
	 */
	public abstract int getIndex();

	/**
	 * Get the object's serializer.
	 *
	 * @return the serializer.
	 */
	public abstract WrappedDataWatcherSerializer getSerializer();

	/**
	 * Get the wrapper handle.
	 *
	 * @return the handle.
	 */
	public abstract Object getRaw();

	/**
	 * Get if the wrapped class exists.
	 *
	 * @return if the wrapped class is present.
	 */
	public static boolean isPresent() {
		return TYPE != null;
	}

	/**
	 * Create a data watcher object for an index and serializer.
	 *
	 * @param index Target index.
	 * @param serializer Target serializer or null.
	 * @return the data watcher object.
	 */
	public static DataWatcherObject fromIndex(int index, WrappedDataWatcherSerializer serializer) {
		if (isPresent()) {
			try {
				return new WrappedDataWatcherObject(CONSTRUCTOR.newInstance(index, serializer == null ? null : serializer.getRaw()));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
				// TODO: Proper exception handling
				throw new RuntimeException("Could not invoke constructor", exception);
			}
		}
		return new DummyDataWatcherObject(index);
	}

	/**
	 * Create a data watcher object from handle.
	 * Note that this method only works if the DataWatcherObject exists.
	 *
	 * @param handle Target handle.
	 * @return the data watcher object.
	 */
	public static DataWatcherObject fromHandle(Object handle) {
		if (!isPresent()) {
			throw new UnsupportedOperationException("Handle type is not present");
		}
		return new WrappedDataWatcherObject(handle);
	}

	/**
	 * A DataWatcherObject.
	 *
	 * @author SteelPhoenix
	 */
	private static class WrappedDataWatcherObject extends DataWatcherObject {

		private static final Field INDEX;
		private static final Field SERIALIZER;
		private final Object raw;

		static {
			Field index = null;
			Field serializer = null;
			if (isPresent()) {
				// Look for first fields of the correct type
				for (Field f : TYPE.getDeclaredFields()) {
					if (index == null && f.getType() == Integer.TYPE) {
						index = f;
					}
					if (serializer == null && f.getType() == WrappedDataWatcherSerializer.TYPE) {
						serializer = f;
					}
				}
			}

			INDEX = index;
			SERIALIZER = serializer;
		}

		private WrappedDataWatcherObject(Object nms) {
			if (nms == null) {
				throw new NullPointerException("Object cannot be null");
			}
			if (!TYPE.isAssignableFrom(nms.getClass())) {
				throw new IllegalArgumentException("Object is not of type " + TYPE);
			}
			this.raw = nms;
		}

		@Override
		public int getIndex() {
			try {
				return (int) INDEX.get(raw);
			} catch (IllegalArgumentException | IllegalAccessException exception) {
				// TODO: Proper exception handling
				throw new RuntimeException("Could not read field value", exception);
			}
		}

		@Override
		public WrappedDataWatcherSerializer getSerializer() {
			Object handle;
			try {
				handle = SERIALIZER.get(raw);
			} catch (IllegalArgumentException | IllegalAccessException exception) {
				// TODO: Proper exception handling
				throw new RuntimeException("Could not read field value", exception);
			}

			// Not set
			if (handle == null) {
				return null;
			}

			// TODO: Get from known serializers

			// Unknown handle type
			return WrappedDataWatcherSerializer.of(handle, null, false);
		}

		@Override
		public Object getRaw() {
			return raw;
		}
	}

	/**
	 * A 1.7-1.8 compat dummy object.
	 * This only stores an index.
	 *
	 * @author SteelPhoenix
	 */
	public static class DummyDataWatcherObject extends DataWatcherObject {

		private final int index;

		public DummyDataWatcherObject(int index) {
			this.index = index;
		}

		@Override
		public int getIndex() {
			return index;
		}

		@Override
		public WrappedDataWatcherSerializer getSerializer() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object getRaw() {
			throw new UnsupportedOperationException();
		}
	}
}
