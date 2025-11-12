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

package com.github.retrooper.packetevents.protocol.potion;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

public class PotionEffect {

    private final PotionType type;
    private final Properties properties;

    public PotionEffect(
            PotionType type, int amplifier, int duration, boolean ambient, boolean showParticles,
            boolean showIcon, @Nullable Properties hiddenEffect
    ) {
        this(type, new Properties(amplifier, duration,
                ambient, showParticles, showIcon, hiddenEffect));
    }

    public PotionEffect(PotionType type, Properties properties) {
        this.type = type;
        this.properties = properties;
    }

    public static PotionEffect read(PacketWrapper<?> wrapper) {
        PotionType type = wrapper.readMappedEntity(PotionTypes::getById);
        Properties props = Properties.read(wrapper);
        return new PotionEffect(type, props);
    }

    public static void write(PacketWrapper<?> wrapper, PotionEffect effect) {
        wrapper.writeMappedEntity(effect.type);
        Properties.write(wrapper, effect.properties);
    }

    public static class Properties {

        private final int amplifier;
        private final int duration;
        private final boolean ambient;
        private final boolean showParticles;
        private final boolean showIcon;
        private final @Nullable Properties hiddenEffect;

        public Properties(
                int amplifier, int duration, boolean ambient, boolean showParticles,
                boolean showIcon, @Nullable Properties hiddenEffect
        ) {
            this.amplifier = amplifier;
            this.duration = duration;
            this.ambient = ambient;
            this.showParticles = showParticles;
            this.showIcon = showIcon;
            this.hiddenEffect = hiddenEffect;
        }

        public static Properties read(PacketWrapper<?> wrapper) {
            int amplifier = wrapper.readVarInt();
            int duration = wrapper.readVarInt();
            boolean ambient = wrapper.readBoolean();
            boolean showParticles = wrapper.readBoolean();
            boolean showIcon = wrapper.readBoolean();
            Properties hiddenEffect = wrapper.readOptional(Properties::read);
            return new Properties(amplifier, duration, ambient, showParticles, showIcon, hiddenEffect);
        }

        public static void write(PacketWrapper<?> wrapper, Properties props) {
            wrapper.writeVarInt(props.amplifier);
            wrapper.writeVarInt(props.duration);
            wrapper.writeBoolean(props.ambient);
            wrapper.writeBoolean(props.showParticles);
            wrapper.writeBoolean(props.showIcon);
            wrapper.writeOptional(props.hiddenEffect, Properties::write);
        }
    }
}
