package com.github.retrooper.packetevents.protocol.particle.data;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
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
public class ParticleGeyserBaseData extends ParticleData {

    public static final NbtMapCodec<ParticleGeyserBaseData> MAP_CODEC = new NbtMapCodec<ParticleGeyserBaseData>() {
        @Override
        public ParticleGeyserBaseData decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            int waterBlocks = tag.getNumberTagOrThrow("water_blocks").getAsInt();
            float burstImpulseBase = tag.getNumberTagOrThrow("burst_impulse_base").getAsFloat();
            return new ParticleGeyserBaseData(waterBlocks, burstImpulseBase);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, ParticleGeyserBaseData value) throws NbtCodecException {
            tag.setTag("water_blocks", new NBTInt(value.waterBlocks));
            tag.setTag("burst_impulse_base", new NBTFloat(value.burstImpulseBase));
        }
    };

    private final int waterBlocks;
    private final float burstImpulseBase;

    public ParticleGeyserBaseData(int waterBlocks, float burstImpulseBase) {
        this.waterBlocks = waterBlocks;
        this.burstImpulseBase = burstImpulseBase;
    }

    public static ParticleGeyserBaseData read(PacketWrapper<?> wrapper) {
        int waterBlocks = wrapper.readInt();
        float burstImpulseBase = wrapper.readFloat();
        return new ParticleGeyserBaseData(waterBlocks, burstImpulseBase);
    }

    public static void write(PacketWrapper<?> wrapper, ParticleGeyserBaseData data) {
        wrapper.writeInt(data.waterBlocks);
        wrapper.writeFloat(data.burstImpulseBase);
    }

    public int getWaterBlocks() {
        return this.waterBlocks;
    }

    public float getBurstImpulseBase() {
        return this.burstImpulseBase;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        ParticleGeyserBaseData that = (ParticleGeyserBaseData) obj;
        if (this.waterBlocks != that.waterBlocks) return false;
        return Float.compare(that.burstImpulseBase, this.burstImpulseBase) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.waterBlocks, this.burstImpulseBase);
    }
}
