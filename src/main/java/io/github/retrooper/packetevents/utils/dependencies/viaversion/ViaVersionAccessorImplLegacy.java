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

import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.netty.buffer.ByteBuf;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ViaVersionAccessorImplLegacy implements ViaVersionAccessor {
    private static Class<?> viaClass;
    private static Method apiAccessor;
    private static Method getPlayerVersionMethod;
    private static Class<?> userConnectionClass;
    private static Method cancelDecoderExceptionGenerator;
    private static Method cancelEncoderExceptionGenerator;
    private Method transformServerboundMethod;
    private Method transformClientboundMethod;
    private Method setActiveMethod;
    private Method isActiveMethod;

    private void load() {
        if (viaClass == null) {
            try {
                viaClass = Class.forName("us.myles.ViaVersion.api.Via");
                Class<?> viaAPIClass = Class.forName("us.myles.ViaVersion.api.ViaAPI");
                apiAccessor = viaClass.getMethod("getAPI");
                getPlayerVersionMethod = viaAPIClass.getMethod("getPlayerVersion", Object.class);
            } catch (ClassNotFoundException | NoSuchMethodException e) {
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
                cancelDecoderExceptionGenerator= Reflection.getMethod(Class.forName("us.myles.ViaVersion.exception.CancelDecoderException"), "generate", 0);
                cancelEncoderExceptionGenerator= Reflection.getMethod(Class.forName("us.myles.ViaVersion.exception.CancelEncoderException"), "generate", 0);
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
    public void transformPacket(Object userConnectionObj, Object byteBufObj, boolean clientSide) {
        load();
        ByteBuf byteBuf = (ByteBuf) byteBufObj;
        try {
            if (clientSide) {
                transformServerboundMethod.invoke(userConnectionObj, byteBuf, cancelDecoderExceptionGenerator.invoke(null));
            } else {
                transformClientboundMethod.invoke(userConnectionObj, byteBuf, cancelEncoderExceptionGenerator.invoke(null));
            }
        }
        catch (Exception ex) {
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
    public Class<?> getUserConnectionClass() {
        load();
        return userConnectionClass;
    }
}
