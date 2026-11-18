package com.github.retrooper.packetevents.protocol.entity.sulfurcube;

import com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.protocol.valueproviders.floats.FloatProvider;
import com.github.retrooper.packetevents.protocol.world.damagetype.DamageType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * @versions 26.2+
 */
@NullMarked
public final class SulfurCubeContactDamage {

    public static final NbtCodec<SulfurCubeContactDamage> CODEC = new NbtMapCodec<SulfurCubeContactDamage>() {
        @Override
        public SulfurCubeContactDamage decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            DamageType damageType = tag.getOrThrow("damage_type", DamageType.CODEC, wrapper);
            FloatProvider amount = tag.getOrThrow("amount", FloatProvider.CODEC, wrapper);
            boolean attributeToSource = tag.getBooleanOrThrow("attribute_to_source");
            return new SulfurCubeContactDamage(damageType, amount, attributeToSource);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, SulfurCubeContactDamage value) throws NbtCodecException {
            tag.set("damage_type", value.damageType, DamageType.CODEC, wrapper);
            tag.set("amount", value.amount, FloatProvider.CODEC, wrapper);
            tag.setTag("attribute_to_source", new NBTByte(value.attributeToSource));
        }
    }.codec();

    private final DamageType damageType;
    private final FloatProvider amount;
    private final boolean attributeToSource;

    public SulfurCubeContactDamage(DamageType damageType, FloatProvider amount, boolean attributeToSource) {
        this.damageType = damageType;
        this.amount = amount;
        this.attributeToSource = attributeToSource;
    }

    public DamageType getDamageType() {
        return this.damageType;
    }

    public FloatProvider getAmount() {
        return this.amount;
    }

    public boolean isAttributeToSource() {
        return this.attributeToSource;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        SulfurCubeContactDamage that = (SulfurCubeContactDamage) obj;
        if (this.attributeToSource != that.attributeToSource) return false;
        if (!this.damageType.equals(that.damageType)) return false;
        return this.amount.equals(that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.damageType, this.amount, this.attributeToSource);
    }
}
