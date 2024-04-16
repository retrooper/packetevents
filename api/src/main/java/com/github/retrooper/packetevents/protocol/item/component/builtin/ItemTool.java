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

package com.github.retrooper.packetevents.protocol.item.component.builtin;

import com.github.retrooper.packetevents.protocol.mapper.GenericMappedEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public class ItemTool {

    private final List<Rule> rules;
    private final float defaultMiningSpeed;
    private final int damagePerBlock;

    public ItemTool(List<Rule> rules, float defaultMiningSpeed, int damagePerBlock) {
        this.rules = rules;
        this.defaultMiningSpeed = defaultMiningSpeed;
        this.damagePerBlock = damagePerBlock;
    }

    public static ItemTool read(PacketWrapper<?> wrapper) {
        List<Rule> rules = wrapper.readList(Rule::read);
        float defaultMiningSpeed = wrapper.readFloat();
        int damagePerBlock = wrapper.readVarInt();
        return new ItemTool(rules, defaultMiningSpeed, damagePerBlock);
    }

    public static void write(PacketWrapper<?> wrapper, ItemTool tool) {
        wrapper.writeList(tool.rules, Rule::write);
        wrapper.writeFloat(tool.defaultMiningSpeed);
        wrapper.writeVarInt(tool.damagePerBlock);
    }

    public static class Rule {

        private final MappedEntitySet blocks;
        private final @Nullable Float speed;
        private final @Nullable Boolean correctForDrops;

        public Rule(MappedEntitySet blocks, @Nullable Float speed, @Nullable Boolean correctForDrops) {
            this.blocks = blocks;
            this.speed = speed;
            this.correctForDrops = correctForDrops;
        }

        public static Rule read(PacketWrapper<?> wrapper) {
            MappedEntitySet blocks = MappedEntitySet.read(wrapper, GenericMappedEntity::getById);
            Float speed = wrapper.readOptional(PacketWrapper::readFloat);
            Boolean correctForDrops = wrapper.readOptional(PacketWrapper::readBoolean);
            return new Rule(blocks, speed, correctForDrops);
        }

        public static void write(PacketWrapper<?> wrapper, Rule rule) {
            MappedEntitySet.write(wrapper, rule.blocks);
            wrapper.writeOptional(rule.speed, PacketWrapper::writeFloat);
            wrapper.writeOptional(rule.correctForDrops, PacketWrapper::writeBoolean);
        }
    }
}
