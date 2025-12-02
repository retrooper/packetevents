package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.potion.PotionType;
import com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.BitSet;

public class WrapperPlayServerEntityEffect extends PacketWrapper<WrapperPlayServerEntityEffect> {
    private static final int FLAG_AMBIENT = 1;
    private static final int FLAG_VISIBLE = 2;
    private static final int FLAG_SHOW_ICONS = 4;

    private int entityID;
    private PotionType potionType;
    private int effectAmplifier;
    private int effectDurationTicks;
    private byte flags;
    private NBTCompound factorData;

    public WrapperPlayServerEntityEffect(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerEntityEffect(int entityID, PotionType potionType, int amplifier, int duration, byte flags) {
        super(PacketType.Play.Server.ENTITY_EFFECT);
        this.entityID = entityID;
        this.potionType = potionType;
        this.effectAmplifier = amplifier;
        this.effectDurationTicks = duration;
        this.flags = flags;
    }

    @Override
    public void read() {
        this.entityID = readVarInt();
        int effectId;
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18_2)) {
            effectId = readVarInt();
        } else {
            effectId = readByte();
        }
        this.potionType = PotionTypes.getById(effectId, this.serverVersion);
        this.effectAmplifier = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)
                ? this.readVarInt() : this.readByte();
        this.effectDurationTicks = readVarInt();
        if (serverVersion.isNewerThan(ServerVersion.V_1_7_10)) {
            this.flags = readByte();
        }
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)
                && this.serverVersion.isOlderThan(ServerVersion.V_1_20_5)) {
            factorData = readOptional(PacketWrapper::readNBT);
        }
    }

    @Override
    public void write() {
        writeVarInt(entityID);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18_2)) {
            writeVarInt(potionType.getId(serverVersion.toClientVersion()));
        } else {
            writeByte(potionType.getId(serverVersion.toClientVersion()));
        }
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
            this.writeVarInt(this.effectAmplifier);
        } else {
            this.writeByte(this.effectAmplifier);
        }
        writeVarInt(effectDurationTicks);
        if (serverVersion.isNewerThan(ServerVersion.V_1_7_10)) {
            writeByte(flags);
        }
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)
                && this.serverVersion.isOlderThan(ServerVersion.V_1_20_5)) {
            this.writeOptional(this.factorData, PacketWrapper::writeNBT);
        }
    }

    @Override
    public void copy(WrapperPlayServerEntityEffect wrapper) {
        entityID = wrapper.entityID;
        potionType = wrapper.potionType;
        effectAmplifier = wrapper.effectAmplifier;
        effectDurationTicks = wrapper.effectDurationTicks;
        flags = wrapper.flags;
        factorData = wrapper.factorData;
    }

    public PotionType getPotionType() {
        return potionType;
    }

    public void setPotionType(PotionType potionType) {
        this.potionType = potionType;
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityId(int entityID) {
        this.entityID = entityID;
    }

    public int getEffectAmplifier() {
        return effectAmplifier;
    }

    public void setEffectAmplifier(int effectAmplifier) {
        this.effectAmplifier = effectAmplifier;
    }

    public int getEffectDurationTicks() {
        return effectDurationTicks;
    }

    public void setEffectDurationTicks(int effectDurationTicks) {
        this.effectDurationTicks = effectDurationTicks;
    }

    private byte getFlags() {
        return flags;
    }

    private void setFlags(byte flags) {
        this.flags = flags;
    }

    public @Nullable NBTCompound getFactorData() {
        return factorData;
    }

    public void setFactorData(@Nullable NBTCompound factorData) {
        this.factorData = factorData;
    }


    private byte constructFlags(boolean ambient, boolean visible, boolean icons) {
        BitSet bitSet = new BitSet(3);
        bitSet.set(0, ambient);
        bitSet.set(1, visible);
        bitSet.set(2, icons);
        return bitSet.toByteArray()[0];
    }

    public boolean isAmbient() {
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_8_8)) {
            return false;
        }
        return (getFlags() & FLAG_AMBIENT) == FLAG_AMBIENT;
    }

    public void setAmbient(boolean isAmbient) {
        if (serverVersion.isNewerThan(ServerVersion.V_1_8_8)) {
            // used as a boolean in 1.9, works either way though
            setFlags(constructFlags(isVisible(), isAmbient, isShowIcon()));
        }
    }

    public boolean isVisible() {
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
            return false;
        }
        if (serverVersion.isOlderThan(ServerVersion.V_1_10)) {
            //hideParticles field is used as a boolean
            return getFlags() != 0;
        } else {
            return (getFlags() & FLAG_VISIBLE) == FLAG_VISIBLE;
        }
    }

    public void setVisible(boolean isVisible) {
        if (serverVersion.isOlderThan(ServerVersion.V_1_10)) {
            return;
        }
        setFlags(constructFlags(isVisible, isAmbient(), isShowIcon()));
    }

    public boolean isShowIcon() {
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_13_2)) {
            return false;
        }
        return (getFlags() & FLAG_SHOW_ICONS) == FLAG_SHOW_ICONS;
    }

    public void setShowIcon(boolean showIcon) {
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_13_2)) {
            return;
        }
        setFlags(constructFlags(isVisible(), isAmbient(), showIcon));
    }
}
