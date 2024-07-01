package com.github.retrooper.packetevents.protocol.nbt.serializer;

import com.github.retrooper.packetevents.protocol.nbt.NBT;

import java.io.IOException;

public interface NBTWriter<T extends NBT, OUT> {

    default void serializeTag(OUT to, T tag) throws IOException {
        serializeTag(to, tag, true);
    }

    void serializeTag(OUT to, NBT tag, boolean named) throws IOException;

}
