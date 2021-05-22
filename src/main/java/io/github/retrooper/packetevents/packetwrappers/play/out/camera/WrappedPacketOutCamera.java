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

package io.github.retrooper.packetevents.packetwrappers.play.out.camera;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.lang.reflect.Constructor;

public class WrappedPacketOutCamera extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> packetConstructor;
    private int cameraID;

    public WrappedPacketOutCamera(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutCamera(int cameraID) {
        this.cameraID = cameraID;
    }

    @Override
    protected void load() {
        try {
            packetConstructor = PacketTypeClasses.Play.Server.CAMERA.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public int getCameraId() {
        if (packet != null) {
            return readInt(0);
        }
        return cameraID;
    }

    public void setCameraId(int cameraID) {
        if (packet != null) {
            writeInt(0, cameraID);
        } else {
            this.cameraID = cameraID;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance = packetConstructor.newInstance();
        WrappedPacket packetWrapper = new WrappedPacket(new NMSPacket(packetInstance));
        packetWrapper.writeInt(0, getCameraId());
        return packetInstance;
    }

    @Override
    public boolean isSupported() {
        return version.isNewerThan(ServerVersion.v_1_7_10);
    }
}
