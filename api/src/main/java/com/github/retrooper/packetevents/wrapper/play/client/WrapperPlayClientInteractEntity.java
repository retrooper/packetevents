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
import com.github.retrooper.packetevents.protocol.util.LpVector3d;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.UnknownNullability;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

/**
 * This packet is sent from the client to the server when the client right-clicks another entity (a player, minecart, etc) or attacks an entity (for versions older than 26.1).
 * The vanilla server discards this packet if the entity being attacked is not within a 4-unit radius of the player's position.
 * Please note that this packet is NOT sent whenever the client middle-clicks, the {@link WrapperPlayClientCreativeInventoryAction} packet is sent instead.
 * <p>
 * Mojang name: ServerboundInteractPacket
 */
public class WrapperPlayClientInteractEntity extends PacketWrapper<WrapperPlayClientInteractEntity> {

    private int entityId;
    /**
     * @versions -1.21.5
     */
    private InteractAction action;
    private @Nullable Vector3d location;
    private InteractionHand hand;
    private boolean sneaking;

    public WrapperPlayClientInteractEntity(PacketReceiveEvent event) {
        super(event);
    }

    @Deprecated
    public WrapperPlayClientInteractEntity(
            int entityId, InteractAction action, InteractionHand hand,
            Optional<Vector3f> location, Optional<Boolean> sneaking
    ) {
        this(
                entityId, action,
                location.map(Vector3d::new).orElse(null),
                hand, sneaking.orElse(false)
        );
    }

    /**
     * @versions 26.1+
     */
    public WrapperPlayClientInteractEntity(
            int entityId, InteractionHand hand,
            Vector3d location, boolean sneaking
    ) {
        this(entityId, InteractAction.INTERACT_AT, location, hand, sneaking);
    }

    /**
     * @versions -1.21.11
     */
    @ApiStatus.Obsolete
    public WrapperPlayClientInteractEntity(
            int entityId, InteractAction action, @Nullable Vector3d location,
            InteractionHand hand, boolean sneaking
    ) {
        super(PacketType.Play.Client.INTERACT_ENTITY);
        this.entityId = entityId;
        this.action = action;
        this.location = location;
        this.hand = hand;
        this.sneaking = sneaking;
    }

    @Override
    public void read() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_26_1)) {
            this.entityId = this.readVarInt();
            this.hand = this.readEnum(InteractionHand.values());
            this.location = LpVector3d.read(this);
            this.sneaking = this.readBoolean();
            this.action = InteractAction.INTERACT_AT; // compat
        } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            this.entityId = this.readVarInt();
            this.action = this.readEnum(InteractAction.values());
            if (this.action == InteractAction.INTERACT_AT) {
                this.location = new Vector3d(Vector3f.read(this));
            } else {
                this.location = Vector3d.zero();
            }
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)
                    && (this.action == InteractAction.INTERACT || this.action == InteractAction.INTERACT_AT)) {
                this.hand = this.readEnum(InteractionHand.values());
            } else {
                this.hand = InteractionHand.MAIN_HAND;
            }
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
                this.sneaking = this.readBoolean();
            }
        } else {
            this.entityId = this.readInt();
            this.action = InteractAction.VALUES[this.readByte()];
            this.hand = InteractionHand.MAIN_HAND; // compat
            this.action = InteractAction.INTERACT; // compat
        }
    }

    @Override
    public void write() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_26_1)) {
            this.writeVarInt(this.entityId);
            this.writeEnum(this.hand);
            LpVector3d.write(this, this.location != null ? this.location : Vector3d.zero());
            this.writeBoolean(this.sneaking);
        } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            this.writeVarInt(this.entityId);
            this.writeEnum(this.action);
            if (this.action == InteractAction.INTERACT_AT) {
                Vector3f.write(this, new Vector3f(this.location != null ? this.location : Vector3d.zero()));
            }
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)
                    && (this.action == InteractAction.INTERACT || this.action == InteractAction.INTERACT_AT)) {
                this.writeEnum(this.hand);
            }
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
                this.writeBoolean(this.sneaking);
            }
        } else {
            this.writeInt(this.entityId);
            this.writeByte(this.action.ordinal());
        }
    }

    @Override
    public void copy(WrapperPlayClientInteractEntity wrapper) {
        this.entityId = wrapper.entityId;
        this.action = wrapper.action;
        this.location = wrapper.location;
        this.hand = wrapper.hand;
        this.sneaking = wrapper.sneaking;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public void setEntityId(int entityID) {
        this.entityId = entityID;
    }

    /**
     * @versions -1.21.11
     */
    @ApiStatus.Obsolete
    public InteractAction getAction() {
        return this.action;
    }

    /**
     * @versions -1.21.11
     */
    @ApiStatus.Obsolete
    public void setAction(InteractAction interactAction) {
        this.action = interactAction;
    }

    public InteractionHand getHand() {
        return this.hand;
    }

    public void setHand(InteractionHand interactionHand) {
        this.hand = interactionHand;
    }

    /**
     * Nullable for versions older than 26.1
     */
    public @UnknownNullability Vector3d getLocation() {
        return this.location;
    }

    /**
     * Nullable for versions older than 26.1
     */
    public void setLocation(@UnknownNullability Vector3d location) {
        this.location = location;
    }

    @Deprecated
    public Optional<Vector3f> getTarget() {
        return Optional.ofNullable(this.location).map(Vector3f::new);
    }

    @Deprecated
    public void setTarget(Optional<Vector3f> location) {
        this.location = location.map(Vector3d::new).orElse(null);
    }

    /**
     * @versions 1.16+
     */
    public Optional<Boolean> isSneaking() {
        return Optional.of(this.sneaking);
    }

    /**
     * @versions 1.16+
     */
    public void setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
    }

    /**
     * @versions 1.16+
     */
    @Deprecated
    public void setSneaking(Optional<Boolean> sneaking) {
        this.sneaking = sneaking.orElse(false);
    }

    /**
     * @versions -1.21.11
     */
    @ApiStatus.Obsolete
    public enum InteractAction {

        INTERACT,
        ATTACK,
        INTERACT_AT,
        ;

        @Deprecated
        public static final InteractAction[] VALUES = values();
    }
}
