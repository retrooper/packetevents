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

package com.github.retrooper.packetevents.protocol.component;

import com.github.retrooper.packetevents.protocol.color.DyeColor;
import com.github.retrooper.packetevents.protocol.component.builtin.AxolotlVariantComponent;
import com.github.retrooper.packetevents.protocol.component.builtin.CatCollarComponent;
import com.github.retrooper.packetevents.protocol.component.builtin.CatVariantComponent;
import com.github.retrooper.packetevents.protocol.component.builtin.ChickenVariantComponent;
import com.github.retrooper.packetevents.protocol.component.builtin.CowVariantComponent;
import com.github.retrooper.packetevents.protocol.component.builtin.FoxVariantComponent;
import com.github.retrooper.packetevents.protocol.component.builtin.FrogVariantComponent;
import com.github.retrooper.packetevents.protocol.component.builtin.HorseVariantComponent;
import com.github.retrooper.packetevents.protocol.component.builtin.LlamaVariantComponent;
import com.github.retrooper.packetevents.protocol.component.builtin.MooshroomVariantComponent;
import com.github.retrooper.packetevents.protocol.component.builtin.PaintingVariantComponent;
import com.github.retrooper.packetevents.protocol.component.builtin.ParrotVariantComponent;
import com.github.retrooper.packetevents.protocol.component.builtin.PigVariantComponent;
import com.github.retrooper.packetevents.protocol.component.builtin.RabbitVariantComponent;
import com.github.retrooper.packetevents.protocol.component.builtin.SalmonSizeComponent;
import com.github.retrooper.packetevents.protocol.component.builtin.SheepColorComponent;
import com.github.retrooper.packetevents.protocol.component.builtin.ShulkerColorComponent;
import com.github.retrooper.packetevents.protocol.component.builtin.TropicalFishBaseColorComponent;
import com.github.retrooper.packetevents.protocol.component.builtin.TropicalFishPatternColorComponent;
import com.github.retrooper.packetevents.protocol.component.builtin.TropicalFishPatternComponent;
import com.github.retrooper.packetevents.protocol.component.builtin.VillagerVariantComponent;
import com.github.retrooper.packetevents.protocol.component.builtin.WolfCollarComponent;
import com.github.retrooper.packetevents.protocol.component.builtin.WolfSoundVariantComponent;
import com.github.retrooper.packetevents.protocol.component.builtin.WolfVariantComponent;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ArmorTrim;
import com.github.retrooper.packetevents.protocol.component.builtin.item.BannerLayers;
import com.github.retrooper.packetevents.protocol.component.builtin.item.BundleContents;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ChargedProjectiles;
import com.github.retrooper.packetevents.protocol.component.builtin.item.CustomData;
import com.github.retrooper.packetevents.protocol.component.builtin.item.DebugStickState;
import com.github.retrooper.packetevents.protocol.component.builtin.item.FireworkExplosion;
import com.github.retrooper.packetevents.protocol.component.builtin.item.FoodProperties;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemAdventurePredicate;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemAttributeModifiers;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemBees;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemBlockStateProperties;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemBlocksAttacks;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemBreakSound;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemConsumable;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemContainerContents;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemContainerLoot;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemCustomModelData;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemDamageResistant;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemDeathProtection;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemDyeColor;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemEnchantable;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemEnchantments;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemEquippable;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemFireworks;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemInstrument;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemJukeboxPlayable;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemLock;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemLore;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemMapDecorations;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemMapPostProcessingState;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemModel;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemPotionContents;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemPotionDurationScale;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemProfile;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemProvidesBannerPatterns;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemProvidesTrimMaterial;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemRarity;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemRecipes;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemRepairable;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemTool;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemTooltipDisplay;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemTooltipStyle;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemUnbreakable;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemUseCooldown;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemUseRemainder;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemWeapon;
import com.github.retrooper.packetevents.protocol.component.builtin.item.LodestoneTracker;
import com.github.retrooper.packetevents.protocol.component.builtin.item.PotDecorations;
import com.github.retrooper.packetevents.protocol.component.builtin.item.SuspiciousStewEffects;
import com.github.retrooper.packetevents.protocol.component.builtin.item.WritableBookContent;
import com.github.retrooper.packetevents.protocol.component.builtin.item.WrittenBookContent;
import com.github.retrooper.packetevents.protocol.item.instrument.Instrument;
import com.github.retrooper.packetevents.protocol.mapper.MaybeMappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.*;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.util.NbtDecoder;
import com.github.retrooper.packetevents.protocol.util.NbtEncoder;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Dummy;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.PacketWrapper.Reader;
import com.github.retrooper.packetevents.wrapper.PacketWrapper.Writer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Locale;
import java.util.function.Function;

/**
 * Contains all item data component types.
 *
 * @see EnchantEffectComponentTypes
 */
public final class ComponentTypes {

    private static final VersionedRegistry<ComponentType<?>> REGISTRY = new VersionedRegistry<>("data_component_type");

    private ComponentTypes() {
    }

    @ApiStatus.Internal
    public static <T> ComponentType<T> define(String key) {
        return define(key, null, null);
    }

    @ApiStatus.Internal
    public static <T> ComponentType<T> define(String key, @Nullable Reader<T> reader, @Nullable Writer<T> writer) {
        return REGISTRY.define(key, data -> new StaticComponentType<>(data, reader, writer));
    }

    @ApiStatus.Internal
    public static <T> ComponentType<T> define(String key, @Nullable Reader<T> reader, @Nullable Writer<T> writer, @Nullable ComponentType.Decoder<T> decoder, @Nullable ComponentType.Encoder<T> encoder) {
        return REGISTRY.define(key, data -> new StaticComponentType<>(data, reader, writer, decoder, encoder));
    }

    @ApiStatus.Internal
    public static <T> ComponentType<T> define(String key, @Nullable Reader<T> reader, @Nullable Writer<T> writer, @Nullable Function<ClientVersion, NbtDecoder<T>> decoder, @Nullable Function<ClientVersion, NbtEncoder<T>> encoder) {
        return define(
                key,
                reader,
                writer,
                decoder == null ? null : (NBT nbt, ClientVersion version) -> decoder.apply(version).decode(nbt, PacketWrapper.createDummyWrapper(version)),
                encoder == null ? null : (T value, ClientVersion version) -> encoder.apply(version).encode(PacketWrapper.createDummyWrapper(version), value)
        );
    }

    @ApiStatus.Internal
    public static <T, N extends NBT> ComponentType<T> define(String key, @Nullable Reader<T> reader, @Nullable Writer<T> writer, NBTType<N> nbtType, Function<N, T> decoder, Function<T, N> encoder) {
        return define(
                key,
                reader,
                writer,
                (nbt, version) -> {
                    N casted = nbtType.getNBTClass().cast(nbt);
                    return decoder.apply(casted);
                },
                (value, version) -> encoder.apply(value)
        );
    }

    @ApiStatus.Internal
    public static <T extends Enum<T>> ComponentType<T> define(String key, Class<T> enumClass) {
        return define(
                key,
                wrapper -> wrapper.readEnum(enumClass),
                PacketWrapper::writeEnum,
                (nbt, version) -> {
                    NBTString string = (NBTString) nbt;
                    String value = string.getValue();
                    return Enum.valueOf(enumClass, value.toUpperCase(Locale.ROOT));
                },
                (value, version) -> {
                    String name = value.name().toLowerCase(Locale.ROOT);
                    return new NBTString(name);
                }
        );
    }

    public static VersionedRegistry<ComponentType<?>> getRegistry() {
        return REGISTRY;
    }

    public static ComponentType<?> getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static ComponentType<?> getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static final ComponentType<NBTCompound> CUSTOM_DATA = define("custom_data",
            // mojang wraps their "persistent" codec as a stream codec just here,
            // so packetevents has to handle nbt strings
            CustomData::read, CustomData::write, CustomData::decode, CustomData::encode);
    public static final ComponentType<Integer> MAX_STACK_SIZE = define("max_stack_size",
            PacketWrapper::readVarInt, PacketWrapper::writeVarInt, NBTType.INT, NBTInt::getAsInt, NBTInt::new);
    public static final ComponentType<Integer> MAX_DAMAGE = define("max_damage",
            PacketWrapper::readVarInt, PacketWrapper::writeVarInt, NBTType.INT, NBTInt::getAsInt, NBTInt::new);
    public static final ComponentType<Integer> DAMAGE = define("damage",
            PacketWrapper::readVarInt, PacketWrapper::writeVarInt, NBTType.INT, NBTInt::getAsInt, NBTInt::new);
    public static final ComponentType<ItemUnbreakable> UNBREAKABLE_MODERN = define("unbreakable",
            ItemUnbreakable::read, ItemUnbreakable::write);
    /**
     * Changed in 1.21.5
     */
    @Deprecated
    public static final ComponentType<Boolean> UNBREAKABLE = UNBREAKABLE_MODERN.legacyMap(
            ItemUnbreakable::isShowInTooltip, ItemUnbreakable::new);
    public static final ComponentType<Component> CUSTOM_NAME = define("custom_name",
            PacketWrapper::readComponent, PacketWrapper::writeComponent, AdventureSerializer::serializer, AdventureSerializer::serializer);
    public static final ComponentType<Component> ITEM_NAME = define("item_name",
            PacketWrapper::readComponent, PacketWrapper::writeComponent, AdventureSerializer::serializer, AdventureSerializer::serializer);
    public static final ComponentType<ItemLore> LORE = define("lore",
            ItemLore::read, ItemLore::write, ItemLore::decode, ItemLore::encode);
    public static final ComponentType<ItemRarity> RARITY = define("rarity", ItemRarity.class);
    public static final ComponentType<ItemEnchantments> ENCHANTMENTS = define("enchantments",
            ItemEnchantments::read, ItemEnchantments::write, ItemEnchantments::decode, ItemEnchantments::encode);
    public static final ComponentType<ItemAdventurePredicate> CAN_PLACE_ON = define("can_place_on",
            ItemAdventurePredicate::read, ItemAdventurePredicate::write);
    public static final ComponentType<ItemAdventurePredicate> CAN_BREAK = define("can_break",
            ItemAdventurePredicate::read, ItemAdventurePredicate::write);
    public static final ComponentType<ItemAttributeModifiers> ATTRIBUTE_MODIFIERS = define("attribute_modifiers",
            ItemAttributeModifiers::read, ItemAttributeModifiers::write, ItemAttributeModifiers::decode, ItemAttributeModifiers::encode);
    public static final ComponentType<ItemCustomModelData> CUSTOM_MODEL_DATA_LISTS = define("custom_model_data",
            ItemCustomModelData::read, ItemCustomModelData::write, ItemCustomModelData::decode, ItemCustomModelData::encode);
    /**
     * Rewritten in 1.21.4
     */
    @Deprecated
    public static final ComponentType<Integer> CUSTOM_MODEL_DATA = CUSTOM_MODEL_DATA_LISTS.legacyMap(
            ItemCustomModelData::getLegacyId, ItemCustomModelData::new);
    /**
     * Removed with 1.21.5
     */
    @ApiStatus.Obsolete
    public static final ComponentType<Dummy> HIDE_ADDITIONAL_TOOLTIP = define("hide_additional_tooltip",
            Dummy::dummyRead, Dummy::dummyWrite);
    /**
     * Removed with 1.21.5
     */
    @ApiStatus.Obsolete
    public static final ComponentType<Dummy> HIDE_TOOLTIP = define("hide_tooltip",
            Dummy::dummyRead, Dummy::dummyWrite);
    public static final ComponentType<Integer> REPAIR_COST = define("repair_cost",
            PacketWrapper::readVarInt, PacketWrapper::writeVarInt, NBTType.INT, NBTInt::getAsInt, NBTInt::new);
    public static final ComponentType<Dummy> CREATIVE_SLOT_LOCK = define("creative_slot_lock",
            Dummy::dummyRead, Dummy::dummyWrite, Dummy::dummyDecode, Dummy::dummyEncode);
    public static final ComponentType<Boolean> ENCHANTMENT_GLINT_OVERRIDE = define("enchantment_glint_override",
            PacketWrapper::readBoolean, PacketWrapper::writeBoolean, NBTType.BYTE, NBTByte::getAsBool, NBTByte::new);
    public static final ComponentType<Dummy> INTANGIBLE_PROJECTILE = define("intangible_projectile",
            Dummy::dummyReadNbt, Dummy::dummyWriteNbt, Dummy::dummyDecode, Dummy::dummyEncode);
    public static final ComponentType<FoodProperties> FOOD = define("food",
            FoodProperties::read, FoodProperties::write);
    /**
     * Has been migrated to #DAMAGE_RESISTANT with 1.21.2.
     */
    @ApiStatus.Obsolete
    public static final ComponentType<Dummy> FIRE_RESISTANT = define("fire_resistant",
            Dummy::dummyRead, Dummy::dummyWrite);
    public static final ComponentType<ItemTool> TOOL = define("tool",
            ItemTool::read, ItemTool::write);
    public static final ComponentType<ItemEnchantments> STORED_ENCHANTMENTS = define("stored_enchantments",
            ItemEnchantments::read, ItemEnchantments::write, ItemEnchantments::decode, ItemEnchantments::encode);
    public static final ComponentType<ItemDyeColor> DYED_COLOR = define("dyed_color",
            ItemDyeColor::read, ItemDyeColor::write, ItemDyeColor::decode, ItemDyeColor::encode);
    public static final ComponentType<Integer> MAP_COLOR = define("map_color",
            PacketWrapper::readInt, PacketWrapper::writeInt, NBTType.INT, NBTInt::getAsInt, NBTInt::new);
    public static final ComponentType<Integer> MAP_ID = define("map_id",
            PacketWrapper::readVarInt, PacketWrapper::writeVarInt, NBTType.INT, NBTInt::getAsInt, NBTInt::new);
    public static final ComponentType<ItemMapDecorations> MAP_DECORATIONS = define("map_decorations",
            ItemMapDecorations::read, ItemMapDecorations::write);
    public static final ComponentType<ItemMapPostProcessingState> MAP_POST_PROCESSING = define("map_post_processing",
            ItemMapPostProcessingState.class);
    public static final ComponentType<ChargedProjectiles> CHARGED_PROJECTILES = define("charged_projectiles",
            ChargedProjectiles::read, ChargedProjectiles::write, ChargedProjectiles::decode, ChargedProjectiles::encode);
    public static final ComponentType<BundleContents> BUNDLE_CONTENTS = define("bundle_contents",
            BundleContents::read, BundleContents::write, BundleContents::decode, BundleContents::encode);
    public static final ComponentType<ItemPotionContents> POTION_CONTENTS = define("potion_contents",
            ItemPotionContents::read, ItemPotionContents::write);
    public static final ComponentType<SuspiciousStewEffects> SUSPICIOUS_STEW_EFFECTS = define("suspicious_stew_effects",
            SuspiciousStewEffects::read, SuspiciousStewEffects::write);
    public static final ComponentType<WritableBookContent> WRITABLE_BOOK_CONTENT = define("writable_book_content",
            WritableBookContent::read, WritableBookContent::write);
    public static final ComponentType<WrittenBookContent> WRITTEN_BOOK_CONTENT = define("written_book_content",
            WrittenBookContent::read, WrittenBookContent::write);
    public static final ComponentType<ArmorTrim> TRIM = define("trim",
            ArmorTrim::read, ArmorTrim::write, ArmorTrim::decode, ArmorTrim::encode);
    public static final ComponentType<DebugStickState> DEBUG_STICK_STATE = define("debug_stick_state",
            DebugStickState::read, DebugStickState::write);
    public static final ComponentType<NBTCompound> ENTITY_DATA = define("entity_data",
            PacketWrapper::readNBT, PacketWrapper::writeNBT, NBTType.COMPOUND, Function.identity(), Function.identity());
    public static final ComponentType<NBTCompound> BUCKET_ENTITY_DATA = define("bucket_entity_data",
            PacketWrapper::readNBT, PacketWrapper::writeNBT, NBTType.COMPOUND, Function.identity(), Function.identity());
    public static final ComponentType<NBTCompound> BLOCK_ENTITY_DATA = define("block_entity_data",
            PacketWrapper::readNBT, PacketWrapper::writeNBT, NBTType.COMPOUND, Function.identity(), Function.identity());
    public static final ComponentType<ItemInstrument> ITEM_INSTRUMENT = define("instrument",
            ItemInstrument::read, ItemInstrument::write);
    @Deprecated
    public static final ComponentType<Instrument> INSTRUMENT = ITEM_INSTRUMENT.legacyMap(
            inst -> inst.getInstrument().getValue(),
            inst -> new ItemInstrument(new MaybeMappedEntity<>(inst)));
    public static final ComponentType<Integer> OMINOUS_BOTTLE_AMPLIFIER = define("ominous_bottle_amplifier",
            PacketWrapper::readVarInt, PacketWrapper::writeVarInt, NBTType.INT, NBTInt::getAsInt, NBTInt::new);
    public static final ComponentType<ItemRecipes> RECIPES = define("recipes",
            ItemRecipes::read, ItemRecipes::write, ItemRecipes::decode, ItemRecipes::encode);
    public static final ComponentType<LodestoneTracker> LODESTONE_TRACKER = define("lodestone_tracker",
            LodestoneTracker::read, LodestoneTracker::write);
    public static final ComponentType<FireworkExplosion> FIREWORK_EXPLOSION = define("firework_explosion",
            FireworkExplosion::read, FireworkExplosion::write);
    public static final ComponentType<ItemFireworks> FIREWORKS = define("fireworks",
            ItemFireworks::read, ItemFireworks::write);
    public static final ComponentType<ItemProfile> PROFILE = define("profile",
            ItemProfile::read, ItemProfile::write, ItemProfile::decode, ItemProfile::encode);
    public static final ComponentType<ResourceLocation> NOTE_BLOCK_SOUND = define("note_block_sound",
            PacketWrapper::readIdentifier, PacketWrapper::writeIdentifier, ResourceLocation::decode, ResourceLocation::encode);
    public static final ComponentType<BannerLayers> BANNER_PATTERNS = define("banner_patterns",
            BannerLayers::read, BannerLayers::write, BannerLayers::decode, BannerLayers::encode);
    public static final ComponentType<DyeColor> BASE_COLOR = define("base_color", DyeColor.class);
    public static final ComponentType<PotDecorations> POT_DECORATIONS = define("pot_decorations",
            PotDecorations::read, PotDecorations::write);
    public static final ComponentType<ItemContainerContents> CONTAINER = define("container",
            ItemContainerContents::read, ItemContainerContents::write);
    public static final ComponentType<ItemBlockStateProperties> BLOCK_STATE = define("block_state",
            ItemBlockStateProperties::read, ItemBlockStateProperties::write, ItemBlockStateProperties::decode, ItemBlockStateProperties::encode);
    public static final ComponentType<ItemBees> BEES = define("bees",
            ItemBees::read, ItemBees::write);
    public static final ComponentType<ItemLock> LOCK = define("lock",
            ItemLock::read, ItemLock::write);
    public static final ComponentType<ItemContainerLoot> CONTAINER_LOOT = define("container_loot",
            ItemContainerLoot::read, ItemContainerLoot::write, ItemContainerLoot::decode, ItemContainerLoot::encode);

    // added in 1.21
    public static final ComponentType<ItemJukeboxPlayable> JUKEBOX_PLAYABLE = define("jukebox_playable",
            ItemJukeboxPlayable::read, ItemJukeboxPlayable::write);

    // added in 1.21.2
    public static final ComponentType<ItemConsumable> CONSUMABLE = define("consumable",
            ItemConsumable::read, ItemConsumable::write);
    public static final ComponentType<ItemUseRemainder> USE_REMAINDER = define("use_remainder",
            ItemUseRemainder::read, ItemUseRemainder::write, ItemUseRemainder::decode, ItemUseRemainder::encode);
    public static final ComponentType<ItemUseCooldown> USE_COOLDOWN = define("use_cooldown",
            ItemUseCooldown::read, ItemUseCooldown::write, ItemUseCooldown::decode, ItemUseCooldown::encode);
    public static final ComponentType<ItemEnchantable> ENCHANTABLE = define("enchantable",
            ItemEnchantable::read, ItemEnchantable::write, ItemEnchantable::decode, ItemEnchantable::encode);
    public static final ComponentType<ItemRepairable> REPAIRABLE = define("repairable",
            ItemRepairable::read, ItemRepairable::write, ItemRepairable::decode, ItemRepairable::encode);
    public static final ComponentType<ItemModel> ITEM_MODEL = define("item_model",
            ItemModel::read, ItemModel::write, ItemModel::decode, ItemModel::encode);
    public static final ComponentType<ItemDamageResistant> DAMAGE_RESISTANT = define("damage_resistant",
            ItemDamageResistant::read, ItemDamageResistant::write, ItemDamageResistant::decode, ItemDamageResistant::encode);
    public static final ComponentType<ItemEquippable> EQUIPPABLE = define("equippable",
            ItemEquippable::read, ItemEquippable::write);
    public static final ComponentType<Dummy> GLIDER = define("glider",
            Dummy::dummyRead, Dummy::dummyWrite, Dummy::dummyDecode, Dummy::dummyEncode);
    public static final ComponentType<ItemDeathProtection> DEATH_PROTECTION = define("death_protection",
            ItemDeathProtection::read, ItemDeathProtection::write);
    public static final ComponentType<ItemTooltipStyle> TOOLTIP_STYLE = define("tooltip_style",
            ItemTooltipStyle::read, ItemTooltipStyle::write, ItemTooltipStyle::decode, ItemTooltipStyle::encode);

    /**
     * Added with 1.21.5
     */
    public static final ComponentType<ItemTooltipDisplay> TOOLTIP_DISPLAY = define("tooltip_display",
            ItemTooltipDisplay::read, ItemTooltipDisplay::write, ItemTooltipDisplay::decode, ItemTooltipDisplay::encode);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<ItemWeapon> WEAPON = define("weapon",
            ItemWeapon::read, ItemWeapon::write, ItemWeapon::decode, ItemWeapon::encode);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<ItemBlocksAttacks> BLOCKS_ATTACKS = define("blocks_attacks",
            ItemBlocksAttacks::read, ItemBlocksAttacks::write);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<ItemPotionDurationScale> POTION_DURATION_SCALE = define("potion_duration_scale",
            ItemPotionDurationScale::read, ItemPotionDurationScale::write, ItemPotionDurationScale::decode, ItemPotionDurationScale::encode);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<ItemProvidesTrimMaterial> PROVIDES_TRIM_MATERIAL = define("provides_trim_material",
            ItemProvidesTrimMaterial::read, ItemProvidesTrimMaterial::write, ItemProvidesTrimMaterial::decode, ItemProvidesTrimMaterial::encode);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<ItemProvidesBannerPatterns> PROVIDES_BANNER_PATTERNS = define("provides_banner_patterns",
            ItemProvidesBannerPatterns::read, ItemProvidesBannerPatterns::write, ItemProvidesBannerPatterns::decode, ItemProvidesBannerPatterns::encode);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<ItemBreakSound> BREAK_SOUND = define("break_sound",
            ItemBreakSound::read, ItemBreakSound::write, ItemBreakSound::decode, ItemBreakSound::encode);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<VillagerVariantComponent> VILLAGER_VARIANT = define("villager/variant",
            VillagerVariantComponent::read, VillagerVariantComponent::write);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<WolfVariantComponent> WOLF_VARIANT = define("wolf/variant",
            WolfVariantComponent::read, WolfVariantComponent::write);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<WolfSoundVariantComponent> WOLF_SOUND_VARIANT = define("wolf/sound_variant",
            WolfSoundVariantComponent::read, WolfSoundVariantComponent::write);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<WolfCollarComponent> WOLF_COLLAR = define("wolf/collar",
            WolfCollarComponent::read, WolfCollarComponent::write);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<FoxVariantComponent> FOX_VARIANT = define("fox/variant",
            FoxVariantComponent::read, FoxVariantComponent::write);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<SalmonSizeComponent> SALMON_SIZE = define("salmon/size",
            SalmonSizeComponent::read, SalmonSizeComponent::write);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<ParrotVariantComponent> PARROT_VARIANT = define("parrot/variant",
            ParrotVariantComponent::read, ParrotVariantComponent::write);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<TropicalFishPatternComponent> TROPICAL_FISH_PATTERN = define("tropical_fish/pattern",
            TropicalFishPatternComponent::read, TropicalFishPatternComponent::write);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<TropicalFishBaseColorComponent> TROPICAL_FISH_BASE_COLOR = define("tropical_fish/base_color",
            TropicalFishBaseColorComponent::read, TropicalFishBaseColorComponent::write);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<TropicalFishPatternColorComponent> TROPICAL_FISH_PATTERN_COLOR = define("tropical_fish/pattern_color",
            TropicalFishPatternColorComponent::read, TropicalFishPatternColorComponent::write);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<MooshroomVariantComponent> MOOSHROOM_VARIANT = define("mooshroom/variant",
            MooshroomVariantComponent::read, MooshroomVariantComponent::write);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<RabbitVariantComponent> RABBIT_VARIANT = define("rabbit/variant",
            RabbitVariantComponent::read, RabbitVariantComponent::write);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<PigVariantComponent> PIG_VARIANT = define("pig/variant",
            PigVariantComponent::read, PigVariantComponent::write);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<CowVariantComponent> COW_VARIANT = define("cow/variant",
            CowVariantComponent::read, CowVariantComponent::write);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<ChickenVariantComponent> CHICKEN_VARIANT = define("chicken/variant",
            ChickenVariantComponent::read, ChickenVariantComponent::write);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<FrogVariantComponent> FROG_VARIANT = define("frog/variant",
            FrogVariantComponent::read, FrogVariantComponent::write);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<HorseVariantComponent> HORSE_VARIANT = define("horse/variant",
            HorseVariantComponent::read, HorseVariantComponent::write);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<PaintingVariantComponent> PAINTING_VARIANT = define("painting/variant",
            PaintingVariantComponent::read, PaintingVariantComponent::write);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<LlamaVariantComponent> LLAMA_VARIANT = define("llama/variant",
            LlamaVariantComponent::read, LlamaVariantComponent::write);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<AxolotlVariantComponent> AXOLOTL_VARIANT = define("axolotl/variant",
            AxolotlVariantComponent::read, AxolotlVariantComponent::write);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<CatVariantComponent> CAT_VARIANT = define("cat/variant",
            CatVariantComponent::read, CatVariantComponent::write);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<CatCollarComponent> CAT_COLLAR = define("cat/collar",
            CatCollarComponent::read, CatCollarComponent::write);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<SheepColorComponent> SHEEP_COLOR = define("sheep/color",
            SheepColorComponent::read, SheepColorComponent::write);
    /**
     * Added with 1.21.5
     */
    public static final ComponentType<ShulkerColorComponent> SHULKER_COLOR = define("shulker/color",
            ShulkerColorComponent::read, ShulkerColorComponent::write);

    /**
     * Returns an immutable view of the component types.
     *
     * @return Component Types
     */
    public static Collection<ComponentType<?>> values() {
        return REGISTRY.getEntries();
    }

    static {
        REGISTRY.unloadMappings();
    }
}
