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

package io.github.retrooper.packetevents.packetwrappers.play.in.windowclick;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

public class WrappedPacketInWindowClick extends WrappedPacket {
    private static boolean v_1_17;
    private static Class<? extends Enum<?>> invClickTypeClass;
    private static boolean isClickModePrimitive;


    public WrappedPacketInWindowClick(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        v_1_17 = version.isNewerThanOrEquals(ServerVersion.v_1_17);
        invClickTypeClass = NMSUtils.getNMSEnumClassWithoutException("InventoryClickType");
        if (invClickTypeClass == null) {
            invClickTypeClass = NMSUtils.getNMEnumClassWithoutException("world.inventory.InventoryClickType");
        }
        isClickModePrimitive = Reflection.getField(PacketTypeClasses.Play.Client.WINDOW_CLICK, int.class, 3) != null;
    }

    public int getWindowId() {
        return readInt(0);
    }

    public void setWindowId(int windowID) {
        writeInt(0, windowID);
    }

    public int getWindowSlot() {
        return readInt(1);
    }

    public void setWindowSlot(int slot) {
        writeInt(1, slot);
    }

    public int getWindowButton() {
        return readInt(2);
    }

    public void setWindowButton(int button) {
        writeInt(2, button);
    }

    public Optional<Short> getActionNumber() {
        if (v_1_17) {
            return Optional.empty();
        }
        return Optional.of(readShort(0));
    }

    public void setActionNumber(short actionNumber) {
        if (!v_1_17) {
            writeShort(0, actionNumber);
        }
    }

    public int getMode() {
        if (isClickModePrimitive) {
            return readInt(3);
        } else {
            Enum<?> enumConst = readEnumConstant(0, invClickTypeClass);
            return enumConst.ordinal();
        }
    }

    public void setMode(int mode) {
        if (isClickModePrimitive) {
            writeInt(3, mode);
        } else {
            Enum<?> enumConst = EnumUtil.valueByIndex(invClickTypeClass, mode);
            writeEnumConstant(0, enumConst);
        }
    }

    /**
     * Get the clicked item.
     *
     * @return Get Clicked ItemStack
     */
    public ItemStack getClickedItemStack() {
        return readItemStack(0);
    }

    public void setClickedItemStack(ItemStack stack) {
       writeItemStack(0, stack);
    }
}
