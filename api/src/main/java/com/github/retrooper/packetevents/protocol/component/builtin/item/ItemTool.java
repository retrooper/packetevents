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

import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ItemTool {

    private List<Rule> rules;
    private float defaultMiningSpeed;
    private int damagePerBlock;

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

    public void addRule(Rule rule) {
        this.rules.add(rule);
    }

    public List<Rule> getRules() {
        return this.rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public float getDefaultMiningSpeed() {
        return this.defaultMiningSpeed;
    }

    public void setDefaultMiningSpeed(float defaultMiningSpeed) {
        this.defaultMiningSpeed = defaultMiningSpeed;
    }

    public int getDamagePerBlock() {
        return this.damagePerBlock;
    }

    public void setDamagePerBlock(int damagePerBlock) {
        this.damagePerBlock = damagePerBlock;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemTool)) return false;
        ItemTool itemTool = (ItemTool) obj;
        if (Float.compare(itemTool.defaultMiningSpeed, this.defaultMiningSpeed) != 0) return false;
        if (this.damagePerBlock != itemTool.damagePerBlock) return false;
        return this.rules.equals(itemTool.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.rules, this.defaultMiningSpeed, this.damagePerBlock);
    }

    public static class Rule {

        private MappedEntitySet<StateType.Mapped> blocks;
        private @Nullable Float speed;
        private @Nullable Boolean correctForDrops;

        public Rule(
                MappedEntitySet<StateType.Mapped> blocks,
                @Nullable Float speed,
                @Nullable Boolean correctForDrops
        ) {
            this.blocks = blocks;
            this.speed = speed;
            this.correctForDrops = correctForDrops;
        }

        public static Rule read(PacketWrapper<?> wrapper) {
            MappedEntitySet<StateType.Mapped> blocks = MappedEntitySet.read(wrapper, StateTypes::getMappedById);
            Float speed = wrapper.readOptional(PacketWrapper::readFloat);
            Boolean correctForDrops = wrapper.readOptional(PacketWrapper::readBoolean);
            return new Rule(blocks, speed, correctForDrops);
        }

        public static void write(PacketWrapper<?> wrapper, Rule rule) {
            MappedEntitySet.write(wrapper, rule.blocks);
            wrapper.writeOptional(rule.speed, PacketWrapper::writeFloat);
            wrapper.writeOptional(rule.correctForDrops, PacketWrapper::writeBoolean);
        }

        public MappedEntitySet<StateType.Mapped> getBlocks() {
            return this.blocks;
        }

        public void setBlocks(MappedEntitySet<StateType.Mapped> blocks) {
            this.blocks = blocks;
        }

        public @Nullable Float getSpeed() {
            return this.speed;
        }

        public void setSpeed(@Nullable Float speed) {
            this.speed = speed;
        }

        public @Nullable Boolean getCorrectForDrops() {
            return this.correctForDrops;
        }

        public void setCorrectForDrops(@Nullable Boolean correctForDrops) {
            this.correctForDrops = correctForDrops;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Rule)) return false;
            Rule rule = (Rule) obj;
            if (!this.blocks.equals(rule.blocks)) return false;
            if (!Objects.equals(this.speed, rule.speed)) return false;
            return Objects.equals(this.correctForDrops, rule.correctForDrops);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.blocks, this.speed, this.correctForDrops);
        }
    }
}
