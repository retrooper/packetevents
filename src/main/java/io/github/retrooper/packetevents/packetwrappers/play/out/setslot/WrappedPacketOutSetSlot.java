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

package io.github.retrooper.packetevents.packetwrappers.play.out.setslot;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.util.Optional;

/**
 * @author AoElite
 * @since 1.8
 */
public class WrappedPacketOutSetSlot extends WrappedPacket implements SendableWrapper {
    private static boolean v_1_17, v_1_17_1;
    private static Constructor<?> packetConstructor;

    private int windowID;
    private int stateID;
    private int slot;
    private ItemStack itemStack;

    public WrappedPacketOutSetSlot(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutSetSlot(int windowID, int slot, ItemStack itemstack) {
        this.windowID = windowID;
        this.stateID = 0;
        this.slot = slot;
        this.itemStack = itemstack;
    }

    public WrappedPacketOutSetSlot(int windowID, int stateID, int slot, ItemStack itemstack) {
        this.windowID = windowID;
        this.stateID = stateID;
        this.slot = slot;
        this.itemStack = itemstack;
    }


    @Override
    protected void load() {
        v_1_17 = version.isNewerThanOrEquals(ServerVersion.v_1_17);
        v_1_17_1 = version.isNewerThanOrEquals(ServerVersion.v_1_17_1);
        try {
            if (v_1_17_1) {
                packetConstructor = PacketTypeClasses.Play.Server.SET_SLOT.getConstructor(int.class, int.class, int.class, NMSUtils.nmsItemStackClass);

            } else {
                packetConstructor = PacketTypeClasses.Play.Server.SET_SLOT.getConstructor(int.class, int.class, NMSUtils.nmsItemStackClass);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    public int getWindowId() {
        if (packet != null) {
            int index = v_1_17 ? 2 : 0;
            return readInt(index);
        } else {
            return windowID;
        }
    }

    public void setWindowId(int windowID) {
        if (packet != null) {
            int index = v_1_17 ? 2 : 0;
            writeInt(index, windowID);
        } else {
            this.windowID = windowID;
        }
    }

    public Optional<Integer> getStateId() {
        if (v_1_17_1) {
            if (packet != null) {
                return Optional.of(readInt(4));
            } else {
                return Optional.of(stateID);
            }
        } else {
            return Optional.empty();
        }
    }

    public void setStateId(int stateID) {
        if (v_1_17_1) {
            if (packet != null) {
                writeInt(4, stateID);
            } else {
                this.stateID = stateID;
            }
        }
    }

    public int getSlot() {
        if (packet != null) {
            int index = v_1_17_1 ? 4 : v_1_17 ? 3 : 1;
            return readInt(index);
        } else {
            return slot;
        }
    }

    public void setSlot(int slot) {
        if (packet != null) {
            int index = v_1_17_1 ? 4 : v_1_17 ? 3 : 1;
            writeInt(index, slot);
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
        if (v_1_17_1) {
            return packetConstructor.newInstance(getWindowId(), getStateId().get(), getSlot(), NMSUtils.toNMSItemStack(getItemStack()));
        }
        else {
            return packetConstructor.newInstance(getWindowId(), getSlot(), NMSUtils.toNMSItemStack(getItemStack()));
        }
    }
}
