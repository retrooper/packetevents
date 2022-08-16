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
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientUseItem extends PacketWrapper<WrapperPlayClientUseItem> {
    private InteractionHand hand;
    private int sequence;

    public WrapperPlayClientUseItem(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientUseItem(InteractionHand hand) {
        super(PacketType.Play.Client.USE_ITEM);
        this.hand = hand;
    }

    @Override
    public void read() {
        hand = InteractionHand.getById(readVarInt());
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            sequence = readVarInt();
        }
    }

    @Override
    public void write() {
        writeVarInt(hand.getId());
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            writeVarInt(sequence);
        }
    }

    @Override
    public void copy(WrapperPlayClientUseItem packet) {
        this.hand = packet.hand;
        this.sequence = packet.sequence;
    }

    public InteractionHand getHand() {
        return hand;
    }

    public void setHand(InteractionHand hand) {
        this.hand = hand;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
