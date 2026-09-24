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
import org.jetbrains.annotations.ApiStatus;

/**
 * Mojang name: ClientboundLevelParticlesPacket
 */
public class WrapperPlayServerParticle extends PacketWrapper<WrapperPlayServerParticle> {

    private Particle<?> particle;
    private boolean longDistance;
    private Vector3d position;
    private Vector3f offset;
    /**
     * 3-dimensional vector starting with 26.3, a singular float before
     */
    private Vector3f maxSpeed;
    private int particleCount;
    /**
     * @versions 1.21.4+
     */
    private boolean alwaysShow;
    /**
     * @versions 26.3+
     */
    private RandomizationType randomizationType = RandomizationType.DEFAULT;

    public WrapperPlayServerParticle(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerParticle(
            Particle<?> particle, boolean longDistance, Vector3d position, Vector3f offset,
            float maxSpeed, int particleCount
    ) {
        this(particle, longDistance, position, offset, maxSpeed, particleCount, false);
    }

    /**
     * @versions 1.21.4+
     */
    public WrapperPlayServerParticle(
            Particle<?> particle, boolean longDistance, Vector3d position, Vector3f offset,
            float maxSpeed, int particleCount, boolean alwaysShow
    ) {
        super(PacketType.Play.Server.PARTICLE);
        this.particle = particle;
        this.longDistance = longDistance;
        this.position = position;
        this.offset = offset;
        this.maxSpeed = new Vector3f(maxSpeed, maxSpeed, maxSpeed);
        this.particleCount = particleCount;
        this.alwaysShow = alwaysShow;
    }

    /**
     * @versions 26.3+
     */
    public WrapperPlayServerParticle(
            Particle<?> particle, boolean longDistance, Vector3d position, Vector3f offset,
            Vector3f maxSpeed, int particleCount, boolean alwaysShow, RandomizationType randomizationType
    ) {
        super(PacketType.Play.Server.PARTICLE);
        this.particle = particle;
        this.longDistance = longDistance;
        this.position = position;
        this.offset = offset;
        this.maxSpeed = maxSpeed;
        this.particleCount = particleCount;
        this.alwaysShow = alwaysShow;
        this.randomizationType = randomizationType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read() {
        boolean v263 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_26_3);
        boolean v1205 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5);
        if (v263) {
            this.particle = Particle.read(this);
        }

        int particleTypeId = 0;
        ParticleType<?> particleType = null;
        if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
            String particleName = readString(64);
            particleType = ParticleTypes.getByName("minecraft:" + particleName);
        } else if (!v1205) {
            particleTypeId = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19) ? readVarInt() : readInt();
            particleType = ParticleTypes.getById(serverVersion.toClientVersion(), particleTypeId);
        }
        longDistance = readBoolean();
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_4)) {
            this.alwaysShow = this.readBoolean();
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15)) {
            position = Vector3d.read(this);
        } else {
            position = new Vector3d(readFloat(), readFloat(), readFloat());
        }
        this.offset = Vector3f.read(this);
        if (v263) {
            this.maxSpeed = Vector3f.read(this);
        } else {
            float f = this.readFloat();
            this.maxSpeed = new Vector3f(f, f, f);
        }
        this.particleCount = v263 ? this.readVarInt() : this.readInt();

        if (v263) {
            this.randomizationType = this.readEnum(RandomizationType.values());
        } else if (v1205) {
            this.particle = Particle.read(this);
        } else {
            ParticleData data;
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
                data = particleType.readData(this);
            } else {
                data = ParticleData.emptyData();
                if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
                    //TODO Understand the legacy data: https://wiki.vg/index.php?title=Protocol&oldid=14204
                    data = LegacyParticleData.read(this, particleTypeId);
                }
            }
            this.particle = new Particle<>((ParticleType<ParticleData>) particleType, data);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write() {
        boolean v263 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_26_3);
        //TODO on 1.7 we get particle type by 64 len string
        if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
            writeString(particle.getType().getName().getKey(), 64);
        } else if (this.serverVersion.isOlderThan(ServerVersion.V_1_20_5)) {
            int id = this.particle.getType().getId(this.serverVersion.toClientVersion());
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
                writeVarInt(id);
            } else {
                writeInt(id);
            }
        }
        if (v263) {
            Particle.write(this, this.particle);
        }
        writeBoolean(longDistance);
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_4)) {
            this.writeBoolean(this.alwaysShow);
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15)) {
            Vector3d.write(this, this.position);
        } else {
            writeFloat((float) position.getX());
            writeFloat((float) position.getY());
            writeFloat((float) position.getZ());
        }
        Vector3f.write(this, this.offset);
        if (v263) {
            Vector3f.write(this, this.maxSpeed);
            this.writeVarInt(this.particleCount);
            this.writeEnum(this.randomizationType);
        } else {
            this.writeFloat(this.maxSpeed.x);
            this.writeInt(this.particleCount);

            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
                Particle.write(this, this.particle);
            } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
                ((ParticleType<ParticleData>) this.particle.getType()).writeData(this, this.particle.getData());
            } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
                int id = this.particle.getType().getId(this.serverVersion.toClientVersion());
                LegacyParticleData legacyData = this.particle.getData() instanceof LegacyConvertible
                        ? ((LegacyConvertible) this.particle.getData()).toLegacy(this.serverVersion.toClientVersion())
                        : LegacyParticleData.nullValue(id);
                LegacyParticleData.write(this, id, legacyData);
            }
        }
    }

    @Override
    public void copy(WrapperPlayServerParticle wrapper) {
        this.particle = wrapper.particle;
        this.longDistance = wrapper.longDistance;
        this.position = wrapper.position;
        this.offset = wrapper.offset;
        this.maxSpeed = wrapper.maxSpeed;
        this.particleCount = wrapper.particleCount;
        this.alwaysShow = wrapper.alwaysShow;
        this.randomizationType = wrapper.randomizationType;
    }

    public Particle<?> getParticle() {
        return particle;
    }

    public void setParticle(Particle<?> particle) {
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

    /**
     * @versions -26.2
     */
    @ApiStatus.Obsolete
    public float getMaxSpeed() {
        // just use the first value of the vector
        return this.maxSpeed.x;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = new Vector3f(maxSpeed, maxSpeed, maxSpeed);
    }

    /**
     * @versions 26.3+
     */
    public Vector3f getMaxSpeedVec() {
        return this.maxSpeed;
    }

    /**
     * @versions 26.3+
     */
    public void setMaxSpeed(Vector3f maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getParticleCount() {
        return particleCount;
    }

    public void setParticleCount(int particleCount) {
        this.particleCount = particleCount;
    }

    /**
     * @versions 1.21.4+
     */
    public boolean isAlwaysShow() {
        return this.alwaysShow;
    }

    /**
     * @versions 1.21.4+
     */
    public void setAlwaysShow(boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
    }

    /**
     * @versions 26.3+
     */
    public RandomizationType getRandomizationType() {
        return this.randomizationType;
    }

    /**
     * @versions 26.3+
     */
    public void setRandomizationType(RandomizationType randomizationType) {
        this.randomizationType = randomizationType;
    }

    /**
     * Changes randomization behavior when <code>particleCount != 0</code>.
     *
     * @versions 26.3+
     */
    public enum RandomizationType {
        /**
         * Default particle randomization (pre 26.3 behavior), uses random Gaussian-distributed numbers
         * to determine speed/velocity (with <code>maxSpeed</code>) and variance/offset (with <code>offset</code>).
         */
        DEFAULT,
        /**
         * Uses uniformly distributed random-generated variance/offset (with <code>offset</code> being the exclusive maximum)
         * and always uses exactly <code>maxSpeed</code> for speed/velocity.
         */
        ALTERNATIVE,
        /**
         * Same as {@link #ALTERNATIVE}, but also applies a uniformly distributed random-generated factor to the speed/velocity.
         */
        ALTERNATIVE_WITH_SPEED,
    }
}
