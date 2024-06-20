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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WrapperPlayServerTags extends PacketWrapper<WrapperPlayServerTags> {

    private Map<ResourceLocation, List<Tag>> tags;

    public WrapperPlayServerTags(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerTags(Map<ResourceLocation, List<Tag>> tags) {
        super(PacketType.Play.Server.TAGS);
        this.tags = tags;
    }

    @Override
    public void read() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            this.tags = this.readMap(
                    PacketWrapper::readIdentifier,
                    ew -> ew.readList(Tag::read));
        } else {
            this.tags = new HashMap<>(4);
            this.tags.put(ResourceLocation.minecraft("block"), this.readList(Tag::read));
            this.tags.put(ResourceLocation.minecraft("item"), this.readList(Tag::read));
            this.tags.put(ResourceLocation.minecraft("fluid"), this.readList(Tag::read));
            this.tags.put(ResourceLocation.minecraft("entity_type"), this.readList(Tag::read));
        }
    }

    @Override
    public void write() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            this.writeMap(this.tags,
                    PacketWrapper::writeIdentifier,
                    (ew, tags) -> ew.writeList(tags, Tag::write));
        } else {
            this.writeList(this.tags.get(ResourceLocation.minecraft("block")), Tag::write);
            this.writeList(this.tags.get(ResourceLocation.minecraft("item")), Tag::write);
            this.writeList(this.tags.get(ResourceLocation.minecraft("fluid")), Tag::write);
            this.writeList(this.tags.get(ResourceLocation.minecraft("entity_type")), Tag::write);
        }
    }

    @Override
    public void copy(WrapperPlayServerTags wrapper) {
        this.tags = wrapper.tags;
    }

    public Map<ResourceLocation, List<Tag>> getTagMap() {
        return this.tags;
    }

    public void setTagMap(Map<ResourceLocation, List<Tag>> tags) {
        this.tags = tags;
    }

    @Deprecated
    public Map<String, List<Tag>> getTags() {
        if (this.tags == null) {
            return null;
        }
        Map<String, List<Tag>> tags = new HashMap<>(this.tags.size());
        for (Map.Entry<ResourceLocation, List<Tag>> entry : this.tags.entrySet()) {
            tags.put(entry.getKey().toString(), entry.getValue());
        }
        return Collections.unmodifiableMap(tags);
    }

    @Deprecated
    public void setTags(HashMap<String, List<Tag>> tags) {
        if (tags == null) {
            this.tags = null;
        } else {
            this.tags = new HashMap<>(tags.size());
            for (Map.Entry<String, List<Tag>> entry : tags.entrySet()) {
                this.tags.put(new ResourceLocation(entry.getKey()), entry.getValue());
            }
        }
    }

    public static class Tag {

        private ResourceLocation key;
        private List<Integer> values;

        public Tag(String name, List<Integer> values) {
            this(new ResourceLocation(name), values);
        }

        public Tag(ResourceLocation key, List<Integer> values) {
            this.key = key;
            this.values = values;
        }

        public static Tag read(PacketWrapper<?> wrapper) {
            ResourceLocation tagName = wrapper.readIdentifier();
            List<Integer> values = wrapper.readList(PacketWrapper::readVarInt);
            return new Tag(tagName, values);
        }

        public static void write(PacketWrapper<?> wrapper, Tag tag) {
            wrapper.writeIdentifier(tag.key);
            wrapper.writeList(tag.values, PacketWrapper::writeVarInt);
        }

        public String getName() {
            return this.key.toString();
        }

        public void setName(String name) {
            this.key = new ResourceLocation(name);
        }

        public ResourceLocation getKey() {
            return this.key;
        }

        public void setKey(ResourceLocation key) {
            this.key = key;
        }

        public List<Integer> getValues() {
            return this.values;
        }

        public void setValues(List<Integer> values) {
            this.values = values;
        }
    }
}
