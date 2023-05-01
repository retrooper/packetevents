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


package com.github.retrooper.packetevents.protocol.chat;

import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Parsers {
    private static final List<Parser> parsers = Arrays.asList(
            new Parser("brigadier:bool", null, null),
            new Parser("brigadier:float",
                    packetWrapper -> {
                        byte flags = packetWrapper.readByte();
                        float min = (flags & 0x01) != 0 ? packetWrapper.readFloat() : -Float.MAX_VALUE;
                        float max = (flags & 0x02) != 0 ? packetWrapper.readFloat() : Float.MAX_VALUE;
                        return Arrays.asList(flags, min, max);
                    },
                    (packetWrapper, properties) -> {
                        byte flags = (byte) properties.get(0);
                        packetWrapper.writeByte(flags);
                        if ((flags & 0x01) != 0) packetWrapper.writeFloat((float) properties.get(1));
                        if ((flags & 0x02) != 0) packetWrapper.writeFloat((float) properties.get(2));
                    }
            ),
            new Parser("brigadier:double",
                    packetWrapper -> {
                        byte flags = packetWrapper.readByte();
                        double min = (flags & 0x01) != 0 ? packetWrapper.readDouble() : -Double.MAX_VALUE;
                        double max = (flags & 0x02) != 0 ? packetWrapper.readDouble() : Double.MAX_VALUE;
                        return Arrays.asList(flags, min, max);
                    },
                    (packetWrapper, properties) -> {
                        byte flags = (byte) properties.get(0);
                        packetWrapper.writeByte(flags);
                        if ((flags & 0x01) != 0) packetWrapper.writeDouble((double) properties.get(1));
                        if ((flags & 0x02) != 0) packetWrapper.writeDouble((double) properties.get(2));
                    }
            ),
            new Parser("brigadier:integer",
                    packetWrapper -> {
                        byte flags = packetWrapper.readByte();
                        int min = (flags & 0x01) != 0 ? packetWrapper.readInt() : Integer.MIN_VALUE;
                        int max = (flags & 0x02) != 0 ? packetWrapper.readInt() : Integer.MAX_VALUE;
                        return Arrays.asList(flags, min, max);
                    },
                    (packetWrapper, properties) -> {
                        byte flags = (byte) properties.get(0);
                        packetWrapper.writeByte(flags);
                        if ((flags & 0x01) != 0) packetWrapper.writeInt((int) properties.get(1));
                        if ((flags & 0x02) != 0) packetWrapper.writeInt((int) properties.get(2));
                    }
            ),
            new Parser("brigadier:long",
                    packetWrapper -> {
                        byte flags = packetWrapper.readByte();
                        long min = (flags & 0x01) != 0 ? packetWrapper.readLong() : Long.MIN_VALUE;
                        long max = (flags & 0x02) != 0 ? packetWrapper.readLong() : Long.MAX_VALUE;
                        return Arrays.asList(flags, min, max);
                    },
                    (packetWrapper, properties) -> {
                        byte flags = (byte) properties.get(0);
                        packetWrapper.writeByte(flags);
                        if ((flags & 0x01) != 0) packetWrapper.writeLong((long) properties.get(1));
                        if ((flags & 0x02) != 0) packetWrapper.writeLong((long) properties.get(2));
                    }
            ),
            new Parser("brigadier:string",
                    packetWrapper -> Collections.singletonList(packetWrapper.readVarInt()),
                    (packetWrapper, properties) -> packetWrapper.writeVarInt((Integer) properties.get(0))
            ),
            new Parser("brigadier:entity",
                    packetWrapper -> Collections.singletonList(packetWrapper.readByte()),
                    (packetWrapper, properties) -> packetWrapper.writeByte(((Byte) properties.get(0)).intValue())
            ),
            new Parser("brigadier:game_profile", null, null),
            new Parser("brigadier:block_pos", null, null),
            new Parser("brigadier:column_pos", null, null),
            new Parser("brigadier:vec3", null, null),
            new Parser("brigadier:vec2", null, null),
            new Parser("brigadier:block_state", null, null),
            new Parser("brigadier:block_predicate", null, null),
            new Parser("brigadier:item_stack", null, null),
            new Parser("brigadier:item_predicate", null, null),
            new Parser("brigadier:color", null, null),
            new Parser("brigadier:component", null, null),
            new Parser("brigadier:message", null, null),
            new Parser("brigadier:nbt", null, null),
            new Parser("brigadier:nbt_tag", null, null),
            new Parser("brigadier:nbt_path", null, null),
            new Parser("brigadier:objective", null, null),
            new Parser("brigadier:objective_criteria", null, null),
            new Parser("brigadier:operation", null, null),
            new Parser("brigadier:particle", null, null),
            new Parser("brigadier:angle", null, null),
            new Parser("brigadier:rotation", null, null),
            new Parser("brigadier:scoreboard_slot", null, null),
            new Parser("brigadier:score_holder",
                    packetWrapper -> Collections.singletonList(packetWrapper.readByte()),
                    (packetWrapper, properties) -> packetWrapper.writeByte(((Byte) properties.get(0)).intValue())
            ),
            new Parser("brigadier:swizzle", null, null),
            new Parser("brigadier:team", null, null),
            new Parser("brigadier:item_slot", null, null),
            new Parser("brigadier:resource_location", null, null),
            new Parser("brigadier:function", null, null),
            new Parser("brigadier:entity_anchor", null, null),
            new Parser("brigadier:int_range", null, null),
            new Parser("brigadier:float_range", null, null),
            new Parser("brigadier:dimension", null, null),
            new Parser("brigadier:gamemode", null, null),
            new Parser("brigadier:time",
                    packetWrapper -> Collections.singletonList(packetWrapper.readInt()),
                    (packetWrapper, properties) -> packetWrapper.writeInt((int) properties.get(0))
            ),
            new Parser("brigadier:resource_or_tag",
                    packetWrapper -> Collections.singletonList(packetWrapper.readIdentifier()),
                    (packetWrapper, properties) -> packetWrapper.writeIdentifier((ResourceLocation) properties.get(0))
            ),
            new Parser("brigadier:resource_or_tag_key",
                    packetWrapper -> Collections.singletonList(packetWrapper.readIdentifier()),
                    (packetWrapper, properties) -> packetWrapper.writeIdentifier((ResourceLocation) properties.get(0))
            ),
            new Parser("brigadier:resource",
                    packetWrapper -> Collections.singletonList(packetWrapper.readIdentifier()),
                    (packetWrapper, properties) -> packetWrapper.writeIdentifier((ResourceLocation) properties.get(0))
            ),
            new Parser("brigadier:resource_key",
                    packetWrapper -> Collections.singletonList(packetWrapper.readIdentifier()),
                    (packetWrapper, properties) -> packetWrapper.writeIdentifier((ResourceLocation) properties.get(0))
            ),
            new Parser("brigadier:template_mirror", null, null),
            new Parser("brigadier:template_rotation", null, null),
            new Parser("brigadier:heightmap", null, null),
            new Parser("brigadier:uuid", null, null)
    );

    public static List<Parser> getParsers() {
        return parsers;
    }

    public static final class Parser {
        private final String name; //not really needed, just for toString or something
        private final Optional<Function<PacketWrapper<?>, List<Object>>> read;
        private final Optional<BiConsumer<PacketWrapper<?>, List<Object>>> write;

        public Parser(String name, @Nullable Function<PacketWrapper<?>, List<Object>> read, @Nullable BiConsumer<PacketWrapper<?>, List<Object>> write) {
            this.name = name;
            this.read = Optional.ofNullable(read);
            this.write = Optional.ofNullable(write);
        }

        public Optional<List<Object>> readProperties(PacketWrapper<?> packetWrapper) {
            return read.map(fn -> fn.apply(packetWrapper));
        }

        public void writeProperties(PacketWrapper<?> packetWrapper, List<Object> properties) {
            write.ifPresent(fn -> fn.accept(packetWrapper, properties));
        }
    }
}
