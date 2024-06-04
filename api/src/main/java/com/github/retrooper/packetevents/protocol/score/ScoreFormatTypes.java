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

package com.github.retrooper.packetevents.protocol.score;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class ScoreFormatTypes {

    private static final Map<String, ScoreFormatType> SCORE_FORMAT_TYPE_MAP = new HashMap<>();
    private static final Map<Byte, ScoreFormatType> SCORE_FORMAT_TYPE_ID_MAP = new HashMap<>();

    public static final ScoreFormatType BLANK = define(0, "blank", BlankScoreFormat.class,
            wrapper -> ScoreFormat.blankScore(),
            (wrapper, format) -> { /**/ });
    public static final ScoreFormatType STYLED = define(1, "styled", StyledScoreFormat.class,
            wrapper -> ScoreFormat.styledScore(wrapper.readStyle()),
            (wrapper, format) -> wrapper.writeStyle(format.getStyle()));
    public static final ScoreFormatType FIXED = define(2, "fixed", FixedScoreFormat.class,
            wrapper -> ScoreFormat.fixedScore(wrapper.readComponent()),
            (wrapper, format) -> wrapper.writeComponent(format.getValue()));

    /**
     * Returns an immutable view of the score format types.
     * @return Score Format Types
     */
    public static Collection<ScoreFormatType> values() {
        return Collections.unmodifiableCollection(SCORE_FORMAT_TYPE_MAP.values());
    }

    private ScoreFormatTypes() {
    }

    public static ScoreFormat read(PacketWrapper<?> wrapper) {
        int formatTypeId = wrapper.readVarInt();
        ScoreFormatType formatType = getById(wrapper.getServerVersion().toClientVersion(), formatTypeId);
        if (formatType == null) {
            throw new NullPointerException("Can't resolve format type " + formatTypeId);
        }
        return formatType.read(wrapper);
    }

    public static void write(PacketWrapper<?> wrapper, ScoreFormat format) {
        int formatTypeId = format.getType().getId(wrapper.getServerVersion().toClientVersion());
        wrapper.writeVarInt(formatTypeId);
        format.getType().write(wrapper, format);
    }

    public static <T extends ScoreFormat> ScoreFormatType define(int id, String name,
                                                                 Class<T> formatClass,
                                                                 Function<PacketWrapper<?>, T> reader,
                                                                 BiConsumer<PacketWrapper<?>, T> writer) {
        ResourceLocation location = new ResourceLocation(name);
        ScoreFormatType type = new ScoreFormatType() {
            @Override
            public ScoreFormat read(PacketWrapper<?> wrapper) {
                return reader.apply(wrapper);
            }

            @Override
            public void write(PacketWrapper<?> wrapper, ScoreFormat format) {
                writer.accept(wrapper, formatClass.cast(format));
            }

            @Override
            public ResourceLocation getName() {
                return location;
            }

            @Override
            public int getId() {
                return id;
            }
        };
        SCORE_FORMAT_TYPE_MAP.put(location.toString(), type);
        SCORE_FORMAT_TYPE_ID_MAP.put((byte) id, type);
        return type;
    }

    public static @Nullable ScoreFormatType getById(ClientVersion version, int id) {
        return SCORE_FORMAT_TYPE_ID_MAP.get((byte) id);
    }

    public static @Nullable ScoreFormatType getByName(String name) {
        return getByName(new ResourceLocation(name));
    }

    public static @Nullable ScoreFormatType getByName(ResourceLocation name) {
        return SCORE_FORMAT_TYPE_MAP.get(name.toString());
    }
}
