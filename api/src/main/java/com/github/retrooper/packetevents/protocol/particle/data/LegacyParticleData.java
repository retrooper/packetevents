package com.github.retrooper.packetevents.protocol.particle.data;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Arrays;

public class LegacyParticleData extends ParticleData implements LegacyConvertible {

    private final int[] legacyData;

    public static LegacyParticleData nullValue(int id) {
        int[] data = new int[getSize(id)];
        Arrays.fill(data, 0);
        return new LegacyParticleData(data);
    }

    public static LegacyParticleData ofTwo(int a, int b) {
        return new LegacyParticleData(a, b);
    }

    public static LegacyParticleData ofOne(int a) {
        return new LegacyParticleData(a);
    }

    public static LegacyParticleData zero() {
        return new LegacyParticleData();
    }

    public static LegacyParticleData ofBlock(ItemType type, byte data) {
        return new LegacyParticleData(type.getId(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion()) | data << 12);
    }

    public static LegacyParticleData ofItem(ItemType type, byte data) {
        return new LegacyParticleData(type.getId(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion()), data);
    }

    LegacyParticleData(int... legacyData) {
        this.legacyData = legacyData;
    }

    public int[] getLegacyData() {
        return legacyData;
    }

    public void validate(int id) {
        if (legacyData.length != getSize(id))
            throw new IllegalArgumentException("Invalid size for type " + id + ": " + legacyData.length);
    }

    public static int getSize(int id) {
        if (id == 36) return 2;
        else if (id == 37 || id == 38 || id == 46) return 1;
        else return 0;
    }

    public static LegacyParticleData read(PacketWrapper<?> wrapper, int typeId) {
        int[] data = new int[getSize(typeId)];
        for (int i = 0; i < data.length; i++) {
            data[i] = wrapper.readVarInt();
        }
        return new LegacyParticleData(data);
    }

    public static void write(PacketWrapper<?> wrapper, int typeId, LegacyParticleData data) {
        data.validate(typeId);
        for (int i = 0; i < data.legacyData.length; i++) {
            wrapper.writeVarInt(data.legacyData[i]);
        }
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public LegacyParticleData toLegacy(ClientVersion version) {
        return new LegacyParticleData(legacyData);
    }

}
