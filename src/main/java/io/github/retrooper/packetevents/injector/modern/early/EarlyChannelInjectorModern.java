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
import io.github.retrooper.packetevents.injector.EarlyInjector;
import io.github.retrooper.packetevents.injector.modern.PlayerChannelHandlerModern;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.list.ListWrapper;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EarlyChannelInjectorModern implements EarlyInjector {
    private final List<ChannelFuture> injectedFutures = new ArrayList<>();
    private final List<Map<Field, Object>> injectedLists = new ArrayList<>();

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

    @Override
    public void inject() {
        try {
            if (PaperChannelInjector.PAPER_INJECTION_METHOD) {
                PaperChannelInjector.setPaperChannelInitializeListener();
                return;
            }
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
            throw new IllegalStateException("PacketEvents failed to inject!", ex);
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

                if (channel.pipeline().get(PacketEvents.get().getHandlerName()) != null) {
                    channel.pipeline().remove(PacketEvents.get().getHandlerName());
                }

                if (channel.pipeline().get("packet_handler") != null) {
                    channel.pipeline().addBefore("packet_handler", PacketEvents.get().getHandlerName(), new PlayerChannelHandlerModern());
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
            } catch (Exception ignored) {
            }
        }

        if (bootstrapAcceptor == null) {
            bootstrapAcceptor = channelFuture.channel().pipeline().first();
        }

        ChannelInitializer<?> oldChannelInitializer;
        try {
            oldChannelInitializer = (ChannelInitializer<?>) bootstrapAcceptorField.get(bootstrapAcceptor);

            ChannelInitializer<?> channelInitializer = new PEChannelInitializerModern(oldChannelInitializer);

            //Replace the old channel initializer with our own.
            bootstrapAcceptorField.setAccessible(true);
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
        // TODO: Uninject from players currently online to prevent protocol lib issues.
        if (PaperChannelInjector.PAPER_INJECTION_METHOD) {
            try {
                PaperChannelInjector.removePaperChannelInitializeListener();
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
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

                    ChannelInitializer<Channel> oldInit = (ChannelInitializer<Channel>) childHandlerField.get(handler);
                    if (oldInit instanceof PEChannelInitializerModern) {
                        bootstrapAcceptor = handler;
                    }
                } catch (Exception ignored) {
                    // Not this one
                }
            }
            // Default to first
            if (bootstrapAcceptor == null) {
                bootstrapAcceptor = future.channel().pipeline().first();
            }

            try {
                ChannelInitializer<Channel> oldInit = (ChannelInitializer<Channel>) childHandlerField.get(bootstrapAcceptor);
                if (oldInit instanceof PEChannelInitializerModern) {
                    childHandlerField.setAccessible(true);
                    childHandlerField.set(bootstrapAcceptor, ((PEChannelInitializerModern) oldInit).getOldChannelInitializer());
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
        Object channel = PacketEvents.get().getPlayerUtils().getChannel(player);
        if (channel != null) {
            updatePlayerObject(player, channel);
        }
    }

    @Override
    public void ejectPlayer(Player player) {
        Object channel = PacketEvents.get().getPlayerUtils().getChannel(player);
        if (channel != null) {
            Channel chnl = (Channel) channel;
            try {
                chnl.pipeline().remove(PacketEvents.get().getHandlerName());
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public boolean hasInjected(Player player) {
        Object channel = PacketEvents.get().getPlayerUtils().getChannel(player);
        if (channel == null) {
            return false;
        }
        PlayerChannelHandlerModern handler = getHandler(channel);
        return handler != null && handler.player != null;
    }

    @Override
    public void sendPacket(Object ch, Object rawNMSPacket) {
        Channel channel = (Channel) ch;
        channel.writeAndFlush(rawNMSPacket);
    }

    @Override
    public void sendPacketWithoutFlush(Object ch, Object rawNMSPacket) {
        Channel channel = (Channel) ch;
        channel.write(rawNMSPacket);
    }

    private PlayerChannelHandlerModern getHandler(Object rawChannel) {
        Channel channel = (Channel) rawChannel;
        ChannelHandler handler = channel.pipeline().get(PacketEvents.get().getHandlerName());
        if (handler instanceof PlayerChannelHandlerModern) {
            return (PlayerChannelHandlerModern) handler;
        } else {
            return null;
        }
    }

    @Override
    public void updatePlayerObject(Player player, Object rawChannel) {
        PlayerChannelHandlerModern handler = getHandler(rawChannel);
        if (handler != null) {
            handler.player = player;
        }
    }
}
