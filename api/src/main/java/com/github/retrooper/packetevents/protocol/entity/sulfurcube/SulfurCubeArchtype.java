package com.github.retrooper.packetevents.protocol.entity.sulfurcube;

import com.github.retrooper.packetevents.protocol.attribute.AttributeEntry;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * @versions 26.2+
 */
@NullMarked
public interface SulfurCubeArchtype extends MappedEntity, CopyableEntity<SulfurCubeArchtype>, DeepComparableEntity {

    NbtCodec<SulfurCubeArchtype> DIRECT_CODEC = new NbtMapCodec<SulfurCubeArchtype>() {
        @Override
        public SulfurCubeArchtype decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            MappedEntitySet<ItemType> items = tag.getOrThrow("items", MappedEntitySet.codec(ItemTypes.getRegistry()), wrapper);
            List<AttributeEntry> attributeModifiers = tag.getListOrThrow("attribute_modifiers", AttributeEntry.CODEC, wrapper);
            boolean buoyant = tag.getBooleanOr("buoyant", false);
            SulfurCubeExplosionData explosion = tag.getOrNull("explosion", SulfurCubeExplosionData.CODEC, wrapper);
            SulfurCubeContactDamage contactDamage = tag.getOrNull("contact_damage", SulfurCubeContactDamage.CODEC, wrapper);
            SulfurCubeKnockbackModifiers knockbackModifiers = tag.getOrThrow("knockback_modifiers", SulfurCubeKnockbackModifiers.CODEC, wrapper);
            SulfurCubeSoundSettings soundSettings = tag.getOrThrow("sound_settings", SulfurCubeSoundSettings.CODEC, wrapper);
            return new StaticSulfurCubeArchtype(null, items, attributeModifiers, buoyant, explosion, contactDamage, knockbackModifiers, soundSettings);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, SulfurCubeArchtype value) throws NbtCodecException {
            tag.set("items", value.getItems(), MappedEntitySet.codec(ItemTypes.getRegistry()), wrapper);
            tag.setList("attribute_modifiers", value.getAttributeModifiers(), AttributeEntry.CODEC, wrapper);
            if (value.isBuoyant()) {
                tag.setTag("buoyant", new NBTByte(true));
            }
            if (value.getExplosion() != null) {
                tag.set("explosion", value.getExplosion(), SulfurCubeExplosionData.CODEC, wrapper);
            }
            if (value.getContactDamage() != null) {
                tag.set("contact_damage", value.getContactDamage(), SulfurCubeContactDamage.CODEC, wrapper);
            }
            tag.set("knockback_modifiers", value.getKnockbackModifiers(), SulfurCubeKnockbackModifiers.CODEC, wrapper);
            tag.set("sound_settings", value.getSoundSettings(), SulfurCubeSoundSettings.CODEC, wrapper);
        }
    }.codec();

    MappedEntitySet<ItemType> getItems();

    List<AttributeEntry> getAttributeModifiers();

    boolean isBuoyant();

    @Nullable SulfurCubeExplosionData getExplosion();

    @Nullable SulfurCubeContactDamage getContactDamage();

    SulfurCubeKnockbackModifiers getKnockbackModifiers();

    SulfurCubeSoundSettings getSoundSettings();
}
