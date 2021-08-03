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

    public static void setPaperChannelInitializeListener() throws ReflectiveOperationException {
        // Call io.papermc.paper.network.ChannelInitializeListenerHolder.addListener(net.kyori.adventure.key.Key, io.papermc.paper.network.ChannelInitializeListener)
        // Create an interface proxy of ChannelInitializeListener
        Class<?> listenerClass = Class.forName("io.papermc.paper.network.ChannelInitializeListener");
        Object channelInitializeListener = Proxy.newProxyInstance(EarlyChannelInjectorModern.class.getClassLoader(), new Class[]{listenerClass}, (proxy, method, args) -> {
            if (method.getName().equals("afterInitChannel")) {
                PEChannelInitializerModern.postInitChannel((Channel) args[0]);
                return null;
            }
            return method.invoke(proxy, args);
        });

        Class<?> holderClass = Class.forName("io.papermc.paper.network.ChannelInitializeListenerHolder");
        Method addListenerMethod = holderClass.getDeclaredMethod("addListener", Key.class, listenerClass);
        Key key = Key.key(PacketEvents.get().handlerName, "injector");
        addListenerMethod.invoke(null, key, channelInitializeListener);
    }

    public static void removePaperChannelInitializeListener() throws ReflectiveOperationException {
        Class<?> holderClass = Class.forName("io.papermc.paper.network.ChannelInitializeListenerHolder");
        Method addListenerMethod = holderClass.getDeclaredMethod("removeListener", Key.class);
        Key key = Key.key(PacketEvents.get().handlerName.toLowerCase(), "injector");
        addListenerMethod.invoke(null, key);
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
