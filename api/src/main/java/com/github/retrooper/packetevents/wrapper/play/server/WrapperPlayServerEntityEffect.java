package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.potion.PotionType;
import com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.BitSet;

public class WrapperPlayServerEntityEffect extends PacketWrapper<WrapperPlayServerEntityEffect> {
    private static final int FLAG_AMBIENT = 1;
    private static final int FLAG_VISIBLE = 2;
    private static final int FLAG_SHOW_ICONS = 4;
    private int entityID;
    private int effectID;
    private int effectAmplifier;
    private int effectDurationTicks;
    private byte flags;

    public WrapperPlayServerEntityEffect(PacketSendEvent event) {
        super(event);
    }

    @Override
    public void readData() {
        this.entityID = readVarInt();
        this.effectID = readByte();
        this.effectAmplifier = readByte();
        this.effectDurationTicks = readVarInt();
        if (serverVersion.isNewerThan(ServerVersion.V_1_7_10)) {
            this.flags = readByte();
        }
    }

    @Override
    public void writeData() {
        writeVarInt(entityID);
        writeByte(effectID);
        writeByte(effectAmplifier);
        writeVarInt(effectDurationTicks);
        if (serverVersion.isNewerThan(ServerVersion.V_1_7_10)) {
            writeByte(flags);
        }
    }

    @Override
    public void readData(WrapperPlayServerEntityEffect wrapper) {
        entityID = wrapper.entityID;
        effectID = wrapper.effectID;
        effectAmplifier = wrapper.effectAmplifier;
        effectDurationTicks = wrapper.effectDurationTicks;
        flags = wrapper.flags;
    }

    public PotionType getPotionType() {
        return PotionTypes.getById(effectID);
    }

    public void setPotionType(PotionType type) {
        setEffectID(type.getId());
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public int getEffectID() {
        return effectID;
    }

    public void setEffectID(int effectID) {
        this.effectID = effectID;
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
