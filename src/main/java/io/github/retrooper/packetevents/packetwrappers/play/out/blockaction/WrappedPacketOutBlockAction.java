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

package io.github.retrooper.packetevents.packetwrappers.play.out.blockaction;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import org.bukkit.Material;

import java.lang.reflect.Constructor;

/**
 * This packet is used for a number of actions and animations performed by blocks, usually non-persistent.
 *
 * @author Tecnio
 * @see <a href="https://wiki.vg/Protocol#Block_Action"</a>
 */
public class WrappedPacketOutBlockAction extends WrappedPacket implements SendableWrapper {

    private Vector3i blockPos;
    private int action, actionParam;
    private Material material;

    private static Constructor<?> packetConstructor;

    public WrappedPacketOutBlockAction(final NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutBlockAction(final Vector3i blockPos, final int action, final int actionParam, final Material material) {
        this.blockPos = blockPos;
        this.action = action;
        this.actionParam = actionParam;
        this.material = material;
    }

    @Override
    protected void load() {
        try {
            packetConstructor = PacketTypeClasses.Play.Server.BLOCK_ACTION.getConstructors()[1];
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public Vector3i getBlockPosition() {
        if (packet != null) {
            final Vector3i blockPos = new Vector3i();

            final Object blockPosObj = readObject(0, NMSUtils.blockPosClass);

            try {
                blockPos.x = (int) NMSUtils.getBlockPosX.invoke(blockPosObj);
                blockPos.y = (int) NMSUtils.getBlockPosY.invoke(blockPosObj);
                blockPos.z = (int) NMSUtils.getBlockPosZ.invoke(blockPosObj);
            } catch (final Exception e) {
                e.printStackTrace();
            }

            return blockPos;
        } else {
            return this.blockPos;
        }
    }

    public void setBlockPosition(final Vector3i blockPos) {
        if (packet != null) {
            final Object nmsBlockPos = NMSUtils.generateNMSBlockPos(blockPos.x, blockPos.y, blockPos.z);
            write(NMSUtils.blockPosClass, 0, nmsBlockPos);
        } else {
            this.blockPos = blockPos;
        }
    }

    public int getActionId() {
        if (packet != null) {
            return readInt(0);
        } else {
            return this.action;
        }
    }

    public void setActionId(final int actionId) {
        if (packet != null) {
            writeInt(0, actionId);
        } else {
            this.action = actionId;
        }
    }

    public int getActionParam() {
        if (packet != null) {
            return readInt(1);
        } else {
            return this.actionParam;
        }
    }

    public void setActionParam(final int actionParam) {
        if (packet != null) {
            writeInt(1, actionParam);
        } else {
            this.actionParam = actionParam;
        }
    }

    public Material getBlockType() {
        if (packet != null) {
            return NMSUtils.getMaterialFromNMSBlock(readObject(0, NMSUtils.blockClass));
        } else {
            return this.material;
        }
    }

    public void setBlockType(final Material material) {
        if (packet != null) {
            final Object nmsBlock = NMSUtils.getNMSBlockFromMaterial(material);
            write(NMSUtils.blockClass, 0, nmsBlock);
        } else {
            this.material = material;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        final Object nmsBlockPos = NMSUtils.generateNMSBlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        final Object nmsBlock = NMSUtils.getNMSBlockFromMaterial(material);

        return packetConstructor.newInstance(
                nmsBlockPos, nmsBlock, action, actionParam
        );
    }
}
