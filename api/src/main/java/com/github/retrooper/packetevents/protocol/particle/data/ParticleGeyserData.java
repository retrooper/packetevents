package com.github.retrooper.packetevents.protocol.particle.data;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
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
public class ParticleGeyserData extends ParticleData {

    public static final NbtMapCodec<ParticleGeyserData> MAP_CODEC = new NbtMapCodec<ParticleGeyserData>() {
        @Override
        public ParticleGeyserData decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            int waterBlocks = tag.getNumberTagOrThrow("water_blocks").getAsInt();
            return new ParticleGeyserData(waterBlocks);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, ParticleGeyserData value) throws NbtCodecException {
            tag.setTag("water_blocks", new NBTInt(value.waterBlocks));
        }
    };

    private final int waterBlocks;

    public ParticleGeyserData(int waterBlocks) {
        this.waterBlocks = waterBlocks;
    }

    public static ParticleGeyserData read(PacketWrapper<?> wrapper) {
        int waterBlocks = wrapper.readInt();
        return new ParticleGeyserData(waterBlocks);
    }

    public static void write(PacketWrapper<?> wrapper, ParticleGeyserData data) {
        wrapper.writeInt(data.waterBlocks);
    }

    public int getWaterBlocks() {
        return this.waterBlocks;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        ParticleGeyserData that = (ParticleGeyserData) obj;
        return this.waterBlocks == that.waterBlocks;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.waterBlocks);
    }
}
