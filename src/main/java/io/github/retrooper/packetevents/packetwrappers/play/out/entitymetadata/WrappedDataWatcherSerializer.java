package io.github.retrooper.packetevents.packetwrappers.play.out.entitymetadata;

import io.github.retrooper.packetevents.utils.nms.NMSUtils;

/**
 * A data watcher serializer.
 * This class is only present in 1.9+.
 * Note that this is not a proper wrapper due to generics.
 *
 * @author SteelPhoenix
 */
public class WrappedDataWatcherSerializer {

    public static final Class<?> TYPE = NMSUtils.getNMSClassWithoutException("DataWatcherSerializer");

    private final Class<?> type;
    private final boolean optional;
    private Object raw;

    private WrappedDataWatcherSerializer(Object nms, Class<?> type, boolean optional) {
        this.raw = nms;
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

    public Object getRaw() {
        return raw;
    }

    /**
     * Wrap a DataWatcherSerializer.
     *
     * @param nms      Handle.
     * @param type     Type it serializes.
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
