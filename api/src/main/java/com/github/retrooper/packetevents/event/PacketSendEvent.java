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

package com.github.retrooper.packetevents.event;

import com.github.retrooper.packetevents.event.simple.PacketHandshakeSendEvent;
import com.github.retrooper.packetevents.exception.PacketProcessException;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.User;

import java.util.ArrayList;
import java.util.List;

public class PacketSendEvent extends ProtocolPacketEvent {
    private List<Runnable> tasksAfterSend = null;

    protected PacketSendEvent(Object channel, User user, Object player, Object rawByteBuf,
                              boolean autoProtocolTranslation) throws PacketProcessException {
        super(PacketSide.SERVER, channel, user, player, rawByteBuf, autoProtocolTranslation);
    }

    protected PacketSendEvent(int packetID, PacketTypeCommon packetType,
                              ServerVersion serverVersion,
                              Object channel, User user,
                              Object player, Object byteBuf) throws PacketProcessException {
        super(packetID, packetType,
                serverVersion,
                channel, user, player, byteBuf);
    }

    @Override
    public void call(PacketListenerCommon listener) {
        listener.onPacketSend(this);
    }


    public List<Runnable> getTasksAfterSend() {
        if (tasksAfterSend == null) {
            tasksAfterSend = new ArrayList<>();
        }
        return tasksAfterSend;
    }

    public boolean hasTasksAfterSend() {
        return tasksAfterSend != null && !tasksAfterSend.isEmpty();
    }

    @Override
    public PacketSendEvent clone() {
        try {
            Object clonedBuffer = ByteBufHelper.retainedDuplicate(getByteBuf());
            return new PacketSendEvent(getPacketId(), getPacketType(), getServerVersion(),
                    getChannel(), getUser(), getPlayer(), clonedBuffer);
        } catch (PacketProcessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
