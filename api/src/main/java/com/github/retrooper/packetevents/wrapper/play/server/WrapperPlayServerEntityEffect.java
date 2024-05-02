package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
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
        effectId = readVarInt();
        this.potionType = PotionTypes.getById(effectId, this.serverVersion);
        this.effectAmplifier = this.readVarInt();
        this.effectDurationTicks = readVarInt();
        this.flags = readByte();
    }

    @Override
    public void write() {
        writeVarInt(entityID);
        writeVarInt(potionType.getId(serverVersion.toClientVersion()));
        this.writeVarInt(this.effectAmplifier);
        writeVarInt(effectDurationTicks);
        writeByte(flags);
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
        return (getFlags() & FLAG_AMBIENT) == FLAG_AMBIENT;
    }

    public void setAmbient(boolean isAmbient) {
        // used as a boolean in 1.9, works either way though
        setFlags(constructFlags(isVisible(), isAmbient, isShowIcon()));
    }

    public boolean isVisible() {
        return (getFlags() & FLAG_VISIBLE) == FLAG_VISIBLE;
    }

    public void setVisible(boolean isVisible) {
        setFlags(constructFlags(isVisible, isAmbient(), isShowIcon()));
    }

    public boolean isShowIcon() {
        return (getFlags() & FLAG_SHOW_ICONS) == FLAG_SHOW_ICONS;
    }

    public void setShowIcon(boolean showIcon) {
        setFlags(constructFlags(isVisible(), isAmbient(), showIcon));
    }
}
