/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2016-2021 retrooper and contributors
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

package io.github.retrooper.packetevents.packetwrappers.play.out.setslot;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;

/**
 * @author AoElite
 * @since 1.8
 */
public class WrappedPacketOutSetSlot extends WrappedPacket implements SendableWrapper {

    private static Constructor<?> packetConstructor;

    private int windowID;
    private int slot;
    private ItemStack itemStack;

    public WrappedPacketOutSetSlot(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutSetSlot(int windowID, int slot, ItemStack itemstack) {
        this.windowID = windowID;
        this.slot = slot;
        this.itemStack = itemstack;
    }


    @Override
    protected void load() {
        Class<?> packetClass = PacketTypeClasses.Play.Server.SET_SLOT;
        try {
            packetConstructor = packetClass.getConstructor(int.class, int.class, NMSUtils.nmsItemStackClass);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    public int getWindowId() {
        if (packet != null) {
            return readInt(0);
        } else {
            return windowID;
        }
    }

    public void setWindowId(int windowID) {
        if (packet != null) {
            writeInt(0, windowID);
        } else {
            this.windowID = windowID;
        }
    }

    public int getSlot() {
        if (packet != null) {
            return readInt(1);
        } else {
            return slot;
        }
    }

    public void setSlot(int slot) {
        if (packet != null) {
            writeInt(1, slot);
        } else {
            this.slot = slot;
        }
    }

    public ItemStack getItemStack() {
        if (packet != null) {
            return readItemStack(0);
        } else {
            return itemStack;
        }
    }

    public void setItemStack(ItemStack itemStack) {
        if (packet != null) {
            writeItemStack(0, itemStack);
        } else {
            this.itemStack = itemStack;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        return packetConstructor.newInstance(getWindowId(), getSlot(), NMSUtils.toNMSItemStack(getItemStack()));
    }
}
