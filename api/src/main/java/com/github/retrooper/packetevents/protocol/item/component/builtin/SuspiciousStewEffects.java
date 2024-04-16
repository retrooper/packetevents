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

import com.github.retrooper.packetevents.protocol.potion.PotionType;
import com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.List;

public class SuspiciousStewEffects {

    private final List<EffectEntry> effects;

    public SuspiciousStewEffects(List<EffectEntry> effects) {
        this.effects = effects;
    }

    public static SuspiciousStewEffects read(PacketWrapper<?> wrapper) {
        List<EffectEntry> effects = wrapper.readList(EffectEntry::read);
        return new SuspiciousStewEffects(effects);
    }

    public static void write(PacketWrapper<?> wrapper, SuspiciousStewEffects effects) {
        wrapper.writeList(effects.effects, EffectEntry::write);
    }

    public static class EffectEntry {

        private final PotionType type;
        private final int duration;

        public EffectEntry(PotionType type, int duration) {
            this.type = type;
            this.duration = duration;
        }

        public static EffectEntry read(PacketWrapper<?> wrapper) {
            PotionType type = wrapper.readMappedEntity(PotionTypes::getById);
            int duration = wrapper.readVarInt();
            return new EffectEntry(type, duration);
        }

        public static void write(PacketWrapper<?> wrapper, EffectEntry effect) {
            wrapper.writeMappedEntity(effect.type);
            wrapper.writeVarInt(effect.duration);
        }
    }
}