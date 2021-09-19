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

package io.github.retrooper.packetevents.wrapper.play.client;

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Optional;
//TODO Finish, 1.13 added the transactionid, but 1.12 seems to have more fields??? tf
public class WrapperPlayClientTabComplete extends PacketWrapper<WrapperPlayClientTabComplete> {
    private Optional<Integer> transactionID;
    private String text;

    public WrapperPlayClientTabComplete(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientTabComplete(Optional<Integer> transactionID, String text) {
        super(PacketType.Play.Client.TAB_COMPLETE.getID());
        this.transactionID = transactionID;
        this.text = text;
    }

    @Override
    public void readData() {

    }

    @Override
    public void readData(WrapperPlayClientTabComplete wrapper) {
        super.readData(wrapper);
    }

    @Override
    public void writeData() {
        super.writeData();
    }
}
