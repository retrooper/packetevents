package com.github.retrooper.packetevents.manager.registry;

import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import org.jetbrains.annotations.Nullable;

public interface ItemRegistry {
    @Nullable ItemType getByName(String name);
    @Nullable ItemType getById(int id);
}
