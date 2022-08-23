package com.github.retrooper.packetevents.protocol.particle.data;

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

    @Override
    public boolean isEmpty() {
        return false;
    }

}
