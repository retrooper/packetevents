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
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LivingEntityDataProvider extends EntityDataProvider{
    private boolean handActive;
    private InteractionHand hand;
    private boolean isInRiptideSpinAttack;
    //TODO Finish
    public LivingEntityDataProvider(@Nullable BaseComponent customName, boolean customNameVisible, EntityPose pose,
                                    int airTicks, int ticksFrozenInPowderedSnow, boolean onFire, boolean crouching,
                                    boolean riding, boolean sprinting, boolean swimming, boolean invisible, boolean glowing,
                                    boolean flyingWithElytra, boolean silent, boolean hasGravity, boolean handActive, InteractionHand hand, boolean isInRiptideSpinAttack) {
        super(customName, customNameVisible, pose, airTicks, ticksFrozenInPowderedSnow, onFire, crouching,
                riding, sprinting, swimming, invisible, glowing, flyingWithElytra, silent, hasGravity);
        this.handActive = handActive;
        this.hand = hand;
        this.isInRiptideSpinAttack = isInRiptideSpinAttack;
    }

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
        return metadata;
    }

    @Override
    public void decode(List<EntityData> data) {
        super.decode(data);
        //more code
    }
}
