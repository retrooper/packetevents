/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package io.github.retrooper.packetevents.impl.netty.channel;

import com.github.retrooper.packetevents.netty.channel.ChannelOperator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.net.SocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ChannelOperatorImpl implements ChannelOperator {

    private static final Logger LOGGER = Logger.getLogger(ChannelOperatorImpl.class.getName());
    /** Bereits gemeldete NIO-Fallback-Methoden — jede wird nur EINMAL geloggt (keine Flut). */
    private static final Set<String> LOGGED_NIO_FALLBACKS = ConcurrentHashMap.newKeySet();

    /**
     * Meldet EINMALIG, dass eine Netty-Pipeline-/Write-Methode auf einem NIO-Channel (Minestom) landete.
     * Diese Methoden gehören NICHT zum Port-Pfad (Sends laufen über MinestomPacketSender, Inbound über
     * MinestomPacketFeeder). Ein Treffer heißt: hier nutzt Code noch den Netty-Pfad → Port-Lücke, die
     * gefixt werden sollte. Wird bewusst nur einmal je Methode geloggt, um Log-Fluten zu vermeiden.
     */
    private static void warnNioFallbackOnce(String method) {
        if (LOGGED_NIO_FALLBACKS.add(method)) {
            LOGGER.warning("[packetevents/minestom] NIO-Fallback in ChannelOperator." + method
                    + "() — dieser Netty-Pipeline-Pfad ist auf dem Minestom-Port nicht implementiert "
                    + "(No-Op). Wenn hier Funktionalität fehlt, muss der Pfad portiert werden.");
        }
    }
    @Override
    public SocketAddress remoteAddress(Object channel) {
        if (channel instanceof Channel) {
            return ((Channel) channel).remoteAddress();
        }
        // OnThePixel (Minestom): raw NIO SocketChannel, not a Netty Channel.
        if (channel instanceof java.nio.channels.SocketChannel) {
            try {
                return ((java.nio.channels.SocketChannel) channel).getRemoteAddress();
            } catch (java.io.IOException ignored) {
                return null;
            }
        }
        return null;
    }

    @Override
    public SocketAddress localAddress(Object channel) {
        if (channel instanceof Channel) {
            return ((Channel) channel).localAddress();
        }
        // OnThePixel (Minestom): raw NIO SocketChannel, not a Netty Channel.
        if (channel instanceof java.nio.channels.SocketChannel) {
            try {
                return ((java.nio.channels.SocketChannel) channel).getLocalAddress();
            } catch (java.io.IOException ignored) {
                return null;
            }
        }
        return null;
    }

    @Override
    public boolean isOpen(Object channel) {
        if (channel instanceof Channel) {
            return ((Channel) channel).isOpen();
        }
        // OnThePixel (Minestom): the "channel" is a raw NIO java.nio.channels.SocketChannel, not a
        // Netty Channel. Grim's shouldCheck() calls ChannelHelper.isOpen() on the tick thread; the
        // blind Netty cast threw ClassCastException and stopped every player from being checked.
        if (channel instanceof java.nio.channels.Channel) {
            return ((java.nio.channels.Channel) channel).isOpen();
        }
        return false;
    }

    @Override
    public Object close(Object channel) {
        if (channel instanceof Channel) {
            return ((Channel) channel).close();
        }
        // OnThePixel (Minestom): the "channel" is a raw NIO java.nio.channels.SocketChannel, not a Netty
        // Channel. Closing it via the Netty cast threw ClassCastException on the tick thread. Close it
        // directly so no closeConnection() path can ever take down the server tick.
        if (channel instanceof java.nio.channels.Channel) {
            try {
                ((java.nio.channels.Channel) channel).close();
            } catch (java.io.IOException ignored) {
            }
        }
        return null;
    }

    // OnThePixel (Minestom): der "channel" ist ein roher NIO-SocketChannel ohne Netty-Pipeline/EventLoop.
    // Auf dem Port laufen Sends über MinestomProtocolManager→MinestomPacketSender und Inbound über
    // MinestomPacketFeeder — die folgenden Netty-Pipeline/Write-Methoden gehören NICHT zum Port-Pfad.
    // Statt blind auf Netty zu casten (ClassCastException → riss z.B. die Paket-/Tick-Verarbeitung mit),
    // greifen sie nur noch bei echten Netty-Channels und degradieren auf NIO gefahrlos (No-Op/leer).

    @Override
    public Object write(Object channel, Object buffer) {
        if (channel instanceof Channel) {
            return ((Channel) channel).write(buffer);
        }
        warnNioFallbackOnce("write");
        return null;
    }

    @Override
    public Object flush(Object channel) {
        if (channel instanceof Channel) {
            return ((Channel) channel).flush();
        }
        warnNioFallbackOnce("flush");
        return null;
    }

    @Override
    public Object writeAndFlush(Object channel, Object buffer) {
        if (channel instanceof Channel) {
            return ((Channel) channel).writeAndFlush(buffer);
        }
        warnNioFallbackOnce("writeAndFlush");
        return null;
    }

    @Override
    public Object fireChannelRead(Object channel, Object buffer) {
        if (channel instanceof Channel) {
            return ((Channel) channel).pipeline().fireChannelRead(buffer);
        }
        warnNioFallbackOnce("fireChannelRead");
        return null;
    }

    @Override
    public Object writeInContext(Object channel, String ctx, Object buffer) {
        if (channel instanceof Channel) {
            return ((Channel) channel).pipeline().context(ctx).write(buffer);
        }
        warnNioFallbackOnce("writeInContext");
        return null;
    }

    @Override
    public Object flushInContext(Object channel, String ctx) {
        if (channel instanceof Channel) {
            return ((Channel) channel).pipeline().context(ctx).flush();
        }
        warnNioFallbackOnce("flushInContext");
        return null;
    }

    @Override
    public Object writeAndFlushInContext(Object channel, String ctx, Object buffer) {
        if (channel instanceof Channel) {
            return ((Channel) channel).pipeline().context(ctx).writeAndFlush(buffer);
        }
        warnNioFallbackOnce("writeAndFlushInContext");
        return null;
    }

    @Override
    public Object fireChannelReadInContext(Object channel, String ctx, Object buffer) {
        if (channel instanceof Channel) {
            return ((Channel) channel).pipeline().context(ctx).fireChannelRead(buffer);
        }
        warnNioFallbackOnce("fireChannelReadInContext");
        return null;
    }

    @Override
    public List<String> pipelineHandlerNames(Object channel) {
        if (channel instanceof Channel) {
            return ((Channel) channel).pipeline().names();
        }
        warnNioFallbackOnce("pipelineHandlerNames");
        return Collections.emptyList();
    }

    @Override
    public Object getPipelineHandler(Object channel, String name) {
        if (channel instanceof Channel) {
            return ((Channel) channel).pipeline().get(name);
        }
        warnNioFallbackOnce("getPipelineHandler");
        return null;
    }

    @Override
    public Object getPipelineContext(Object channel, String name) {
        if (channel instanceof Channel) {
            return ((Channel) channel).pipeline().context(name);
        }
        warnNioFallbackOnce("getPipelineContext");
        return null;
    }

    @Override
    public Object getPipeline(Object channel) {
        if (channel instanceof Channel) {
            return ((Channel) channel).pipeline();
        }
        warnNioFallbackOnce("getPipeline");
        return null;
    }

    @Override
    public void runInEventLoop(Object channel, Runnable runnable) {
        if (channel instanceof Channel) {
            ((Channel) channel).eventLoop().execute(runnable);
            return;
        }
        // OnThePixel (Minestom): a raw NIO SocketChannel has no Netty event loop. Grim uses this via
        // runSafely() to get channel-thread affinity; with no event loop to hand off to, run inline on
        // the calling thread (the Minestom packet/tick thread that already drives this player's flow).
        runnable.run();
    }

    @Override
    public Object pooledByteBuf(Object channel) {
        if (channel instanceof Channel) {
            return ((Channel) channel).alloc().buffer();
        }
        // OnThePixel (Minestom): kein Netty-Allocator am NIO-Channel. Unpooled-Heap-Buffer liefern,
        // damit Aufrufer einen nutzbaren ByteBuf bekommen statt NPE/ClassCastException.
        return Unpooled.buffer();
    }
}
