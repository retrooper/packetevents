/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.PlayerModelType;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import net.kyori.adventure.text.object.PlayerHeadObjectContents.ProfileProperty;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static net.kyori.adventure.text.object.PlayerHeadObjectContents.property;

@NullMarked
public final class ItemProfile {

    private @Nullable String name;
    private @Nullable UUID id;
    private List<Property> properties;
    /**
     * @versions 1.21.9+
     */
    private SkinPatch skinPatch;

    public ItemProfile(
            @Nullable String name,
            @Nullable UUID id,
            List<Property> properties
    ) {
        this(name, id, properties, SkinPatch.EMPTY);
    }

    public ItemProfile(
            @Nullable String name,
            @Nullable UUID id,
            List<Property> properties,
            SkinPatch skinPatch
    ) {
        this.name = name;
        this.id = id;
        this.properties = properties;
        this.skinPatch = skinPatch;
    }

    public static ItemProfile read(PacketWrapper<?> wrapper) {
        String name;
        UUID id;
        boolean partial = wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_9) || !wrapper.readBoolean();
        if (!partial) {
            id = wrapper.readUUID();
            name = wrapper.readString(16);
        } else {
            name = wrapper.readOptional(ew -> ew.readString(16));
            id = wrapper.readOptional(PacketWrapper::readUUID);
        }
        List<Property> properties = wrapper.readList(Property::read);
        SkinPatch skinPatch = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9)
                ? SkinPatch.read(wrapper) : SkinPatch.EMPTY;
        return new ItemProfile(name, id, properties, skinPatch);
    }

    public static void write(PacketWrapper<?> wrapper, ItemProfile profile) {
        boolean partial;
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
            partial = profile.name == null || profile.id == null;
            wrapper.writeBoolean(!partial);
        } else {
            // always partial profile (which is way simpler, why did they change this?)
            partial = true;
        }
        if (!partial) {
            wrapper.writeUUID(profile.id);
            wrapper.writeString(profile.name, 16);
        } else {
            wrapper.writeOptional(profile.name, (ew, name) -> ew.writeString(name, 16));
            wrapper.writeOptional(profile.id, PacketWrapper::writeUUID);
        }
        wrapper.writeList(profile.properties, Property::write);
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
            SkinPatch.write(wrapper, profile.skinPatch);
        }
    }

    public static ItemProfile decode(NBT nbt, PacketWrapper<?> wrapper) {
        if (nbt instanceof NBTString) {
            String name = ((NBTString) nbt).getValue();
            return new ItemProfile(name, null, new ArrayList<>());
        }
        NBTCompound compound = (NBTCompound) nbt;
        UUID id = compound.getOrNull("id", NbtCodecs.UUID, wrapper);
        String name = compound.getStringTagValueOrNull("name");
        List<Property> properties = compound.getOrSupply("properties",
                Property.PROPERTY_MAP, ArrayList::new, wrapper);
        SkinPatch patch = SkinPatch.decode(compound, wrapper);
        return new ItemProfile(name, id, properties, patch);
    }

    public static NBT encode(PacketWrapper<?> wrapper, ItemProfile profile) {
        NBTCompound compound = new NBTCompound();
        if (profile.id != null) {
            compound.set("id", profile.id, NbtCodecs.UUID, wrapper);
        }
        if (profile.name != null) {
            compound.setTag("name", new NBTString(profile.name));
        }
        if (!profile.properties.isEmpty()) {
            compound.set("properties", profile.properties, Property.PROPERTY_MAP, wrapper);
        }
        SkinPatch.encode(compound, wrapper, profile.skinPatch);
        return compound;
    }

    public static ItemProfile fromAdventure(PlayerHeadObjectContents headContents) {
        List<ProfileProperty> advProps = headContents.profileProperties();
        List<Property> properties = new ArrayList<>(advProps.size());
        for (ProfileProperty property : advProps) {
            properties.add(Property.fromAdventure(property));
        }
        return new ItemProfile(headContents.name(), headContents.id(), properties);
    }

    public List<ProfileProperty> getAdventureProperties() {
        if (this.properties.isEmpty()) {
            return Collections.emptyList();
        }
        List<ProfileProperty> properties = new ArrayList<>(this.properties.size());
        for (Property property : this.properties) {
            properties.add(property.asAdventure());
        }
        return Collections.unmodifiableList(properties);
    }

    public @Nullable String getName() {
        return this.name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public @Nullable UUID getId() {
        return this.id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }

    public void addProperty(Property property) {
        this.properties.add(property);
    }

    public List<Property> getProperties() {
        return this.properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    /**
     * @versions 1.21.9+
     */
    public SkinPatch getSkinPatch() {
        return this.skinPatch;
    }

    /**
     * @versions 1.21.9+
     */
    public void setSkinPatch(SkinPatch skinPatch) {
        this.skinPatch = skinPatch;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemProfile)) return false;
        ItemProfile that = (ItemProfile) obj;
        if (!Objects.equals(this.name, that.name)) return false;
        if (!Objects.equals(this.id, that.id)) return false;
        if (!this.properties.equals(that.properties)) return false;
        return this.skinPatch.equals(that.skinPatch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.id, this.properties, this.skinPatch);
    }

    public static class Property {

        public static final NbtCodec<Property> CODEC = new NbtCodec<ItemProfile.Property>() {
            @Override
            public ItemProfile.Property decode(NBT nbt, PacketWrapper<?> wrapper) {
                NBTCompound compound = (NBTCompound) nbt;
                String name = compound.getStringTagValueOrThrow("name");
                String value = compound.getStringTagValueOrThrow("value");
                String signature = compound.getStringTagValueOrNull("signature");
                return new ItemProfile.Property(name, value, signature);
            }

            @Override
            public NBT encode(PacketWrapper<?> wrapper, ItemProfile.Property value) {
                NBTCompound compound = new NBTCompound();
                compound.setTag("name", new NBTString(value.getName()));
                compound.setTag("value", new NBTString(value.getValue()));
                if (value.getSignature() != null) {
                    compound.setTag("signature", new NBTString(value.getSignature()));
                }
                return compound;
            }
        };

        public static final NbtCodec<List<ItemProfile.Property>> PROPERTY_MAP = new NbtCodec<List<ItemProfile.Property>>() {
            private final NbtCodec<List<ItemProfile.Property>> propertyList = CODEC.applyList();

            @Override
            public List<ItemProfile.Property> decode(NBT nbt, PacketWrapper<?> wrapper) {
                if (nbt instanceof NBTCompound) {
                    Map<String, NBT> tags = ((NBTCompound) nbt).getTags();
                    List<ItemProfile.Property> properties = new ArrayList<>(tags.size());
                    for (Map.Entry<String, NBT> entry : tags.entrySet()) {
                        for (String value : NbtCodecs.STRING_LIST.decode(entry.getValue(), wrapper)) {
                            properties.add(new ItemProfile.Property(entry.getKey(), value, null));
                        }
                    }
                    return properties;
                }
                return this.propertyList.decode(nbt, wrapper);
            }

            @Override
            public NBT encode(PacketWrapper<?> wrapper, List<ItemProfile.Property> value) {
                return this.propertyList.encode(wrapper, value);
            }
        };

        private String name;
        private String value;
        private @Nullable String signature;

        public Property(String name, String value, @Nullable String signature) {
            this.name = name;
            this.value = value;
            this.signature = signature;
        }

        public static Property read(PacketWrapper<?> wrapper) {
            String name = wrapper.readString(64);
            String value = wrapper.readString(32767);
            String signature = wrapper.readOptional(ew -> ew.readString(1024));
            return new Property(name, value, signature);
        }

        public static void write(PacketWrapper<?> wrapper, Property property) {
            wrapper.writeString(property.name, 64);
            wrapper.writeString(property.value, 32767);
            wrapper.writeOptional(property.signature,
                    (ew, signature) -> ew.writeString(signature, 1024));
        }

        public static Property fromAdventure(ProfileProperty property) {
            return new Property(property.name(), property.value(), property.signature());
        }

        public ProfileProperty asAdventure() {
            return property(this.name, this.value, this.signature);
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public @Nullable String getSignature() {
            return this.signature;
        }

        public void setSignature(@Nullable String signature) {
            this.signature = signature;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Property)) return false;
            Property property = (Property) obj;
            if (!this.name.equals(property.name)) return false;
            if (!this.value.equals(property.value)) return false;
            return Objects.equals(this.signature, property.signature);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.name, this.value, this.signature);
        }
    }

    public static class SkinPatch {

        public static final SkinPatch EMPTY = new SkinPatch(null, null, null, null);

        private final @Nullable ResourceLocation body;
        private final @Nullable ResourceLocation cape;
        private final @Nullable ResourceLocation elytra;
        private final @Nullable PlayerModelType model;

        public SkinPatch(
                @Nullable ResourceLocation body,
                @Nullable ResourceLocation cape,
                @Nullable ResourceLocation elytra,
                @Nullable PlayerModelType model
        ) {
            this.body = body;
            this.cape = cape;
            this.elytra = elytra;
            this.model = model;
        }

        public static SkinPatch read(PacketWrapper<?> wrapper) {
            ResourceLocation body = wrapper.readOptional(ResourceLocation::read);
            ResourceLocation cape = wrapper.readOptional(ResourceLocation::read);
            ResourceLocation elytra = wrapper.readOptional(ResourceLocation::read);
            PlayerModelType model = wrapper.readOptional(PlayerModelType::read);
            return new SkinPatch(body, cape, elytra, model);
        }

        public static void write(PacketWrapper<?> wrapper, SkinPatch patch) {
            wrapper.writeOptional(patch.body, ResourceLocation::write);
            wrapper.writeOptional(patch.cape, ResourceLocation::write);
            wrapper.writeOptional(patch.elytra, ResourceLocation::write);
            wrapper.writeOptional(patch.model, PlayerModelType::write);
        }

        public static SkinPatch decode(NBTCompound nbt, PacketWrapper<?> wrapper) {
            ResourceLocation body = nbt.getOrNull("texture", ResourceLocation.CODEC, wrapper);
            ResourceLocation cape = nbt.getOrNull("cape", ResourceLocation.CODEC, wrapper);
            ResourceLocation elytra = nbt.getOrNull("elytra", ResourceLocation.CODEC, wrapper);
            PlayerModelType model = nbt.getOrNull("model", PlayerModelType.CODEC, wrapper);
            return new SkinPatch(body, cape, elytra, model);
        }

        public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, SkinPatch patch) {
            if (patch.body != null) {
                compound.set("texture", patch.body, ResourceLocation.CODEC, wrapper);
            }
            if (patch.cape != null) {
                compound.set("cape", patch.cape, ResourceLocation.CODEC, wrapper);
            }
            if (patch.elytra != null) {
                compound.set("elytra", patch.elytra, ResourceLocation.CODEC, wrapper);
            }
            if (patch.model != null) {
                compound.set("model", patch.model, PlayerModelType.CODEC, wrapper);
            }
        }

        public @Nullable ResourceLocation getBody() {
            return this.body;
        }

        public @Nullable ResourceLocation getCape() {
            return this.cape;
        }

        public @Nullable ResourceLocation getElytra() {
            return this.elytra;
        }

        public @Nullable PlayerModelType getModel() {
            return this.model;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || this.getClass() != obj.getClass()) return false;
            SkinPatch skinPatch = (SkinPatch) obj;
            if (!Objects.equals(this.body, skinPatch.body)) return false;
            if (!Objects.equals(this.cape, skinPatch.cape)) return false;
            if (!Objects.equals(this.elytra, skinPatch.elytra)) return false;
            return this.model == skinPatch.model;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.body, this.cape, this.elytra, this.model);
        }
    }
}
