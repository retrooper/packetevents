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

package io.github.retrooper.packetevents.utils.dependencies.viaversion;

import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.util.reflection.Reflection;
import com.github.retrooper.packetevents.util.reflection.ReflectionObject;
import io.netty.buffer.ByteBuf;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ViaVersionAccessorImplLegacy implements ViaVersionAccessor {
    private Class<?> viaClass;
    private Class<?> viaManagerClass;
    private Class<?> protocolInfoClass;
    private Class<?> bukkitDecodeHandlerClass;
    private Class<?> bukkitEncodeHandlerClass;
    private Class<?> cancelCodecExceptionClass;
    private Class<?> informativeExceptionClass;
    private Class<? extends Enum<?>> protocolStateClass;
    private Field viaManagerField;
    private Method apiAccessor;
    private Method getPlayerVersionMethod;
    private Method checkServerBoundMethod;
    private Method checkClientBoundMethod;
    private Class<?> userConnectionClass;
    private Method cancelDecoderExceptionGenerator;
    private Method cancelEncoderExceptionGenerator;
    private Method transformServerboundMethod;
    private Method transformClientboundMethod;
    private Method setActiveMethod;
    private Method isActiveMethod;
    private Method getProtocolInfoMethod;

    private void load() {
        if (viaClass == null) {
            try {
                viaClass = Class.forName("us.myles.ViaVersion.api.Via");

                viaManagerClass = Class.forName("us.myles.ViaVersion.ViaManager");
                viaManagerField = viaClass.getDeclaredField("manager");

                protocolInfoClass = Class.forName("us.myles.ViaVersion.protocols.base.ProtocolInfo");

                protocolStateClass = (Class<? extends Enum<?>>) Class.forName("us.myles.ViaVersion.packets.State");

                bukkitDecodeHandlerClass = Class.forName("us.myles.ViaVersion.bukkit.handlers.BukkitDecodeHandler");
                bukkitEncodeHandlerClass = Class.forName("us.myles.ViaVersion.bukkit.handlers.BukkitEncodeHandler");
                cancelCodecExceptionClass = Class.forName("us.myles.ViaVersion.exception.CancelCodecException");
                informativeExceptionClass = Class.forName("us.myles.ViaVersion.exception.InformativeException");

                Class<?> viaAPIClass = Class.forName("us.myles.ViaVersion.api.ViaAPI");
                apiAccessor = viaClass.getMethod("getAPI");
                getPlayerVersionMethod = viaAPIClass.getMethod("getPlayerVersion", Object.class);
                checkServerBoundMethod = viaAPIClass.getDeclaredMethod("checkServerBound");
                checkClientBoundMethod = viaAPIClass.getDeclaredMethod("checkClientBound");
            } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        if (userConnectionClass == null) {
            try {
                userConnectionClass = Class.forName("us.myles.ViaVersion.api.data.UserConnection");
                transformServerboundMethod = Reflection.getMethod(userConnectionClass, "transformServerbound", 0);
                transformClientboundMethod = Reflection.getMethod(userConnectionClass, "transformClientbound", 0);
                setActiveMethod = Reflection.getMethod(userConnectionClass, "setActive", 0);
                isActiveMethod = Reflection.getMethod(userConnectionClass, "isActive", 0);
                cancelDecoderExceptionGenerator = Reflection.getMethod(Class.forName("us.myles.ViaVersion.exception.CancelDecoderException"), "generate", 0);
                cancelEncoderExceptionGenerator = Reflection.getMethod(Class.forName("us.myles.ViaVersion.exception.CancelEncoderException"), "generate", 0);
                getProtocolInfoMethod = Reflection.getMethod(userConnectionClass, "getProtocolInfo", 0);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getProtocolVersion(Player player) {
        load();
        try {
            Object viaAPI = apiAccessor.invoke(null);
            return (int) getPlayerVersionMethod.invoke(viaAPI, player);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean isDebug() {
        load();
        try {
            Object manager = viaManagerField.get(null);
            ReflectionObject reflectManager = new ReflectionObject(manager);
            return reflectManager.readBoolean(0);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Exception throwCancelDecoderException(Throwable throwable) {
        load();
        try {
            return (Exception) cancelDecoderExceptionGenerator.invoke(throwable);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Exception throwCancelEncoderException(Throwable throwable) {
        load();
        try {
            return (Exception) cancelEncoderExceptionGenerator.invoke(throwable);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void transformPacket(Object userConnectionObj, Object byteBufObj, boolean clientSide) {
        load();
        ByteBuf byteBuf = (ByteBuf) byteBufObj;
        try {
            if (clientSide) {
                transformServerboundMethod.invoke(userConnectionObj, byteBuf, cancelDecoderExceptionGenerator.invoke(null));
            } else {
                transformClientboundMethod.invoke(userConnectionObj, byteBuf, cancelEncoderExceptionGenerator.invoke(null));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setUserConnectionActive(Object userConnectionObj, boolean active) {
        load();
        try {
            setActiveMethod.invoke(userConnectionObj, active);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isUserConnectionActive(Object userConnectionObj) {
        load();
        try {
            return (boolean) isActiveMethod.invoke(userConnectionObj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean checkServerboundPacketUserConnection(Object userConnectionObj) {
        load();
        try {
            return (boolean) checkServerBoundMethod.invoke(userConnectionObj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean checkClientboundPacketUserConnection(Object userConnectionObj) {
        load();

        try {
            return (boolean) checkClientBoundMethod.invoke(userConnectionObj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ConnectionState getUserConnectionProtocolState(Object userConnectionObj) {
        try {
            Object protocolInfo = getProtocolInfoMethod.invoke(userConnectionObj);
            ReflectionObject reflectProtocolInfo = new ReflectionObject(protocolInfo);
            return ConnectionState.VALUES[reflectProtocolInfo.readEnumConstant(0, protocolStateClass).ordinal()];
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Class<?> getUserConnectionClass() {
        load();
        return userConnectionClass;
    }

    @Override
    public Class<?> getBukkitDecodeHandlerClass() {
        load();
        return bukkitDecodeHandlerClass;
    }

    @Override
    public Class<?> getBukkitEncodeHandlerClass() {
        load();
        return bukkitEncodeHandlerClass;
    }

    @Override
    public Class<?> getCancelCodecExceptionClass() {
        load();
        return cancelCodecExceptionClass;
    }

    @Override
    public Class<?> getInformativeExceptionClass() {
        load();
        return informativeExceptionClass;
    }
}
