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
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import net.minecraft.util.io.netty.channel.Channel;
import net.minecraft.util.io.netty.channel.ChannelFuture;
import net.minecraft.util.io.netty.channel.ChannelHandler;
import net.minecraft.util.io.netty.channel.ChannelInitializer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Early channel injector for server version 1.7.10.
 * @author retrooper, Thomazz
 * @since 1.8
 */
public class EarlyChannelInjectorLegacy implements EarlyInjector {
    /**
     * This method will replace the channel initializer in the server connection with our own channel initializer allowing us to add our own channel handler.
     */
    @Override
    public void inject() {
        Object serverConnection = NMSUtils.getMinecraftServerConnection();
        WrappedPacket serverConnectionWrapper = new WrappedPacket(new NMSPacket(serverConnection));
        boolean searching = true;
        for (int i = 0; searching; i++) {
            try {
                //Get the list.
                List<Object> serverChannelList = (List<Object>) serverConnectionWrapper.readObject(i, List.class);
                synchronized (serverChannelList) {
                    for (Object serverChannel : serverChannelList) {
                        //Is this the server channel list?
                        if (serverChannel instanceof ChannelFuture) {
                            //Yes it is...
                            Channel channel = ((ChannelFuture) serverChannel).channel();
                            List<String> channelHandlerNames = channel.pipeline().names();
                            ChannelHandler bootstrapAcceptor = null;
                            Field bootstrapAcceptorField = null;
                            for (String handlerName : channelHandlerNames) {
                                ChannelHandler handler = channel.pipeline().get(handlerName);
                                try {
                                    Field field = handler.getClass().getDeclaredField("childHandler");
                                    bootstrapAcceptor = handler;
                                    bootstrapAcceptorField = field;
                                    bootstrapAcceptorField.setAccessible(true);
                                }
                                catch (Exception ex) {

                                }
                            }


                            ChannelInitializer<?> oldChannelInitializer = (ChannelInitializer<?>) bootstrapAcceptorField.get(bootstrapAcceptor);
                            ChannelInitializer<?> channelInitializer = new PEChannelInitializerLegacy(oldChannelInitializer);

                            //REMOVE FINAL MODIFIER
                            Field modifiersField = Field.class.getDeclaredField("modifiers");
                            modifiersField.setAccessible(true);
                            modifiersField.setInt(bootstrapAcceptorField, bootstrapAcceptorField.getModifiers() & ~Modifier.FINAL);
                            bootstrapAcceptorField.set(bootstrapAcceptor, channelInitializer);
                            searching = false;
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                break;
            }
        }
    }

    @Override
    public void eject() {

    }

    @Override
    public void injectPlayer(Player player) {
        Object channel = PacketEvents.get().packetProcessorInternal.getChannel(player);
        updatePlayerObject(player, channel);
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
        PlayerChannelHandlerLegacy handler = getHandler(channel);
        if (handler == null) {
            return false;
        }
        return handler.player != null;
    }

    @Override
    public void sendPacket(Object ch, Object rawNMSPacket) {
        Channel channel = (Channel)ch;
        channel.writeAndFlush(rawNMSPacket);
    }

    private PlayerChannelHandlerLegacy getHandler(Object rawChannel) {
        Channel channel = (Channel) rawChannel;
        ChannelHandler handler = channel.pipeline().get(PacketEvents.handlerName);
        if (handler instanceof PlayerChannelHandlerLegacy) {
            return (PlayerChannelHandlerLegacy) handler;
        }
        else {
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
}
