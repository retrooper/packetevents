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
public final class TrapezoidFloat implements FloatProvider {

    public static final NbtMapCodec<TrapezoidFloat> MAP_CODEC = new NbtMapCodec<TrapezoidFloat>() {
        @Override
        public TrapezoidFloat decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            float min = tag.getNumberTagValueOrThrow("min").floatValue();
            float max = tag.getNumberTagValueOrThrow("max").floatValue();
            float plateau = tag.getNumberTagValueOrThrow("plateau").floatValue();
            return new TrapezoidFloat(min, max, plateau);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, TrapezoidFloat value) throws NbtCodecException {
            tag.setTag("min", new NBTFloat(value.min));
            tag.setTag("max", new NBTFloat(value.max));
            tag.setTag("plateau", new NBTFloat(value.plateau));
        }
    };

    private final float min;
    private final float max;
    private final float plateau;

    public TrapezoidFloat(float min, float max, float plateau) {
        this.min = min;
        this.max = max;
        this.plateau = plateau;
    }

    @Override
    public FloatProviderType<?> getType() {
        return FloatProviderTypes.TRAPEZOID;
    }

    @Override
    public float getSample(Random random) {
        float range = this.max - this.min;
        float plateauStart = (range - this.plateau) / 2f;
        float plateauEnd = range - plateauStart;
        return this.min
                + random.nextFloat() * plateauEnd
                + random.nextFloat() * plateauStart;
    }

    @Override
    public float getMin() {
        return this.min;
    }

    @Override
    public float getMax() {
        return this.max;
    }

    public float getPlateau() {
        return this.plateau;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        TrapezoidFloat that = (TrapezoidFloat) obj;
        if (Float.compare(that.min, this.min) != 0) return false;
        if (Float.compare(that.max, this.max) != 0) return false;
        return Float.compare(that.plateau, this.plateau) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.min, this.max, this.plateau);
    }
}
