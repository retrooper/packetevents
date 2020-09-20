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

import io.github.retrooper.packetevents.annotations.Nullable;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public final class WrappedPacketInBlockPlace extends WrappedPacket {
    private static boolean isHigherThan_v_1_8_8, isHigherThan_v_1_7_10;
    private int x, y, z;
    private ItemStack itemStack;

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

    @Override
    protected void setup() {
        try {
            if (isHigherThan_v_1_8_8) {
                final WrappedPacketInBlockPlace_1_9 blockPlace_1_9 = new WrappedPacketInBlockPlace_1_9(getPlayer(), packet);
                final Block block = blockPlace_1_9.block;
                x = block.getX();
                y = block.getY();
                z = block.getZ();
                itemStack = new ItemStack(block.getType());
            } else if (isHigherThan_v_1_7_10) {
                final WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(packet);
                x = blockPlace_1_8.x;
                y = blockPlace_1_8.y;
                z = blockPlace_1_8.z;
                itemStack = blockPlace_1_8.itemStack;
            } else {
                final WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(getPlayer(), packet);
                x = blockPlace_1_7_10.x;
                y = blockPlace_1_7_10.y;
                z = blockPlace_1_7_10.z;
                itemStack = blockPlace_1_7_10.itemStack;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the player that placed the block
     *
     * @return Block placer
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get X position of the block
     *
     * @return Block Position X
     */
    public int getBlockPositionX() {
        return x;
    }

    /**
     * Get Y position of the block
     *
     * @return Block Position Y
     */
    public int getBlockPositionY() {
        return y;
    }

    /**
     * Get Z position of the block
     *
     * @return Block Position Z
     */
    public int getBlockPositionZ() {
        return z;
    }


    /**
     * The ItemStack of the placed block.
     *
     * @return ItemStack
     */
    @Nullable
    public ItemStack getItemStack() {
        return itemStack;
    }

}
