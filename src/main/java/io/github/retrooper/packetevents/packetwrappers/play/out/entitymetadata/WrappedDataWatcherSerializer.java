package io.github.retrooper.packetevents.packetwrappers.play.out.entitymetadata;

import java.util.function.Supplier;

import io.github.retrooper.packetevents.utils.nms.NMSUtils;

/**
 * A data watcher serializer.
 * This class is only present in 1.9+.
 * Note that this is not a proper wrapper due to generics.
 *
 * @author SteelPhoenix
 */
public class WrappedDataWatcherSerializer extends AbstractWrapper {

	// Look at this beautiful code
	// That's what you get for not including proper reflection helpers
	// TODO: Move to static initializer
	public static final Class<?> TYPE = ((Supplier<Class<?>>) (() -> {
		try {
			return NMSUtils.getNMSClass("DataWatcherSerializer");
		} catch (ClassNotFoundException exception) {
			return null;
		}
	})).get();

	private final Class<?> type;
	private final boolean optional;

	private WrappedDataWatcherSerializer(Object nms, Class<?> type, boolean optional) {
		super(TYPE, nms);

		this.type = type;
		this.optional = optional;
	}

	/**
	 * Get the serializer type.
	 *
	 * @return the serializer type.
	 */
	public Class<?> getType() {
		return type;
	}

	/**
	 * Get if the value is wrapped in an {@link java.util.Optional}.
	 *
	 * @return if the value is an optional.
	 */
	public boolean isOptional() {
		return optional;
	}

	/**
	 * Get if the wrapped class exists.
	 *
	 * @return if the wrapped class is present.
	 */
	public static boolean isPresent() {
		return TYPE != null;
	}

	/**
	 * Wrap a DataWatcherSerializer.
	 *
	 * @param nms Handle.
	 * @param type Type it serializes.
	 * @param optional If the return type is wrapped in an optional.
	 * @return the wrapped instance.
	 */
	public static WrappedDataWatcherSerializer of(Object nms, Class<?> type, boolean optional) {
		if (!isPresent()) {
			throw new UnsupportedOperationException("DataWatcherSerializer does not exist in this minecraft version");
		}
		return new WrappedDataWatcherSerializer(nms, type, optional);
	}
}
