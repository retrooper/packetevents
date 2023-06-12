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
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.vector.Vector3i;

public class WrappedPacketInUpdateSign extends WrappedPacket {
    private static boolean v_1_7_mode, strArrayMode;

    public WrappedPacketInUpdateSign(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        v_1_7_mode = Reflection.getField(PacketTypeClasses.Play.Client.UPDATE_SIGN, int.class, 0) != null;
        strArrayMode = Reflection.getField(PacketTypeClasses.Play.Client.UPDATE_SIGN, String[].class, 0) != null;
    }

    public Vector3i getBlockPosition() {
        if (v_1_7_mode) {
            int x = readInt(0);
            int y = readInt(1);
            int z = readInt(2);
            return new Vector3i(x, y, z);
        } else {
            return readBlockPosition(0);
        }
    }

    public void setBlockPosition(Vector3i blockPos) {
        if (v_1_7_mode) {
            writeInt(0, blockPos.x);
            writeInt(1, blockPos.y);
            writeInt(2, blockPos.z);
        } else {
            writeBlockPosition(0, blockPos);
        }
    }

    public String[] getTextLines() {
        if (strArrayMode) {
            //1.7.10 and 1.17+
            return readStringArray(0);
        } else {
            //1.8 -> 1.16.5
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

    public boolean isFrontText() {
        if (version.isNewerThanOrEquals(ServerVersion.v_1_20)) {
            return readBoolean(0);
        }
        else {
            return true;
        }
    }

    public void setFrontText(boolean frontText) {
        if (version.isNewerThanOrEquals(ServerVersion.v_1_20)) {
            writeBoolean(0,frontText);
        }
    }
}
