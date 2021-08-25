/*
 *
 * This file is part of Bukkit - https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/browse
 * Copyright (C) 2011 Bukkit author and contributors
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
 *
 */

package io.github.retrooper.packetevents.utils.boundingbox;

import org.apache.commons.lang.Validate;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class RayTraceResult {
    private final Vector hitPosition;
    private final Block hitBlock;
    private final BlockFace hitBlockFace;
    private final Entity hitEntity;

    private RayTraceResult(@NotNull Vector hitPosition, @Nullable Block hitBlock, @Nullable BlockFace hitBlockFace, @Nullable Entity hitEntity) {
        Validate.notNull(hitPosition, "Hit position is null!");
        this.hitPosition = hitPosition.clone();
        this.hitBlock = hitBlock;
        this.hitBlockFace = hitBlockFace;
        this.hitEntity = hitEntity;
    }

    public RayTraceResult(@NotNull Vector hitPosition) {
        this(hitPosition, null, null, null);
    }

    public RayTraceResult(@NotNull Vector hitPosition, @Nullable BlockFace hitBlockFace) {
        this(hitPosition, null, hitBlockFace, null);
    }

    public RayTraceResult(@NotNull Vector hitPosition, @Nullable Block hitBlock, @Nullable BlockFace hitBlockFace) {
        this(hitPosition, hitBlock, hitBlockFace, null);
    }

    public RayTraceResult(@NotNull Vector hitPosition, @Nullable Entity hitEntity) {
        this(hitPosition, null, null, hitEntity);
    }

    public RayTraceResult(@NotNull Vector hitPosition, @Nullable Entity hitEntity, @Nullable BlockFace hitBlockFace) {
        this(hitPosition, null, hitBlockFace, hitEntity);
    }

    @NotNull
    public Vector getHitPosition() {
        return this.hitPosition.clone();
    }

    @Nullable
    public Block getHitBlock() {
        return this.hitBlock;
    }

    @Nullable
    public BlockFace getHitBlockFace() {
        return this.hitBlockFace;
    }

    @Nullable
    public Entity getHitEntity() {
        return this.hitEntity;
    }

    public int hashCode() {
        int result = 31 + this.hitPosition.hashCode();
        result = 31 * result + (this.hitBlock == null ? 0 : this.hitBlock.hashCode());
        result = 31 * result + (this.hitBlockFace == null ? 0 : this.hitBlockFace.hashCode());
        result = 31 * result + (this.hitEntity == null ? 0 : this.hitEntity.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof RayTraceResult)) {
            return false;
        } else {
            RayTraceResult other = (RayTraceResult) obj;
            if (!this.hitPosition.equals(other.hitPosition)) {
                return false;
            } else if (!Objects.equals(this.hitBlock, other.hitBlock)) {
                return false;
            } else if (!Objects.equals(this.hitBlockFace, other.hitBlockFace)) {
                return false;
            } else {
                return Objects.equals(this.hitEntity, other.hitEntity);
            }
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RayTraceResult [hitPosition=");
        builder.append(this.hitPosition);
        builder.append(", hitBlock=");
        builder.append(this.hitBlock);
        builder.append(", hitBlockFace=");
        builder.append(this.hitBlockFace);
        builder.append(", hitEntity=");
        builder.append(this.hitEntity);
        builder.append("]");
        return builder.toString();
    }
}