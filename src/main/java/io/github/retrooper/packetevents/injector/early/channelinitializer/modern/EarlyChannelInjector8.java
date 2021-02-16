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

package io.github.retrooper.packetevents.injector.early.channelinitializer.modern;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.injector.early.channelinitializer.Injector;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

public class EarlyChannelInjector8 implements Injector {
    @Override
    public void inject() {
        System.out.println("Injection procedure started...");
        Object serverConnection = NMSUtils.getMinecraftServerConnection();
        WrappedPacket serverConnectionWrapper = new WrappedPacket(new NMSPacket(serverConnection));
        boolean searching = true;
        for (int i = 0; searching; i++) {
            try {
                System.out.println("Still searching...");
                //Get the list.
                List<Object> serverChannelList = (List<Object>) serverConnectionWrapper.readObject(i, List.class);
                synchronized (serverChannelList) {
                    for (Object serverChannel : serverChannelList) {
                        System.out.println("Iterating through a server channel...");
                        //Is this the server channel list?
                        if (serverChannel instanceof ChannelFuture) {
                            System.out.print("Found it...");
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
                                    System.out.println("Found the bootstrap acceptor");
                                }
                                catch (Exception ex) {

                                }
                            }


                            ChannelInitializer<?> oldChannelInitializer = (ChannelInitializer<?>) bootstrapAcceptorField.get(bootstrapAcceptor);
                            ChannelInitializer<?> channelInitializer = new PEChannelInitializer8(oldChannelInitializer);

                            //REMOVE FINAL MODIFIER
                            Field modifiersField = Field.class.getDeclaredField("modifiers");
                            modifiersField.setAccessible(true);
                            modifiersField.setInt(bootstrapAcceptorField, bootstrapAcceptorField.getModifiers() & ~Modifier.FINAL);
                            bootstrapAcceptorField.set(bootstrapAcceptor, channelInitializer);
                            System.out.println("Replaced old channel initializer with our new one!\n}");
                            searching = false;
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                break;
            }
        }
        System.out.print("Finished injecting...");
    }

    @Override
    public void eject() {

    }

    public PlayerChannelHandler8 getHandler(Object rawChannel) {
        Channel channel = (Channel) rawChannel;
        ChannelHandler handler = channel.pipeline().get(PacketEvents.handlerName);
        if (handler instanceof PlayerChannelHandler8) {
            return (PlayerChannelHandler8) handler;
        }
        else {
            return null;
        }
    }

    public void updatePlayerObject(Player player, Object rawChannel) {
        PlayerChannelHandler8 handler = getHandler(rawChannel);
        System.out.print("Updating player object...");
        if (handler != null) {
            handler.player = player;
            System.out.println("Updated player object!");
        }
    }
}
