package com.github.retrooper.packetevents.protocol.world.generation.fluids;

import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Fluids {

    private static final VersionedRegistry<Fluid> REGISTRY = new VersionedRegistry<>("fluid");

    private Fluids() {
    }

    public static VersionedRegistry<Fluid> getRegistry() {
        return REGISTRY;
    }

    private static Fluid define(String name) {
        return REGISTRY.define(name, StaticFluid::new);
    }

    public static final Fluid EMPTY = define("empty");
    public static final Fluid FLOWING_WATER = define("flowing_water");
    public static final Fluid WATER = define("water");
    public static final Fluid FLOWING_LAVA = define("flowing_lava");
    public static final Fluid LAVA = define("lava");

    static {
        REGISTRY.unloadMappings();
    }
}
