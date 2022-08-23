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
import com.github.retrooper.packetevents.protocol.particle.Particle;
import com.github.retrooper.packetevents.protocol.particle.data.LegacyConvertible;
import com.github.retrooper.packetevents.protocol.particle.data.LegacyParticleData;
import com.github.retrooper.packetevents.protocol.particle.data.ParticleData;
import com.github.retrooper.packetevents.protocol.particle.type.ParticleType;
import com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

//Might be worthy to document
//TODO: Check changelog through out the versions
public class WrapperPlayServerParticle extends PacketWrapper<WrapperPlayServerParticle> {

    private Particle particle;
    private boolean longDistance;
    private Vector3d position;
    private Vector3f offset;
    private float maxSpeed;
    private int particleCount;

    public WrapperPlayServerParticle(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerParticle(Particle particle, boolean longDistance, Vector3d position, Vector3f offset,
                                     float maxSpeed, int particleCount) {
        super(PacketType.Play.Server.PARTICLE);
        this.particle = particle;
        this.longDistance = longDistance;
        this.position = position;
        this.offset = offset;
        this.maxSpeed = maxSpeed;
        this.particleCount = particleCount;
    }

    @Override
    public void read() {
        int particleTypeId = 0;
        ParticleType particleType;
        if (serverVersion == ServerVersion.V_1_7_10) {
            String particleName = readString(64);
            particleType = ParticleTypes.getByName("minecraft:" + particleName);
        } else {
            particleTypeId = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19) ? readVarInt() : readInt();
            particleType = ParticleTypes.getById(serverVersion.toClientVersion(), particleTypeId);
        }
        longDistance = readBoolean();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15)) {
            position = new Vector3d(readDouble(), readDouble(), readDouble());
        } else {
            position = new Vector3d(readFloat(), readFloat(), readFloat());
        }
        offset = new Vector3f(readFloat(), readFloat(), readFloat());
        maxSpeed = readFloat();
        particleCount = readInt();
        ParticleData data;
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            data = particleType.readDataFunction().apply(this);
        } else {
            data = new ParticleData();
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
                //TODO Understand the legacy data: https://wiki.vg/index.php?title=Protocol&oldid=14204
                data = LegacyParticleData.read(this, particleTypeId);
            }
        }
        particle = new Particle(particleType, data);
    }

    @Override
    public void write() {
        int id = particle.getType().getId(serverVersion.toClientVersion());
        //TODO on 1.7 we get particle type by 64 len string
        if (serverVersion == ServerVersion.V_1_7_10) {
            writeString(particle.getType().getName().getKey(), 64);
        } else {
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
                writeVarInt(id);
            } else {
                writeInt(id);
            }
        }
        writeBoolean(longDistance);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15)) {
            writeDouble(position.getX());
            writeDouble(position.getY());
            writeDouble(position.getZ());
        } else {
            writeFloat((float) position.getX());
            writeFloat((float) position.getY());
            writeFloat((float) position.getZ());
        }
        writeFloat(offset.getX());
        writeFloat(offset.getY());
        writeFloat(offset.getZ());
        writeFloat(maxSpeed);
        writeInt(particleCount);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            particle.getType().writeDataFunction().accept(this, particle.getData());
        } else if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            LegacyParticleData legacyData;
            if (particle.getData() instanceof LegacyConvertible) {
                legacyData = ((LegacyConvertible) particle.getData()).toLegacy(serverVersion.toClientVersion());
            } else {
                legacyData = LegacyParticleData.nullValue(id);
            }
            LegacyParticleData.write(this, id, legacyData);
        }
    }

    @Override
    public void copy(WrapperPlayServerParticle wrapper) {
        particle = wrapper.particle;
        longDistance = wrapper.longDistance;
        position = wrapper.position;
        offset = wrapper.offset;
        maxSpeed = wrapper.maxSpeed;
        particleCount = wrapper.particleCount;
    }

    public Particle getParticle() {
        return particle;
    }

    public void setParticle(Particle particle) {
        this.particle = particle;
    }

    public boolean isLongDistance() {
        return longDistance;
    }

    public void setLongDistance(boolean longDistance) {
        this.longDistance = longDistance;
    }

    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public Vector3f getOffset() {
        return offset;
    }

    public void setOffset(Vector3f offset) {
        this.offset = offset;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getParticleCount() {
        return particleCount;
    }

    public void setParticleCount(int particleCount) {
        this.particleCount = particleCount;
    }

}
