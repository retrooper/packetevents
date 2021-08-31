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

package io.github.retrooper.packetevents.packetwrappers.play.out.windowitems;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
//TODO Test wrapper
public class WrappedPacketOutWindowItems extends WrappedPacket implements SendableWrapper {
    private static boolean v_1_17;
    private static Object nonNullListInstance;
    private static Class<?> nonNullListClass;
    private static Constructor<?> packetConstructor;
    private int windowID;
    private List<ItemStack> slotData;

    public WrappedPacketOutWindowItems(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutWindowItems(int windowID, List<ItemStack> slots) {
        this.windowID = windowID;
        this.slotData = slots;
    }

    @Override
    protected void load() {
        v_1_17 = version.isNewerThanOrEquals(ServerVersion.v_1_17);
        try {
            if (v_1_17) {
                nonNullListClass = NMSUtils.getNMClassWithoutException("core.NonNullList");
                Constructor<?> nonNullListConstructor = nonNullListClass.getConstructor();
                nonNullListConstructor.setAccessible(true);
                nonNullListInstance = nonNullListConstructor.newInstance();
                packetConstructor = PacketTypeClasses.Play.Server.WINDOW_ITEMS.getConstructor(int.class, nonNullListClass);
            }
            else {
                packetConstructor = PacketTypeClasses.Play.Server.WINDOW_ITEMS.getConstructor();
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
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

    public List<ItemStack> getSlots() {
        if (packet != null) {
            List<ItemStack> slots = new ArrayList<>();
            if (version.isNewerThan(ServerVersion.v_1_10_2)) {
                List<Object> nmsItemStacks = readList(0);
                for (Object nmsItemStack : nmsItemStacks) {
                    slots.add(NMSUtils.toBukkitItemStack(nmsItemStack));
                }
            } else {
                Object[] nmsItemStacks = (Object[]) readAnyObject(1);
                for (Object nmsItemStack : nmsItemStacks) {
                    slots.add(NMSUtils.toBukkitItemStack(nmsItemStack));
                }
            }
            return slots;
        }
        else {
            return slotData;
        }
    }

    public void setSlots(List<ItemStack> slots) {
        if (packet != null) {
            if (version.isNewerThan(ServerVersion.v_1_10_2)) {
                List<Object> nmsItemStacks = new ArrayList<>();
                for (ItemStack itemStack : slots) {
                    nmsItemStacks.add(NMSUtils.toNMSItemStack(itemStack));
                }
                writeList(0, nmsItemStacks);
            } else {
                Object[] nmsItemStacks = new Object[slots.size()];
                for (int i = 0; i < slots.size(); i++) {
                    nmsItemStacks[i] = NMSUtils.toNMSItemStack(slots.get(i));
                }
                writeAnyObject(1, nmsItemStacks);
            }
        }
        else {
            this.slotData = slots;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance;
        if (v_1_17) {
            packetInstance = packetConstructor.newInstance(getWindowId(), nonNullListInstance);
            WrappedPacketOutWindowItems wrappedPacketOutWindowItems = new WrappedPacketOutWindowItems(new NMSPacket(packetInstance));
            wrappedPacketOutWindowItems.setSlots(getSlots());
        }
        else {
            packetInstance = packetConstructor.newInstance();
            WrappedPacketOutWindowItems wrappedPacketOutWindowItems = new WrappedPacketOutWindowItems(new NMSPacket(packetInstance));
            wrappedPacketOutWindowItems.setWindowId(getWindowId());
            wrappedPacketOutWindowItems.setSlots(getSlots());
        }

        return packetInstance;
    }
}
