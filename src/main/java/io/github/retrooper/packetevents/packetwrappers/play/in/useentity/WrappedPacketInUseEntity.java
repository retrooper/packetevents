/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package io.github.retrooper.packetevents.packetwrappers.play.in.useentity;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.Hand;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.vector.Vector3d;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public final class WrappedPacketInUseEntity extends WrappedPacketEntityAbstraction {
    private static Class<? extends Enum<?>> enumEntityUseActionClass;
    private static Class<?> obfuscatedDataInterface, obfuscatedHandContainerClass, obfuscatedTargetAndHandContainerClass;
    private static Method getObfuscatedEntityUseActionMethod;
    private static boolean v_1_7_10, v_1_9, v_1_17;
    private EntityUseAction action;
    private Object obfuscatedDataObj;

    public WrappedPacketInUseEntity(final NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        v_1_7_10 = version.isOlderThan(ServerVersion.v_1_8);
        v_1_9 = version.isNewerThanOrEquals(ServerVersion.v_1_9);
        v_1_17 = version.isNewerThanOrEquals(ServerVersion.v_1_17);
        try {
            enumEntityUseActionClass = NMSUtils.getNMSEnumClass("EnumEntityUseAction");
        } catch (ClassNotFoundException e) {
            //That is fine, it is probably a subclass
            if (v_1_17) {
                enumEntityUseActionClass = SubclassUtil.getEnumSubClass(PacketTypeClasses.Play.Client.USE_ENTITY, 3);
                obfuscatedDataInterface = SubclassUtil.getSubClass(PacketTypeClasses.Play.Client.USE_ENTITY, 0);
                obfuscatedHandContainerClass = SubclassUtil.getSubClass(PacketTypeClasses.Play.Client.USE_ENTITY, 1);
                obfuscatedTargetAndHandContainerClass = SubclassUtil.getSubClass(PacketTypeClasses.Play.Client.USE_ENTITY, 2);
                getObfuscatedEntityUseActionMethod = Reflection.getMethod(obfuscatedDataInterface, enumEntityUseActionClass, 0);
            } else {
                enumEntityUseActionClass = SubclassUtil.getEnumSubClass(PacketTypeClasses.Play.Client.USE_ENTITY, "EnumEntityUseAction");
            }
        }
    }

    public Optional<Vector3d> getTarget() {
        if (v_1_7_10 || getAction() != EntityUseAction.INTERACT_AT) {
            return Optional.empty();
        }
        Object vec3DObj;
        if (v_1_17) {
            //TODO Add 1.17 support
            vec3DObj = null;
        } else {
            vec3DObj = readObject(0, NMSUtils.vec3DClass);
        }
        WrappedPacket vec3DWrapper = new WrappedPacket(new NMSPacket(vec3DObj));
        return Optional.of(new Vector3d(vec3DWrapper.readDouble(0), vec3DWrapper.readDouble(1), vec3DWrapper.readDouble(2)));
    }

    public void setTarget(Vector3d target) {
        //TODO add 1.17 support
        if (v_1_7_10 && getAction() == EntityUseAction.INTERACT_AT) {
            Object vec3DObj = NMSUtils.generateVec3D(target.x, target.y, target.z);
            write(NMSUtils.vec3DClass, 0, vec3DObj);
        }
    }

    public EntityUseAction getAction() {
        if (action == null) {
            Enum<?> useActionEnum;
            if (v_1_17) {
                if (obfuscatedDataObj == null) {
                    obfuscatedDataObj = readObject(0, obfuscatedDataInterface);
                }
                try {
                    useActionEnum = (Enum<?>) getObfuscatedEntityUseActionMethod.invoke(obfuscatedDataObj);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                useActionEnum = readEnumConstant(0, enumEntityUseActionClass);
                if (useActionEnum == null) {
                    //This happens on some weird spigots apparently? Not sure why this is null.
                    return action = EntityUseAction.INTERACT;
                }
            }
            return action = EntityUseAction.values()[useActionEnum.ordinal()];
        }
        return action;
    }

    public void setAction(EntityUseAction action) {
        //TODO add 1.17 support
        this.action = action;
        Enum<?> enumConst = EnumUtil.valueByIndex(enumEntityUseActionClass, action.ordinal());
        writeEnumConstant(0, enumConst);
    }

    public Hand getHand() {
        if (v_1_9 && (getAction() == EntityUseAction.INTERACT || getAction() == EntityUseAction.INTERACT_AT)) {
            Enum<?> enumHandConst;
            if (v_1_17) {
                if (obfuscatedDataObj == null) {
                    obfuscatedDataObj = readObject(0, obfuscatedDataInterface);
                }
                if (obfuscatedHandContainerClass.isInstance(obfuscatedDataObj)) {
                    Object obfuscatedHandContainerObj = obfuscatedHandContainerClass.cast(obfuscatedDataObj);
                    WrappedPacket wrappedHandContainer = new WrappedPacket(new NMSPacket(obfuscatedHandContainerObj));
                    System.out.println("HAND");
                    enumHandConst = wrappedHandContainer.readEnumConstant(0, NMSUtils.enumHandClass);
                }
                else if (obfuscatedTargetAndHandContainerClass.isInstance(obfuscatedDataObj)) {
                    Object obfuscatedTargetAndHandContainerObj = obfuscatedTargetAndHandContainerClass.cast(obfuscatedDataObj);
                    WrappedPacket wrappedTargetAndHandContainer = new WrappedPacket(new NMSPacket(obfuscatedTargetAndHandContainerObj));
                    System.out.println("TARGET AND HAND");
                    enumHandConst = wrappedTargetAndHandContainer.readEnumConstant(0, NMSUtils.enumHandClass);
                }
                else {
                    System.out.println("NONE");
                    return Hand.MAIN_HAND;
                }
            } else {
                enumHandConst = readEnumConstant(0, NMSUtils.enumHandClass);
            }
            //Should actually never be null, but we will handle such a case
            if (enumHandConst == null) {
                return Hand.MAIN_HAND;
            }
            return Hand.values()[enumHandConst.ordinal()];
        }
        return Hand.MAIN_HAND;
    }

    public void setHand(Hand hand) {
        if (v_1_9 && (getAction() == EntityUseAction.INTERACT || getAction() == EntityUseAction.INTERACT_AT)) {
            //TODO 1.17 support
            Enum<?> enumConst = EnumUtil.valueByIndex(NMSUtils.enumHandClass, hand.ordinal());
            writeEnumConstant(0, enumConst);
        }
    }

    public enum EntityUseAction {
        INTERACT, ATTACK, INTERACT_AT
    }
}
