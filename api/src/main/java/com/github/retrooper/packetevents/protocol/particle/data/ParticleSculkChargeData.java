package com.github.retrooper.packetevents.protocol.particle.data;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class ParticleSculkChargeData extends ParticleData {

    private float roll;

    public ParticleSculkChargeData(float roll) {
        this.roll = roll;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public static ParticleSculkChargeData read(PacketWrapper<?> wrapper) {
        return new ParticleSculkChargeData(wrapper.readFloat());
    }

    public static void write(PacketWrapper<?> wrapper, ParticleSculkChargeData data) {
        wrapper.writeFloat(data.getRoll());
    }

    public static ParticleSculkChargeData decode(NBTCompound compound, ClientVersion version) {
        float roll = compound.getNumberTagOrThrow("roll").getAsFloat();
        return new ParticleSculkChargeData(roll);
    }

    public static void encode(ParticleSculkChargeData data, ClientVersion version, NBTCompound compound) {
        compound.setTag("roll", new NBTFloat(data.roll));
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

}
