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
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public final class WrappedPacketInBlockPlace extends WrappedPacket {
    private static boolean isHigherThan_v_1_8_8, isHigherThan_v_1_7_10;
    private Vector3i blockPosition;
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
        //1.7.10
        Vector3i position = null;
        ItemStack itemStack = null;
        try {
            if (isHigherThan_v_1_8_8) {
                final WrappedPacketInBlockPlace_1_9 blockPlace_1_9 = new WrappedPacketInBlockPlace_1_9(getPlayer(), packet);
                final Block block = blockPlace_1_9.getBlock();
                position = new Vector3i(block.getX(), block.getY(), block.getZ());
                itemStack = new ItemStack(block.getType());
            } else if (isHigherThan_v_1_7_10) {
                final WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(packet);
                position = blockPlace_1_8.getBlockPosition();
                itemStack = blockPlace_1_8.getItemStack();
            } else {
                final WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(getPlayer(), packet);
                position = blockPlace_1_7_10.getBlockPosition();
                itemStack = blockPlace_1_7_10.getItemStack();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        this.blockPosition = position;
        this.itemStack = itemStack;
    }

    /**
     * Get the player that placed the block
     * @return Block placer
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get X position of the block
     * @return Block Position X
     */
    public int getBlockPositionX() {
        return blockPosition.x;
    }

    /**
     * Get Y position of the block
     * @return Block Position Y
     */
    public int getBlockPositionY() {
        return blockPosition.y;
    }

    /**
     * Get Z position of the block
     * @return Block Position Z
     */
    public int getBlockPositionZ() {
        return blockPosition.z;
    }

    /**
     * Use {@link #getBlockPositionX()}, {@link #getBlockPositionY()}, {@link #getBlockPositionZ()}
     * @return Block Position
     */
    @Deprecated
    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    /**
     * The ItemStack of the placed block.
     * @return ItemStack
     */
    @Nullable
    public ItemStack getItemStack() {
        return itemStack;
    }

}
