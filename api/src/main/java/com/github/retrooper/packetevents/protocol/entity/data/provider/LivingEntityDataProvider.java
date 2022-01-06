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

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.util.Vector3i;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class LivingEntityDataProvider extends EntityDataProvider {
    private boolean handActive;
    private InteractionHand hand;
    private boolean isInRiptideSpinAttack;
    private float health = 1.0f;
    private int potionEffectColor;
    private boolean potionEffectAmbient;
    private int arrowInBodyCount;
    private int beeStingsInBodyCount;
    @Nullable
    private Vector3i sleepingPosition;


    public boolean isHandActive() {
        return handActive;
    }

    public void setHandActive(boolean handActive) {
        this.handActive = handActive;
    }

    public InteractionHand getHand() {
        return hand;
    }

    public void setHand(InteractionHand hand) {
        this.hand = hand;
    }

    public boolean isInRiptideSpinAttack() {
        return isInRiptideSpinAttack;
    }

    public void setInRiptideSpinAttack(boolean inRiptideSpinAttack) {
        isInRiptideSpinAttack = inRiptideSpinAttack;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public int getPotionEffectColor() {
        return potionEffectColor;
    }

    public void setPotionEffectColor(int potionEffectColor) {
        this.potionEffectColor = potionEffectColor;
    }

    public boolean isPotionEffectAmbient() {
        return potionEffectAmbient;
    }

    public void setPotionEffectAmbient(boolean potionEffectAmbient) {
        this.potionEffectAmbient = potionEffectAmbient;
    }

    public int getArrowInBodyCount() {
        return arrowInBodyCount;
    }

    public void setArrowInBodyCount(int arrowInBodyCount) {
        this.arrowInBodyCount = arrowInBodyCount;
    }

    public int getBeeStingsInBodyCount() {
        return beeStingsInBodyCount;
    }

    public void setBeeStingsInBodyCount(int beeStingsInBodyCount) {
        this.beeStingsInBodyCount = beeStingsInBodyCount;
    }

    @Nullable
    public Vector3i getSleepingPosition() {
        return sleepingPosition;
    }

    public void setSleepingPosition(@Nullable Vector3i sleepingPosition) {
        this.sleepingPosition = sleepingPosition;
    }

    public static LivingEntityBuilder<LivingEntityBuilder> builderLivingEntity() {
        return new LivingEntityBuilder<>(new LivingEntityDataProvider());
    }

    @Override
    public List<EntityData> encode() {
        List<EntityData> metadata = super.encode();
        byte mask = 0x00;
        if (handActive) {
            mask |= 0x01;
        }

        //TODO Confirm
        if (hand == InteractionHand.OFF_HAND) {
            mask |= 0x02;
        }

        if (isInRiptideSpinAttack) {
            mask |= 0x04;
        }


        metadata.add(new EntityData(8, EntityDataTypes.BYTE, mask));
        metadata.add(new EntityData(9, EntityDataTypes.FLOAT, health));
        metadata.add(new EntityData(10, EntityDataTypes.INT, potionEffectColor));
        metadata.add(new EntityData(11, EntityDataTypes.BOOLEAN, potionEffectAmbient));
        metadata.add(new EntityData(12, EntityDataTypes.INT, arrowInBodyCount));
        metadata.add(new EntityData(13, EntityDataTypes.INT, beeStingsInBodyCount));
        metadata.add(new EntityData(14, EntityDataTypes.OPTIONAL_BLOCK_POSITION, Optional.ofNullable(sleepingPosition)));
        return metadata;
    }

    @Override
    public void decode(List<EntityData> data) {
        super.decode(data);
        for (EntityData entityData : data) {
            switch (entityData.getIndex()) {
                case 8:
                    byte mask = (byte) entityData.getValue();
                    handActive = (mask & 0x01) != 0;
                    //TODO Test, look into
                    hand = (mask & 0x02) != 0 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
                    isInRiptideSpinAttack = (mask & 0x04) != 0;
                    break;
                case 9:
                    health = (float) entityData.getValue();
                    break;
                case 10:
                    potionEffectColor = (int) entityData.getValue();
                    break;
                case 11:
                    potionEffectAmbient = (boolean) entityData.getValue();
                    break;
                case 12:
                    arrowInBodyCount = (int) entityData.getValue();
                    break;
                case 13:
                    beeStingsInBodyCount = (int) entityData.getValue();
                    break;
                case 14:
                    sleepingPosition = ((Optional<Vector3i>) entityData.getValue()).orElse(null);
                    break;
            }
        }
    }

    public static class LivingEntityBuilder<T extends LivingEntityBuilder> extends EntityDataProvider.EntityBuilder<T> {
        public LivingEntityBuilder(LivingEntityDataProvider livingEntityDataProvider) {
            super(livingEntityDataProvider);
        }

        public T handActive(boolean handActive) {
            ((LivingEntityDataProvider) provider).setHandActive(handActive);
            return (T) this;
        }

        public T hand(InteractionHand hand) {
            ((LivingEntityDataProvider) provider).setHand(hand);
            return (T) this;
        }

        public T isInRiptideSpinAttack(boolean isInRiptideSpinAttack) {
            ((LivingEntityDataProvider) provider).setInRiptideSpinAttack(isInRiptideSpinAttack);
            return (T) this;
        }

        public T health(float health) {
            ((LivingEntityDataProvider) provider).setHealth(health);
            return (T) this;
        }

        public T potionEffectColor(int potionEffectColor) {
            ((LivingEntityDataProvider) provider).setPotionEffectColor(potionEffectColor);
            return (T) this;
        }

        public T potionEffectAmbient(boolean potionEffectAmbient) {
            ((LivingEntityDataProvider) provider).setPotionEffectAmbient(potionEffectAmbient);
            return (T) this;
        }

        public T arrowInBodyCount(int arrowInBodyCount) {
            ((LivingEntityDataProvider) provider).setArrowInBodyCount(arrowInBodyCount);
            return (T) this;
        }

        public T beeStingsInBodyCount(int beeStingsInBodyCount) {
            ((LivingEntityDataProvider) provider).setBeeStingsInBodyCount(beeStingsInBodyCount);
            return (T) this;
        }

        public T sleepingPosition(@Nullable Vector3i sleepingPosition) {
            ((LivingEntityDataProvider) provider).setSleepingPosition(sleepingPosition);
            return (T) this;
        }
    }
}
