/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.entity.transform;

import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EntityTransformType {
    public static final EntityTransformType IDENTITY = new EntityTransformType(null, 1.0F, null);

    private @Nullable TypeModifier typeModifier;
    private float scale;
    private @Nullable UserProfile playerSkin;

    //Scale must be between 0.1F and 16.0F
    public EntityTransformType(@Nullable TypeModifier typeModifier, float scale, @Nullable UserProfile playerSkin) {
        this.typeModifier = typeModifier;
        this.scale = scale;
        this.playerSkin = playerSkin;
    }

    public Optional<TypeModifier> getTypeModifier() {
        return Optional.ofNullable(typeModifier);
    }

    public void setTypeModifier(@Nullable TypeModifier typeModifier) {
        this.typeModifier = typeModifier;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Optional<UserProfile> getPlayerSkin() {
        return Optional.ofNullable(playerSkin);
    }

    public void setPlayerSkin(@Nullable UserProfile playerSkin) {
        this.playerSkin = playerSkin;
    }

    public static class TypeModifier {
        private EntityType entityType;
        private @Nullable NBTCompound tag;

        public TypeModifier(EntityType entityType, @Nullable NBTCompound tag) {
            this.entityType = entityType;
            this.tag = tag;
        }

        public EntityType getEntityType() {
            return entityType;
        }

        public void setEntityType(EntityType entityType) {
            this.entityType = entityType;
        }

        public Optional<NBTCompound> getTag() {
            return Optional.ofNullable(tag);
        }

        public void setTag(@Nullable NBTCompound tag) {
            this.tag = tag;
        }
    }
}
