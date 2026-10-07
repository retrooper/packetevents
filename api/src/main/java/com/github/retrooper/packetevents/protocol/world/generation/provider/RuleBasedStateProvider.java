/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.world.generation.provider;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.protocol.world.generation.BlockPredicate;
import com.github.retrooper.packetevents.protocol.world.generation.BlockStateProvider;
import com.github.retrooper.packetevents.protocol.world.generation.BlockStateProviderType;
import com.github.retrooper.packetevents.protocol.world.generation.BlockStateProviderTypes;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @versions 26.3+
 */
@NullMarked
public final class RuleBasedStateProvider extends AbstractMappedEntity implements BlockStateProvider {

    public static final NbtMapCodec<RuleBasedStateProvider> MAP_CODEC = new NbtMapCodec<RuleBasedStateProvider>() {
        @Override
        public RuleBasedStateProvider decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            BlockStateProvider fallback = tag.getOrNull("fallback", BlockStateProvider.CODEC, wrapper);
            List<Rule> rules = tag.getListOrEmpty("rules", Rule.CODEC, wrapper);
            return new RuleBasedStateProvider(null, fallback, rules);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, RuleBasedStateProvider value) throws NbtCodecException {
            if (value.fallback != null) {
                tag.set("fallback", value.fallback, BlockStateProvider.CODEC, wrapper);
            }
            tag.setList("rules", value.rules, Rule.CODEC, wrapper);
        }
    };

    private final @Nullable BlockStateProvider fallback;
    private final List<Rule> rules;

    public RuleBasedStateProvider(@Nullable BlockStateProvider fallback, List<Rule> rules) {
        this(null, fallback, rules);
    }

    @ApiStatus.Internal
    public RuleBasedStateProvider(
            @Nullable TypesBuilderData data,
            @Nullable BlockStateProvider fallback,
            List<Rule> rules
    ) {
        super(data);
        this.fallback = fallback;
        this.rules = Collections.unmodifiableList(rules);
    }

    public @Nullable BlockStateProvider getFallback() {
        return this.fallback;
    }

    public List<Rule> getRules() {
        return this.rules;
    }

    @Override
    public BlockStateProviderType<?> getType() {
        return BlockStateProviderTypes.RULE_BASED;
    }

    @Override
    public RuleBasedStateProvider copy(@Nullable TypesBuilderData newData) {
        return new RuleBasedStateProvider(newData, this.fallback, this.rules);
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (!(obj instanceof RuleBasedStateProvider)) return false;
        RuleBasedStateProvider that = (RuleBasedStateProvider) obj;
        if (!Objects.equals(this.fallback, that.fallback)) return false;
        return this.rules.equals(that.rules);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(this.fallback, this.rules);
    }

    public static final class Rule {

        public static final NbtCodec<Rule> CODEC = new NbtMapCodec<Rule>() {
            @Override
            public Rule decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
                BlockPredicate ifTrue = tag.getOrThrow("if_true", BlockPredicate.CODEC, wrapper);
                BlockStateProvider then = tag.getOrThrow("then", BlockStateProvider.CODEC, wrapper);
                return new Rule(ifTrue, then);
            }

            @Override
            public void encode(NBTCompound tag, PacketWrapper<?> wrapper, Rule value) throws NbtCodecException {
                tag.set("if_true", value.ifTrue, BlockPredicate.CODEC, wrapper);
                tag.set("then", value.then, BlockStateProvider.CODEC, wrapper);
            }
        }.codec();

        private final BlockPredicate ifTrue;
        private final BlockStateProvider then;

        public Rule(BlockPredicate ifTrue, BlockStateProvider then) {
            this.ifTrue = ifTrue;
            this.then = then;
        }

        public BlockPredicate getIfTrue() {
            return this.ifTrue;
        }

        public BlockStateProvider getThen() {
            return this.then;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (this == obj) return true;
            if (obj == null || this.getClass() != obj.getClass()) return false;
            Rule that = (Rule) obj;
            if (!this.ifTrue.equals(that.ifTrue)) return false;
            return this.then.equals(that.then);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.ifTrue, this.then);
        }
    }
}
