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

package io.github.retrooper.packetevents.packetwrappers.play.in.blockplace;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.vector.Vector3f;
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import org.bukkit.inventory.ItemStack;

final class WrappedPacketInBlockPlace_1_7_10 extends WrappedPacket {
    private final net.minecraft.server.v1_7_R4.PacketPlayInBlockPlace blockPlace;

    WrappedPacketInBlockPlace_1_7_10(final NMSPacket packet) {
        super(packet);
        blockPlace =
                (net.minecraft.server.v1_7_R4.PacketPlayInBlockPlace) packet.getRawNMSPacket();
    }

    public Vector3i getBlockPosition() {
        return new Vector3i(blockPlace.c(), blockPlace.d(), blockPlace.e());
    }

    public void setBlockPosition(Vector3i blockPos) {
        Vector3i currentBlockPos = getBlockPosition();
        if (blockPos.x != currentBlockPos.x) {
            writeInt(0, blockPos.x);
        }
        if (blockPos.y != currentBlockPos.y) {
            writeInt(1, blockPos.y);
        }
        if (blockPos.z != currentBlockPos.z) {
            writeInt(2, blockPos.z);
        }
    }

    public int getFace() {
        return blockPlace.getFace();
    }

    public void setFace(int face) {
        writeInt(3, face);
    }

    public ItemStack getItemStack() {
        return org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack.asBukkitCopy(blockPlace.getItemStack());
    }

    public void setItemStack(ItemStack stack) {
        Object nmsItemStack = org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack.asNMSCopy(stack);
        writeObject(0, nmsItemStack);
    }

    public Vector3f getCursorPosition() {
        return new Vector3f(blockPlace.h(), blockPlace.i(), blockPlace.j());
    }

    public void setCursorPosition(Vector3f cursorPos) {
        Vector3f currentCursorPos = getCursorPosition();
        if (cursorPos.x != currentCursorPos.x) {
            writeFloat(0, cursorPos.x);
        }
        if (cursorPos.y != currentCursorPos.y) {
            writeFloat(1, cursorPos.y);
        }
        if (cursorPos.z != currentCursorPos.z) {
            writeFloat(2, cursorPos.z);
        }
    }
}
