/*
 * This file is part of ViaVersion - https://github.com/ViaVersion/ViaVersion
 * Copyright (C) 2016-2021 ViaVersion and contributors
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

package io.github.retrooper.packetevents.injector.modern.early;

import io.github.retrooper.packetevents.PacketEvents;
import io.netty.channel.Channel;
import net.kyori.adventure.key.Key;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class PaperChannelInjector {
    public static final boolean PAPER_INJECTION_METHOD = hasPaperInjectionMethod();

    private PaperChannelInjector() {
    }

    private static void addChannelInitializeListenerHolderListener(Key key, Object listener) throws Exception {
        Class<?> listenerClass = Class.forName("io.papermc.paper.network.ChannelInitializeListener");
        Class<?> holderClass = Class.forName("io.papermc.paper.network.ChannelInitializeListenerHolder");
        Method addListenerMethod = holderClass.getDeclaredMethod("addListener", Key.class, listenerClass);
        addListenerMethod.invoke(null, key, listener);
    }

    private static boolean hasChannelInitializeHolderListener(Key key) throws Exception {
        Class<?> holderClass = Class.forName("io.papermc.paper.network.ChannelInitializeListenerHolder");
        Method hasListenerMethod = holderClass.getDeclaredMethod("hasListener", Key.class);
        return (boolean) hasListenerMethod.invoke(null,  key);
    }

    private static void removeChannelInitializeListenerHolderListener(Key key) throws Exception {
        Class<?> holderClass = Class.forName("io.papermc.paper.network.ChannelInitializeListenerHolder");
        Method removeListenerMethod = holderClass.getDeclaredMethod("removeListener", Key.class);
        removeListenerMethod.invoke(null, key);
    }

    public static void setPaperChannelInitializeListener() throws Exception {
        // Call io.papermc.paper.network.ChannelInitializeListenerHolder.addListener(net.kyori.adventure.key.Key, io.papermc.paper.network.ChannelInitializeListener)
        // Create an interface proxy of ChannelInitializeListener
        boolean shouldHandleViaVersion = hasChannelInitializeHolderListener(Key.key("viaversion", "injector"));
        Class<?> listenerClass = Class.forName("io.papermc.paper.network.ChannelInitializeListener");
        Object channelInitializeListener = Proxy.newProxyInstance(EarlyChannelInjectorModern.class.getClassLoader(), new Class[]{listenerClass}, (proxy, method, args) -> {
            if (method.getName().equals("afterInitChannel")) {
                if (shouldHandleViaVersion) {
                    //Call Via's here
                    Class<?> viaBukkitChannelInitializer = Class.forName("com.viaversion.viaversion.bukkit.handlers.BukkitChannelInitializer");
                    Method viaAfterInitChannelMethod = viaBukkitChannelInitializer.getMethod("afterChannelInitialize", Channel.class);
                    viaAfterInitChannelMethod.invoke(null, (Channel) args[0]);
                }
                PEChannelInitializerModern.postInitChannel((Channel) args[0]);
                return null;
            }
            return method.invoke(proxy, args);
        });

        Key key = Key.key(PacketEvents.get().identifier, "injector");

        addChannelInitializeListenerHolderListener(key, channelInitializeListener);
        if (shouldHandleViaVersion) {
            removeChannelInitializeListenerHolderListener(Key.key("viaversion", "injector"));
        }
    }

    public static void removePaperChannelInitializeListener() throws Exception {
        Key key = Key.key(PacketEvents.get().identifier, "injector");
        removeChannelInitializeListenerHolderListener(key);
    }

    private static boolean hasPaperInjectionMethod() {
        try {
            Class.forName("io.papermc.paper.network.ChannelInitializeListener");
            return true;
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }
}
