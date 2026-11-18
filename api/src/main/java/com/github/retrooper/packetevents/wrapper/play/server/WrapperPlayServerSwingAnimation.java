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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemSwingAnimation;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * Mojang name: ClientboundSwingAnimationPacket
 *
 * @versions 26.3+
 */
public class WrapperPlayServerSwingAnimation extends PacketWrapper<WrapperPlayServerSwingAnimation> {

    private int entityId;
    private InteractionHand hand;
    private ItemSwingAnimation animation;

    public WrapperPlayServerSwingAnimation(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSwingAnimation(int entityId, InteractionHand hand, ItemSwingAnimation animation) {
        super(PacketType.Play.Server.SWING_ANIMATION);
        this.entityId = entityId;
        this.hand = hand;
        this.animation = animation;
    }

    @Override
    public void read() {
        this.entityId = this.readVarInt();
        this.hand = this.readEnum(InteractionHand.class);
        this.animation = ItemSwingAnimation.read(this);
    }

    @Override
    public void write() {
        this.writeVarInt(this.entityId);
        this.writeEnum(this.hand);
        ItemSwingAnimation.write(this, this.animation);
    }

    @Override
    public void copy(WrapperPlayServerSwingAnimation wrapper) {
        this.entityId = wrapper.entityId;
        this.hand = wrapper.hand;
        this.animation = wrapper.animation;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public InteractionHand getHand() {
        return this.hand;
    }

    public void setHand(InteractionHand hand) {
        this.hand = hand;
    }

    public ItemSwingAnimation getAnimation() {
        return this.animation;
    }

    public void setAnimation(ItemSwingAnimation animation) {
        this.animation = animation;
    }
}
