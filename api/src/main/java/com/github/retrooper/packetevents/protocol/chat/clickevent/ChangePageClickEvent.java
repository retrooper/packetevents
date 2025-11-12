/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.chat.clickevent;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ChangePageClickEvent implements ClickEvent {

    private final int page;

    @ApiStatus.Obsolete
    public ChangePageClickEvent(String page) {
        this(Integer.parseInt(page));
    }

    public ChangePageClickEvent(int page) {
        this.page = page;
    }

    public static ChangePageClickEvent decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        int page = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)
                ? compound.getNumberTagValueOrThrow("page").intValue()
                : Integer.parseInt(compound.getStringTagValueOrThrow("value"));
        return new ChangePageClickEvent(page);
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, ChangePageClickEvent clickEvent) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
            compound.setTag("page", new NBTInt(clickEvent.page));
        } else {
            compound.setTag("value", new NBTString(Integer.toString(clickEvent.page)));
        }
    }

    @Override
    public ClickEventAction<?> getAction() {
        return ClickEventActions.CHANGE_PAGE;
    }

    @Override
    public net.kyori.adventure.text.event.ClickEvent asAdventure() {
        return net.kyori.adventure.text.event.ClickEvent.changePage(this.page);
    }

    public int getPage() {
        return this.page;
    }
}
