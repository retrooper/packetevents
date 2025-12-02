/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.advancements;

import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public final class AdvancementHolder {

    private ResourceLocation identifier;
    private Advancement advancement;

    public AdvancementHolder(ResourceLocation identifier, Advancement advancement) {
        this.identifier = identifier;
        this.advancement = advancement;
    }

    public static AdvancementHolder read(PacketWrapper<?> wrapper) {
        ResourceLocation identifier = wrapper.readIdentifier();
        Advancement advancement = Advancement.read(wrapper);
        return new AdvancementHolder(identifier, advancement);
    }

    public static void write(PacketWrapper<?> wrapper, AdvancementHolder holder) {
        wrapper.writeIdentifier(holder.identifier);
        Advancement.write(wrapper, holder.advancement);
    }

    public ResourceLocation getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(ResourceLocation identifier) {
        this.identifier = identifier;
    }

    public Advancement getAdvancement() {
        return this.advancement;
    }

    public void setAdvancement(Advancement advancement) {
        this.advancement = advancement;
    }
}
