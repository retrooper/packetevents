package com.github.retrooper.packetevents.util.mappings;

public interface Diff<T> {

    void applyTo(T t);

}
