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

package io.github.retrooper.packetevents.packetwrappers.in.blockplace;

import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public final class WrappedPacketInBlockPlace extends WrappedPacket {
    private static boolean isHigherThan_v_1_8_8, isHigherThan_v_1_7_10;
    private WrappedPacket cachedWrappedPacket;

    public WrappedPacketInBlockPlace(final Player player, final Object packet) {
        super(player, packet);
    }

    public static void load() {
        isHigherThan_v_1_8_8 = version.isHigherThan(ServerVersion.v_1_8_8);
        isHigherThan_v_1_7_10 = version.isHigherThan(ServerVersion.v_1_7_10);
        if (isHigherThan_v_1_7_10) {
            WrappedPacketInBlockPlace_1_8.load();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public int getX() {
        if (isHigherThan_v_1_8_8) {
            if (cachedWrappedPacket == null) {
                WrappedPacketInBlockPlace_1_9 blockPlace_1_9 = new WrappedPacketInBlockPlace_1_9(getPlayer(), packet);
                cachedWrappedPacket = blockPlace_1_9;
                return blockPlace_1_9.getBlock().getX();
            }
            return ((WrappedPacketInBlockPlace_1_9)cachedWrappedPacket).getBlock().getX();
        } else if (isHigherThan_v_1_7_10) {
           if(cachedWrappedPacket == null) {
               WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(packet);
               cachedWrappedPacket = blockPlace_1_8;
               return blockPlace_1_8.getX();
           }
        } else {
            WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(packet);
            return blockPlace_1_7_10.x;
        }
        return 0;
    }

    public int getY() {
        if (isHigherThan_v_1_8_8) {
            if (cachedWrappedPacket == null) {
                WrappedPacketInBlockPlace_1_9 blockPlace_1_9 = new WrappedPacketInBlockPlace_1_9(getPlayer(), packet);
                cachedWrappedPacket = blockPlace_1_9;
                return blockPlace_1_9.getBlock().getY();
            }
            return ((WrappedPacketInBlockPlace_1_9)cachedWrappedPacket).getBlock().getX();
        } else if (isHigherThan_v_1_7_10) {
            if(cachedWrappedPacket == null) {
                WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(packet);
                cachedWrappedPacket = blockPlace_1_8;
                return blockPlace_1_8.getY();
            }
        } else {
            WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(packet);
            return blockPlace_1_7_10.y;
        }
        return 0;
    }

    public int getZ() {
        if (isHigherThan_v_1_8_8) {
            if (cachedWrappedPacket == null) {
                WrappedPacketInBlockPlace_1_9 blockPlace_1_9 = new WrappedPacketInBlockPlace_1_9(getPlayer(), packet);
                cachedWrappedPacket = blockPlace_1_9;
                return blockPlace_1_9.getBlock().getZ();
            }
            return ((WrappedPacketInBlockPlace_1_9)cachedWrappedPacket).getBlock().getX();
        } else if (isHigherThan_v_1_7_10) {
            if(cachedWrappedPacket == null) {
                WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(packet);
                cachedWrappedPacket = blockPlace_1_8;
                return blockPlace_1_8.getZ();
            }
        } else {
            WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(packet);
            return blockPlace_1_7_10.z;
        }
        return 0;
    }

    public ItemStack getItemStack() {
        if (isHigherThan_v_1_8_8) {
            if (cachedWrappedPacket == null) {
                WrappedPacketInBlockPlace_1_9 blockPlace_1_9 = new WrappedPacketInBlockPlace_1_9(getPlayer(), packet);
                cachedWrappedPacket = blockPlace_1_9;
                return new ItemStack(blockPlace_1_9.getBlock().getType());
            }
            return new ItemStack(((WrappedPacketInBlockPlace_1_9)cachedWrappedPacket).getBlock().getType());
        } else if (isHigherThan_v_1_7_10) {
            if(cachedWrappedPacket == null) {
                WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(packet);
                cachedWrappedPacket = blockPlace_1_8;
                return blockPlace_1_8.getItemStack();
            }
        } else {
            WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(packet);
            return blockPlace_1_7_10.itemStack;
        }
        return null;
    }

}
