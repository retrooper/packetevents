package com.github.retrooper.packetevents.protocol.particle.data;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class ParticleShriekData extends ParticleData {

    private int delay;

    public ParticleShriekData(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public static ParticleShriekData read(PacketWrapper<?> wrapper) {
        return new ParticleShriekData(wrapper.readVarInt());
    }

    public static void write(PacketWrapper<?> wrapper, ParticleShriekData data) {
        wrapper.writeVarInt(data.getDelay());
    }

    public static ParticleShriekData decode(NBTCompound compound, ClientVersion version) {
        int delay = compound.getNumberTagOrThrow("delay").getAsInt();
        return new ParticleShriekData(delay);
    }

    public static void encode(ParticleShriekData data, ClientVersion version, NBTCompound compound) {
        compound.setTag("delay", new NBTInt(data.delay));
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

}
