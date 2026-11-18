package com.github.retrooper.packetevents.protocol.attribute;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@NullMarked
public final class AttributeEntry {

    public static final NbtCodec<AttributeEntry> CODEC = new NbtMapCodec<AttributeEntry>() {
        @Override
        public AttributeEntry decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            Attribute attribute = tag.getOrThrow("attribute", Attribute.CODEC, wrapper);
            AttributeModifier modifier = AttributeModifier.MAP_CODEC.decode(tag, wrapper);
            return new AttributeEntry(attribute, modifier);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, AttributeEntry value) throws NbtCodecException {
            tag.set("attribute", value.attribute, Attribute.CODEC, wrapper);
            AttributeModifier.MAP_CODEC.encode(tag, wrapper, value.modifier);
        }
    }.codec();

    private final Attribute attribute;
    private final AttributeModifier modifier;

    public AttributeEntry(Attribute attribute, AttributeModifier modifier) {
        this.attribute = attribute;
        this.modifier = modifier;
    }

    public static AttributeEntry read(PacketWrapper<?> wrapper) {
        Attribute attribute = wrapper.readMappedEntity(Attributes.getRegistry());
        AttributeModifier modifier = AttributeModifier.read(wrapper);
        return new AttributeEntry(attribute, modifier);
    }

    public static void write(PacketWrapper<?> wrapper, AttributeEntry entry) {
        wrapper.writeMappedEntity(entry.attribute);
        AttributeModifier.write(wrapper, entry.modifier);
    }

    public Attribute getAttribute() {
        return this.attribute;
    }

    public AttributeModifier getModifier() {
        return this.modifier;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        AttributeEntry that = (AttributeEntry) obj;
        if (!this.attribute.equals(that.attribute)) return false;
        return this.modifier.equals(that.modifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.attribute, this.modifier);
    }
}
