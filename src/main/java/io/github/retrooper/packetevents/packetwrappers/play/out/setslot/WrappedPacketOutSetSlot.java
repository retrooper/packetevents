/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.packetwrappers.play.out.setslot;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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


    public int getSlot() {
        if (packet != null) {
            return readInt(1);
        } else {
            return slot;
        }
    }

    public ItemStack getItemStack() {
        if (packet != null) {
            Object nmsItemStack = readObject(0, NMSUtils.nmsItemStackClass);
            return NMSUtils.toBukkitItemStack(nmsItemStack);
        } else {
            return itemStack;
        }
    }

    public void setWindowId(int windowID) {
        if (packet != null) {
            writeInt(0, windowID);
        }
        else {
            this.windowID = windowID;
        }
    }

    public void setSlot(int slot) {
        if (packet != null) {
            writeInt(1, slot);
        }
        else {
            this.slot = slot;
        }
    }

    public void setItemStack(ItemStack itemStack) {
        if (packet != null) {
            Object nmsItemStack = NMSUtils.toNMSItemStack(itemStack);
            writeObject(0, nmsItemStack);
        }
        else {
            this.itemStack = itemStack;
        }
    }

    @Override
    public Object asNMSPacket() {
        try {
            return packetConstructor.newInstance(getWindowId(), getSlot(), NMSUtils.toNMSItemStack(getItemStack()));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
