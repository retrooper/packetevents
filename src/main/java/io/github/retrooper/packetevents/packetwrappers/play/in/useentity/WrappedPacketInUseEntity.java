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

package io.github.retrooper.packetevents.packetwrappers.play.in.useentity;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.enums.Hand;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.vector.Vector3d;
import org.bukkit.entity.Entity;

import java.util.Objects;

public final class WrappedPacketInUseEntity extends WrappedPacket {
    private static Class<?> enumEntityUseActionClass, enumHandClass;
    private Entity entity;
    private EntityUseAction action;
    private int entityId = -1;

    public WrappedPacketInUseEntity(final NMSPacket packet) {
        super(packet);
    }

    @Override
protected void load() {
        Class<?> useEntityClass = NMSUtils.getNMSClassWithoutException("PacketPlayInUseEntity");
        try {
            enumHandClass = NMSUtils.getNMSClass("EnumHand");
        } catch (ClassNotFoundException e) {
            //Probably a 1.7.10 or 1.8.x server
        }
        try {
            enumEntityUseActionClass = NMSUtils.getNMSClass("EnumEntityUseAction");
        } catch (ClassNotFoundException e) {
            //That is fine, it is probably a subclass
            enumEntityUseActionClass = SubclassUtil.getSubClass(Objects.requireNonNull(useEntityClass), "EnumEntityUseAction");
        }
    }


    /**
     * Lookup the associated entity by the ID that was sent in the packet.
     *
     * @return Entity
     */
    public Entity getEntity() {
        if (entity != null) {
            return entity;
        }
        return entity = NMSUtils.getEntityById(getEntityId());
    }

    /**
     * Get the ID of the entity.
     * If you do not want to use {@link #getEntity()},
     * you lookup the entity by yourself with this entity ID.
     *
     * @return Entity ID
     */
    public int getEntityId() {
        if (entityId != -1) {
            return entityId;
        }
        return entityId = readInt(0);
    }

    public Vector3d getTarget() {
        if (getAction() == EntityUseAction.INTERACT_AT
                && PacketEvents.get().getServerUtils().getVersion() != ServerVersion.v_1_7_10) {
            Object vec3DObj = readObject(0, NMSUtils.vec3DClass);
            WrappedPacket vec3DWrapper = new WrappedPacket(new NMSPacket(vec3DObj));
            return new Vector3d(vec3DWrapper.readDouble(0), vec3DWrapper.readDouble(1), vec3DWrapper.readDouble(2));
        }
        return Vector3d.INVALID;
    }

    /**
     * Get the associated action.
     *
     * @return Get EntityUseAction
     */
    public EntityUseAction getAction() {
        if (action == null) {
            final Object useActionEnum = readObject(0, enumEntityUseActionClass);
            return action = EntityUseAction.valueOf(useActionEnum.toString());
        }
        return action;
    }

    public Hand getHand() {
        if((getAction() == EntityUseAction.INTERACT || getAction() == EntityUseAction.INTERACT_AT)
                && PacketEvents.get().getServerUtils().getVersion().isHigherThan(ServerVersion.v_1_8_8)) {
            Object enumHandObj = readObject(0, enumHandClass);
            //Should actually never be null, but we will handle such a case
            if(enumHandObj == null) {
                return Hand.MAIN_HAND;
            }
            return Hand.valueOf(Objects.requireNonNull(enumHandObj).toString());
        }
        return Hand.MAIN_HAND;
    }

    public enum EntityUseAction {
        INTERACT, INTERACT_AT, ATTACK
    }
}
