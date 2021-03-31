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

package io.github.retrooper.packetevents.injector.earlyinjector.legacy;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.injector.earlyinjector.EarlyInjector;
import io.github.retrooper.packetevents.injector.handler.legacy.PlayerChannelHandlerLegacy;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.list.ListWrapper;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import net.minecraft.util.io.netty.channel.Channel;
import net.minecraft.util.io.netty.channel.ChannelFuture;
import net.minecraft.util.io.netty.channel.ChannelHandler;
import net.minecraft.util.io.netty.channel.ChannelInitializer;
import net.minecraft.util.io.netty.channel.socket.SocketChannel;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Early channel injector for 1.7.10 server versions.
 * Thank you ViaVersion.
 * https://github.com/ViaVersion/ViaVersion/tree/master/bukkit/src/main/java/us/myles/ViaVersion/bukkit/platform/BukkitViaInjector.java
 *
 * @author retrooper, Thomazz, ViaVersion
 * @since 1.8
 */
public class EarlyChannelInjectorLegacy implements EarlyInjector {
    private final List<ChannelFuture> injectedFutures = new ArrayList<>();
    private final List<Map<Field, Object>> injectedLists = new ArrayList<>();

    @Override
    public void inject() {
        try {
            Object serverConnection = NMSUtils.getMinecraftServerConnection();
            for (Field field : serverConnection.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = null;
                try {
                    value = field.get(serverConnection);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (value instanceof List) {
                    //Get the list.
                    List listWrapper = new ListWrapper((List) value) {
                        @Override
                        public void processAdd(Object o) {
                            if (o instanceof ChannelFuture) {
                                try {
                                    injectChannelFuture((ChannelFuture) o);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    };
                    HashMap<Field, Object> map = new HashMap<>();
                    map.put(field, serverConnection);
                    injectedLists.add(map);

                    field.set(serverConnection, listWrapper);

                    synchronized (listWrapper) {
                        for (Object serverChannel : (List) value) {
                            //Is this the server channel future list?
                            if (serverChannel instanceof ChannelFuture) {
                                //Yes it is...
                                injectChannelFuture((ChannelFuture) serverChannel);
                            } else {
                                break;//Wrong list
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            PacketEvents.get().getPlugin().getLogger().severe("PacketEvents failed to inject!");
            ex.printStackTrace();
        }

        //Player channels might have been registered already. Let us add our handlers. We are a little late though.
        //This only happens when you join extremely early on older versions of minecraft.
        List<Object> networkManagers = NMSUtils.getNetworkManagers();
        synchronized (networkManagers) {
            for (Object networkManager : networkManagers) {
                WrappedPacket networkManagerWrapper = new WrappedPacket(new NMSPacket(networkManager));
                Channel channel = (Channel) networkManagerWrapper.readObject(0, NMSUtils.nettyChannelClass);
                if (channel == null) {
                    continue;
                }

                if (channel.pipeline().get("packet_handler") != null) {
                    channel.pipeline().addBefore("packet_handler", PacketEvents.handlerName, new PlayerChannelHandlerLegacy());
                }
            }
        }
    }


    private void injectChannelFuture(ChannelFuture channelFuture) {
        List<String> channelHandlerNames = channelFuture.channel().pipeline().names();
        ChannelHandler bootstrapAcceptor = null;
        Field bootstrapAcceptorField = null;
        for (String handlerName : channelHandlerNames) {
            ChannelHandler handler = channelFuture.channel().pipeline().get(handlerName);
            try {
                bootstrapAcceptorField = handler.getClass().getDeclaredField("childHandler");
                bootstrapAcceptorField.setAccessible(true);
                bootstrapAcceptorField.get(handler);
                bootstrapAcceptor = handler;
            } catch (Exception ex) {

            }
        }

        if (bootstrapAcceptor == null) {
            bootstrapAcceptor = channelFuture.channel().pipeline().first();
        }

        ChannelInitializer<?> oldChannelInitializer = null;
        try {
            oldChannelInitializer = (ChannelInitializer<?>) bootstrapAcceptorField.get(bootstrapAcceptor);

            ChannelInitializer<?> channelInitializer = new PEChannelInitializerLegacy(oldChannelInitializer);

            Reflection.getFieldWithoutFinalModifier(bootstrapAcceptorField);

            //Replace the old channel initializer with our own.
            bootstrapAcceptorField.set(bootstrapAcceptor, channelInitializer);
            injectedFutures.add(channelFuture);
        } catch (IllegalAccessException e) {
            ClassLoader cl = bootstrapAcceptor.getClass().getClassLoader();
            if (cl.getClass().getName().equals("org.bukkit.plugin.java.PluginClassLoader")) {
                PluginDescriptionFile yaml = null;
                try {
                    yaml = (PluginDescriptionFile) PluginDescriptionFile.class.getDeclaredField("description").get(cl);
                } catch (IllegalAccessException | NoSuchFieldException e2) {
                    e2.printStackTrace();
                }
                throw new IllegalStateException("PacketEvents failed to inject, because of " + bootstrapAcceptor.getClass().getName() + ", you might want to try running without " + yaml.getName() + "?");
            } else {
                throw new IllegalStateException("PacketEvents failed to find core component 'childHandler', please check your plugins. issue: " + bootstrapAcceptor.getClass().getName());
            }
        }

    }

    @Override
    public void eject() {
        Field childHandlerField = null;
        for (ChannelFuture future : injectedFutures) {
            List<String> names = future.channel().pipeline().names();
            ChannelHandler bootstrapAcceptor = null;
            // Pick best
            for (String name : names) {
                try {
                    ChannelHandler handler = future.channel().pipeline().get(name);
                    if (childHandlerField == null) {
                        childHandlerField = handler.getClass().getDeclaredField("childHandler");
                        childHandlerField.setAccessible(true);
                    }

                    ChannelInitializer<SocketChannel> oldInit = (ChannelInitializer<SocketChannel>) childHandlerField.get(handler);
                    if (oldInit instanceof PEChannelInitializerLegacy) {
                        bootstrapAcceptor = handler;
                    }
                } catch (Exception e) {
                    // Not this one
                }
            }
            // Default to first
            if (bootstrapAcceptor == null) {
                bootstrapAcceptor = future.channel().pipeline().first();
            }

            try {
                Reflection.getFieldWithoutFinalModifier(childHandlerField);
                ChannelInitializer<SocketChannel> oldInit = (ChannelInitializer<SocketChannel>) childHandlerField.get(bootstrapAcceptor);
                if (oldInit instanceof PEChannelInitializerLegacy) {
                    childHandlerField.set(bootstrapAcceptor, oldInit);
                    childHandlerField.set(bootstrapAcceptor, ((PEChannelInitializerLegacy) oldInit).getOldChannelInitializer());
                }
            } catch (Exception e) {
                PacketEvents.get().getPlugin().getLogger().severe("PacketEvents failed to eject the injection handler! Please reboot!");
            }
        }
        injectedFutures.clear();

        for (Map<Field, Object> map : injectedLists) {
            try {
                for (Field key : map.keySet()) {
                    key.setAccessible(true);
                    Object o = map.get(key);
                    if (o instanceof ListWrapper) {
                        key.set(o, ((ListWrapper) o).getOriginalList());
                    }
                }

            } catch (IllegalAccessException e) {
                PacketEvents.get().getPlugin().getLogger().severe("PacketEvents failed to eject the injection handler! Please reboot!!");
            }
        }

        injectedLists.clear();
    }

    @Override
    public void injectPlayer(Player player) {
        Object channel = PacketEvents.get().packetProcessorInternal.getChannel(player);
        if (channel != null) {
            updatePlayerObject(player, channel);
        }
    }

    @Override
    public void ejectPlayer(Player player) {
        Object channel = PacketEvents.get().packetProcessorInternal.getChannel(player);
        if (channel != null) {
            Channel chnl = (Channel) channel;
            try {
                chnl.pipeline().remove(PacketEvents.handlerName);
            }
            catch (Exception ex) {

            }
        }
    }

    @Override
    public boolean hasInjected(Player player) {
        Object channel = PacketEvents.get().packetProcessorInternal.getChannel(player);
        if (channel == null) {
            return false;
        }
        PlayerChannelHandlerLegacy handler = getHandler(channel);
        return handler != null && handler.player != null;
    }

    @Override
    public void sendPacket(Object ch, Object rawNMSPacket) {
        Channel channel = (Channel) ch;
        channel.writeAndFlush(rawNMSPacket);
    }

    private PlayerChannelHandlerLegacy getHandler(Object rawChannel) {
        Channel channel = (Channel) rawChannel;
        ChannelHandler handler = channel.pipeline().get(PacketEvents.handlerName);
        if (handler instanceof PlayerChannelHandlerLegacy) {
            return (PlayerChannelHandlerLegacy) handler;
        } else {
            return null;
        }
    }

    @Override
    public void updatePlayerObject(Player player, Object rawChannel) {
        PlayerChannelHandlerLegacy handler = getHandler(rawChannel);
        if (handler != null) {
            handler.player = player;
        }
    }

    @Override
    public boolean isBound() {
        try {
            Object connection = NMSUtils.getMinecraftServerConnection();
            if (connection == null) {
                return false;
            }
            for (Field field : connection.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                final Object value = field.get(connection);
                if (value instanceof List) {
                    // Inject the list
                    synchronized (value) {
                        for (Object o : (List) value) {
                            if (o instanceof ChannelFuture) {
                                return true;
                            } else {
                                break; // not the right list.
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return false;
    }
}
