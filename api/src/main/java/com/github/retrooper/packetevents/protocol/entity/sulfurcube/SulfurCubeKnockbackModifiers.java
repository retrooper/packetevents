package com.github.retrooper.packetevents.protocol.entity.sulfurcube;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * @versions 26.2+
 */
@NullMarked
public final class SulfurCubeKnockbackModifiers {

    public static final NbtCodec<SulfurCubeKnockbackModifiers> CODEC = new NbtMapCodec<SulfurCubeKnockbackModifiers>() {
        @Override
        public SulfurCubeKnockbackModifiers decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            float horizontalPower = tag.getNumberTagValueOrThrow("horizontal_power").floatValue();
            float verticalPower = tag.getNumberTagValueOrThrow("vertical_power").floatValue();
            return new SulfurCubeKnockbackModifiers(horizontalPower, verticalPower);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, SulfurCubeKnockbackModifiers value) throws NbtCodecException {
            tag.setTag("horizontal_power", new NBTFloat(value.horizontalPower));
            tag.setTag("vertical_power", new NBTFloat(value.verticalPower));
        }
    }.codec();

    private final float horizontalPower;
    private final float verticalPower;

    public SulfurCubeKnockbackModifiers(float horizontalPower, float verticalPower) {
        this.horizontalPower = horizontalPower;
        this.verticalPower = verticalPower;
    }

    public float getHorizontalPower() {
        return this.horizontalPower;
    }

    public float getVerticalPower() {
        return this.verticalPower;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        SulfurCubeKnockbackModifiers that = (SulfurCubeKnockbackModifiers) obj;
        if (Float.compare(that.horizontalPower, this.horizontalPower) != 0) return false;
        return Float.compare(that.verticalPower, this.verticalPower) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.horizontalPower, this.verticalPower);
    }
}
