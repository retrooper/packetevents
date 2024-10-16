/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.List;
import java.util.Objects;

public class WrapperPlayServerMoveMinecart extends PacketWrapper<WrapperPlayServerMoveMinecart> {

    private int entityId;
    private List<MinecartStep> lerpSteps;

    public WrapperPlayServerMoveMinecart(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerMoveMinecart(int entityId, List<MinecartStep> lerpSteps) {
        super(PacketType.Play.Server.MOVE_MINECART);
        this.entityId = entityId;
        this.lerpSteps = lerpSteps;
    }

    @Override
    public void read() {
        this.entityId = this.readVarInt();
        this.lerpSteps = this.readList(MinecartStep::read);
    }

    @Override
    public void write() {
        this.writeVarInt(this.entityId);
        this.writeList(this.lerpSteps, MinecartStep::write);
    }

    @Override
    public void copy(WrapperPlayServerMoveMinecart wrapper) {
        this.entityId = wrapper.entityId;
        this.lerpSteps = wrapper.lerpSteps;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public List<MinecartStep> getLerpSteps() {
        return this.lerpSteps;
    }

    public void setLerpSteps(List<MinecartStep> lerpSteps) {
        this.lerpSteps = lerpSteps;
    }

    public static final class MinecartStep {

        private Vector3d position;
        private Vector3d movement;
        private float yaw;
        private float pitch;
        private float weight;

        public MinecartStep(Vector3d position, Vector3d movement, float yaw, float pitch, float weight) {
            this.position = position;
            this.movement = movement;
            this.yaw = yaw;
            this.pitch = pitch;
            this.weight = weight;
        }

        public static MinecartStep read(PacketWrapper<?> wrapper) {
            Vector3d position = Vector3d.read(wrapper);
            Vector3d movement = Vector3d.read(wrapper);
            float yaw = wrapper.readRotation();
            float pitch = wrapper.readRotation();
            float weight = wrapper.readFloat();
            return new MinecartStep(position, movement, yaw, pitch, weight);
        }

        public static void write(PacketWrapper<?> wrapper, MinecartStep step) {
            Vector3d.write(wrapper, step.position);
            Vector3d.write(wrapper, step.movement);
            wrapper.writeRotation(step.yaw);
            wrapper.writeRotation(step.pitch);
            wrapper.writeFloat(step.weight);
        }

        public Vector3d getPosition() {
            return this.position;
        }

        public void setPosition(Vector3d position) {
            this.position = position;
        }

        public Vector3d getMovement() {
            return this.movement;
        }

        public void setMovement(Vector3d movement) {
            this.movement = movement;
        }

        public float getYaw() {
            return this.yaw;
        }

        public void setYaw(float yaw) {
            this.yaw = yaw;
        }

        public float getPitch() {
            return this.pitch;
        }

        public void setPitch(float pitch) {
            this.pitch = pitch;
        }

        public float getWeight() {
            return this.weight;
        }

        public void setWeight(float weight) {
            this.weight = weight;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof MinecartStep)) return false;
            MinecartStep that = (MinecartStep) obj;
            if (Float.compare(that.yaw, this.yaw) != 0) return false;
            if (Float.compare(that.pitch, this.pitch) != 0) return false;
            if (Float.compare(that.weight, this.weight) != 0) return false;
            if (!this.position.equals(that.position)) return false;
            return this.movement.equals(that.movement);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.position, this.movement, this.yaw, this.pitch, this.weight);
        }
    }
}
