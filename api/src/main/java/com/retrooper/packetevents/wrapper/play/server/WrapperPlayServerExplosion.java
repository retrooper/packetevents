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

package com.retrooper.packetevents.wrapper.play.server;

import com.retrooper.packetevents.event.impl.PacketSendEvent;
import com.retrooper.packetevents.util.Vector3i;
import com.retrooper.packetevents.protocol.packettype.PacketType;
import com.retrooper.packetevents.util.Vector3f;
import com.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.ArrayList;
import java.util.List;

public class WrapperPlayServerExplosion extends PacketWrapper<WrapperPlayServerExplosion> {
    private Vector3f position;
    private float strength;
    //Chunk posiitons?
    private List<Vector3i> records;
    private Vector3f playerMotion;
    public WrapperPlayServerExplosion(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerExplosion(Vector3f position, float strength, List<Vector3i> records, Vector3f playerMotion) {
        super(PacketType.Play.Server.EXPLOSION);
        this.position = position;
        this.strength = strength;
        this.records = records;
        this.playerMotion = playerMotion;
    }

    @Override
    public void readData() {
        float x = readFloat();
        float y = readFloat();
        float z = readFloat();
        position = new Vector3f(x, y, z);
        strength = readFloat();
        int recordsLength = readInt();
        records = new ArrayList<>(recordsLength);
        for (int i = 0; i < recordsLength; i++) {
            int chunkPosX = readByte() + (int)position.x;
            int chunkPosY = readByte() + (int)position.y;
            int chunkPosZ = readByte() + (int)position.z;
            records.add(new Vector3i(chunkPosX, chunkPosY, chunkPosZ));
        }

        float motX = readFloat();
        float motY = readFloat();
        float motZ = readFloat();
        playerMotion = new Vector3f(motX, motY, motZ);
    }

    @Override
    public void readData(WrapperPlayServerExplosion wrapper) {
        position = wrapper.position;
        strength = wrapper.strength;
        records = wrapper.records;
        playerMotion = wrapper.playerMotion;
    }

    @Override
    public void writeData() {
        writeFloat(position.x);
        writeFloat(position.y);
        writeFloat(position.z);
       writeFloat(strength);
       writeInt(records.size());
        for (Vector3i record : records) {
            writeByte(record.x - (int)position.x);
            writeByte(record.y - (int)position.y);
            writeByte(record.z - (int)position.z);
        }

        writeFloat(playerMotion.x);
        writeFloat(playerMotion.y);
        writeFloat(playerMotion.z);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
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
