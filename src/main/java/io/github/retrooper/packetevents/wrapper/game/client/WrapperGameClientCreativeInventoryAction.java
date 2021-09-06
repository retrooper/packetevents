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
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperGameClientCreativeInventoryAction extends PacketWrapper<WrapperGameClientCreativeInventoryAction> {
    private int slot;

    public WrapperGameClientCreativeInventoryAction(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperGameClientCreativeInventoryAction(ClientVersion clientVersion) {
        super(PacketType.Game.Client.CREATIVE_INVENTORY_ACTION.getPacketID(clientVersion), clientVersion);
    }

    @Override
    public void readData() {
        this.slot = readShort();
        net.minecraft.server.v1_7_R4.PacketPlayInSetCreativeSlot cs0;
        net.minecraft.server.v1_8_R3.PacketPlayInSetCreativeSlot cs1;
        net.minecraft.server.v1_9_R1.PacketPlayInSetCreativeSlot cs2;
        net.minecraft.server.v1_10_R1.PacketPlayInSetCreativeSlot cs3;
        net.minecraft.server.v1_11_R1.PacketPlayInSetCreativeSlot cs4;
        net.minecraft.server.v1_12_R1.PacketPlayInSetCreativeSlot cs5;
        net.minecraft.server.v1_13_R1.PacketPlayInSetCreativeSlot cs6;
        net.minecraft.server.v1_14_R1.PacketPlayInSetCreativeSlot cs7;
        net.minecraft.server.v1_15_R1.PacketPlayInSetCreativeSlot cs8;
        net.minecraft.server.v1_16_R1.PacketPlayInSetCreativeSlot cs9;
        net.minecraft.network.protocol.game.PacketPlayInSetCreativeSlot cs10;
    }

    @Override
    public void readData(WrapperGameClientCreativeInventoryAction wrapper) {
        this.slot = wrapper.slot;
    }

    @Override
    public void writeData() {
        writeShort(slot);
    }
}
