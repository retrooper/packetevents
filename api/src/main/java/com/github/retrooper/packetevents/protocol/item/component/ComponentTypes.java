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

import com.github.retrooper.packetevents.protocol.color.DyeColor;
import com.github.retrooper.packetevents.protocol.item.component.builtin.ArmorTrim;
import com.github.retrooper.packetevents.protocol.item.component.builtin.BannerLayers;
import com.github.retrooper.packetevents.protocol.item.component.builtin.BundleContents;
import com.github.retrooper.packetevents.protocol.item.component.builtin.ChargedProjectiles;
import com.github.retrooper.packetevents.protocol.item.component.builtin.FireworkExplosion;
import com.github.retrooper.packetevents.protocol.item.component.builtin.FoodProperties;
import com.github.retrooper.packetevents.protocol.item.component.builtin.ItemAdventurePredicate;
import com.github.retrooper.packetevents.protocol.item.component.builtin.ItemAttributeModifiers;
import com.github.retrooper.packetevents.protocol.item.component.builtin.ItemBees;
import com.github.retrooper.packetevents.protocol.item.component.builtin.ItemBlockStateProperties;
import com.github.retrooper.packetevents.protocol.item.component.builtin.ItemContainerContents;
import com.github.retrooper.packetevents.protocol.item.component.builtin.ItemDyeColor;
import com.github.retrooper.packetevents.protocol.item.component.builtin.ItemEnchantments;
import com.github.retrooper.packetevents.protocol.item.component.builtin.ItemFireworks;
import com.github.retrooper.packetevents.protocol.item.component.builtin.ItemMapPostProcessingState;
import com.github.retrooper.packetevents.protocol.item.component.builtin.ItemPotionContents;
import com.github.retrooper.packetevents.protocol.item.component.builtin.ItemProfile;
import com.github.retrooper.packetevents.protocol.item.component.builtin.ItemRarity;
import com.github.retrooper.packetevents.protocol.item.component.builtin.ItemTool;
import com.github.retrooper.packetevents.protocol.item.component.builtin.LodestoneTracker;
import com.github.retrooper.packetevents.protocol.item.component.builtin.PotDecorations;
import com.github.retrooper.packetevents.protocol.item.component.builtin.SuspiciousStewEffects;
import com.github.retrooper.packetevents.protocol.item.component.builtin.WritableBookContent;
import com.github.retrooper.packetevents.protocol.item.component.builtin.WrittenBookContent;
import com.github.retrooper.packetevents.protocol.mapper.GenericMappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.TypesBuilder;
import com.github.retrooper.packetevents.util.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
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

    public static final ComponentType<Void> CUSTOM_DATA = define("custom_data"); // not synchronized
    public static final ComponentType<Integer> MAX_STACK_SIZE = define("max_stack_size",
            PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
    public static final ComponentType<Integer> MAX_DAMAGE = define("max_damage",
            PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
    public static final ComponentType<Integer> DAMAGE = define("damage",
            PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
    public static final ComponentType<Boolean> UNBREAKABLE = define("unbreakable",
            PacketWrapper::readBoolean, PacketWrapper::writeBoolean);
    public static final ComponentType<Component> CUSTOM_NAME = define("custom_name",
            PacketWrapper::readComponent, PacketWrapper::writeComponent);
    public static final ComponentType<Component> ITEM_NAME = define("item_name",
            PacketWrapper::readComponent, PacketWrapper::writeComponent);
    public static final ComponentType<List<Component>> LORE = define("lore",
            wrapper -> wrapper.readList(PacketWrapper::readComponent),
            (wrapper, lines) -> wrapper.writeList(lines, PacketWrapper::writeComponent)
    );
    public static final ComponentType<ItemRarity> RARITY = define("rarity",
            wrapper -> wrapper.readEnum(ItemRarity.values()), PacketWrapper::writeEnum);
    public static final ComponentType<ItemEnchantments> ENCHANTMENTS = define("enchantments",
            ItemEnchantments::read, ItemEnchantments::write);
    public static final ComponentType<ItemAdventurePredicate> CAN_PLACE_ON = define("can_place_on",
            ItemAdventurePredicate::read, ItemAdventurePredicate::write);
    public static final ComponentType<ItemAdventurePredicate> CAN_BREAK = define("can_break",
            ItemAdventurePredicate::read, ItemAdventurePredicate::write);
    public static final ComponentType<ItemAttributeModifiers> ATTRIBUTE_MODIFIERS = define("attribute_modifiers",
            ItemAttributeModifiers::read, ItemAttributeModifiers::write);
    public static final ComponentType<Integer> CUSTOM_MODEL_DATA = define("custom_model_data",
            PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
    public static final ComponentType<Void> HIDE_ADDITIONAL_TOOLTIP = define("hide_additional_tooltip");
    public static final ComponentType<Void> HIDE_TOOLTIP = define("hide_tooltip");
    public static final ComponentType<Integer> REPAIR_COST = define("repair_cost",
            PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
    public static final ComponentType<Void> CREATIVE_SLOT_LOCK = define("creative_slot_lock");
    public static final ComponentType<Boolean> ENCHANTMENT_GLINT_OVERRIDE = define("enchantment_glint_override",
            PacketWrapper::readBoolean, PacketWrapper::writeBoolean);
    public static final ComponentType<Void> INTANGIBLE_PROJECTILE = define("intangible_projectile"); // not synchronized
    public static final ComponentType<FoodProperties> FOOD = define("food",
            FoodProperties::read, FoodProperties::write);
    public static final ComponentType<Void> FIRE_RESISTANT = define("fire_resistant");
    public static final ComponentType<ItemTool> TOOL = define("tool",
            ItemTool::read, ItemTool::write);
    public static final ComponentType<ItemEnchantments> STORED_ENCHANTMENTS = define("stored_enchantments",
            ItemEnchantments::read, ItemEnchantments::write);
    public static final ComponentType<ItemDyeColor> DYED_COLOR = define("dyed_color",
            ItemDyeColor::read, ItemDyeColor::write);
    public static final ComponentType<Integer> MAP_COLOR = define("map_color",
            PacketWrapper::readInt, PacketWrapper::writeInt);
    public static final ComponentType<Integer> MAP_ID = define("map_id",
            PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
    public static final ComponentType<Void> MAP_DECORATIONS = define("map_decorations"); // not synchronized
    public static final ComponentType<ItemMapPostProcessingState> MAP_POST_PROCESSING = define("map_post_processing",
            wrapper -> wrapper.readEnum(ItemMapPostProcessingState.values()), PacketWrapper::writeEnum);
    public static final ComponentType<ChargedProjectiles> CHARGED_PROJECTILES = define("charged_projectiles",
            ChargedProjectiles::read, ChargedProjectiles::write);
    public static final ComponentType<BundleContents> BUNDLE_CONTENTS = define("bundle_contents",
            BundleContents::read, BundleContents::write);
    public static final ComponentType<ItemPotionContents> POTION_CONTENTS = define("potion_contents",
            ItemPotionContents::read, ItemPotionContents::write);
    public static final ComponentType<SuspiciousStewEffects> SUSPICIOUS_STEW_EFFECTS = define("suspicious_stew_effects",
            SuspiciousStewEffects::read, SuspiciousStewEffects::write);
    public static final ComponentType<WritableBookContent> WRITABLE_BOOK_CONTENT = define("writable_book_content",
            WritableBookContent::read, WritableBookContent::write);
    public static final ComponentType<WrittenBookContent> WRITTEN_BOOK_CONTENT = define("written_book_content",
            WrittenBookContent::read, WrittenBookContent::write);
    public static final ComponentType<ArmorTrim> TRIM = define("trim",
            ArmorTrim::read, ArmorTrim::write);
    public static final ComponentType<Void> DEBUG_STICK_STATE = define("debug_stick_state"); // not synchronized
    public static final ComponentType<NBTCompound> ENTITY_DATA = define("entity_data",
            PacketWrapper::readNBT, PacketWrapper::writeNBT);
    public static final ComponentType<NBTCompound> BUCKET_ENTITY_DATA = define("bucket_entity_data",
            PacketWrapper::readNBT, PacketWrapper::writeNBT);
    public static final ComponentType<NBTCompound> BLOCK_ENTITY_DATA = define("block_entity_data",
            PacketWrapper::readNBT, PacketWrapper::writeNBT);
    // TODO: GenericMappedEntity -> minecraft:instrument registry
    public static final ComponentType<GenericMappedEntity> INSTRUMENT = define("instrument",
            GenericMappedEntity::read, PacketWrapper::writeMappedEntity);
    public static final ComponentType<Integer> OMINOUS_BOTTLE_AMPLIFIER = define("ominous_bottle_amplifier",
            PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
    public static final ComponentType<Void> RECIPES = define("recipes"); // not synchronized
    public static final ComponentType<LodestoneTracker> LODESTONE_TRACKER = define("lodestone_tracker",
            LodestoneTracker::read, LodestoneTracker::write);
    public static final ComponentType<FireworkExplosion> FIREWORK_EXPLOSION = define("firework_explosion",
            FireworkExplosion::read, FireworkExplosion::write);
    public static final ComponentType<ItemFireworks> FIREWORKS = define("fireworks",
            ItemFireworks::read, ItemFireworks::write);
    public static final ComponentType<ItemProfile> PROFILE = define("profile",
            ItemProfile::read, ItemProfile::write);
    public static final ComponentType<ResourceLocation> NOTE_BLOCK_SOUND = define("note_block_sound",
            PacketWrapper::readIdentifier, PacketWrapper::writeIdentifier);
    public static final ComponentType<BannerLayers> BANNER_PATTERNS = define("banner_patterns",
            BannerLayers::read, BannerLayers::write);
    public static final ComponentType<DyeColor> BASE_COLOR = define("base_color",
            wrapper -> wrapper.readEnum(DyeColor.values()), PacketWrapper::writeEnum);
    public static final ComponentType<PotDecorations> POT_DECORATIONS = define("pot_decorations",
            PotDecorations::read, PotDecorations::write);
    public static final ComponentType<ItemContainerContents> CONTAINER = define("container",
            ItemContainerContents::read, ItemContainerContents::write);
    public static final ComponentType<ItemBlockStateProperties> BLOCK_STATE = define("block_state",
            ItemBlockStateProperties::read, ItemBlockStateProperties::write);
    public static final ComponentType<ItemBees> BEES = define("bees",
            ItemBees::read, ItemBees::write);
    public static final ComponentType<Void> LOCK = define("lock"); // not synchronized
    public static final ComponentType<Void> CONTAINER_LOOT = define("container_loot"); // not synchronized

    static {
        TYPES_BUILDER.unloadFileMappings();
    }

    @FunctionalInterface
    public interface Reader<T> extends Function<PacketWrapper<?>, T> {}

    @FunctionalInterface
    public interface Writer<T> extends BiConsumer<PacketWrapper<?>, T> {}
}
