package com.github.retrooper.packetevents.protocol.valueproviders.floats;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

import java.util.Random;

/**
 * @versions 1.17+
 */
@NullMarked
public interface FloatProvider {

    NbtMapCodec<FloatProvider> MAP_CODEC = new NbtMapCodec<FloatProvider>() {
        @Override
        public FloatProvider decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            FloatProviderType<?> type = tag.getOrThrow("type", FloatProviderType.CODEC, wrapper);
            return type.getCodec().decode(tag, wrapper);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, FloatProvider value) throws NbtCodecException {
            @SuppressWarnings("unchecked")
            FloatProviderType<FloatProvider> unsafeType = (FloatProviderType<FloatProvider>) value.getType();
            unsafeType.getCodec().encode(tag, wrapper, value);
            tag.set("type", value.getType(), FloatProviderType.CODEC, wrapper);
        }
    };
    NbtCodec<FloatProvider> CODEC = new NbtCodec<FloatProvider>() {
        private final NbtCodec<FloatProvider> delegate = MAP_CODEC.codec();

        @Override
        public FloatProvider decode(NBT tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            if (tag instanceof NBTNumber) {
                return new ConstantFloat(((NBTNumber) tag).getAsFloat());
            }
            return this.delegate.decode(tag, wrapper);
        }

        @Override
        public NBT encode(PacketWrapper<?> wrapper, FloatProvider value) throws NbtCodecException {
            if (value instanceof ConstantFloat) {
                return new NBTFloat(((ConstantFloat) value).getValue());
            }
            return this.delegate.encode(wrapper, value);
        }
    };

    FloatProviderType<?> getType();

    float getSample(Random random);

    float getMin();

    float getMax();
}
