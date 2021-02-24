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

package io.github.retrooper.packetevents.injector.earlyinjector.modern;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.injector.earlyinjector.EarlyInjector;
import io.github.retrooper.packetevents.utils.list.ConcurrentList;
import io.github.retrooper.packetevents.utils.list.ListWrapper;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Early channel injector for all server versions higher than 1.7.10.
 * Thank you ViaVersion.
 * https://github.com/ViaVersion/ViaVersion/tree/master/bukkit/src/main/java/us/myles/ViaVersion/bukkit/platform/BukkitViaInjector.java
 *
 * @author retrooper, Thomazz, ViaVersion
 * @since 1.8
 */
public class EarlyChannelInjector implements EarlyInjector {
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
                            }
                            else {
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
    }

    private void injectChannelFuture(ChannelFuture channelFuture) {
        List<String> channelHandlerNames = channelFuture.channel().pipeline().names();
        ChannelHandler bootstrapAcceptor = null;
        Field bootstrapAcceptorField = null;
        for (String handlerName : channelHandlerNames) {
            ChannelHandler handler = channelFuture.channel().pipeline().get(handlerName);
            try {
                Field field = handler.getClass().getDeclaredField("childHandler");
                bootstrapAcceptor = handler;
                bootstrapAcceptorField = field;
                bootstrapAcceptorField.setAccessible(true);
            } catch (Exception ex) {

            }
        }

        ChannelInitializer<?> oldChannelInitializer = null;
        try {
            oldChannelInitializer = (ChannelInitializer<?>) bootstrapAcceptorField.get(bootstrapAcceptor);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        ChannelInitializer<?> channelInitializer = new PEChannelInitializer(oldChannelInitializer);

        Reflection.getFieldWithoutFinalModifier(bootstrapAcceptorField);

        //Replace the old channel initializer with our own.
        try {
            bootstrapAcceptorField.set(bootstrapAcceptor, channelInitializer);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        injectedFutures.add(channelFuture);
    }

    public void patchLists() {
        Object connection = NMSUtils.getMinecraftServerConnection();
        if (connection == null) {
            throw new IllegalStateException("Server connection is null?");
        }

        for (Field field : connection.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(connection);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (!(value instanceof List)) continue;
            if (value instanceof ConcurrentList) continue;

            ConcurrentList list = new ConcurrentList();
            list.addAll((Collection) value);
            try {
                field.set(connection, list);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
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
                ChannelHandler handler = future.channel().pipeline().get(name);
                try {
                    if (childHandlerField == null) {
                        childHandlerField = handler.getClass().getDeclaredField("childHandler");
                        childHandlerField.setAccessible(true);
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                try {
                    ChannelInitializer<SocketChannel> oldInit = (ChannelInitializer<SocketChannel>) childHandlerField.get(handler);
                    if (oldInit instanceof PEChannelInitializer) {
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
                if (oldInit instanceof PEChannelInitializer) {
                    childHandlerField.set(bootstrapAcceptor, ((PEChannelInitializer) oldInit));
                    childHandlerField.set(bootstrapAcceptor, ((PEChannelInitializer) oldInit).getOldChannelInitializer());
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
        } else {
            Bukkit.getScheduler().runTaskLater(PacketEvents.get().getPlugin(), () -> {
                player.kickPlayer("We were unable to inject you, please reconnect.");
            }, 20L);
        }
    }

    @Override
    public void ejectPlayer(Player player) {

    }

    @Override
    public boolean hasInjected(Player player) {
        Object channel = PacketEvents.get().packetProcessorInternal.getChannel(player);
        if (channel == null) {
            return false;
        }
        PlayerChannelHandler handler = getHandler(channel);
        if (handler == null) {
            return false;
        }
        return handler.player != null;
    }

    @Override
    public void sendPacket(Object ch, Object rawNMSPacket) {
        Channel channel = (Channel) ch;
        channel.writeAndFlush(rawNMSPacket);
    }

    private PlayerChannelHandler getHandler(Object rawChannel) {
        Channel channel = (Channel) rawChannel;
        ChannelHandler handler = channel.pipeline().get(PacketEvents.handlerName);
        if (handler instanceof PlayerChannelHandler) {
            return (PlayerChannelHandler) handler;
        } else {
            return null;
        }
    }

    @Override
    public void updatePlayerObject(Player player, Object rawChannel) {
        PlayerChannelHandler handler = getHandler(rawChannel);
        if (handler != null) {
            handler.player = player;
        }
    }
}
