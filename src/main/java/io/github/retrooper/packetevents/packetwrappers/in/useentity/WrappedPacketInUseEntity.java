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

package io.github.retrooper.packetevents.packetwrappers.in.useentity;

import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import org.bukkit.entity.Entity;

public final class WrappedPacketInUseEntity extends WrappedPacket {
    private static Class<?> useEntityClass;
    private static Class<?> enumEntityUseActionClass;
    private Entity entity;
    private int entityID;
    private EntityUseAction action;
    public WrappedPacketInUseEntity(final Object packet) {
        super(packet);
    }

    public static void load() {
        useEntityClass = PacketTypeClasses.Client.USE_ENTITY;

        try {
            enumEntityUseActionClass = NMSUtils.getNMSClass("EnumEntityUseAction");
        } catch (ClassNotFoundException e) {
            //That is fine, it is probably a subclass
            enumEntityUseActionClass = SubclassUtil.getSubClass(useEntityClass, "EnumEntityUseAction");
        }
    }

    @Override
    protected void setup() {
        this.entityID = readInt(0);
        final Object useActionEnum = readObject(0, enumEntityUseActionClass);
        this.action = EntityUseAction.valueOf(useActionEnum.toString());
    }

    /**
     * Lookup the associated entity by the ID that was sent in the packet.
     * @return Entity
     */
    public Entity getEntity() {
        if(entity != null) {
            return entity;
        }
        return entity = NMSUtils.getEntityById(this.entityID);
    }

    /**
     * Get the ID of the entity.
     * If you do not want to use {@link #getEntity()},
     * you lookup the entity by yourself with this entity ID.
     * @return Entity ID
     */
    public int getEntityId() {
        return entityID;
    }

    /**
     * Get the associated action.
     * @return Get EntityUseAction
     */
    public EntityUseAction getAction() {
        return action;
    }

    public enum EntityUseAction {
        INTERACT, INTERACT_AT, ATTACK, INVALID
    }
}
