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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Optional;

/**
 * This packet is sent from the client to the server when the client attacks or right-clicks another entity (a player, minecart, etc).
 * The vanilla server discards this packet if the entity being attacked is not within a 4-unit radius of the player's position.
 * Please note that this packet is NOT sent whenever the client middle-clicks, the {@link WrapperPlayClientCreativeInventoryAction} packet is sent instead.
 */
public class WrapperPlayClientInteractEntity extends PacketWrapper<WrapperPlayClientInteractEntity> {
    private int entityID;
    private InteractAction interactAction;
    private Optional<Vector3f> target;
    private InteractionHand interactionHand;
    private Optional<Boolean> sneaking;

    public WrapperPlayClientInteractEntity(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientInteractEntity(int entityID, InteractAction interactAction, InteractionHand interactionHand, Optional<Vector3f> target, Optional<Boolean> sneaking) {
        super(PacketType.Play.Client.INTERACT_ENTITY);
        this.entityID = entityID;
        this.interactAction = interactAction;
        this.interactionHand = interactionHand;
        this.target = target;
        this.sneaking = sneaking;
    }

    @Override
    public void read() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            this.entityID = readInt();
            byte typeIndex = readByte();
            this.interactAction = InteractAction.VALUES[typeIndex];
            this.target = Optional.empty();
            this.interactionHand = InteractionHand.MAIN_HAND;
            this.sneaking = Optional.empty();
        } else {
            this.entityID = readVarInt();
            int typeIndex = readVarInt();
            this.interactAction = InteractAction.VALUES[typeIndex];
            if (interactAction == InteractAction.INTERACT_AT) {
                float x = readFloat();
                float y = readFloat();
                float z = readFloat();
                this.target = Optional.of(new Vector3f(x, y, z));
            } else {
                this.target = Optional.empty();
            }

            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9) && (interactAction == InteractAction.INTERACT || interactAction == InteractAction.INTERACT_AT)) {
                int handID = readVarInt();
                this.interactionHand = InteractionHand.getById(handID);
            } else {
                this.interactionHand = InteractionHand.MAIN_HAND;
            }

            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
                this.sneaking = Optional.of(readBoolean());
            } else {
                this.sneaking = Optional.empty();
            }
        }
    }

    @Override
    public void write() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            writeInt(entityID);
            writeByte(interactAction.ordinal());
        } else {
            writeVarInt(entityID);
            writeVarInt(interactAction.ordinal());
            if (interactAction == InteractAction.INTERACT_AT) {
                Vector3f targetVec = target.orElse(new Vector3f(0.0F, 0.0F, 0.0F));
                writeFloat(targetVec.x);
                writeFloat(targetVec.y);
                writeFloat(targetVec.z);
            }

            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9) && (interactAction == InteractAction.INTERACT || interactAction == InteractAction.INTERACT_AT)) {
                writeVarInt(interactionHand.getId());
            }

            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
                writeBoolean(sneaking.orElse(false));
            }
        }
    }

    @Override
    public void copy(WrapperPlayClientInteractEntity wrapper) {
        this.entityID = wrapper.entityID;
        this.interactAction = wrapper.interactAction;
        this.target = wrapper.target;
        this.interactionHand = wrapper.interactionHand;
        this.sneaking = wrapper.sneaking;
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityId(int entityID) {
        this.entityID = entityID;
    }

    public InteractAction getAction() {
        return interactAction;
    }

    public void setAction(InteractAction interactAction) {
        this.interactAction = interactAction;
    }

    public InteractionHand getHand() {
        return interactionHand;
    }

    public void setHand(InteractionHand interactionHand) {
        this.interactionHand = interactionHand;
    }

    public Optional<Vector3f> getTarget() {
        return target;
    }

    public void setTarget(Optional<Vector3f> target) {
        this.target = target;
    }

    public Optional<Boolean> isSneaking() {
        return sneaking;
    }

    public void setSneaking(Optional<Boolean> sneaking) {
        this.sneaking = sneaking;
    }

    public enum InteractAction {
        INTERACT, ATTACK, INTERACT_AT;
        public static final InteractAction[] VALUES = values();
    }
}
