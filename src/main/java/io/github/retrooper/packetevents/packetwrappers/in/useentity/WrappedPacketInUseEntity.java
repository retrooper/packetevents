/**
MIT License

Copyright (c) 2020 retrooper

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package io.github.retrooper.packetevents.packetwrappers.in.useentity;

import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;
import org.bukkit.entity.Entity;

public final class WrappedPacketInUseEntity extends WrappedPacket {
    private static Class<?> useEntityClass;
    private static Class<?> enumEntityUseActionClass;
    private int entityId;
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
            enumEntityUseActionClass = Reflection.getSubClass(useEntityClass, "EnumEntityUseAction");
        }
    }

    @Override
    protected void setup() {
        try {
            this.entityId = Reflection.getField(useEntityClass, int.class, 0).getInt(packet);
            final Object useActionEnum = Reflection.getField(useEntityClass, enumEntityUseActionClass, 0).get(packet);
            this.action = EntityUseAction.valueOf(useActionEnum.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Entity getEntity() {
        return NMSUtils.getEntityById(this.entityId);
    }

    public int getEntityId() {
        return entityId;
    }

    public EntityUseAction getAction() {
        return action;
    }

    public enum EntityUseAction {
        INTERACT, INTERACT_AT, ATTACK, INVALID
    }
}
