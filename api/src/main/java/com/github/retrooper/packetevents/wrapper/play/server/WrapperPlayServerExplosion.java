/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.ArrayList;
import java.util.List;

public class WrapperPlayServerExplosion extends PacketWrapper<WrapperPlayServerExplosion> {
    private Vector3d position;
    private float strength;
    //Chunk posiitons?
    private List<Vector3i> records;
    private Vector3f playerMotion;

    public WrapperPlayServerExplosion(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerExplosion(Vector3d position, float strength, List<Vector3i> records, Vector3f playerMotion) {
        super(PacketType.Play.Server.EXPLOSION);
        this.position = position;
        this.strength = strength;
        this.records = records;
        this.playerMotion = playerMotion;
    }

    @Override
    public void read() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
            position = new Vector3d(readDouble(), readDouble(), readDouble());
        } else {
            position = new Vector3d(readFloat(), readFloat(), readFloat());
        }
        strength = readFloat();
        int recordsLength = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17) ? readVarInt() : readInt();
        records = new ArrayList<>(recordsLength);

        Vector3i floor = toFloor(position);

        for (int i = 0; i < recordsLength; i++) {
            int chunkPosX = readByte() + floor.getX();
            int chunkPosY = readByte() + floor.getY();
            int chunkPosZ = readByte() + floor.getZ();
            records.add(new Vector3i(chunkPosX, chunkPosY, chunkPosZ));
        }

        float motX = readFloat();
        float motY = readFloat();
        float motZ = readFloat();
        playerMotion = new Vector3f(motX, motY, motZ);
    }

    @Override
    public void write() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
            writeDouble(position.getX());
            writeDouble(position.getY());
            writeDouble(position.getZ());
        } else {
            writeFloat((float) position.getX());
            writeFloat((float) position.getY());
            writeFloat((float) position.getZ());
        }
        writeFloat(strength);

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            writeVarInt(records.size());
        } else {
            writeInt(records.size());
        }

        Vector3i floor = toFloor(position);

        for (Vector3i record : records) {
            writeByte(record.x - floor.getX());
            writeByte(record.y - floor.getY());
            writeByte(record.z - floor.getZ());
        }

        writeFloat(playerMotion.x);
        writeFloat(playerMotion.y);
        writeFloat(playerMotion.z);
    }

    @Override
    public void copy(WrapperPlayServerExplosion wrapper) {
        position = wrapper.position;
        strength = wrapper.strength;
        records = wrapper.records;
        playerMotion = wrapper.playerMotion;
    }

    private Vector3i toFloor(Vector3d position) {
        int floorX;
        int floorY;
        int floorZ;
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
            floorX = (int) Math.floor(position.x);
            floorY = (int) Math.floor(position.y);
            floorZ = (int) Math.floor(position.z);
        } else { // pre-1.14 does this weird way to round
            floorX = (int) position.x;
            floorY = (int) position.y;
            floorZ = (int) position.z;
        }
        return new Vector3i(floorX, floorY, floorZ);
    }

    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public float getStrength() {
        return strength;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public List<Vector3i> getRecords() {
        return records;
    }

    public void setRecords(List<Vector3i> records) {
        this.records = records;
    }

    public Vector3f getPlayerMotion() {
        return playerMotion;
    }

    public void setPlayerMotion(Vector3f playerMotion) {
        this.playerMotion = playerMotion;
    }
}
