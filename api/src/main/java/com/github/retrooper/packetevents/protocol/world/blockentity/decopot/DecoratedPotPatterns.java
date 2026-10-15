package com.github.retrooper.packetevents.protocol.world.blockentity.decopot;

import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import net.kyori.adventure.key.KeyPattern;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 26.3+
 */
@NullMarked
public final class DecoratedPotPatterns {

    private static final VersionedRegistry<DecoratedPotPattern> REGISTRY = new VersionedRegistry<>("decorated_pot_pattern");

    private DecoratedPotPatterns() {
    }

    public static VersionedRegistry<DecoratedPotPattern> getRegistry() {
        return REGISTRY;
    }

    private static DecoratedPotPattern define(@KeyPattern.Value String name) {
        ResourceLocation assetId = ResourceLocation.minecraft(name + "_pottery_pattern");
        return REGISTRY.define(name, data -> new StaticDecoratedPotPattern(data, assetId));
    }

    public static final DecoratedPotPattern ANGLER = define("angler");
    public static final DecoratedPotPattern ARCHER = define("archer");
    public static final DecoratedPotPattern ARMS_UP = define("arms_up");
    public static final DecoratedPotPattern BLADE = define("blade");
    public static final DecoratedPotPattern BREWER = define("brewer");
    public static final DecoratedPotPattern BURN = define("burn");
    public static final DecoratedPotPattern DANGER = define("danger");
    public static final DecoratedPotPattern EXPLORER = define("explorer");
    public static final DecoratedPotPattern FLOW = define("flow");
    public static final DecoratedPotPattern FRIEND = define("friend");
    public static final DecoratedPotPattern GUSTER = define("guster");
    public static final DecoratedPotPattern HEART = define("heart");
    public static final DecoratedPotPattern HEARTBREAK = define("heartbreak");
    public static final DecoratedPotPattern HOWL = define("howl");
    public static final DecoratedPotPattern MINER = define("miner");
    public static final DecoratedPotPattern MOURNER = define("mourner");
    public static final DecoratedPotPattern PLENTY = define("plenty");
    public static final DecoratedPotPattern PRIZE = define("prize");
    public static final DecoratedPotPattern SCRAPE = define("scrape");
    public static final DecoratedPotPattern SHEAF = define("sheaf");
    public static final DecoratedPotPattern SHELTER = define("shelter");
    public static final DecoratedPotPattern SKULL = define("skull");
    public static final DecoratedPotPattern SNORT = define("snort");

    static {
        REGISTRY.unloadMappings();
    }
}
