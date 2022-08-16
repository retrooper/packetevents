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
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WrapperPlayServerTags extends PacketWrapper<WrapperPlayServerTags> {
    private Map<String, List<Tag>> tags;

    public WrapperPlayServerTags(PacketSendEvent event) {
        super(event);
    }

    @Override
    public void read() {
        int count = readVarInt(); // Number of resource tags sent
        tags = new HashMap<>(count);

        for (int tagIter = 0; tagIter < count; tagIter++) {
            String resourceName = readString();
            int elements = readVarInt(); // Number of tags in this resource tag

            List<Tag> tagList = new ArrayList<>(elements);

            for (int valueIter = 0; valueIter < elements; valueIter++) {
                String tagName = readString(); // The actual tag name
                int tagElements = readVarInt(); // Number of blocks/items in this tag

                List<Integer> tagValues = new ArrayList<>(tagElements);
                for (int tagValueIter = 0; tagValueIter < tagElements; tagValueIter++) {
                    tagValues.add(readVarInt());
                }

                tagList.add(new Tag(tagName, tagValues));
            }

            tags.put(resourceName, tagList);
        }
    }

    @Override
    public void write() {
        writeVarInt(tags.size());

        for (Map.Entry<String, List<Tag>> entry : tags.entrySet()) {
            writeString(entry.getKey());
            writeVarInt(entry.getValue().size());

            for (Tag tag : entry.getValue()) {
                writeString(tag.getName());
                writeVarInt(tag.getValues().size());

                for (Integer value : tag.getValues()) {
                    writeVarInt(value);
                }
            }
        }
    }

    @Override
    public void copy(WrapperPlayServerTags wrapper) {
        this.tags = wrapper.tags;
    }

    public Map<String, List<Tag>> getTags() {
        return tags;
    }

    public void setTags(HashMap<String, List<Tag>> tags) {
        this.tags = tags;
    }

    public static class Tag {
        private String name;
        private List<Integer> values;

        public Tag(String name, List<Integer> values) {
            this.name = name;
            this.values = values;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Integer> getValues() {
            return values;
        }

        public void setValues(List<Integer> values) {
            this.values = values;
        }
    }
}
