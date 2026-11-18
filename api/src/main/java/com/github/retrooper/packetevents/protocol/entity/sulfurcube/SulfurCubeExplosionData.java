package com.github.retrooper.packetevents.protocol.entity.sulfurcube;

import com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
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
public final class SulfurCubeExplosionData {

    public static final NbtCodec<SulfurCubeExplosionData> CODEC = new NbtMapCodec<SulfurCubeExplosionData>() {
        @Override
        public SulfurCubeExplosionData decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            int power = tag.getNumberTagValueOrThrow("power").intValue();
            boolean causesFire = tag.getBooleanOrThrow("causes_fire");
            int fuse = tag.getNumberTagValueOrThrow("fuse").intValue();
            return new SulfurCubeExplosionData(power, causesFire, fuse);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, SulfurCubeExplosionData value) throws NbtCodecException {
            tag.setTag("power", new NBTInt(value.power));
            tag.setTag("causes_fire", new NBTByte(value.causesFire));
            tag.setTag("fuse", new NBTInt(value.fuse));
        }
    }.codec();

    private final int power;
    private final boolean causesFire;
    private final int fuse;

    public SulfurCubeExplosionData(int power, boolean causesFire, int fuse) {
        this.power = power;
        this.causesFire = causesFire;
        this.fuse = fuse;
    }

    public int getPower() {
        return this.power;
    }

    public boolean isCausesFire() {
        return this.causesFire;
    }

    public int getFuse() {
        return this.fuse;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        SulfurCubeExplosionData that = (SulfurCubeExplosionData) obj;
        if (this.power != that.power) return false;
        if (this.causesFire != that.causesFire) return false;
        return this.fuse == that.fuse;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.power, this.causesFire, this.fuse);
    }
}
