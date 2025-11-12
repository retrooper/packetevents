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
import com.github.retrooper.packetevents.protocol.util.WeightedList;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

/**
 * Mojang name: ClientboundExplodePacket
 */
@NullMarked
public class WrapperPlayServerExplosion extends PacketWrapper<WrapperPlayServerExplosion> {

    private Vector3d position;
    /**
     * @versions 1.7-1.21.2 and 1.21.9+
     */
    private float strength;
    /**
     * @versions 1.21.9+
     */
    private int blockCount;
    /**
     * @versions 1.7-1.21.1
     */
    private List<Vector3i> records;
    /**
     * Only nullable for 1.21.2+
     */
    private @Nullable Vector3d knockback;

    /**
     * @versions 1.20.3-1.21.2
     */
    @ApiStatus.Obsolete
    private Particle<?> smallParticle;
    /**
     * @versions 1.20.3+
     */
    private Particle<?> particle;
    /**
     * @versions 1.20.3-1.21.2
     */
    @ApiStatus.Obsolete
    private BlockInteraction blockInteraction;
    /**
     * @versions 1.20.3+
     */
    private Sound explosionSound;
    /**
     * @versions 1.21.9+
     */
    private WeightedList<ParticleInfo> blockParticles;

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

    public WrapperPlayServerExplosion(
            Vector3d position, float strength, List<Vector3i> records, Vector3d playerMotion,
            Particle<?> smallParticle, Particle<?> particle,
            BlockInteraction blockInteraction, Sound explosionSound
    ) {
        this(position, strength, records, playerMotion, smallParticle, particle,
                blockInteraction, explosionSound, new WeightedList<>());
    }

    public WrapperPlayServerExplosion(
            Vector3d position, float strength, List<Vector3i> records, Vector3d playerMotion,
            Particle<?> smallParticle, Particle<?> particle,
            BlockInteraction blockInteraction, Sound explosionSound, WeightedList<ParticleInfo> blockParticles
    ) {
        super(PacketType.Play.Server.EXPLOSION);
        this.position = position;
        this.strength = strength;
        this.blockCount = records.size();
        this.records = records;
        this.knockback = playerMotion;
        this.smallParticle = smallParticle;
        this.particle = particle;
        this.blockInteraction = blockInteraction;
        this.explosionSound = explosionSound;
        this.blockParticles = blockParticles;
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
        this(position, 0f, 0, playerMotion,
                particle, explosionSound, new WeightedList<>());
    }

    public WrapperPlayServerExplosion(
            Vector3d position, float strength, int blockCount,
            @Nullable Vector3d playerMotion, Particle<?> particle,
            Sound explosionSound, WeightedList<ParticleInfo> blockParticles
    ) {
        super(PacketType.Play.Server.EXPLOSION);
        this.position = position;
        this.strength = strength;
        this.blockCount = blockCount;
        this.knockback = playerMotion;
        this.particle = particle;
        this.explosionSound = explosionSound;
        this.blockParticles = blockParticles;
    }

    @Override
    public void read() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
            this.position = Vector3d.read(this);
        } else {
            position = new Vector3d(readFloat(), readFloat(), readFloat());
        }
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            // this packet has been basically completely emptied with 1.21.2
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
                this.strength = this.readFloat();
                this.blockCount = this.readInt();
            }
            this.knockback = this.readOptional(Vector3d::read);
            this.particle = Particle.read(this);
            this.explosionSound = Sound.read(this);
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
                this.blockParticles = WeightedList.read(this, ParticleInfo::read);
            }
            // legacy fields
            this.blockInteraction = BlockInteraction.DESTROY_BLOCKS;
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
            } else {
                // set newer fields
                this.blockInteraction = BlockInteraction.DESTROY_BLOCKS;
                this.explosionSound = Sounds.INTENTIONALLY_EMPTY;
            }
        }
    }

    @Override
    public void write() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
            Vector3d.write(this, this.position);
        } else {
            writeFloat((float) position.getX());
            writeFloat((float) position.getY());
            writeFloat((float) position.getZ());
        }
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            // this packet has been basically completely emptied with 1.21.2
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
                this.writeFloat(this.strength);
                this.writeInt(this.blockCount);
            }
            this.writeOptional(this.knockback, Vector3d::write);
            Particle.write(this, this.particle);
            Sound.write(this, this.explosionSound);
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
                WeightedList.write(this, this.blockParticles, ParticleInfo::write);
            }
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
        blockCount = wrapper.blockCount;
        records = wrapper.records;
        knockback = wrapper.knockback;
        smallParticle = wrapper.smallParticle;
        particle = wrapper.particle;
        blockInteraction = wrapper.blockInteraction;
        explosionSound = wrapper.explosionSound;
        blockParticles = wrapper.blockParticles;
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

    /**
     * @versions 1.7-1.21.1 and 1.21.9+
     */
    public float getStrength() {
        return strength;
    }

    /**
     * @versions 1.7-1.21.1 and 1.21.9+
     */
    public void setStrength(float strength) {
        this.strength = strength;
    }

    /**
     * @versions 1.21.9+
     */
    public int getBlockCount() {
        if (this.serverVersion.isOlderThan(ServerVersion.V_1_21_9) && this.blockCount == 0) {
            return this.records.size();
        }
        return this.blockCount;
    }

    /**
     * @versions 1.21.9+
     */
    public void setBlockCount(int blockCount) {
        this.blockCount = blockCount;
    }

    /**
     * @versions 1.7-1.21.1
     */
    public List<Vector3i> getRecords() {
        if (this.records == null) {
            this.records = new ArrayList<>();
        }
        return records;
    }

    /**
     * @versions 1.7-1.21.1
     */
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
        if (this.smallParticle == null) {
            return new Particle<>(ParticleTypes.EXPLOSION);
        }
        return this.smallParticle;
    }

    @ApiStatus.Obsolete // removed in 1.21.2
    public void setSmallExplosionParticles(Particle<?> smallExplosionParticles) {
        this.smallParticle = smallExplosionParticles;
    }

    public Particle<?> getParticle() {
        if (this.particle == null) {
            return new Particle<>(ParticleTypes.EXPLOSION_EMITTER);
        }
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

    /**
     * @versions 1.21.9+
     */
    public WeightedList<ParticleInfo> getBlockParticles() {
        return this.blockParticles;
    }

    /**
     * @versions 1.21.9+
     */
    public void setBlockParticles(WeightedList<ParticleInfo> blockParticles) {
        this.blockParticles = blockParticles;
    }

    public enum BlockInteraction {
        KEEP_BLOCKS,
        DESTROY_BLOCKS,
        DECAY_DESTROYED_BLOCKS,
        TRIGGER_BLOCKS,
    }

    public static final class ParticleInfo {

        private final Particle<?> particle;
        private final float scaling;
        private final float speed;

        public ParticleInfo(Particle<?> particle, float scaling, float speed) {
            this.particle = particle;
            this.scaling = scaling;
            this.speed = speed;
        }

        public static ParticleInfo read(PacketWrapper<?> wrapper) {
            Particle<?> particle = Particle.read(wrapper);
            float scaling = wrapper.readFloat();
            float speed = wrapper.readFloat();
            return new ParticleInfo(particle, scaling, speed);
        }

        public static void write(PacketWrapper<?> wrapper, ParticleInfo info) {
            Particle.write(wrapper, info.particle);
            wrapper.writeFloat(info.scaling);
            wrapper.writeFloat(info.speed);
        }

        public Particle<?> getParticle() {
            return this.particle;
        }

        public float getScaling() {
            return this.scaling;
        }

        public float getSpeed() {
            return this.speed;
        }
    }
}
