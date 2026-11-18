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

package com.github.retrooper.packetevents.protocol.item.consumables.builtin;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.consumables.ConsumeEffect;
import com.github.retrooper.packetevents.protocol.item.consumables.ConsumeEffectTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class TeleportRandomlyConsumeEffect extends ConsumeEffect<TeleportRandomlyConsumeEffect> {

    private final float diameter;
    /**
     * @versions 26.3+
     */
    private final boolean directionalParticles;

    public TeleportRandomlyConsumeEffect(float diameter) {
        this(diameter, true);
    }

    /**
     * @versions 26.3+
     */
    public TeleportRandomlyConsumeEffect(float diameter, boolean directionalParticles) {
        super(ConsumeEffectTypes.TELEPORT_RANDOMLY);
        this.diameter = diameter;
        this.directionalParticles = directionalParticles;
    }

    public static TeleportRandomlyConsumeEffect read(PacketWrapper<?> wrapper) {
        float diameter = wrapper.readFloat();
        boolean directionalParticles = wrapper.getServerVersion().isOlderThan(ServerVersion.V_26_3) || wrapper.readBoolean();
        return new TeleportRandomlyConsumeEffect(diameter, directionalParticles);
    }

    public static void write(PacketWrapper<?> wrapper, TeleportRandomlyConsumeEffect effect) {
        wrapper.writeFloat(effect.diameter);
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_3)) {
            wrapper.writeBoolean(effect.directionalParticles);
        }
    }

    public float getDiameter() {
        return this.diameter;
    }

    /**
     * @versions 26.3+
     */
    public boolean isDirectionalParticles() {
        return this.directionalParticles;
    }
}
