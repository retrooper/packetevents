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

import java.util.HashSet;
import java.util.Set;

public enum SkinSection {
    CAPE(0x01),

    JACKET(0x02),

    LEFT_SLEEVE(0x04),

    RIGHT_SLEEVE(0x08),

    LEFT_PANTS(0x10),

    RIGHT_PANTS(0x20),

    HAT(0x40);

    public static final SkinSection[] VALUES = values();

    final byte maskFlag;

    SkinSection(int maskFlag) {
        this.maskFlag = (byte) maskFlag;
    }

    public byte getMaskFlag() {
        return maskFlag;
    }

    public static boolean isSectionPresent(byte mask, SkinSection section) {
        return (mask & section.maskFlag) != 0;
    }

    public static Set<SkinSection> getSectionsByMask(byte mask) {
        Set<SkinSection> visibleSkinSections = new HashSet<>();
        for (SkinSection skinSection : SkinSection.VALUES) {
            if (SkinSection.isSectionPresent(mask, skinSection)) {
                visibleSkinSections.add(skinSection);
            }
        }
        return visibleSkinSections;
    }

    public static byte getMaskBySections(Set<SkinSection> sections) {
        byte mask = 0;
        for (SkinSection section : sections) {
            mask |= section.maskFlag;
        }
        return mask;
    }
}
