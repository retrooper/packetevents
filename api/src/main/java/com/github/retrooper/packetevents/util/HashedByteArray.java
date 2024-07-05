package com.github.retrooper.packetevents.util;

import java.util.Arrays;

public class HashedByteArray {

    private final byte[] data;
    private final int hash;

    public HashedByteArray(byte[] data) {
        this.data = data;
        this.hash = calculateHash(data);
    }

    public byte[] getData() {
        return data;
    }

    private int calculateHash(byte[] data) {
        return Arrays.hashCode(data);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof HashedByteArray) {
            HashedByteArray other = (HashedByteArray) obj;
            return Arrays.equals(data, other.data);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public String toString() {
        return Arrays.toString(data) + " hash: " + hash;
    }
}
