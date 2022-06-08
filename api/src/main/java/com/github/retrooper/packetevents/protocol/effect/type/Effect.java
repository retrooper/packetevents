package com.github.retrooper.packetevents.protocol.effect.type;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.TypesBuilder;
import com.github.retrooper.packetevents.util.TypesBuilderData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Effect {
    private static final Map<String, Effects> EFFECT_TYPE_MAPPINGS;
    private static final Map<Byte, Map<Integer, Effects>> EFFECT_TYPE_ID_MAPPINGS;
    private static final TypesBuilder TYPES_BUILDER;

    static {
        EFFECT_TYPE_MAPPINGS = new HashMap<>();
        EFFECT_TYPE_ID_MAPPINGS = new HashMap<>();
        TYPES_BUILDER = new TypesBuilder("effect/effect_type_mappings",
                ClientVersion.V_1_8,
                ClientVersion.V_1_9,
                ClientVersion.V_1_15,
                ClientVersion.V_1_16,
                ClientVersion.V_1_17);
    }

    public static Effects define(@NotNull final String key) {
        final TypesBuilderData data = TYPES_BUILDER.define(key);
        final Effects effect = new Effects() {
            @Override
            public ResourceLocation getName() {
                return data.getName();
            }

            @Override
            public int getId(ClientVersion version) {
                final int index = TYPES_BUILDER.getDataIndex(version);
                return data.getData()[index];
            }

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof Effects) {
                    return getName() == ((Effects) obj).getName();
                }
                return false;
            }
        };

        EFFECT_TYPE_MAPPINGS.put(effect.getName().getKey(), effect);
        for (ClientVersion version : TYPES_BUILDER.getVersions()) {
            final int index = TYPES_BUILDER.getDataIndex(version);
            final Map<Integer, Effects> typeIdMap = EFFECT_TYPE_ID_MAPPINGS.computeIfAbsent((byte) index, k -> new HashMap<>());
            typeIdMap.put(effect.getId(version), effect);
        }
        return effect;
    }

    @Nullable
    public static Effects getByName(@NotNull final String name) {
        return EFFECT_TYPE_MAPPINGS.get(name);
    }

    @Nullable
    public static Effects getById(@NotNull final ClientVersion version, final int id) {
        final int index = TYPES_BUILDER.getDataIndex(version);
        final Map<Integer, Effects> typeIdMap = EFFECT_TYPE_ID_MAPPINGS.get((byte) index);
        return typeIdMap.get(id);
    }

    // 1.8
    public static final Effects CLICK2 = define("click2");
    public static final Effects CLICK1 = define("click1");
    public static final Effects BOW_FIRE = define("bow_fire");
    public static final Effects DOOR_TOGGLE = define("door_toggle");
    public static final Effects EXTINGUISH = define("extinguish");
    public static final Effects RECORD_PLAY = define("record_play");
    public static final Effects GHAST_SHRIEK = define("ghast_shriek");
    public static final Effects GHAST_SHOOT = define("ghast_shoot");
    public static final Effects BALZE_SHOOT = define("blaze_shoot");
    public static final Effects ZOMBIE_CHEW_WOODEN_DOOR = define("zombie_chew_wooden_door");
    public static final Effects ZOMBIE_CHEW_IRON_DOOR = define("zombie_chew_iron_door");
    public static final Effects ZOMBIE_DESTROY_DOOR = define("zombie_destroy_door");
    public static final Effects SMOKE = define("smoke");
    public static final Effects STEP_SOUND = define("step_sound");
    public static final Effects POTION_BREAK = define("potion_break");
    public static final Effects ENDER_SIGNAL = define("ender_signal");
    public static final Effects MOBSPAWNER_FLAMES = define("mobspawner_flames");

    // 1.9
    public static final Effects IRON_DOOR_TOGGLE = define("iron_door_toggle");
    public static final Effects TRAPDOOR_TOGGLE = define("trapdoor_toggle");
    public static final Effects IRON_TRAPDOOR_TOGGLE = define("iron_trapdoor_toggle");
    public static final Effects FENCE_GATE_TOGGLE = define("fence_gate_toggle");
    public static final Effects DOOR_CLOSE = define("door_close");
    public static final Effects IRON_DOOR_CLOSE = define("iron_door_close");
    public static final Effects TRAPDOOR_CLOSE = define("trapdoor_close");
    public static final Effects IRON_TRAPDOOR_CLOSE = define("iron_trapdoor_close");
    public static final Effects FENCE_GATE_CLOSE = define("fence_gate_close");
    public static final Effects BREWING_STAND_BREW = define("brewing_stand_brew");
    public static final Effects CHORUS_FLOWER_GROW = define("chorus_flower_grow");
    public static final Effects CHORUS_FLOWER_DEATH = define("chorus_flower_death");
    public static final Effects PORTAL_TRAVEL = define("portal_travel");
    public static final Effects ENDEREYE_LAUNCH = define("endereye_launch");
    public static final Effects FIREWORK_SHOOT = define("firework_shoot");
    public static final Effects VILLAGER_PLANT_GROW = define("villager_plant_grow");
    public static final Effects DRAGON_BREATH = define("dragon_breath");
    public static final Effects ANVIL_BREAK = define("anvil_break");
    public static final Effects ANVIL_USE = define("anvil_use");
    public static final Effects ANVIL_LAND = define("anvil_land");
    public static final Effects ENDERDRAGON_SHOOT = define("enderdragon_shoot");
    public static final Effects WITHER_BREAK_BLOCK = define("wither_break_block");
    public static final Effects WITHER_SHOOT = define("wither_shoot");
    public static final Effects ZOMBIE_INFECT_ = define("zombie_infect");
    public static final Effects ZOMBIE_CONVERTED_VILLAGER = define("zombie_converted_villager");
    public static final Effects BAT_TAKEOFF = define("bat_takeoff");
    public static final Effects END_GATEWAY_SPAWN = define("end_gateway_spawn");
    public static final Effects ENDERDRAGON_GROWL = define("enderdragon_growl");

    // 1.15
    public static final Effects INSTANT_POTION_BREAK = define("instant_potion_break");

    // 1.16
    public static final Effects WITHER_SPAWNED = define("wither_spawned");
    public static final Effects ENDER_DRAGON_DEATH = define("ender_dragon_death");
    public static final Effects END_PORTAL_CREATED_IN_OVERWORLD = define("end_portal_created_in_overworld");
    public static final Effects PHANTOM_BITES = define("phantom_bites");
    public static final Effects ZOMBIE_CONVERTS_TO_DROWNED = define("zombie_converts_to_drowned");
    public static final Effects HUSK_CONVERTS_TO_ZOMBIE = define("husk_converts_to_zombie");
    public static final Effects GRINDSTONE_USED = define("grindstone_used");
    public static final Effects BOOK_PAGE_TURNED = define("book_page_turned");
    public static final Effects COMPOSTER_COMPOSTS = define("composter_composts");
    public static final Effects LAVA_CONVERTS_BLOCK = define("lava_converts_block");
    public static final Effects REDSTONE_TORCH_BURN = define("redstone_torch_burns_out");
    public static final Effects ENDER_EYE_PLACED = define("ender_eye_placed");
    public static final Effects ENDER_DRAGON_DESTROYS_BLOCK = define("ender_dragon_destroys_block");
    public static final Effects WET_SPONGE_VAPORIZES_IN_NETHER = define("wet_sponge_vaporizes_in_nether");

    // 1.17
    public static final Effects PHANTOM_BITE = define("phantom_bite");
    public static final Effects ZOMBIE_CONVERTED_TO_DROWNED = define("zombie_converted_to_drowned");
    public static final Effects HUSK_CONVERTED_TO_ZOMBIE = define("husk_converted_to_zombie");
    public static final Effects GRINDSTONE_USE = define("grindstone_use");
    public static final Effects BOOK_PAGE_TURN = define("book_page_turn");
    public static final Effects SMITHING_TABLE_USE = define("smithing_table_use");
    public static final Effects POINTED_DRIPSTONE_LAND = define("pointed_dripstone_land");
    public static final Effects POINTED_DRIPSTONE_DRIP_LAVA_INTO_CAULDRON = define("pointed_dripstone_drip_lava_into_cauldron");
    public static final Effects POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON = define("pointed_dripstone_drip_water_into_cauldron");
    public static final Effects SKELETON_CONVERTED_TO_STRAY = define("skeleton_converted_to_stray");
    public static final Effects COMPOSTER_FILL_ATTEMPT = define("composter_fill_attempt");
    public static final Effects LAVA_INTERACT = define("lava_interact");
    public static final Effects REDSTONE_TORCH_BURNOUT = define("redstone_torch_burnout");
    public static final Effects END_PORTAL_FRAME_FILL = define("end_portal_frame_fill");
    public static final Effects DRIPS_DRIPSTONE = define("dripping_dripstone");
    public static final Effects BONE_MEAL_USE = define("bone_meal_use");
    public static final Effects ENDER_DRAGON_DESTROY_BLOCK = define("ender_dragon_destroy_block");
    public static final Effects SPONGE_DRY = define("sponge_dry");
    public static final Effects ELECTRIC_SPARK = define("electric_spark");
    public static final Effects COPPER_WAX_ON = define("copper_wax_on");
    public static final Effects COPPER_WAX_OFF = define("copper_wax_off");
    public static final Effects OXIDISED_COPPER_SCRAPE = define("oxidised_copper_scrape");

    public static Collection<Effects> values = EFFECT_TYPE_MAPPINGS.values();
}
