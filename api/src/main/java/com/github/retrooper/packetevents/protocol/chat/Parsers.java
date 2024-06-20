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

package com.github.retrooper.packetevents.protocol.chat;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.MappingHelper;
import com.github.retrooper.packetevents.util.mappings.TypesBuilder;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Parsers {

    private static final List<Parser> ALL_PARSERS = new ArrayList<>(); // support for old methods
    private static final Map<String, Parser> PARSER_MAP = new HashMap<>();
    private static final Map<Byte, Map<Integer, Parser>> PARSER_ID_MAP = new HashMap<>();
    private static final TypesBuilder TYPES_BUILDER = new TypesBuilder("command/argument_parser_mappings");

    public static Parser define(String key) {
        return define(key, null, null);
    }

    public static Parser define(String key, @Nullable Reader reader, @Nullable Writer writer) {
        TypesBuilderData data = TYPES_BUILDER.define(key);
        Parser parser = new Parser(data, reader, writer);

        ALL_PARSERS.add(parser);
        MappingHelper.registerMapping(TYPES_BUILDER, PARSER_MAP, PARSER_ID_MAP, parser);
        return parser;
    }

    // with key
    public static Parser getByName(String name) {
        return PARSER_MAP.get(name);
    }

    public static Parser getById(ClientVersion version, int id) {
        int index = TYPES_BUILDER.getDataIndex(version);
        Map<Integer, Parser> idMap = PARSER_ID_MAP.get((byte) index);
        return idMap.get(id);
    }

    public static List<Parser> getParsers() {
        return Collections.unmodifiableList(ALL_PARSERS);
    }

    public static final Parser BRIGADIER_BOOL = define("brigadier:bool", null, null);
    public static final Parser BRIGADIER_FLOAT = define("brigadier:float",
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
    );
    public static final Parser BRIGADIER_DOUBLE = define("brigadier:double",
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
    );
    public static final Parser BRIGADIER_INTEGER = define("brigadier:integer",
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
    );
    public static final Parser BRIGADIER_LONG = define("brigadier:long",
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
    );
    public static final Parser BRIGADIER_STRING = define("brigadier:string",
            packetWrapper -> Collections.singletonList(packetWrapper.readVarInt()),
            (packetWrapper, properties) -> packetWrapper.writeVarInt((Integer) properties.get(0))
    );
    public static final Parser ENTITY = define("entity",
            packetWrapper -> Collections.singletonList(packetWrapper.readByte()),
            (packetWrapper, properties) -> packetWrapper.writeByte(((Byte) properties.get(0)).intValue())
    );
    public static final Parser GAME_PROFILE = define("game_profile", null, null);
    public static final Parser BLOCK_POS = define("block_pos", null, null);
    public static final Parser COLUMN_POS = define("column_pos", null, null);
    public static final Parser VEC3 = define("vec3", null, null);
    public static final Parser VEC2 = define("vec2", null, null);
    public static final Parser BLOCK_STATE = define("block_state", null, null);
    public static final Parser BLOCK_PREDICATE = define("block_predicate", null, null);
    public static final Parser ITEM_STACK = define("item_stack", null, null);
    public static final Parser ITEM_PREDICATE = define("item_predicate", null, null);
    public static final Parser COLOR = define("color", null, null);
    public static final Parser COMPONENT = define("component", null, null);
    public static final Parser STYLE = define("style", null, null);
    public static final Parser MESSAGE = define("message", null, null);
    public static final Parser NBT_COMPOUND_TAG = define("nbt_compound_tag", null, null);
    @Deprecated
    public static final Parser NBT = NBT_COMPOUND_TAG;
    public static final Parser NBT_TAG = define("nbt_tag", null, null);
    public static final Parser NBT_PATH = define("nbt_path", null, null);
    public static final Parser OBJECTIVE = define("objective", null, null);
    public static final Parser OBJECTIVE_CRITERIA = define("objective_criteria", null, null);
    public static final Parser OPERATION = define("operation", null, null);
    public static final Parser PARTICLE = define("particle", null, null);
    public static final Parser ANGLE = define("angle", null, null);
    public static final Parser ROTATION = define("rotation", null, null);
    public static final Parser SCOREBOARD_SLOT = define("scoreboard_slot", null, null);
    public static final Parser SCORE_HOLDER = define("score_holder",
            packetWrapper -> Collections.singletonList(packetWrapper.readByte()),
            (packetWrapper, properties) -> packetWrapper.writeByte(((Byte) properties.get(0)).intValue())
    );
    public static final Parser SWIZZLE = define("swizzle", null, null);
    public static final Parser TEAM = define("team", null, null);
    public static final Parser ITEM_SLOT = define("item_slot", null, null);
    public static final Parser ITEM_SLOTS = define("item_slots", null, null);
    public static final Parser RESOURCE_LOCATION = define("resource_location", null, null);
    public static final Parser MOB_EFFECT = define("mob_effect", null, null);
    public static final Parser FUNCTION = define("function", null, null);
    public static final Parser ENTITY_ANCHOR = define("entity_anchor", null, null);
    public static final Parser INT_RANGE = define("int_range", null, null);
    public static final Parser FLOAT_RANGE = define("float_range", null, null);
    public static final Parser ITEM_ENCHANTMENT = define("item_enchantment", null, null);
    public static final Parser ENTITY_SUMMON = define("entity_summon", null, null);
    public static final Parser DIMENSION = define("dimension", null, null);
    public static final Parser GAMEMODE = define("gamemode", null, null);
    public static final Parser TIME = define("time",
            wrapper -> Collections.singletonList(wrapper.getServerVersion()
                    .isNewerThanOrEquals(ServerVersion.V_1_19_4) ? wrapper.readInt() : 0),
            (wrapper, properties) -> {
                if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_4)) {
                    wrapper.writeInt((int) properties.get(0));
                }
            }
    );
    public static final Parser RESOURCE_OR_TAG = define("resource_or_tag",
            packetWrapper -> Collections.singletonList(packetWrapper.readIdentifier()),
            (packetWrapper, properties) -> packetWrapper.writeIdentifier((ResourceLocation) properties.get(0))
    );
    public static final Parser RESOURCE_OR_TAG_KEY = define("resource_or_tag_key",
            packetWrapper -> Collections.singletonList(packetWrapper.readIdentifier()),
            (packetWrapper, properties) -> packetWrapper.writeIdentifier((ResourceLocation) properties.get(0))
    );
    public static final Parser RESOURCE = define("resource",
            packetWrapper -> Collections.singletonList(packetWrapper.readIdentifier()),
            (packetWrapper, properties) -> packetWrapper.writeIdentifier((ResourceLocation) properties.get(0))
    );
    public static final Parser RESOURCE_KEY = define("resource_key",
            packetWrapper -> Collections.singletonList(packetWrapper.readIdentifier()),
            (packetWrapper, properties) -> packetWrapper.writeIdentifier((ResourceLocation) properties.get(0))
    );
    public static final Parser TEMPLATE_MIRROR = define("template_mirror", null, null);
    public static final Parser TEMPLATE_ROTATION = define("template_rotation", null, null);
    public static final Parser HEIGHTMAP = define("heightmap", null, null);
    public static final Parser LOOT_TABLE = define("loot_table", null, null);
    public static final Parser LOOT_PREDICATE = define("loot_predicate", null, null);
    public static final Parser LOOT_MODIFIER = define("loot_modifier", null, null);
    public static final Parser UUID = define("uuid", null, null);

    static {
        TYPES_BUILDER.unloadFileMappings();
    }

    @FunctionalInterface
    public interface Reader extends Function<PacketWrapper<?>, List<Object>> {}

    @FunctionalInterface
    public interface Writer extends BiConsumer<PacketWrapper<?>, List<Object>> {}

    public static final class Parser implements MappedEntity {

        private final TypesBuilderData data;
        private final Reader reader;
        private final Writer writer;

        @Deprecated
        public Parser(String name, @Nullable Function<PacketWrapper<?>, List<Object>> read, @Nullable BiConsumer<PacketWrapper<?>, List<Object>> write) {
            this(
                    new TypesBuilderData(new ResourceLocation(name), new int[0]),
                    read == null ? null : read::apply,
                    write == null ? null : write::accept
            );
        }

        private Parser(TypesBuilderData data, @Nullable Reader reader, Writer writer) {
            this.data = data;
            this.reader = reader;
            this.writer = writer;
        }

        public Optional<List<Object>> readProperties(PacketWrapper<?> wrapper) {
            if (this.reader != null) {
                return Optional.of(this.reader.apply(wrapper));
            }
            return Optional.empty();
        }

        public void writeProperties(PacketWrapper<?> wrapper, List<Object> properties) {
            if (this.writer != null) {
                this.writer.accept(wrapper, properties);
            }
        }

        @Override
        public ResourceLocation getName() {
            return this.data.getName();
        }

        @Override
        public int getId(ClientVersion version) {
            int index = TYPES_BUILDER.getDataIndex(version);
            return this.data.getData()[index];
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Parser) {
                return this.getName().equals(((Parser) obj).getName());
            }
            return false;
        }
    }
}
