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

package io.github.retrooper.packetevents.wrapper.game.client;

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.manager.player.Hand;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * This packet is sent when the client swings their arm.
 */
public class WrapperGameClientAnimation extends PacketWrapper<WrapperGameClientAnimation> {
    private Hand hand;

    public WrapperGameClientAnimation(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperGameClientAnimation(ClientVersion clientVersion, Hand hand) {
        super(PacketType.Game.Client.ANIMATION.getPacketID(clientVersion), clientVersion);
        this.hand = hand;
    }

    @Override
    public void readData() {
        if (clientVersion.isNewerThanOrEquals(ClientVersion.v_1_9)) {
            this.hand = Hand.VALUES[readVarInt()];
        } else {
            //Default to main hand, because 1.8 and 1.7.10 don't have an off-hand anyway.
            this.hand = Hand.MAIN_HAND;
        }
    }

    @Override
    public void readData(WrapperGameClientAnimation wrapper) {
        this.hand = wrapper.hand;
    }

    @Override
    public void writeData() {
        if (clientVersion.isNewerThanOrEquals(ClientVersion.v_1_9)) {
            writeVarInt(hand.ordinal());
        }
    }

    /**
     * Hand used for the animation.
     * On {@link ClientVersion#v_1_9}, an off-hand was introduced and specifies which arm has been swung.
     * For {@link ClientVersion#v_1_8} and {@link ClientVersion#v_1_7_10} clients only have a main hand.
     *
     * @return Hand
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Modify the hand used for the animation.
     * On {@link ClientVersion#v_1_9}, an off-hand was introduced and specifies which arm has been swung.
     * For {@link ClientVersion#v_1_8} and {@link ClientVersion#v_1_7_10} clients only have a main hand.
     * Modifying the hand on 1.8 and 1.7 clients is redundant.
     *
     * @param hand Hand used for the animation
     */
    public void setHand(Hand hand) {
        this.hand = hand;
    }
}
