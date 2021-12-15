/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.player;

/**
 * The {@code Hand} enum represents what hand was used in an interaction.
 *
 * @author retrooper
 * @see <a href="https://wiki.vg/Protocol#Open_Book">https://wiki.vg/Protocol#Open_Book</a>
 * @since 1.8
 */
public enum InteractionHand {
    MAIN_HAND,
    OFF_HAND;

    public int getLegacyId() {
        return this == MAIN_HAND ? 0 : 1;
    }

    public static final InteractionHand[] VALUES = values();

    public static InteractionHand getByLegacyId(int handValue) {
        return handValue == 0 ? MAIN_HAND : OFF_HAND;
    }
}
