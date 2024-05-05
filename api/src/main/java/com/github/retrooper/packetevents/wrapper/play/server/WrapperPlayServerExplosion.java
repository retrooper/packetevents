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
import com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.protocol.sound.StaticSound;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WrapperPlayServerExplosion extends PacketWrapper<WrapperPlayServerExplosion> {
    private Vector3d position;
    private float strength;
    //Chunk posiitons?
    private List<Vector3i> records;
    private Vector3f playerMotion;

    private Particle<?> smallExplosionParticles;
    private Particle<?> largeExplosionParticles;
    private BlockInteraction blockInteraction;
    private Sound explosionSound;

    public WrapperPlayServerExplosion(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerExplosion(Vector3d position, float strength, List<Vector3i> records, Vector3f playerMotion) {
        this(position, strength, records, playerMotion, new Particle<>(ParticleTypes.EXPLOSION),
                new Particle<>(ParticleTypes.EXPLOSION_EMITTER), BlockInteraction.DESTROY_BLOCKS,
                new ResourceLocation("minecraft:entity.generic.explode"), null);
    }

    public WrapperPlayServerExplosion(Vector3d position, float strength, List<Vector3i> records, Vector3f playerMotion,
                                      Particle<?> smallExplosionParticles, Particle<?> largeExplosionParticles,
                                      BlockInteraction blockInteraction, ResourceLocation explosionSoundKey,
                                      @Nullable Float explosionSoundRange) {
        this(position, strength, records, playerMotion, smallExplosionParticles, largeExplosionParticles,
                blockInteraction, new StaticSound(explosionSoundKey, explosionSoundRange));
    }

    public WrapperPlayServerExplosion(Vector3d position, float strength, List<Vector3i> records, Vector3f playerMotion,
                                      Particle<?> smallExplosionParticles, Particle<?> largeExplosionParticles,
                                      BlockInteraction blockInteraction, Sound explosionSound) {
        super(PacketType.Play.Server.EXPLOSION);
        this.position = position;
        this.strength = strength;
        this.records = records;
        this.playerMotion = playerMotion;
        this.smallExplosionParticles = smallExplosionParticles;
        this.largeExplosionParticles = largeExplosionParticles;
        this.blockInteraction = blockInteraction;
        this.explosionSound = explosionSound;
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

        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
            this.blockInteraction = BlockInteraction.values()[this.readVarInt()];
            this.smallExplosionParticles = Particle.read(this);
            this.largeExplosionParticles = Particle.read(this);

            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
                this.explosionSound = Sound.read(this);
            } else {
                ResourceLocation explosionSoundKey = this.readIdentifier();
                Float explosionSoundRange = this.readOptional(PacketWrapper::readFloat);
                this.explosionSound = new StaticSound(explosionSoundKey, explosionSoundRange);
            }
        }
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

        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
            this.writeVarInt(this.blockInteraction.ordinal());
            Particle.write(this, this.smallExplosionParticles);
            Particle.write(this, this.largeExplosionParticles);

            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
                Sound.write(this, this.explosionSound);
            } else {
                this.writeIdentifier(this.explosionSound.getSoundId());
                this.writeOptional(this.explosionSound.getRange(), PacketWrapper::writeFloat);
            }
        }
    }

    @Override
    public void copy(WrapperPlayServerExplosion wrapper) {
        position = wrapper.position;
        strength = wrapper.strength;
        records = wrapper.records;
        playerMotion = wrapper.playerMotion;
        smallExplosionParticles = wrapper.smallExplosionParticles;
        largeExplosionParticles = wrapper.largeExplosionParticles;
        blockInteraction = wrapper.blockInteraction;
        explosionSound = wrapper.explosionSound;
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

    public Particle<?> getSmallExplosionParticles() {
        return this.smallExplosionParticles;
    }

    public void setSmallExplosionParticles(Particle<?> smallExplosionParticles) {
        this.smallExplosionParticles = smallExplosionParticles;
    }

    public Particle<?> getLargeExplosionParticles() {
        return this.largeExplosionParticles;
    }

    public void setLargeExplosionParticles(Particle<?> largeExplosionParticles) {
        this.largeExplosionParticles = largeExplosionParticles;
    }

    public BlockInteraction getBlockInteraction() {
        return this.blockInteraction;
    }

    public void setBlockInteraction(BlockInteraction blockInteraction) {
        this.blockInteraction = blockInteraction;
    }

    public ResourceLocation getExplosionSoundKey() {
        return this.explosionSound.getSoundId();
    }

    public void setExplosionSoundKey(ResourceLocation explosionSoundKey) {
        this.explosionSound = new StaticSound(explosionSoundKey, this.explosionSound.getRange());
    }

    public @Nullable Float getExplosionSoundRange() {
        return this.explosionSound.getRange();
    }

    public void setExplosionSoundRange(@Nullable Float explosionSoundRange) {
        this.explosionSound = new StaticSound(this.explosionSound.getSoundId(), explosionSoundRange);
    }

    public Sound getExplosionSound() {
        return this.explosionSound;
    }

    public void setExplosionSound(Sound explosionSound) {
        this.explosionSound = explosionSound;
    }

    public enum BlockInteraction {
        KEEP_BLOCKS,
        DESTROY_BLOCKS,
        DECAY_DESTROYED_BLOCKS,
        TRIGGER_BLOCKS,
    }
}
