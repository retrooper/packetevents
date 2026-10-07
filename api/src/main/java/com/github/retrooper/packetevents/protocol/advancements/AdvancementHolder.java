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

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class AdvancementHolder {

    private ResourceLocation identifier;
    private Advancement advancement;

    /**
     * @versions 26.3+
     */
    private float x;
    /**
     * @versions 26.3+
     */
    private float y;

    @ApiStatus.Obsolete
    public AdvancementHolder(ResourceLocation identifier, Advancement advancement) {
        this.identifier = identifier;
        this.advancement = advancement;
        // legacy compat
        AdvancementDisplay display = advancement.getDisplay();
        if (display != null) {
            this.x = display.getX();
            this.y = display.getY();
        }
    }

    public AdvancementHolder(ResourceLocation identifier, Advancement advancement, float x, float y) {
        this.identifier = identifier;
        this.advancement = advancement;
        this.x = x;
        this.y = y;
    }

    public static AdvancementHolder read(PacketWrapper<?> wrapper) {
        ResourceLocation identifier = wrapper.readIdentifier();
        Advancement advancement = Advancement.read(wrapper);
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_26_3)) {
            return new AdvancementHolder(identifier, advancement);
        }
        float x = wrapper.readFloat();
        float y = wrapper.readFloat();
        // minimal legacy compat
        AdvancementDisplay display = advancement.getDisplay();
        if (display != null) {
            display.setX(x);
            display.setY(y);
        }
        return new AdvancementHolder(identifier, advancement, x, y);
    }

    public static void write(PacketWrapper<?> wrapper, AdvancementHolder holder) {
        wrapper.writeIdentifier(holder.identifier);
        Advancement.write(wrapper, holder.advancement);
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_3)) {
            wrapper.writeFloat(holder.x);
            wrapper.writeFloat(holder.y);
        }
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

    /**
     * @versions 26.3+
     */
    public float getX() {
        return this.x;
    }

    /**
     * @versions 26.3+
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @versions 26.3+
     */
    public float getY() {
        return this.y;
    }

    /**
     * @versions 26.3+
     */
    public void setY(float y) {
        this.y = y;
    }
}
