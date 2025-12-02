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

package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.protocol.sound.Sounds;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ItemEquippable {

    private EquipmentSlot slot;
    private Sound equipSound;
    private @Nullable ResourceLocation assetId;
    private @Nullable ResourceLocation cameraOverlay;
    private @Nullable MappedEntitySet<EntityType> allowedEntities;
    private boolean dispensable;
    private boolean swappable;
    private boolean damageOnHurt;
    /**
     * Added with 1.21.5
     */
    private boolean equipOnInteract;
    /**
     * Added with 1.21.6
     */
    private boolean canBeSheared;
    /**
     * Added with 1.21.6
     */
    private Sound shearingSound;

    @ApiStatus.Obsolete
    public ItemEquippable(
            EquipmentSlot slot,
            Sound equipSound,
            @Nullable ResourceLocation assetId,
            @Nullable ResourceLocation cameraOverlay,
            @Nullable MappedEntitySet<EntityType> allowedEntities,
            boolean dispensable,
            boolean swappable,
            boolean damageOnHurt
    ) {
        this(slot, equipSound, assetId, cameraOverlay, allowedEntities,
                dispensable, swappable, damageOnHurt, false);
    }

    @ApiStatus.Obsolete
    public ItemEquippable(
            EquipmentSlot slot,
            Sound equipSound,
            @Nullable ResourceLocation assetId,
            @Nullable ResourceLocation cameraOverlay,
            @Nullable MappedEntitySet<EntityType> allowedEntities,
            boolean dispensable,
            boolean swappable,
            boolean damageOnHurt,
            boolean equipOnInteract
    ) {
        this(slot, equipSound, assetId, cameraOverlay, allowedEntities,
                dispensable, swappable, damageOnHurt, equipOnInteract,
                false, Sounds.ITEM_SHEARS_SNIP);
    }

    public ItemEquippable(
            EquipmentSlot slot,
            Sound equipSound,
            @Nullable ResourceLocation assetId,
            @Nullable ResourceLocation cameraOverlay,
            @Nullable MappedEntitySet<EntityType> allowedEntities,
            boolean dispensable,
            boolean swappable,
            boolean damageOnHurt,
            boolean equipOnInteract,
            boolean canBeSheared,
            Sound shearingSound
    ) {
        this.slot = slot;
        this.equipSound = equipSound;
        this.assetId = assetId;
        this.cameraOverlay = cameraOverlay;
        this.allowedEntities = allowedEntities;
        this.dispensable = dispensable;
        this.swappable = swappable;
        this.damageOnHurt = damageOnHurt;
        this.equipOnInteract = equipOnInteract;
        this.canBeSheared = canBeSheared;
        this.shearingSound = shearingSound;
    }

    public static ItemEquippable read(PacketWrapper<?> wrapper) {
        EquipmentSlot slot = wrapper.readEnum(EquipmentSlot.values());
        Sound equipSound = Sound.read(wrapper);
        ResourceLocation assetId = wrapper.readOptional(PacketWrapper::readIdentifier);
        ResourceLocation cameraOverlay = wrapper.readOptional(PacketWrapper::readIdentifier);
        MappedEntitySet<EntityType> allowedEntities = wrapper.readOptional(
                ew -> MappedEntitySet.read(ew, EntityTypes::getById));
        boolean dispensable = wrapper.readBoolean();
        boolean swappable = wrapper.readBoolean();
        boolean damageOnHurt = wrapper.readBoolean();
        boolean equipOnInteract = false;
        boolean canBeSheared = false;
        Sound shearingSound = Sounds.ITEM_SHEARS_SNIP;
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
            equipOnInteract = wrapper.readBoolean();
            if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
                canBeSheared = wrapper.readBoolean();
                shearingSound = Sound.read(wrapper);
            }
        }
        return new ItemEquippable(slot, equipSound, assetId,
                cameraOverlay, allowedEntities, dispensable, swappable,
                damageOnHurt, equipOnInteract, canBeSheared, shearingSound);
    }

    public static void write(PacketWrapper<?> wrapper, ItemEquippable equippable) {
        wrapper.writeEnum(equippable.slot);
        Sound.write(wrapper, equippable.equipSound);
        wrapper.writeOptional(equippable.assetId, PacketWrapper::writeIdentifier);
        wrapper.writeOptional(equippable.cameraOverlay, PacketWrapper::writeIdentifier);
        wrapper.writeOptional(equippable.allowedEntities, MappedEntitySet::write);
        wrapper.writeBoolean(equippable.dispensable);
        wrapper.writeBoolean(equippable.swappable);
        wrapper.writeBoolean(equippable.damageOnHurt);
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
            wrapper.writeBoolean(equippable.equipOnInteract);
            if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
                wrapper.writeBoolean(equippable.canBeSheared);
                Sound.write(wrapper, equippable.shearingSound);
            }
        }
    }

    public EquipmentSlot getSlot() {
        return this.slot;
    }

    public void setSlot(EquipmentSlot slot) {
        this.slot = slot;
    }

    public Sound getEquipSound() {
        return this.equipSound;
    }

    public void setEquipSound(Sound equipSound) {
        this.equipSound = equipSound;
    }

    public @Nullable ResourceLocation getAssetId() {
        return this.assetId;
    }

    public void setAssetId(@Nullable ResourceLocation assetId) {
        this.assetId = assetId;
    }

    public @Nullable ResourceLocation getCameraOverlay() {
        return this.cameraOverlay;
    }

    public void setCameraOverlay(@Nullable ResourceLocation cameraOverlay) {
        this.cameraOverlay = cameraOverlay;
    }

    public @Nullable MappedEntitySet<EntityType> getAllowedEntities() {
        return this.allowedEntities;
    }

    public void setAllowedEntities(@Nullable MappedEntitySet<EntityType> allowedEntities) {
        this.allowedEntities = allowedEntities;
    }

    public boolean isDispensable() {
        return this.dispensable;
    }

    public void setDispensable(boolean dispensable) {
        this.dispensable = dispensable;
    }

    public boolean isSwappable() {
        return this.swappable;
    }

    public void setSwappable(boolean swappable) {
        this.swappable = swappable;
    }

    public boolean isDamageOnHurt() {
        return this.damageOnHurt;
    }

    public void setDamageOnHurt(boolean damageOnHurt) {
        this.damageOnHurt = damageOnHurt;
    }

    /**
     * Added with 1.21.5
     */
    public boolean isEquipOnInteract() {
        return this.equipOnInteract;
    }

    /**
     * Added with 1.21.5
     */
    public void setEquipOnInteract(boolean equipOnInteract) {
        this.equipOnInteract = equipOnInteract;
    }

    /**
     * Added with 1.21.6
     */
    public boolean isCanBeSheared() {
        return this.canBeSheared;
    }

    /**
     * Added with 1.21.6
     */
    public void setCanBeSheared(boolean canBeSheared) {
        this.canBeSheared = canBeSheared;
    }

    /**
     * Added with 1.21.6
     */
    public Sound getShearingSound() {
        return this.shearingSound;
    }

    /**
     * Added with 1.21.6
     */
    public void setShearingSound(Sound shearingSound) {
        this.shearingSound = shearingSound;
    }

    @Deprecated
    public @Nullable ResourceLocation getModel() {
        return this.assetId;
    }

    @Deprecated
    public void setModel(@Nullable ResourceLocation assetId) {
        this.assetId = assetId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemEquippable)) return false;
        ItemEquippable that = (ItemEquippable) obj;
        if (this.dispensable != that.dispensable) return false;
        if (this.swappable != that.swappable) return false;
        if (this.damageOnHurt != that.damageOnHurt) return false;
        if (this.equipOnInteract != that.equipOnInteract) return false;
        if (this.canBeSheared != that.canBeSheared) return false;
        if (this.slot != that.slot) return false;
        if (!this.equipSound.equals(that.equipSound)) return false;
        if (!Objects.equals(this.assetId, that.assetId)) return false;
        if (!Objects.equals(this.cameraOverlay, that.cameraOverlay)) return false;
        if (!Objects.equals(this.allowedEntities, that.allowedEntities)) return false;
        return this.shearingSound.equals(that.shearingSound);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.slot, this.equipSound, this.assetId, this.cameraOverlay, this.allowedEntities, this.dispensable, this.swappable, this.damageOnHurt, this.equipOnInteract, this.canBeSheared, this.shearingSound);
    }

    @Override
    public String toString() {
        return "ItemEquippable{slot=" + this.slot + ", equipSound=" + this.equipSound + ", assetId=" + this.assetId + ", cameraOverlay=" + this.cameraOverlay + ", allowedEntities=" + this.allowedEntities + ", dispensable=" + this.dispensable + ", swappable=" + this.swappable + ", damageOnHurt=" + this.damageOnHurt + ", equipOnInteract=" + this.equipOnInteract + ", canBeSheared=" + this.canBeSheared + ", shearingSound=" + this.shearingSound + '}';
    }
}
