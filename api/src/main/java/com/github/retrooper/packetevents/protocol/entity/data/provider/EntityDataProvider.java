/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.entity.data.provider;

import com.github.retrooper.packetevents.protocol.chat.component.BaseComponent;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntityDataProvider implements DataProvider {
    @Nullable
    private BaseComponent customName;

    private boolean customNameVisible;
    private EntityPose pose;
    private int airTicks;
    private int ticksFrozenInPowderedSnow = 0;
    private boolean onFire;
    private boolean crouching;
    //Now unused
    private boolean riding;

    private boolean sprinting;
    private boolean swimming;
    private boolean invisible;
    private boolean glowing;
    private boolean flyingWithElytra;
    private boolean silent;
    private boolean hasGravity;

    @Nullable
    public BaseComponent getCustomName() {
        return customName;
    }

    public void setCustomName(@Nullable BaseComponent customName) {
        this.customName = customName;
    }

    public boolean isCustomNameVisible() {
        return customNameVisible;
    }

    public void setCustomNameVisible(boolean customNameVisible) {
        this.customNameVisible = customNameVisible;
    }

    public EntityPose getPose() {
        return pose;
    }

    public void setPose(EntityPose pose) {
        this.pose = pose;
    }

    public int getAirTicks() {
        return airTicks;
    }

    public void setAirTicks(int airTicks) {
        this.airTicks = airTicks;
    }

    public int getTicksFrozenInPowderedSnow() {
        return ticksFrozenInPowderedSnow;
    }

    public void setTicksFrozenInPowderedSnow(int ticksFrozenInPowderedSnow) {
        this.ticksFrozenInPowderedSnow = ticksFrozenInPowderedSnow;
    }

    public boolean isOnFire() {
        return onFire;
    }

    public void setOnFire(boolean onFire) {
        this.onFire = onFire;
    }

    public boolean isCrouching() {
        return crouching;
    }

    public void setCrouching(boolean crouching) {
        this.crouching = crouching;
    }

    public boolean isRiding() {
        return riding;
    }

    public void setRiding(boolean riding) {
        this.riding = riding;
    }

    public boolean isSprinting() {
        return sprinting;
    }

    public void setSprinting(boolean sprinting) {
        this.sprinting = sprinting;
    }

    public boolean isSwimming() {
        return swimming;
    }

    public void setSwimming(boolean swimming) {
        this.swimming = swimming;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public boolean isGlowing() {
        return glowing;
    }

    public void setGlowing(boolean glowing) {
        this.glowing = glowing;
    }

    public boolean isFlyingWithElytra() {
        return flyingWithElytra;
    }

    public void setFlyingWithElytra(boolean flyingWithElytra) {
        this.flyingWithElytra = flyingWithElytra;
    }

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    public boolean isHasGravity() {
        return hasGravity;
    }

    public void setHasGravity(boolean hasGravity) {
        this.hasGravity = hasGravity;
    }

    @Override
    public List<EntityData> encode() {
        byte mask = 0x00;
        if (onFire) {
            mask |= 0x01;
        }
        if (crouching) {
            mask |= 0x02;
        }
        //TODO Rethink, should we add this?
        if (riding) {
            mask |= 0x04;
        }
        if (sprinting) {
            mask |= 0x08;
        }
        if (swimming) {
            mask |= 0x10;
        }
        if (invisible) {
            mask |= 0x20;
        }
        if (glowing) {
            mask |= 0x40;
        }
        if (flyingWithElytra) {
            mask |= 0x80;
        }
        List<EntityData> metadata = new ArrayList<>(8);
        metadata.add(new EntityData(0, EntityDataTypes.BYTE, mask));
        metadata.add(new EntityData(1, EntityDataTypes.INT, airTicks));
        metadata.add(new EntityData(2, EntityDataTypes.OPTIONAL_COMPONENT, Optional.ofNullable(customName)));
        metadata.add(new EntityData(3, EntityDataTypes.BOOLEAN, customNameVisible));
        metadata.add(new EntityData(4, EntityDataTypes.BOOLEAN, silent));
        metadata.add(new EntityData(5, EntityDataTypes.BOOLEAN, !hasGravity));
        metadata.add(new EntityData(6, EntityDataTypes.ENTITY_POSE, pose));
        metadata.add(new EntityData(7, EntityDataTypes.INT, ticksFrozenInPowderedSnow));
        return metadata;
    }

    @Override
    public void decode(List<EntityData> data) {
        for (EntityData entityData : data) {
            switch (entityData.getIndex()) {
                case 0:
                    byte mask = (byte) entityData.getValue();
                    onFire = (mask & 0x01) != 0;
                    crouching = (mask & 0x02) != 0;
                    riding = (mask & 0x04) != 0;
                    sprinting = (mask & 0x08) != 0;
                    swimming = (mask & 0x10) != 0;
                    invisible = (mask & 0x20) != 0;
                    glowing = (mask & 0x40) != 0;
                    flyingWithElytra = (mask & 0x80) != 0;
                    break;
                case 1:
                    airTicks = (int) entityData.getValue();
                    break;
                case 2:
                    customName = ((Optional<BaseComponent>) entityData.getValue()).orElse(null);
                    break;
                case 3:
                    customNameVisible = (boolean) entityData.getValue();
                    break;
                case 4:
                    silent = (boolean) entityData.getValue();
                    break;
                case 5:
                    hasGravity = !(boolean) entityData.getValue();
                    break;
                case 6:
                    pose = (EntityPose) entityData.getValue();
                    break;
                case 7:
                    ticksFrozenInPowderedSnow = (int) entityData.getValue();
                    break;
            }
        }
    }

    public static Builder<Builder, EntityDataProvider> builder() {
        return new Builder<>(new EntityDataProvider());
    }

    public static class Builder<T extends Builder, K extends EntityDataProvider> {
        protected K provider;

        public Builder(K provider) {
            this.provider = provider;
        }

        public T customName(@Nullable BaseComponent customName) {
            provider.setCustomName(customName);
            return (T) this;
        }

        public T customNameVisible(boolean customNameVisible) {
            provider.setCustomNameVisible(customNameVisible);
            return (T) this;
        }

        public T pose(EntityPose pose) {
            provider.setPose(pose);
            return (T) this;
        }

        public T onFire(boolean onFire) {
            provider.setOnFire(onFire);
            return (T) this;
        }

        public T crouching(boolean crouching) {
            provider.setCrouching(crouching);
            return (T) this;
        }

        public T riding(boolean riding) {
            provider.setRiding(riding);
            return (T) this;
        }

        public T sprinting(boolean sprinting) {
            provider.setSprinting(sprinting);
            return (T) this;
        }

        public T swimming(boolean swimming) {
            provider.setSwimming(swimming);
            return (T) this;
        }

        public T invisible(boolean invisible) {
            provider.setInvisible(invisible);
            return (T) this;
        }

        public T glowing(boolean glowing) {
            provider.setGlowing(glowing);
            return (T) this;
        }

        public T flyingWithElytra(boolean flyingWithElytra) {
            provider.setFlyingWithElytra(flyingWithElytra);
            return (T) this;
        }

        public T silent(boolean silent) {
            provider.setSilent(silent);
            return (T) this;
        }

        public T hasGravity(boolean hasGravity) {
            provider.setHasGravity(hasGravity);
            return (T) this;
        }

        public T airTicks(int airTicks) {
            provider.setAirTicks(airTicks);
            return (T) this;
        }

        public T ticksFrozenInPowderedSnow(int ticksFrozenInPowderedSnow) {
            provider.setTicksFrozenInPowderedSnow(ticksFrozenInPowderedSnow);
            return (T) this;
        }

        public K build() {
            return provider;
        }
    }
}
