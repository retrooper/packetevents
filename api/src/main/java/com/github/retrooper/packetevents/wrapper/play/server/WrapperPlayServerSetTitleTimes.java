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
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerSetTitleTimes extends PacketWrapper<WrapperPlayServerSetTitleTimes> {
    private int fadeInTicks;
    private int stayTicks;
    private int fadeOutTicks;

    public WrapperPlayServerSetTitleTimes(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSetTitleTimes(int fadeInTicks, int stayTicks, int fadeOutTicks) {
        super(PacketType.Play.Server.SET_TITLE_TIMES);
        this.fadeInTicks = fadeInTicks;
        this.stayTicks = stayTicks;
        this.fadeOutTicks = fadeOutTicks;
    }

    @Override
    public void read() {
        fadeInTicks = readInt();
        stayTicks = readInt();
        fadeOutTicks = readInt();
    }

    @Override
    public void write() {
        writeInt(fadeInTicks);
        writeInt(stayTicks);
        writeInt(fadeOutTicks);
    }

    @Override
    public void copy(WrapperPlayServerSetTitleTimes wrapper) {
        fadeInTicks = wrapper.fadeInTicks;
        stayTicks = wrapper.stayTicks;
        fadeOutTicks = wrapper.fadeOutTicks;
    }

    public int getFadeInTicks() {
        return fadeInTicks;
    }

    public void setFadeInTicks(int fadeInTicks) {
        this.fadeInTicks = fadeInTicks;
    }

    public int getStayTicks() {
        return stayTicks;
    }

    public void setStayTicks(int stayTicks) {
        this.stayTicks = stayTicks;
    }

    public int getFadeOutTicks() {
        return fadeOutTicks;
    }

    public void setFadeOutTicks(int fadeOutTicks) {
        this.fadeOutTicks = fadeOutTicks;
    }
}
