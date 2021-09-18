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

package io.github.retrooper.packetevents.utils.dependencies.protocollib;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.error.ErrorReporter;
import com.comphenix.protocol.injector.ListenerInvoker;
import com.comphenix.protocol.injector.PacketFilterManager;
import com.comphenix.protocol.injector.netty.ProtocolInjector;
import com.comphenix.protocol.injector.player.PlayerInjectionHandler;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.reflection.ReflectionObject;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;

public class ProtocolLibUtil {
    public static boolean isAvailable() {
        return Bukkit.getPluginManager().getPlugin("ProtocolLib") != null;
    }

    public static void patchProtocolInjector() {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        PacketFilterManager filterManager = (PacketFilterManager) manager;
        System.out.println("filter manager: " + filterManager.getClass().getSimpleName());
        ReflectionObject reflectManager = new ReflectionObject(manager, PacketFilterManager.class);

        ProtocolInjector protocolInjector = reflectManager.read(0, ProtocolInjector.class);
        ReflectionObject reflectProtocolInjector = new ReflectionObject(protocolInjector);

        Plugin plugin = Bukkit.getPluginManager().getPlugin("ProtocolLib");
        ListenerInvoker invoker = reflectProtocolInjector.read(0, ListenerInvoker.class);
        ErrorReporter reporter = reflectProtocolInjector.read(0, ErrorReporter.class);
        CustomProtocolInjector customProtocolInjector = new CustomProtocolInjector(plugin, invoker, reporter);
        reflectManager.write(ProtocolInjector.class, 0, customProtocolInjector);

        PlayerInjectionHandler playerInjectionHandler = customProtocolInjector.getPlayerInjector();
        reflectManager.write(PlayerInjectionHandler.class, 0, playerInjectionHandler);
        System.out.println("SUCCESSFULLY PATCHED");
    }
}
