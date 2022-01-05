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
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.player.HumanoidArm;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.util.Vector3i;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlayerDataProvider extends LivingEntityDataProvider {
    private float additionalHealth;
    private int score;
    private byte skinPartsMask;
    private HumanoidArm mainArm;
    private NBTCompound leftShoulderNBT;
    private NBTCompound rightShoulderNBT;

    public PlayerDataProvider(@Nullable BaseComponent customName, boolean customNameVisible, EntityPose pose, int airTicks,
                              int ticksFrozenInPowderedSnow, boolean onFire, boolean crouching, boolean riding,
                              boolean sprinting, boolean swimming, boolean invisible, boolean glowing, boolean flyingWithElytra,
                              boolean silent, boolean hasGravity, boolean handActive, InteractionHand hand,
                              boolean isInRiptideSpinAttack, float health, int potionEffectColor,
                              boolean potionEffectAmbient, int arrowInBodyCount, int beeStingsInBodyCount,
                              @Nullable Vector3i sleepingPosition, float additionalHealth, int score,
                              byte skinPartsMask, HumanoidArm mainArm, NBTCompound leftShoulderNBT, NBTCompound rightShoulderNBT) {
        super(customName, customNameVisible, pose, airTicks, ticksFrozenInPowderedSnow, onFire,
                crouching, riding, sprinting, swimming, invisible, glowing, flyingWithElytra,
                silent, hasGravity, handActive, hand, isInRiptideSpinAttack, health, potionEffectColor,
                potionEffectAmbient, arrowInBodyCount, beeStingsInBodyCount, sleepingPosition);
        this.additionalHealth = additionalHealth;
        this.score = score;
        this.skinPartsMask = skinPartsMask;
        this.mainArm = mainArm;
        this.leftShoulderNBT = leftShoulderNBT;
        this.rightShoulderNBT = rightShoulderNBT;

    }

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

    @Override
    public List<EntityData> encode() {
        List<EntityData> metadata = super.encode();
        metadata.add(new EntityData(15, EntityDataTypes.FLOAT, additionalHealth));
        metadata.add(new EntityData(16, EntityDataTypes.INT, score));
        metadata.add(new EntityData(17, EntityDataTypes.BYTE, skinPartsMask));
        metadata.add(new EntityData(18, EntityDataTypes.BYTE, (byte)mainArm.ordinal()));
        metadata.add(new EntityData(19, EntityDataTypes.NBT, leftShoulderNBT));
        metadata.add(new EntityData(19, EntityDataTypes.NBT, rightShoulderNBT));
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
                    mainArm = HumanoidArm.values()[(byte) entityData.getValue()];
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
}
