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
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.player.HumanoidArm;
import com.github.retrooper.packetevents.protocol.player.SkinSection;

import java.util.List;

public class PlayerDataProvider extends LivingEntityDataProvider {
    private float additionalHealth;
    private int score;
    private byte skinPartsMask = SkinSection.ALL.getMask();
    private HumanoidArm mainArm = HumanoidArm.RIGHT;
    private NBTCompound leftShoulderNBT = new NBTCompound();
    private NBTCompound rightShoulderNBT = new NBTCompound();

    public float getAdditionalHealth() {
        return additionalHealth;
    }

    public void setAdditionalHealth(float additionalHealth) {
        this.additionalHealth = additionalHealth;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public byte getSkinPartsMask() {
        return skinPartsMask;
    }

    public void setSkinPartsMask(byte skinPartsMask) {
        this.skinPartsMask = skinPartsMask;
    }

    public HumanoidArm getMainArm() {
        return mainArm;
    }

    public void setMainArm(HumanoidArm mainArm) {
        this.mainArm = mainArm;
    }

    public NBTCompound getLeftShoulderNBT() {
        return leftShoulderNBT;
    }

    public void setLeftShoulderNBT(NBTCompound leftShoulderNBT) {
        this.leftShoulderNBT = leftShoulderNBT;
    }

    public NBTCompound getRightShoulderNBT() {
        return rightShoulderNBT;
    }

    public void setRightShoulderNBT(NBTCompound rightShoulderNBT) {
        this.rightShoulderNBT = rightShoulderNBT;
    }

    public static PlayerBuilder<PlayerBuilder> builderPlayer() {
        return new PlayerBuilder<>(new PlayerDataProvider());
    }

    @Override
    public List<EntityData> encode() {
        List<EntityData> metadata = super.encode();
        metadata.add(new EntityData(15, EntityDataTypes.FLOAT, additionalHealth));
        metadata.add(new EntityData(16, EntityDataTypes.INT, score));
        metadata.add(new EntityData(17, EntityDataTypes.BYTE, skinPartsMask));
        metadata.add(new EntityData(18, EntityDataTypes.BYTE, (byte) mainArm.ordinal()));
        metadata.add(new EntityData(19, EntityDataTypes.NBT, leftShoulderNBT));
        metadata.add(new EntityData(20, EntityDataTypes.NBT, rightShoulderNBT));
        return metadata;
    }

    @Override
    public void decode(List<EntityData> data) {
        super.decode(data);
        for (EntityData entityData : data) {
            switch (entityData.getIndex()) {
                case 15:
                    additionalHealth = (float) entityData.getValue();
                    break;
                case 16:
                    score = (int) entityData.getValue();
                    break;
                case 17:
                    skinPartsMask = (byte) entityData.getValue();
                    break;
                case 18:
                    mainArm = HumanoidArm.VALUES[(byte) entityData.getValue()];
                    break;
                case 19:
                    leftShoulderNBT = (NBTCompound) entityData.getValue();
                    break;
                case 20:
                    rightShoulderNBT = (NBTCompound) entityData.getValue();
                    break;
            }
        }
    }

    public static class PlayerBuilder<T extends PlayerBuilder> extends LivingEntityDataProvider.LivingEntityBuilder<T> {
        public PlayerBuilder(PlayerDataProvider entityDataProvider) {
            super(entityDataProvider);
        }

        public T additionalHealth(float additionalHealth) {
            ((PlayerDataProvider)provider).setAdditionalHealth(additionalHealth);
            return (T) this;
        }

        public T score(int score) {
            ((PlayerDataProvider)provider).setScore(score);
            return (T) this;
        }

        public T skinPartsMask(byte skinPartsMask) {
            ((PlayerDataProvider)provider).setSkinPartsMask(skinPartsMask);
            return (T)this;
        }

        public T skinParts(SkinSection skinSections) {
            ((PlayerDataProvider)provider).setSkinPartsMask(skinSections.getMask());
            return (T)this;
        }

        public T mainArm(HumanoidArm mainArm) {
            ((PlayerDataProvider)provider).setMainArm(mainArm);
            return (T)this;
        }

        public T leftShoulderNBT(NBTCompound leftShoulderNBT) {
            ((PlayerDataProvider)provider).setLeftShoulderNBT(leftShoulderNBT);
            return (T)this;
        }

        public T rightShoulderNBT(NBTCompound rightShoulderNBT) {
            ((PlayerDataProvider)provider).setRightShoulderNBT(rightShoulderNBT);
            return (T)this;
        }
    }
}
