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
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// why do I have to implement this mess, I just want to deserialize items
public class ItemAdventurePredicate {

    private List<BlockPredicate> predicates;
    private boolean showInTooltip;

    public ItemAdventurePredicate(List<BlockPredicate> predicates, boolean showInTooltip) {
        this.predicates = predicates;
        this.showInTooltip = showInTooltip;
    }

    public static ItemAdventurePredicate read(PacketWrapper<?> wrapper) {
        List<BlockPredicate> predicates = wrapper.readList(BlockPredicate::read);
        boolean showInTooltip = wrapper.readBoolean();
        return new ItemAdventurePredicate(predicates, showInTooltip);
    }

    public static void write(PacketWrapper<?> wrapper, ItemAdventurePredicate predicate) {
        wrapper.writeList(predicate.predicates, BlockPredicate::write);
        wrapper.writeBoolean(predicate.showInTooltip);
    }

    public void addPredicate(BlockPredicate predicate) {
        this.predicates.add(predicate);
    }

    public List<BlockPredicate> getPredicates() {
        return this.predicates;
    }

    public void setPredicates(List<BlockPredicate> predicates) {
        this.predicates = predicates;
    }

    public boolean isShowInTooltip() {
        return this.showInTooltip;
    }

    public void setShowInTooltip(boolean showInTooltip) {
        this.showInTooltip = showInTooltip;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemAdventurePredicate)) return false;
        ItemAdventurePredicate that = (ItemAdventurePredicate) obj;
        if (this.showInTooltip != that.showInTooltip) return false;
        return this.predicates.equals(that.predicates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.predicates, this.showInTooltip);
    }

    public static class BlockPredicate {

        private @Nullable MappedEntitySet<StateType.Mapped> blocks;
        private @Nullable List<PropertyMatcher> properties;
        private @Nullable NBTCompound nbt;

        public BlockPredicate(
                @Nullable MappedEntitySet<StateType.Mapped> blocks,
                @Nullable List<PropertyMatcher> properties,
                @Nullable NBTCompound nbt
        ) {
            this.blocks = blocks;
            this.properties = properties;
            this.nbt = nbt;
        }

        public static BlockPredicate read(PacketWrapper<?> wrapper) {
            MappedEntitySet<StateType.Mapped> blocks = wrapper.readOptional(
                    ew -> MappedEntitySet.read(ew, StateTypes::getMappedById));
            List<PropertyMatcher> properties = wrapper.readOptional(
                    ew -> wrapper.readList(PropertyMatcher::read));
            NBTCompound nbt = wrapper.readOptional(PacketWrapper::readNBT);
            return new BlockPredicate(blocks, properties, nbt);
        }

        public static void write(PacketWrapper<?> wrapper, BlockPredicate predicate) {
            wrapper.writeOptional(predicate.blocks, MappedEntitySet::write);
            wrapper.writeOptional(predicate.properties,
                    (ew, val) -> ew.writeList(val, PropertyMatcher::write));
            wrapper.writeOptional(predicate.nbt, PacketWrapper::writeNBT);
        }

        public @Nullable MappedEntitySet<StateType.Mapped> getBlocks() {
            return this.blocks;
        }

        public void setBlocks(@Nullable MappedEntitySet<StateType.Mapped> blocks) {
            this.blocks = blocks;
        }

        public void addProperty(PropertyMatcher propertyMatcher) {
            if (this.properties == null) {
                this.properties = new ArrayList<>(4);
            }
            this.properties.add(propertyMatcher);
        }

        public @Nullable List<PropertyMatcher> getProperties() {
            return this.properties;
        }

        public void setProperties(@Nullable List<PropertyMatcher> properties) {
            this.properties = properties;
        }

        public @Nullable NBTCompound getNbt() {
            return this.nbt;
        }

        public void setNbt(@Nullable NBTCompound nbt) {
            this.nbt = nbt;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof BlockPredicate)) return false;
            BlockPredicate that = (BlockPredicate) obj;
            if (!Objects.equals(this.blocks, that.blocks)) return false;
            if (!Objects.equals(this.properties, that.properties)) return false;
            return Objects.equals(this.nbt, that.nbt);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.blocks, this.properties, this.nbt);
        }
    }

    public static class PropertyMatcher {

        private String name;
        private ValueMatcher matcher;

        public PropertyMatcher(String name, ValueMatcher matcher) {
            this.name = name;
            this.matcher = matcher;
        }

        public static PropertyMatcher read(PacketWrapper<?> wrapper) {
            String name = wrapper.readString();
            ValueMatcher matcher = ValueMatcher.read(wrapper);
            return new PropertyMatcher(name, matcher);
        }

        public static void write(PacketWrapper<?> wrapper, PropertyMatcher matcher) {
            wrapper.writeString(matcher.name);
            ValueMatcher.write(wrapper, matcher.matcher);
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ValueMatcher getMatcher() {
            return this.matcher;
        }

        public void setMatcher(ValueMatcher matcher) {
            this.matcher = matcher;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof PropertyMatcher)) return false;
            PropertyMatcher that = (PropertyMatcher) obj;
            if (!this.name.equals(that.name)) return false;
            return this.matcher.equals(that.matcher);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.name, this.matcher);
        }
    }

    public interface ValueMatcher {

        static ValueMatcher read(PacketWrapper<?> wrapper) {
            if (wrapper.readBoolean()) {
                return ExactValueMatcher.read(wrapper);
            }
            return RangedValueMatcher.read(wrapper);
        }

        static void write(PacketWrapper<?> wrapper, ValueMatcher matcher) {
            if (matcher instanceof ExactValueMatcher) {
                wrapper.writeBoolean(true);
                ExactValueMatcher.write(wrapper, (ExactValueMatcher) matcher);
            } else if (matcher instanceof RangedValueMatcher) {
                wrapper.writeBoolean(false);
                RangedValueMatcher.write(wrapper, (RangedValueMatcher) matcher);
            } else {
                throw new IllegalArgumentException("Illegal matcher implementation: " + matcher);
            }
        }
    }

    public static class ExactValueMatcher implements ValueMatcher {

        private String value;

        public ExactValueMatcher(String value) {
            this.value = value;
        }

        public static ExactValueMatcher read(PacketWrapper<?> wrapper) {
            return new ExactValueMatcher(wrapper.readString());
        }

        public static void write(PacketWrapper<?> wrapper, ExactValueMatcher matcher) {
            wrapper.writeString(matcher.value);
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof ExactValueMatcher)) return false;
            ExactValueMatcher that = (ExactValueMatcher) obj;
            return this.value.equals(that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.value);
        }
    }

    public static class RangedValueMatcher implements ValueMatcher {

        private @Nullable String minValue;
        private @Nullable String maxValue;

        public RangedValueMatcher(@Nullable String minValue, @Nullable String maxValue) {
            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        public static RangedValueMatcher read(PacketWrapper<?> wrapper) {
            String minValue = wrapper.readOptional(PacketWrapper::readString);
            String maxValue = wrapper.readOptional(PacketWrapper::readString);
            return new RangedValueMatcher(minValue, maxValue);
        }

        public static void write(PacketWrapper<?> wrapper, RangedValueMatcher matcher) {
            wrapper.writeOptional(matcher.minValue, PacketWrapper::writeString);
            wrapper.writeOptional(matcher.maxValue, PacketWrapper::writeString);
        }

        public @Nullable String getMinValue() {
            return this.minValue;
        }

        public void setMinValue(@Nullable String minValue) {
            this.minValue = minValue;
        }

        public @Nullable String getMaxValue() {
            return this.maxValue;
        }

        public void setMaxValue(@Nullable String maxValue) {
            this.maxValue = maxValue;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof RangedValueMatcher)) return false;
            RangedValueMatcher that = (RangedValueMatcher) obj;
            if (!Objects.equals(this.minValue, that.minValue)) return false;
            return Objects.equals(this.maxValue, that.maxValue);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.minValue, this.maxValue);
        }
    }
}
