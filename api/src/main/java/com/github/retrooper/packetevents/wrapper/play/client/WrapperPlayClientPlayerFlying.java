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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientPlayerFlying extends PacketWrapper<WrapperPlayClientPlayerFlying> {
    private boolean positionChanged;
    private boolean rotationChanged;
    private Location location;
    private boolean onGround;

    public WrapperPlayClientPlayerFlying(PacketReceiveEvent event) {
        super(event, false);
        positionChanged = event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION ||
                event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION;
        rotationChanged = event.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION ||
                event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION;
        readEvent(event);
    }

    public WrapperPlayClientPlayerFlying(boolean positionChanged, boolean rotationChanged, boolean onGround, Location location) {
        super((positionChanged && rotationChanged) ? PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION :
                (positionChanged ? PacketType.Play.Client.PLAYER_POSITION : rotationChanged ? PacketType.Play.Client.PLAYER_ROTATION
                        : PacketType.Play.Client.PLAYER_FLYING));
        this.positionChanged = positionChanged;
        this.rotationChanged = rotationChanged;
        this.onGround = onGround;
        this.location = location;
    }

    public static boolean isFlying(PacketTypeCommon type) {
        return type == PacketType.Play.Client.PLAYER_FLYING
                || type == PacketType.Play.Client.PLAYER_POSITION
                || type == PacketType.Play.Client.PLAYER_ROTATION
                || type == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION;
    }

    @Override
    public void readData() {
        Vector3d position = new Vector3d();
        float yaw = 0.0f;
        float pitch = 0.0f;
        if (positionChanged) {
            double x = readDouble();
            double y = readDouble();
            if (serverVersion == ServerVersion.V_1_7_10) {
                //Can be ignored, cause stance = (y + 1.62)
                double stance = readDouble();
            }
            double z = readDouble();
            position.setX(x);
            position.setY(y);
            position.setZ(z);
        }
        if (rotationChanged) {
            yaw = readFloat();
            pitch = readFloat();
        }
        location = new Location(position, yaw, pitch);
        onGround = readBoolean();
    }

    @Override
    public void readData(WrapperPlayClientPlayerFlying wrapper) {
        positionChanged = wrapper.positionChanged;
        rotationChanged = wrapper.rotationChanged;
        location = wrapper.location;
        onGround = wrapper.onGround;
    }

    @Override
    public void writeData() {
        if (positionChanged) {
            writeDouble(location.getPosition().getX());
            if (serverVersion == ServerVersion.V_1_7_10) {
                //Can be ignored, cause stance = (y + 1.62)
                writeDouble(location.getPosition().getY() + 1.62);
            }
            writeDouble(location.getPosition().getY());
            writeDouble(location.getPosition().getZ());
        }
        if (rotationChanged) {
            writeFloat(location.getYaw());
            writeFloat(location.getPitch());
        }
        writeBoolean(onGround);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean hasPositionChanged() {
        return positionChanged;
    }

    public void setPositionChanged(boolean positionChanged) {
        this.positionChanged = positionChanged;
    }

    public boolean hasRotationChanged() {
        return rotationChanged;
    }

    public void setRotationChanged(boolean rotationChanged) {
        this.rotationChanged = rotationChanged;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
