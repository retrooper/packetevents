package io.github.retrooper.packetevents.utils.gameprofile;

import java.util.Collection;

public interface WrappedPropertyMapAbstract<T, K> {
    Collection<K> get(T key);

    void put(T key, K value);
}
