/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.packetevents.wrapper.configuration.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The packet has been completely repurposed with 1.20.5. Before 1.20.5,
 * all registries would be sent with this one packet, encoded with nbt.
 * With 1.20.5, this packet gets sent for every registry which needs
 * to be synchronized.
 */
public class WrapperConfigServerRegistryData extends PacketWrapper<WrapperConfigServerRegistryData> {

    // before 1.20.5
    private NBTCompound registryData;
    // with 1.20.5
    private ResourceLocation registryKey;
    private List<RegistryElement> elements;

    public WrapperConfigServerRegistryData(PacketSendEvent event) {
        super(event);
    }

    @ApiStatus.Obsolete
    public WrapperConfigServerRegistryData(NBTCompound registryData) {
        this(registryData, null, null);
    }

    public WrapperConfigServerRegistryData(ResourceLocation registryKey, List<RegistryElement> elements) {
        this(null, registryKey, elements);
    }

    @ApiStatus.Obsolete
    public WrapperConfigServerRegistryData(
            @Nullable NBTCompound registryData,
            @Nullable ResourceLocation registryKey,
            @Nullable List<RegistryElement> elements
    ) {
        super(PacketType.Configuration.Server.REGISTRY_DATA);
        this.registryData = registryData;
        this.registryKey = registryKey;
        this.elements = elements;
    }

    @Override
    public void read() {
        if (this.serverVersion.isOlderThan(ServerVersion.V_1_20_5)) {
            this.registryData = this.readUnlimitedNBT();
            return;
        }
        this.registryKey = this.readIdentifier();
        this.elements = this.readList(wrapper -> {
            ResourceLocation id = wrapper.readIdentifier();
            NBT data = wrapper.readOptional(PacketWrapper::readNBTRaw);
            return new RegistryElement(id, data);
        });
    }

    @Override
    public void write() {
        if (this.serverVersion.isOlderThan(ServerVersion.V_1_20_5)) {
            this.writeNBT(this.registryData);
            return;
        }
        this.writeIdentifier(this.registryKey);
        this.writeList(this.elements, (wrapper, element) -> {
            wrapper.writeIdentifier(element.getId());
            wrapper.writeOptional(element.getData(), PacketWrapper::writeNBTRaw);
        });
    }

    @Override
    public void copy(WrapperConfigServerRegistryData wrapper) {
        this.registryData = wrapper.registryData;
        this.registryKey = wrapper.registryKey;
        this.elements = wrapper.elements;
    }

    @ApiStatus.Obsolete
    public @Nullable NBTCompound getRegistryData() {
        return this.registryData;
    }

    @ApiStatus.Obsolete
    public void setRegistryData(NBTCompound registryData) {
        this.registryData = registryData;
    }

    public @Nullable ResourceLocation getRegistryKey() {
        return this.registryKey;
    }

    public void setRegistryKey(ResourceLocation registryKey) {
        this.registryKey = registryKey;
    }

    public @Nullable List<RegistryElement> getElements() {
        return this.elements;
    }

    public void setElements(List<RegistryElement> elements) {
        this.elements = elements;
    }

    public static class RegistryElement {

        private final ResourceLocation id;
        private final @Nullable NBT data;

        public RegistryElement(NBTCompound nbt) {
            this(new ResourceLocation(nbt.getStringTagValueOrThrow("name")),
                    nbt.getTagOrNull("element"));
        }

        public RegistryElement(ResourceLocation id, @Nullable NBT data) {
            this.id = id;
            this.data = data;
        }

        public static List<RegistryElement> convertNbt(NBTList<NBTCompound> list) {
            List<RegistryElement> elements = new ArrayList<>(list.size());
            for (NBTCompound tag : list.getTags()) {
                elements.add(new RegistryElement(tag));
            }
            return Collections.unmodifiableList(elements);
        }

        public ResourceLocation getId() {
            return this.id;
        }

        public @Nullable NBT getData() {
            return this.data;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof RegistryElement)) return false;
            RegistryElement that = (RegistryElement) obj;
            if (!this.id.equals(that.id)) return false;
            return Objects.equals(this.data, that.data);
        }

        @Override
        public int hashCode() {
            int result = this.id.hashCode();
            result = 31 * result + (this.data != null ? this.data.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "RegistryElement{id=" + this.id + ", data=" + this.data + '}';
        }
    }
}
