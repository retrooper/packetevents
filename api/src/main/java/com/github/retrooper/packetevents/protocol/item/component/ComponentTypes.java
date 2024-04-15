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

package com.github.retrooper.packetevents.protocol.item.component;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.TypesBuilder;
import com.github.retrooper.packetevents.util.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ComponentTypes {

    private static final Map<String, ComponentType<?>> COMPONENT_TYPE_MAP = new HashMap<>();
    private static final Map<Byte, Map<Integer, ComponentType<?>>> COMPONENT_TYPE_ID_MAP = new HashMap<>();
    private static final TypesBuilder TYPES_BUILDER = new TypesBuilder("item/item_component_mappings",
            ClientVersion.V_1_20_5);

    public static <T> ComponentType<T> define(String key) {
        return define(key, null, null);
    }

    public static <T> ComponentType<T> define(String key, @Nullable Reader<T> reader, @Nullable Writer<T> writer) {
        TypesBuilderData data = TYPES_BUILDER.defineFromArray(key);
        ComponentType<T> type = new ComponentType<T>() {
            private final int[] ids = data.getData();

            @Override
            public T read(PacketWrapper<?> wrapper) {
                return reader == null ? null : reader.apply(wrapper);
            }

            @Override
            public void write(PacketWrapper<?> wrapper, T content) {
                if (writer != null) {
                    writer.accept(wrapper, content);
                }
            }

            @Override
            public ResourceLocation getName() {
                return data.getName();
            }

            @Override
            public int getId(ClientVersion version) {
                int index = TYPES_BUILDER.getDataIndex(version);
                return this.ids[index];
            }
        };

        COMPONENT_TYPE_MAP.put(type.getName().toString(), type);
        for (ClientVersion version : TYPES_BUILDER.getVersions()) {
            int index = TYPES_BUILDER.getDataIndex(version);
            Map<Integer, ComponentType<?>> idMap = COMPONENT_TYPE_ID_MAP.computeIfAbsent(
                    (byte) index, k -> new HashMap<>());
            idMap.put(type.getId(version), type);
        }
        return type;
    }

    // with key
    public static ComponentType<?> getByName(String name) {
        return COMPONENT_TYPE_MAP.get(name);
    }

    public static ComponentType<?> getById(ClientVersion version, int id) {
        int index = TYPES_BUILDER.getDataIndex(version);
        Map<Integer, ComponentType<?>> idMap = COMPONENT_TYPE_ID_MAP.get((byte) index);
        return idMap.get(id);
    }

    public static final ComponentType<Void> CUSTOM_DATA = define("custom_data");
    public static final ComponentType<Void> MAX_STACK_SIZE = define("max_stack_size");
    public static final ComponentType<Void> MAX_DAMAGE = define("max_damage");
    public static final ComponentType<Void> DAMAGE = define("damage");
    public static final ComponentType<Void> UNBREAKABLE = define("unbreakable");
    public static final ComponentType<Void> CUSTOM_NAME = define("custom_name");
    public static final ComponentType<Void> ITEM_NAME = define("item_name");
    public static final ComponentType<Void> LORE = define("lore");
    public static final ComponentType<Void> RARITY = define("rarity");
    public static final ComponentType<Void> ENCHANTMENTS = define("enchantments");
    public static final ComponentType<Void> CAN_PLACE_ON = define("can_place_on");
    public static final ComponentType<Void> CAN_BREAK = define("can_break");
    public static final ComponentType<Void> ATTRIBUTE_MODIFIERS = define("attribute_modifiers");
    public static final ComponentType<Void> CUSTOM_MODEL_DATA = define("custom_model_data");
    public static final ComponentType<Void> HIDE_ADDITIONAL_TOOLTIP = define("hide_additional_tooltip");
    public static final ComponentType<Void> HIDE_TOOLTIP = define("hide_tooltip");
    public static final ComponentType<Void> REPAIR_COST = define("repair_cost");
    public static final ComponentType<Void> CREATIVE_SLOT_LOCK = define("creative_slot_lock");
    public static final ComponentType<Void> ENCHANTMENT_GLINT_OVERRIDE = define("enchantment_glint_override");
    public static final ComponentType<Void> INTANGIBLE_PROJECTILE = define("intangible_projectile");
    public static final ComponentType<Void> FOOD = define("food");
    public static final ComponentType<Void> FIRE_RESISTANT = define("fire_resistant");
    public static final ComponentType<Void> TOOL = define("tool");
    public static final ComponentType<Void> STORED_ENCHANTMENTS = define("stored_enchantments");
    public static final ComponentType<Void> DYED_COLOR = define("dyed_color");
    public static final ComponentType<Void> MAP_COLOR = define("map_color");
    public static final ComponentType<Void> MAP_ID = define("map_id");
    public static final ComponentType<Void> MAP_DECORATIONS = define("map_decorations");
    public static final ComponentType<Void> MAP_POST_PROCESSING = define("map_post_processing");
    public static final ComponentType<Void> CHARGED_PROJECTILES = define("charged_projectiles");
    public static final ComponentType<Void> BUNDLE_CONTENTS = define("bundle_contents");
    public static final ComponentType<Void> POTION_CONTENTS = define("potion_contents");
    public static final ComponentType<Void> SUSPICIOUS_STEW_EFFECTS = define("suspicious_stew_effects");
    public static final ComponentType<Void> WRITABLE_BOOK_CONTENT = define("writable_book_content");
    public static final ComponentType<Void> WRITTEN_BOOK_CONTENT = define("written_book_content");
    public static final ComponentType<Void> TRIM = define("trim");
    public static final ComponentType<Void> DEBUG_STICK_STATE = define("debug_stick_state");
    public static final ComponentType<Void> ENTITY_DATA = define("entity_data");
    public static final ComponentType<Void> BUCKET_ENTITY_DATA = define("bucket_entity_data");
    public static final ComponentType<Void> BLOCK_ENTITY_DATA = define("block_entity_data");
    public static final ComponentType<Void> INSTRUMENT = define("instrument");
    public static final ComponentType<Void> OMINOUS_BOTTLE_AMPLIFIER = define("ominous_bottle_amplifier");
    public static final ComponentType<Void> RECIPES = define("recipes");
    public static final ComponentType<Void> LODESTONE_TRACKER = define("lodestone_tracker");
    public static final ComponentType<Void> FIREWORK_EXPLOSION = define("firework_explosion");
    public static final ComponentType<Void> FIREWORKS = define("fireworks");
    public static final ComponentType<Void> PROFILE = define("profile");
    public static final ComponentType<Void> NOTE_BLOCK_SOUND = define("note_block_sound");
    public static final ComponentType<Void> BANNER_PATTERNS = define("banner_patterns");
    public static final ComponentType<Void> BASE_COLOR = define("base_color");
    public static final ComponentType<Void> POT_DECORATIONS = define("pot_decorations");
    public static final ComponentType<Void> CONTAINER = define("container");
    public static final ComponentType<Void> BLOCK_STATE = define("block_state");
    public static final ComponentType<Void> BEES = define("bees");
    public static final ComponentType<Void> LOCK = define("lock");
    public static final ComponentType<Void> CONTAINER_LOOT = define("container_loot");

    static {
        TYPES_BUILDER.unloadFileMappings();
    }

    @FunctionalInterface
    public interface Reader<T> extends Function<PacketWrapper<?>, T> {}

    @FunctionalInterface
    public interface Writer<T> extends BiConsumer<PacketWrapper<?>, T> {}
}
