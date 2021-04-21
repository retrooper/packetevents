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
    private final Object raw;

    private WrappedDataWatcherSerializer(Object nms, Class<?> type, boolean optional) {
        this.raw = nms;
        this.type = type;
        this.optional = optional;
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

    public Object getRaw() {
        return raw;
    }
}
