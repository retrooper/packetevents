package com.github.retrooper.packetevents.protocol.valueproviders.floats;

import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 1.17+
 */
@NullMarked
public final class FloatProviderTypes {

    private static final VersionedRegistry<FloatProviderType<?>> REGISTRY = new VersionedRegistry<>("float_provider_type");

    private FloatProviderTypes() {
    }

    @ApiStatus.Internal
    public static <T extends FloatProvider> FloatProviderType<T> define(String name, NbtMapCodec<T> codec) {
        return REGISTRY.define(name, data -> new StaticFloatProviderType<>(data, codec));
    }

    public static final FloatProviderType<ConstantFloat> CONSTANT = define("constant", ConstantFloat.MAP_CODEC);
    public static final FloatProviderType<UniformFloat> UNIFORM = define("uniform", UniformFloat.MAP_CODEC);
    public static final FloatProviderType<ClampedNormalFloat> CLAMPED_NORMAL = define("clamped_normal", ClampedNormalFloat.MAP_CODEC);
    public static final FloatProviderType<TrapezoidFloat> TRAPEZOID = define("trapezoid", TrapezoidFloat.MAP_CODEC);

    public static VersionedRegistry<FloatProviderType<?>> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}
