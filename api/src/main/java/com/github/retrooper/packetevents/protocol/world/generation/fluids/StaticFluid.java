package com.github.retrooper.packetevents.protocol.world.generation.fluids;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class StaticFluid extends AbstractMappedEntity implements Fluid {

    public StaticFluid(@Nullable TypesBuilderData data) {
        super(data);
    }
}
