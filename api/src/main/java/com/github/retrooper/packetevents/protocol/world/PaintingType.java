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

package com.github.retrooper.packetevents.protocol.world;

import java.util.HashMap;
import java.util.Map;

// From MCProtocolLib
public enum PaintingType {
    KEBAB("Kebab"),
    AZTEC("Aztec"),
    ALBAN("Alban"),
    AZTEC2("Aztec2"),
    BOMB("Bomb"),
    PLANT("Plant"),
    WASTELAND("Wasteland"),
    POOL("Pool"),
    COURBET("Courbet"),
    SEA("Sea"),
    SUNSET("Sunset"),
    CREEBET("Creebet"),
    WANDERER("Wanderer"),
    GRAHAM("Graham"),
    MATCH("Match"),
    BUST("Bust"),
    STAGE("Stage"),
    VOID("Void"),
    SKULL_AND_ROSES("SkullAndRoses"),
    WITHER("Wither"),
    FIGHTERS("Fighters"),
    POINTER("Pointer"),
    PIG_SCENE("Pigscene"),
    BURNING_SKULL("BurningSkull"),
    SKELETON("Skeleton"),
    DONKEY_KONG("DonkeyKong");

    private final String title;
    private static final Map<String, PaintingType> TITLE_TO_NAME_MAP = new HashMap<>();

    PaintingType(String title) {
        this.title = title;
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

    public int getId() {
        return ordinal();
    }

    private static final PaintingType[] VALUES = values();
}