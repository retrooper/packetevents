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

package io.github.retrooper.packetevents.packetwrappers.out.blockchange;

import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;

class WrappedPacketOutBlockChange extends WrappedPacket {
    public WrappedPacketOutBlockChange(Object packet) {
        super(packet);
    }
//to-do load
    @Override
    protected void setup() {
        //1.7 - int, int, int, Block, int data
        //1.8+ - BlockPosition, IBlockData

        //Constructors
        //1.7 - int, int, int, World

        //1.8->~1.12.2(World, BlockPosition)

        //~1.13->~1.14(IBlockAccess, BlockPosition)
        //1.16+(BlockPosition, IBlockData)
        net.minecraft.server.v1_7_R4.PacketPlayOutBlockChange bc;
        net.minecraft.server.v1_8_R3.PacketPlayOutBlockChange bc1;
        net.minecraft.server.v1_9_R1.PacketPlayOutBlockChange bc2;
        net.minecraft.server.v1_12_R1.PacketPlayOutBlockChange bc3;
        net.minecraft.server.v1_13_R2.PacketPlayOutBlockChange bc4;
        net.minecraft.server.v1_16_R2.PacketPlayOutBlockChange bc5;
    }
}
