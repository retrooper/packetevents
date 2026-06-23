package com.github.retrooper.packetevents.protocol.valueproviders.floats;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.Random;

/**
 * @versions 1.17+
 */
@NullMarked
public final class ConstantFloat implements FloatProvider {

    public static final NbtMapCodec<ConstantFloat> MAP_CODEC = new NbtMapCodec<ConstantFloat>() {
        @Override
        public ConstantFloat decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            float val = tag.getNumberTagValueOrThrow("value").floatValue();
            return new ConstantFloat(val);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, ConstantFloat value) throws NbtCodecException {
            tag.setTag("value", new NBTFloat(value.value));
        }
    };

    private final float value;

    public ConstantFloat(float value) {
        this.value = value;
    }

    @Override
    public FloatProviderType<?> getType() {
        return FloatProviderTypes.CONSTANT;
    }

    @Override
    public float getSample(Random random) {
        return this.value;
    }

    @Override
    public float getMin() {
        return this.value;
    }

    @Override
    public float getMax() {
        return this.value;
    }

    public float getValue() {
        return this.value;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        ConstantFloat that = (ConstantFloat) obj;
        return Float.compare(that.value, this.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.value);
    }
}
