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
import com.github.retrooper.packetevents.protocol.sound.Sounds;
import com.github.retrooper.packetevents.protocol.sound.StaticSound;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WrapperPlayServerExplosion extends PacketWrapper<WrapperPlayServerExplosion> {

    private Vector3d position;
    private float strength; // removed in 1.21.2
    private List<Vector3i> records; // removed in 1.21.2
    private @Nullable Vector3d knockback; // optional since 1.21.2

    private Particle<?> smallParticle; // removed in 1.21.2
    private Particle<?> particle;
    private BlockInteraction blockInteraction; // removed in 1.21.2
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

    @Deprecated
    public WrapperPlayServerExplosion(Vector3d position, float strength, List<Vector3i> records, Vector3f playerMotion,
                                      Particle<?> smallParticle, Particle<?> particle,
                                      BlockInteraction blockInteraction, Sound explosionSound) {
        this(position, strength, records, new Vector3d(playerMotion.x, playerMotion.y, playerMotion.z),
                smallParticle, particle, blockInteraction, explosionSound);
    }

    public WrapperPlayServerExplosion(Vector3d position, float strength, List<Vector3i> records, Vector3d playerMotion,
                                      Particle<?> smallParticle, Particle<?> particle,
                                      BlockInteraction blockInteraction, Sound explosionSound) {
        super(PacketType.Play.Server.EXPLOSION);
        this.position = position;
        this.strength = strength;
        this.records = records;
        this.knockback = playerMotion;
        this.smallParticle = smallParticle;
        this.particle = particle;
        this.blockInteraction = blockInteraction;
        this.explosionSound = explosionSound;
    }

    public WrapperPlayServerExplosion(
            Vector3d position,
            @Nullable Vector3d playerMotion
    ) {
        this(position, playerMotion,
                new Particle<>(ParticleTypes.EXPLOSION_EMITTER),
                Sounds.ENTITY_GENERIC_EXPLODE);
    }

    public WrapperPlayServerExplosion(
            Vector3d position, @Nullable Vector3d playerMotion,
            Particle<?> particle, Sound explosionSound
    ) {
        super(PacketType.Play.Server.EXPLOSION);
        this.position = position;
        this.knockback = playerMotion;
        this.particle = particle;
        this.explosionSound = explosionSound;
    }

    @Override
    public void read() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
            position = new Vector3d(readDouble(), readDouble(), readDouble());
        } else {
            position = new Vector3d(readFloat(), readFloat(), readFloat());
        }
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            // this packet has been basically completely emptied with 1.21.2
            this.knockback = this.readOptional(Vector3d::read);
            this.particle = Particle.read(this);
            this.explosionSound = Sound.read(this);
        } else {
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
            knockback = new Vector3d(motX, motY, motZ);

            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
                this.blockInteraction = BlockInteraction.values()[this.readVarInt()];
                this.smallParticle = Particle.read(this);
                this.particle = Particle.read(this);

                if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
                    this.explosionSound = Sound.read(this);
                } else {
                    ResourceLocation explosionSoundKey = this.readIdentifier();
                    Float explosionSoundRange = this.readOptional(PacketWrapper::readFloat);
                    this.explosionSound = new StaticSound(explosionSoundKey, explosionSoundRange);
                }
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
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            // this packet has been basically completely emptied with 1.21.2
            this.writeOptional(this.knockback, Vector3d::write);
            Particle.write(this, this.particle);
            Sound.write(this, this.explosionSound);
        } else {
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

            writeFloat((float) knockback.x);
            writeFloat((float) knockback.y);
            writeFloat((float) knockback.z);

            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
                this.writeVarInt(this.blockInteraction.ordinal());
                Particle.write(this, this.smallParticle);
                Particle.write(this, this.particle);

                if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
                    Sound.write(this, this.explosionSound);
                } else {
                    this.writeIdentifier(this.explosionSound.getSoundId());
                    this.writeOptional(this.explosionSound.getRange(), PacketWrapper::writeFloat);
                }
            }
        }
    }

    @Override
    public void copy(WrapperPlayServerExplosion wrapper) {
        position = wrapper.position;
        strength = wrapper.strength;
        records = wrapper.records;
        knockback = wrapper.knockback;
        smallParticle = wrapper.smallParticle;
        particle = wrapper.particle;
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

    @ApiStatus.Obsolete // removed in 1.21.2
    public float getStrength() {
        return strength;
    }

    @ApiStatus.Obsolete // removed in 1.21.2
    public void setStrength(float strength) {
        this.strength = strength;
    }

    @ApiStatus.Obsolete // removed in 1.21.2
    public List<Vector3i> getRecords() {
        return records;
    }

    @ApiStatus.Obsolete // removed in 1.21.2
    public void setRecords(List<Vector3i> records) {
        this.records = records;
    }

    public @Nullable Vector3d getKnockback() {
        return this.knockback;
    }

    public void setKnockback(@Nullable Vector3d knockback) {
        this.knockback = knockback;
    }

    @Deprecated
    public @Nullable Vector3f getPlayerMotion() {
        return this.knockback == null ? null : new Vector3f(
                (float) this.knockback.x,
                (float) this.knockback.y,
                (float) this.knockback.z
        );
    }

    @Deprecated
    public void setPlayerMotion(@Nullable Vector3f playerMotion) {
        this.knockback = playerMotion == null ? null : new Vector3d(
                playerMotion.x, playerMotion.y, playerMotion.z);
    }

    @ApiStatus.Obsolete // removed in 1.21.2
    public Particle<?> getSmallExplosionParticles() {
        return this.smallParticle;
    }

    @ApiStatus.Obsolete // removed in 1.21.2
    public void setSmallExplosionParticles(Particle<?> smallExplosionParticles) {
        this.smallParticle = smallExplosionParticles;
    }

    public Particle<?> getParticle() {
        return this.particle;
    }

    public void setParticle(Particle<?> particle) {
        this.particle = particle;
    }

    @ApiStatus.Obsolete // renamed in 1.21.2
    public Particle<?> getLargeExplosionParticles() {
        return this.getParticle();
    }

    @ApiStatus.Obsolete // renamed in 1.21.2
    public void setLargeExplosionParticles(Particle<?> largeExplosionParticles) {
        this.setParticle(largeExplosionParticles);
    }

    @ApiStatus.Obsolete // removed in 1.21.2
    public BlockInteraction getBlockInteraction() {
        return this.blockInteraction;
    }

    @ApiStatus.Obsolete // removed in 1.21.2
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
