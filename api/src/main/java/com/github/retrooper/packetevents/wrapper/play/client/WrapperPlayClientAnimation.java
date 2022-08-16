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
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * This packet is sent when the client swings their arm.
 */
public class WrapperPlayClientAnimation extends PacketWrapper<WrapperPlayClientAnimation> {
    private InteractionHand interactionHand;

    public WrapperPlayClientAnimation(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientAnimation(InteractionHand interactionHand) {
        super(PacketType.Play.Client.ANIMATION);
        this.interactionHand = interactionHand;
    }

    @Override
    public void read() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            this.interactionHand = InteractionHand.getById(readVarInt());
        } else {
            this.interactionHand = InteractionHand.MAIN_HAND;
        }
    }

    @Override
    public void copy(WrapperPlayClientAnimation wrapper) {
        this.interactionHand = wrapper.interactionHand;
    }

    @Override
    public void write() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            writeVarInt(interactionHand.getId());
        }
    }

    /**
     * Hand used for the animation.
     * On {@link ClientVersion#V_1_9}, an off-hand was introduced and specifies which arm has been swung.
     * For {@link ClientVersion#V_1_8} and {@link ClientVersion#V_1_7_10} clients only have a main hand.
     *
     * @return Hand
     */
    public InteractionHand getHand() {
        return interactionHand;
    }

    /**
     * Modify the hand used for the animation.
     * On {@link ClientVersion#V_1_9}, an off-hand was introduced and specifies which arm has been swung.
     * For {@link ClientVersion#V_1_8} and {@link ClientVersion#V_1_7_10} clients only have a main hand.
     * Modifying the hand on 1.8 and 1.7 clients is redundant.
     *
     * @param interactionHand Hand used for the animation
     */
    public void setHand(InteractionHand interactionHand) {
        this.interactionHand = interactionHand;
    }
}
