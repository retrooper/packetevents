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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.MultiVersion;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.InteractAction;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * This packet is sent from the client to the server when the client attacks or right-clicks another entity (a player, minecart, etc).
 * The vanilla server discards this packet if the entity being attacked is not within a 4-unit radius of the player's position.
 * Please note that this packet is NOT sent whenever the client middle-clicks, the {@link WrapperPlayClientCreativeInventoryAction} packet is sent instead.
 */
public class WrapperPlayClientInteractEntity extends PacketWrapper<WrapperPlayClientInteractEntity> {
    private int entityId;
    private InteractAction interactAction;
    private @Nullable Vector3f target;
    private InteractionHand interactionHand;
    private boolean sneaking;

    public WrapperPlayClientInteractEntity(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientInteractEntity(int entityId, InteractAction interactAction, InteractionHand interactionHand,
                                           @Nullable Vector3f target, boolean sneaking) {
        super(PacketType.Play.Client.INTERACT_ENTITY);
        this.entityId = entityId;
        this.interactAction = interactAction;
        this.interactionHand = interactionHand;
        this.target = target;
        this.sneaking = sneaking;
    }

    @Override
    public void read() {
        this.entityId = readMultiVersional(MultiVersion.EQUALS, ServerVersion.V_1_7_10,
                PacketWrapper::readInt,
                PacketWrapper::readVarInt);

        // Haven't tested this method yet
        readMulti(MultiVersion.EQUALS, ServerVersion.V_1_7_10,
                wrapper -> {
                    this.entityId = wrapper.readInt();
                    this.interactAction = InteractAction.getById(wrapper.readByte());
                    this.interactionHand = InteractionHand.MAIN_HAND;
                }, wrapper -> {
                    this.entityId = wrapper.readVarInt();
                    this.interactAction = InteractAction.getById(wrapper.readVarInt());
                    if (interactAction == InteractAction.INTERACT_AT) {
                        float x = wrapper.readFloat();
                        float y = wrapper.readFloat();
                        float z = wrapper.readFloat();
                        this.target = new Vector3f(x, y, z);
                    }

                    if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)
                            && (interactAction == InteractAction.INTERACT || interactAction == InteractAction.INTERACT_AT)) {
                        this.interactionHand = InteractionHand.getById(wrapper.readVarInt());
                    } else {
                        this.interactionHand = InteractionHand.MAIN_HAND;
                    }

                    if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
                        this.sneaking = wrapper.readBoolean();
                    }
                });
    }

    @Override
    public void write() {
        writeMultiVersional(MultiVersion.EQUALS, ServerVersion.V_1_7_10, entityId,
                PacketWrapper::writeInt,
                PacketWrapper::writeVarInt);
        writeMultiVersional(MultiVersion.EQUALS, ServerVersion.V_1_7_10, interactAction.ordinal(),
                PacketWrapper::writeByte,
                (packetWrapper, integer) -> {
                    packetWrapper.writeVarInt(integer);
                    if (interactAction == InteractAction.INTERACT_AT) {
                        if (target == null) {
                            target = new Vector3f(0.0F, 0.0F, 0.0F);
                        }
                        Vector3f targetVec = target;
                        writeFloat(targetVec.x);
                        writeFloat(targetVec.y);
                        writeFloat(targetVec.z);
                    }

                    if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9) &&
                            (interactAction == InteractAction.INTERACT || interactAction == InteractAction.INTERACT_AT)) {
                        writeVarInt(interactionHand.getId());
                    }

                    if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
                        writeBoolean(sneaking);
                    }
                });
    }

    @Override
    public void copy(WrapperPlayClientInteractEntity wrapper) {
        this.entityId = wrapper.entityId;
        this.interactAction = wrapper.interactAction;
        this.target = wrapper.target;
        this.interactionHand = wrapper.interactionHand;
        this.sneaking = wrapper.sneaking;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public InteractAction getInteractAction() {
        return interactAction;
    }

    public void setInteractAction(InteractAction interactAction) {
        this.interactAction = interactAction;
    }

    public Optional<Vector3f> getTarget() {
        return Optional.ofNullable(target);
    }

    public void setTarget(@Nullable Vector3f target) {
        this.target = target;
    }

    public InteractionHand getInteractionHand() {
        return interactionHand;
    }

    public void setInteractionHand(InteractionHand interactionHand) {
        this.interactionHand = interactionHand;
    }

    public boolean isSneaking() {
        return sneaking;
    }

    public void setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
    }
}
