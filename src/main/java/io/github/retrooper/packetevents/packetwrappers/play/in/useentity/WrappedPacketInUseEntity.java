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
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.Hand;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.vector.Vector3d;

import java.util.Objects;
import java.util.Optional;

public final class WrappedPacketInUseEntity extends WrappedPacketEntityAbstraction {
    private static Class<? extends Enum<?>> enumEntityUseActionClass, enumHandClass;
    private EntityUseAction action;

    public WrappedPacketInUseEntity(final NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        Class<?> useEntityClass = NMSUtils.getNMSClassWithoutException("PacketPlayInUseEntity");
        try {
            enumHandClass = NMSUtils.getNMSEnumClass("EnumHand");
        } catch (ClassNotFoundException e) {
            //Probably a 1.7.10 or 1.8.x server
        }
        try {
            enumEntityUseActionClass = NMSUtils.getNMSEnumClass("EnumEntityUseAction");
        } catch (ClassNotFoundException e) {
            //That is fine, it is probably a subclass
            enumEntityUseActionClass = SubclassUtil.getEnumSubClass(useEntityClass, "EnumEntityUseAction");
        }
    }

    public Optional<Vector3d> getTarget() {
        if (PacketEvents.get().getServerUtils().getVersion() == ServerVersion.v_1_7_10
                || getAction() != EntityUseAction.INTERACT_AT) {
            return Optional.empty();
        }
        Object vec3DObj = readObject(0, NMSUtils.vec3DClass);
        WrappedPacket vec3DWrapper = new WrappedPacket(new NMSPacket(vec3DObj));
        return Optional.of(new Vector3d(vec3DWrapper.readDouble(0), vec3DWrapper.readDouble(1), vec3DWrapper.readDouble(2)));
    }

    public void setTarget(Vector3d target) {
        if (PacketEvents.get().getServerUtils().getVersion() != ServerVersion.v_1_7_10
                && getAction() == EntityUseAction.INTERACT_AT) {
            Object vec3DObj = NMSUtils.generateVec3D(target.x, target.y, target.z);
            write(NMSUtils.vec3DClass, 0, vec3DObj);
        }
    }

    public EntityUseAction getAction() {
        if (action == null) {
            Enum<?> useActionEnum = readEnumConstant(0, enumEntityUseActionClass);
            if (useActionEnum == null) {
                //This happens on some weird spigots apparently? Not sure why this field is null.
                return EntityUseAction.INTERACT;
            }
            return action = EntityUseAction.valueOf(useActionEnum.name());
        }
        return action;
    }

    public void setAction(EntityUseAction action) {
        this.action = action;
        Enum<?> enumConst = EnumUtil.valueOf(enumEntityUseActionClass, action.name());
        writeEnumConstant(0, enumConst);
    }

    public Hand getHand() {
        if ((getAction() == EntityUseAction.INTERACT || getAction() == EntityUseAction.INTERACT_AT)
                && PacketEvents.get().getServerUtils().getVersion().isNewerThan(ServerVersion.v_1_8_8)) {
            Object enumHandObj = readObject(0, enumHandClass);
            //Should actually never be null, but we will handle such a case
            if (enumHandObj == null) {
                return Hand.MAIN_HAND;
            }
            return Hand.valueOf(Objects.requireNonNull(enumHandObj).toString());
        }
        return Hand.MAIN_HAND;
    }

    public void setHand(Hand hand) {
        if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_9) &&
                (getAction() == EntityUseAction.INTERACT || getAction() == EntityUseAction.INTERACT_AT)) {

            Enum<?> enumConst = EnumUtil.valueOf(enumHandClass, hand.name());
            writeEnumConstant(0, enumConst);
        }
    }

    public enum EntityUseAction {
        INTERACT, INTERACT_AT, ATTACK
    }
}
