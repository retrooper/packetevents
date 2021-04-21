/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.packetwrappers.api.helper;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class WrappedPacketEntityAbstraction extends WrappedPacket {
    private final int entityIDFieldIndex;
    protected Entity entity;
    protected int entityID = -1;

    public WrappedPacketEntityAbstraction(NMSPacket packet, int entityIDFieldIndex) {
        super(packet);
        this.entityIDFieldIndex = entityIDFieldIndex;
    }

    public WrappedPacketEntityAbstraction(NMSPacket packet) {
        super(packet);
        this.entityIDFieldIndex = 0;
    }

    public WrappedPacketEntityAbstraction(int entityIDFieldIndex) {
        super();
        this.entityIDFieldIndex = entityIDFieldIndex;
    }

    public WrappedPacketEntityAbstraction() {
        super();
        this.entityIDFieldIndex = 0;
    }

    public int getEntityId() {
        if (entityID != -1 || packet == null) {
            return entityID;
        }
        return entityID = readInt(entityIDFieldIndex);
    }

    public void setEntityId(int entityID) {
        if (packet != null) {
            writeInt(entityIDFieldIndex, this.entityID = entityID);
        } else {
            this.entityID = entityID;
        }
        this.entity = null;
    }

    @Nullable
    public Entity getEntity(@Nullable World world) {
        if (entity != null) {
            return entity;
        }
        return entity = NMSUtils.getEntityById(world, getEntityId());
    }

    public Entity getEntity() {
        return getEntity(null);
    }

    public void setEntity(@NotNull Entity entity) {
        setEntityId(entity.getEntityId());
        this.entity = entity;
    }
}
