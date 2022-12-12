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

package io.github.retrooper.packetevents.packetwrappers.play.out.entitymetadata;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.google.OptionalUtils;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

/**
 * @author SteelPhoenix, retrooper
 * @since 1.8
 */
public class WrappedWatchableObject extends WrappedPacket {
    private static int valueIndex = 2;
    private static Class<?> googleOptionalClass;

    public WrappedWatchableObject(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        if (version.isNewerThanOrEquals(ServerVersion.v_1_19_3)) {
            valueIndex = 2;
            try {
                googleOptionalClass = Class.forName("com.google.common.base.Optional");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if (version.isNewerThan(ServerVersion.v_1_8_8)) {
            valueIndex = 1;
            try {
                googleOptionalClass = Class.forName("com.google.common.base.Optional");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public int getIndex() {
        if (version.isNewerThan(ServerVersion.v_1_8_8) && version.isOlderThan(ServerVersion.v_1_19_3)) {
            Object dataWatcherObject = readAnyObject(0);
            WrappedPacket wrappedDataWatcher = new WrappedPacket(new NMSPacket(dataWatcherObject));
            return wrappedDataWatcher.readInt(0);

        } else {
            //1.19.3 changed back to this
            return readInt(0);
        }
    }

    public void setIndex(int index) {
        if (version.isNewerThan(ServerVersion.v_1_8_8) && version.isOlderThan(ServerVersion.v_1_19_3)) {
            Object dataWatcherObject = readAnyObject(0);
            WrappedPacket wrappedDataWatcher = new WrappedPacket(new NMSPacket(dataWatcherObject));
            wrappedDataWatcher.writeInt(0, index);
        } else {
            writeInt(0, index);
        }
    }

    public boolean isDirty() {
        return !version.isOlderThan(ServerVersion.v_1_19_3) || readBoolean(0);
    }

    public void setDirty(boolean dirty) {
        if (version.isOlderThan(ServerVersion.v_1_19_3)) {
            writeBoolean(0, dirty);
        }
    }

    public Object getRawValue() {
        return readAnyObject(valueIndex);
    }

    public void setRawValue(Object rawValue) {
        writeAnyObject(valueIndex, rawValue);
    }

    //TODO Finish get WrappedWatchableObject#getValue
    protected Object getValue() {
        Object rawValue = getRawValue();
        Class<?> rawType = rawValue.getClass();
        if (rawType.equals(googleOptionalClass)) {
            return OptionalUtils.convertToJavaOptional(rawValue);
        } else if (rawType.equals(NMSUtils.iChatBaseComponentClass)) {
            //TODO make wrapper for ichatbasecomponents
            return rawValue;
        } else if (rawType.equals(NMSUtils.nmsItemStackClass)) {
            return NMSUtils.toBukkitItemStack(rawValue);
        } else {
            //TODO the rest of the classes
            return rawValue;
        }
    }

    //TODO Finish WrappedWatchableObject#setValue
    protected void setValue(Object value) {

    }
}
