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
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.list.ListWrapper;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;

/**
 * Early channel injector for all server versions higher than 1.7.10.
 * Thank you ViaVersion.
 * https://github.com/ViaVersion/ViaVersion/tree/master/bukkit/src/main/java/us/myles/ViaVersion/bukkit/platform/BukkitViaInjector.java
 * @author retrooper, Thomazz, ViaVersion
 * @since 1.8
 */
public class EarlyChannelInjector implements EarlyInjector {
    @Override
    public void inject() {
        Object serverConnection = NMSUtils.getMinecraftServerConnection();
        WrappedPacket serverConnectionWrapper = new WrappedPacket(new NMSPacket(serverConnection));
        boolean searching = true;
        for (int i = 0; searching; i++) {
            try {
                //Get the list.
                List<Object> serverChannelList = (List<Object>) serverConnectionWrapper.readObject(i, List.class);
                List listWrapper = new ListWrapper(serverChannelList) {
                    @Override
                    public void processAdd(Object o) {
                        if (o instanceof ChannelFuture) {
                            try {
                                injectChannelFuture((ChannelFuture) o);
                            }
                            catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                };

                synchronized (listWrapper) {
                    for (Object serverChannel : serverChannelList) {
                        //Is this the server channel future list?
                        if (serverChannel instanceof ChannelFuture) {
                            //Yes it is...
                            injectChannelFuture((ChannelFuture) serverChannel);
                            searching = false;
                            System.out.println("Finished successful injection...");
                        }
                    }
                }
                if (!searching) {
                    serverConnectionWrapper.write(List.class, i, listWrapper);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                break;
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

        //Remove final modifier
        Field modifiersField = null;
        try {
            modifiersField = Field.class.getDeclaredField("modifiers");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        modifiersField.setAccessible(true);
        try {
            modifiersField.setInt(bootstrapAcceptorField, bootstrapAcceptorField.getModifiers() & ~Modifier.FINAL);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //Replace the old channel initializer with our own.
        try {
            bootstrapAcceptorField.set(bootstrapAcceptor, channelInitializer);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eject() {

    }

    @Override
    public void injectPlayer(Player player) {
        Object channel = PacketEvents.get().packetProcessorInternal.getChannel(player);
        if (channel != null) {
            updatePlayerObject(player, channel);
        } else {
            System.out.println("Channel is null...");
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
