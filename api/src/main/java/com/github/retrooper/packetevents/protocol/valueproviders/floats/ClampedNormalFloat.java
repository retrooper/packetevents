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
public final class ClampedNormalFloat implements FloatProvider {

    public static final NbtMapCodec<ClampedNormalFloat> MAP_CODEC = new NbtMapCodec<ClampedNormalFloat>() {
        @Override
        public ClampedNormalFloat decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            float mean = tag.getNumberTagValueOrThrow("mean").floatValue();
            float deviation = tag.getNumberTagValueOrThrow("deviation").floatValue();
            float min = tag.getNumberTagValueOrThrow("min").floatValue();
            float max = tag.getNumberTagValueOrThrow("max").floatValue();
            return new ClampedNormalFloat(mean, deviation, min, max);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, ClampedNormalFloat value) throws NbtCodecException {
            tag.setTag("mean", new NBTFloat(value.mean));
            tag.setTag("deviation", new NBTFloat(value.deviation));
            tag.setTag("min", new NBTFloat(value.min));
            tag.setTag("max", new NBTFloat(value.max));
        }
    };

    private final float mean;
    private final float deviation;
    private final float min;
    private final float max;

    public ClampedNormalFloat(float mean, float deviation, float min, float max) {
        this.mean = mean;
        this.deviation = deviation;
        this.min = min;
        this.max = max;
    }

    @Override
    public FloatProviderType<?> getType() {
        return FloatProviderTypes.CLAMPED_NORMAL;
    }

    @Override
    public float getSample(Random random) {
        float value = this.mean + (float) random.nextGaussian() * this.deviation;
        return Math.min(this.max, Math.max(value, this.min));
    }

    @Override
    public float getMin() {
        return this.min;
    }

    @Override
    public float getMax() {
        return this.max;
    }

    public float getMean() {
        return this.mean;
    }

    public float getDeviation() {
        return this.deviation;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        ClampedNormalFloat that = (ClampedNormalFloat) obj;
        if (Float.compare(that.mean, this.mean) != 0) return false;
        if (Float.compare(that.deviation, this.deviation) != 0) return false;
        if (Float.compare(that.min, this.min) != 0) return false;
        return Float.compare(that.max, this.max) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.mean, this.deviation, this.min, this.max);
    }
}
