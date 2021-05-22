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

package io.github.retrooper.packetevents.packetwrappers.play.in.updatesign;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.vector.Vector3i;

import java.lang.reflect.InvocationTargetException;

public class WrappedPacketInUpdateSign extends WrappedPacket {
    private static boolean v_1_7_mode, strArrayMode;
    private static Class<?> blockPosClass;

    public WrappedPacketInUpdateSign(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        v_1_7_mode = Reflection.getField(PacketTypeClasses.Play.Client.UPDATE_SIGN, int.class, 0) != null;
        strArrayMode = Reflection.getField(PacketTypeClasses.Play.Client.UPDATE_SIGN, String[].class, 0) != null;
        if (!v_1_7_mode) {
            try {
                blockPosClass = NMSUtils.getNMSClass("BlockPosition");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public Vector3i getBlockPosition() {
        int x = 0;
        int y = 0;
        int z = 0;
        if (v_1_7_mode) {
            x = readInt(0);
            y = readInt(1);
            z = readInt(2);

        } else {
            Object blockPosObj = readObject(0, blockPosClass);
            try {
                x = (int) NMSUtils.getBlockPosX.invoke(blockPosObj);
                y = (int) NMSUtils.getBlockPosY.invoke(blockPosObj);
                z = (int) NMSUtils.getBlockPosZ.invoke(blockPosObj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return new Vector3i(x, y, z);
    }

    public void setBlockPosition(Vector3i blockPos) {
        if (v_1_7_mode) {
            writeInt(0, blockPos.x);
            writeInt(1, blockPos.y);
            writeInt(2, blockPos.z);
        } else {
            Object blockPosObj = NMSUtils.generateNMSBlockPos(blockPos.x, blockPos.y, blockPos.z);
            write(NMSUtils.blockPosClass, 0, blockPosObj);
        }
    }

    public String[] getTextLines() {
        if (strArrayMode) {
            //1.7.10
            return readStringArray(0);
        } else {
            //1.8 and above
            Object[] iChatComponents = (Object[]) readAnyObject(1);
            return NMSUtils.readIChatBaseComponents(iChatComponents);
        }
    }

    public void setTextLines(String[] lines) {
        if (strArrayMode) {
            writeStringArray(0, lines);
        } else {
            Object[] iChatComponents = NMSUtils.generateIChatBaseComponents(lines);
            writeAnyObject(1, iChatComponents);
        }
    }
}
