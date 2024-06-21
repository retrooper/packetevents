/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.world;

import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

// From MCProtocolLib
@ApiStatus.Obsolete
public enum PaintingType {
    KEBAB("Kebab", 0, 1, 1),
    AZTEC("Aztec", 1, 1, 1),
    ALBAN("Alban", 2, 1, 1),
    AZTEC2("Aztec2", 3, 1, 1),
    BOMB("Bomb", 4, 1, 1),
    PLANT("Plant", 5, 1, 1),
    WASTELAND("Wasteland", 6, 1, 1),
    POOL("Pool", 7, 2, 1),
    COURBET("Courbet", 8, 2, 1),
    SEA("Sea", 9, 2, 1),
    SUNSET("Sunset", 10, 2, 1),
    CREEBET("Creebet", 11, 2, 1),
    WANDERER("Wanderer", 12, 1, 2),
    GRAHAM("Graham", 13, 1, 2),
    MATCH("Match", 14, 2, 2),
    BUST("Bust", 15, 2, 2),
    STAGE("Stage", 16, 2, 2),
    VOID("Void", 17, 2, 2),
    SKULL_AND_ROSES("SkullAndRoses", 18, 2, 2),
    WITHER("Wither", 19, 2, 2),
    FIGHTERS("Fighters", 20, 4, 2),
    POINTER("Pointer", 21, 4, 4),
    PIG_SCENE("Pigscene", 22, 4, 4),
    BURNING_SKULL("BurningSkull", 23, 4, 4),
    SKELETON("Skeleton", 24, 4, 3),
    DONKEY_KONG("DonkeyKong", 25, 4, 3),
    EARTH("Earth", 26, 2, 2),
    WIND("Wind", 27, 2, 2),
    WATER("Water", 28, 2, 2),
    FIRE("Fire", 29, 2, 2);

    private final String title;
    private final int id;
    private final int width;
    private final int height;

    private static final Map<String, PaintingType> TITLE_TO_NAME_MAP = new HashMap<>();

    PaintingType(String title, int id, int width, int height) {
        this.title = title;
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public static PaintingType getById(int id) {
        return VALUES[id];
    }

    @Deprecated
    public static PaintingType getByTitle(String title) {
        PaintingType type = TITLE_TO_NAME_MAP.get(title);
        if (type == null) {
            for (PaintingType t : VALUES) {
                if (t.title.equals(title)) {
                    TITLE_TO_NAME_MAP.put(title, t);
                    return t;
                }
            }
        }
        return type;
    }

    @Deprecated
    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getId() {
        return id;
    }

    private static final PaintingType[] VALUES = values();
}
