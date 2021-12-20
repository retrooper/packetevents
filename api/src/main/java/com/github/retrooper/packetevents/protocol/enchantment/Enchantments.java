package com.github.retrooper.packetevents.protocol.enchantment;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.MappingHelper;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Enchantments {
    private static final Map<String, Enchantment> ENCHANTMENT_TYPE_MAPPINGS = new HashMap<>();
    private static final Map<Integer, Enchantment> ENCHANTMENT_TYPE_ID_MAPPINGS = new HashMap<>();
    private static JsonObject ENCHANTMENT_TYPE_JSON;

    private static ServerVersion getMappingServerVersion(ServerVersion serverVersion) {
        if (serverVersion.isOlderThan(ServerVersion.V_1_13)) {
            return ServerVersion.V_1_12;
        } else if (serverVersion.isOlderThan(ServerVersion.V_1_14)) {
            return ServerVersion.V_1_13;
        } else if (serverVersion.isOlderThan(ServerVersion.V_1_16)) {
            return ServerVersion.V_1_14;
        } else {
            return ServerVersion.V_1_16;
        }
    }

    public static Enchantment define(String key) {
        if (ENCHANTMENT_TYPE_JSON == null) {
            ENCHANTMENT_TYPE_JSON = MappingHelper.getJSONObject("enchantment/enchantment_type_mappings");
        }

        ResourceLocation identifier = ResourceLocation.minecraft(key);
        ServerVersion mappingsVersion = getMappingServerVersion(PacketEvents.getAPI().getServerManager().getVersion());

        final int id;

        if (ENCHANTMENT_TYPE_JSON.has(mappingsVersion.name())) {
            JsonObject map = ENCHANTMENT_TYPE_JSON.getAsJsonObject(mappingsVersion.name());
            if (map.has(identifier.toString())) {
                id = map.get(identifier.toString()).getAsInt();
            } else {
                id = -1;
            }
        } else {
            throw new IllegalStateException("Failed to find Enchantments mappings for the " + mappingsVersion.name() + " mappings version!");
        }

        Enchantment enchantment = new Enchantment() {
            @Override
            public ResourceLocation getIdentifier() {
                return identifier;
            }

            // TODO: Why is this useful? Does some outdated version use it? I don't know, but ViaVersion translates it.
            // TODO: Fix this (not that it is being used currently)
            @Override
            public int getId() {
                return id;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof Enchantment) {
                    return getId() == ((Enchantment) obj).getId();
                }
                return false;
            }
        };

        ENCHANTMENT_TYPE_MAPPINGS.put(enchantment.getIdentifier().getKey(), enchantment);
        ENCHANTMENT_TYPE_ID_MAPPINGS.put(enchantment.getId(), enchantment);
        return enchantment;
    }

    @Nullable
    public static Enchantment getByKey(String key) {
        return ENCHANTMENT_TYPE_MAPPINGS.get(key);
    }

    @Nullable
    public static Enchantment getById(int id) {
        return ENCHANTMENT_TYPE_ID_MAPPINGS.get(id);
    }

    public static final Enchantment ALL_DAMAGE_PROTECTION = define("protection");
    public static final Enchantment FIRE_PROTECTION = define("fire_protection");
    public static final Enchantment FALL_PROTECTION = define("feather_falling");
    public static final Enchantment BLAST_PROTECTION = define("blast_protection");
    public static final Enchantment PROJECTILE_PROTECTION = define("projectile_protection");
    public static final Enchantment RESPIRATION = define("respiration");
    public static final Enchantment AQUA_AFFINITY = define("aqua_affinity");
    public static final Enchantment THORNS = define("thorns");
    public static final Enchantment DEPTH_STRIDER = define("depth_strider");
    public static final Enchantment FROST_WALKER = define("frost_walker");
    public static final Enchantment BINDING_CURSE = define("binding_curse");
    public static final Enchantment SOUL_SPEED = define("soul_speed");
    public static final Enchantment SHARPNESS = define("sharpness");
    public static final Enchantment SMITE = define("smite");
    public static final Enchantment BANE_OF_ARTHROPODS = define("bane_of_arthropods");
    public static final Enchantment KNOCKBACK = define("knockback");
    public static final Enchantment FIRE_ASPECT = define("fire_aspect");
    public static final Enchantment MOB_LOOTING = define("looting");
    public static final Enchantment SWEEPING_EDGE = define("sweeping");
    public static final Enchantment BLOCK_EFFICIENCY = define("efficiency");
    public static final Enchantment SILK_TOUCH = define("silk_touch");
    public static final Enchantment UNBREAKING = define("unbreaking");
    public static final Enchantment BLOCK_FORTUNE = define("fortune");
    public static final Enchantment POWER_ARROWS = define("power");
    public static final Enchantment PUNCH_ARROWS = define("punch");
    public static final Enchantment FLAMING_ARROWS = define("flame");
    public static final Enchantment INFINITY_ARROWS = define("infinity");
    public static final Enchantment FISHING_LUCK = define("luck_of_the_sea");
    public static final Enchantment FISHING_SPEED = define("lure");
    public static final Enchantment LOYALTY = define("loyalty");
    public static final Enchantment IMPALING = define("impaling");
    public static final Enchantment RIPTIDE = define("riptide");
    public static final Enchantment CHANNELING = define("channeling");
    public static final Enchantment MULTISHOT = define("multishot");
    public static final Enchantment QUICK_CHARGE = define("quick_charge");
    public static final Enchantment PIERCING = define("piercing");
    public static final Enchantment MENDING = define("mending");
    public static final Enchantment VANISHING_CURSE = define("vanishing_curse");
}
