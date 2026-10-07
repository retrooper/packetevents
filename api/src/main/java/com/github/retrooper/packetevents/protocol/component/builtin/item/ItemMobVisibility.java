package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * @versions 26.3+
 */
@NullMarked
public class ItemMobVisibility {

    private final MappedEntitySet<EntityType> targetingEntityTypes;
    private final float visibility;

    public ItemMobVisibility(MappedEntitySet<EntityType> targetingEntityTypes, float visibility) {
        this.targetingEntityTypes = targetingEntityTypes;
        this.visibility = visibility;
    }

    public static ItemMobVisibility read(PacketWrapper<?> wrapper) {
        MappedEntitySet<EntityType> targetingEntityTypes = MappedEntitySet.read(wrapper, EntityTypes.getRegistry());
        float visibility = wrapper.readFloat();
        return new ItemMobVisibility(targetingEntityTypes, visibility);
    }

    public static void write(PacketWrapper<?> wrapper, ItemMobVisibility visibility) {
        MappedEntitySet.write(wrapper, visibility.targetingEntityTypes);
        wrapper.writeFloat(visibility.visibility);
    }

    public MappedEntitySet<EntityType> getTargetingEntityTypes() {
        return this.targetingEntityTypes;
    }

    public float getVisibility() {
        return this.visibility;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        ItemMobVisibility that = (ItemMobVisibility) obj;
        if (Float.compare(that.visibility, this.visibility) != 0) return false;
        return this.targetingEntityTypes.equals(that.targetingEntityTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.targetingEntityTypes, this.visibility);
    }
}
