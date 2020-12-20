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
import org.bukkit.inventory.ItemStack;

final class WrappedPacketInBlockPlace_1_7_10 extends WrappedPacket {
    public int x, y, z;
    public ItemStack itemStack;
    public int face;

    WrappedPacketInBlockPlace_1_7_10(final NMSPacket packet) {
        super(packet);
        final net.minecraft.server.v1_7_R4.PacketPlayInBlockPlace blockPlace =
                (net.minecraft.server.v1_7_R4.PacketPlayInBlockPlace) packet.getRawNMSPacket();

        x = blockPlace.c();
        y = blockPlace.d();
        z = blockPlace.e();

        this.face = blockPlace.d();

        net.minecraft.server.v1_7_R4.ItemStack stack = blockPlace.getItemStack();
        this.itemStack = org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack.asBukkitCopy(stack);
    }
}
