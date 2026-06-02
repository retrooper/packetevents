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

package io.github.retrooper.packetevents.injector.connection;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.UserConnectEvent;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.FakeChannelUtil;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.github.retrooper.packetevents.injector.SpigotChannelInjector;
import io.github.retrooper.packetevents.injector.handlers.PacketEventsDecoder;
import io.github.retrooper.packetevents.injector.handlers.PacketEventsEncoder;
import io.github.retrooper.packetevents.util.viaversion.ViaVersionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPipeline;
import net.kyori.adventure.util.TriState;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.NoSuchElementException;

@NullMarked
@ApiStatus.Internal
public final class ServerConnectionInitializer {

    private static final String VIAVERSION_ENCODER_NAME = "via-encoder";
    private static final String VIAVERSION_DECODER_NAME = "via-decoder";
    private static final String MINECRAFT_DECOMPRESS_NAME = "decompress";
    private static final String MINECRAFT_COMPRESS_NAME = "compress";
    private static final String MINECRAFT_DECODER_NAME = "decoder";
    private static final String MINECRAFT_ENCODER_NAME = "encoder";
    private static final String MINECRAFT_INBOUND_CONFIG_NAME = "inbound_config";
    private static final String MINECRAFT_OUTBOUND_CONFIG_NAME = "outbound_config";
    private static final String MINECRAFT_SPLITTER_NAME = "splitter";

    private ServerConnectionInitializer() {
    }

    @Deprecated
    public static void initChannel(Object ch, ConnectionState state) {
        initChannel((Channel) ch, state);
    }

    public static void initChannel(Channel channel, ConnectionState state) {
        if (FakeChannelUtil.isFakeChannel(channel)) {
            return;
        }
        User user = new User(channel, state, null, new UserProfile(null, null));

        PacketEventsAPI<?> packetevents = PacketEvents.getAPI();
        if (state == ConnectionState.PLAY) {
            // Player connected before ViaVersion init, therefore the player is server version (mostly true except 1.7 servers)
            user.setClientVersion(packetevents.getServerManager().getVersion().toClientVersion());
            packetevents.getLogManager().warn("Late injection detected for " + user.getUUID() + ", we missed packets so some functionality may break!");
        }

        synchronized (channel) {
            /*
             * This is a rather rare one, BUT!
             * If the plugin takes a while to initialize and handshakes/pings pile up,
             * some may not be handled completely, thus, not having a 'splitter' ChannelHandler.
             * We can, of course, wait for them to be handled, but this complexes the algorithm.
             * Taken the above into account, here we just drop all unhandled connections.
             */
            if (channel.pipeline().get(MINECRAFT_SPLITTER_NAME) == null) {
                channel.close();
                return;
            }

            UserConnectEvent connectEvent = new UserConnectEvent(user);
            packetevents.getEventManager().callEvent(connectEvent);
            if (connectEvent.isCancelled()) {
                channel.unsafe().closeForcibly();
                return;
            }

            relocateHandlers(channel, user, false);
            SpigotChannelInjector injector = (SpigotChannelInjector) packetevents.getInjector();
            if (injector.isPreViaVersionActivated()) {
                relocateHandlers(channel, user, true);
            }

            channel.closeFuture().addListener((ChannelFutureListener) future -> PacketEventsImplHelper.handleDisconnection(user.getChannel(), user.getUUID()));
            packetevents.getProtocolManager().setUser(channel, user);
        }
    }

    public static void destroyHandlers(Object ch) {
        Channel channel = (Channel) ch;
        if (channel.pipeline().get(PacketEvents.DECODER_NAME) != null) {
            channel.pipeline().remove(PacketEvents.DECODER_NAME);
        } else {
            PacketEvents.getAPI().getLogManager().warn("Could not find decoder handler in channel pipeline!");
        }

        if (channel.pipeline().get(PacketEvents.ENCODER_NAME) != null) {
            channel.pipeline().remove(PacketEvents.ENCODER_NAME);
        } else {
            PacketEvents.getAPI().getLogManager().warn("Could not find encoder handler in channel pipeline!");
        }
    }

    public static void relocateHandlers(Channel ctx, User user, boolean preVia) {
        try {
            PacketEventsAPI<?> packetevents = PacketEvents.getAPI();

            if (packetevents.getSettings().isDebugEnabled()) {
                packetevents.getLogManager().debug("Pre relocate, preVia: " + preVia + ", " + ctx.pipeline().names());
            }

            String encoderName = preVia ? "pre-" + PacketEvents.ENCODER_NAME : PacketEvents.ENCODER_NAME;
            String decoderName = preVia ? "pre-" + PacketEvents.DECODER_NAME : PacketEvents.DECODER_NAME;

            // Determine where we WANT to be.
            String targetDecoderName;
            String targetEncoderName;
            if (preVia) {
                targetEncoderName = VIAVERSION_ENCODER_NAME;
                targetDecoderName = VIAVERSION_DECODER_NAME;
            } else {
                List<String> handlers = ctx.pipeline().names();
                targetDecoderName = handlers.contains(MINECRAFT_INBOUND_CONFIG_NAME) ? MINECRAFT_INBOUND_CONFIG_NAME : MINECRAFT_DECODER_NAME;
                targetEncoderName = handlers.contains(MINECRAFT_OUTBOUND_CONFIG_NAME) ? MINECRAFT_OUTBOUND_CONFIG_NAME : MINECRAFT_ENCODER_NAME;
            }

            PacketEventsDecoder decoder = (PacketEventsDecoder) ctx.pipeline().get(decoderName);
            PacketEventsEncoder encoder;

            SpigotChannelInjector injector = (SpigotChannelInjector) packetevents.getInjector();
            TriState preViaState = injector.isPreViaVersionActivated() || ViaVersionUtil.isAvailable() ? TriState.byBoolean(preVia) : TriState.NOT_SET;

            if (decoder != null) {
                // check whether we actually need to relocate
                if (handlersAlreadyRelocated(ctx.pipeline(), preVia, decoderName, targetDecoderName, encoderName, targetEncoderName)) {
                    return;
                }
                encoder = new PacketEventsEncoder((PacketEventsEncoder) ctx.pipeline().remove(encoderName));
                decoder = new PacketEventsDecoder((PacketEventsDecoder) ctx.pipeline().remove(decoderName));
                // ensure pre-via state is up-to-date
                encoder.preVia = preViaState;
                decoder.preVia = preViaState;
            } else {
                // no decoder means we haven't made handlers for the user yet
                encoder = new PacketEventsEncoder(user, preViaState);
                decoder = new PacketEventsDecoder(user, preViaState);
            }

            if (packetevents.getSettings().isDebugEnabled()) {
                packetevents.getLogManager().debug("After remove, preVia: " + preVia + ", " + ctx.pipeline().names());
            }

            ctx.pipeline()
                    .addBefore(targetDecoderName, decoderName, decoder)
                    .addBefore(targetEncoderName, encoderName, encoder);

            if (packetevents.getSettings().isDebugEnabled()) {
                packetevents.getLogManager().debug("After add, preVia: " + preVia + ", " + ctx.pipeline().names());
            }
        } catch (NoSuchElementException ex) {
            String handlers = ChannelHelper.pipelineHandlerNamesAsString(ctx);
            throw new IllegalStateException("PacketEvents failed to add a decoder to the netty pipeline. Pipeline handlers: " + handlers, ex);
        }
    }

    /**
     * Checks if 'myHandler' exists and is currently at a lower index (upstream) than 'targetHandler'.
     */
    private static boolean isBefore(List<String> handlers, String myHandler, String targetHandler) {
        // just do one iteration and check which handler we encounter first,
        // but account for target handler just not existing
        boolean foundMe = false;
        for (String handler : handlers) {
            if (handler.equals(myHandler)) {
                foundMe = true;
            } else if (handler.equals(targetHandler)) {
                return foundMe;
            }
        }
        // can't find either, return same as if final handler is our target
        return foundMe;
    }

    private static boolean handlersAlreadyRelocated(
            ChannelPipeline pipeline, boolean preVia,
            String decoderName, String targetDecoderName,
            String encoderName, String targetEncoderName
    ) {
        // ensure we're still before our target
        List<String> handlers = pipeline.names();
        if (!isBefore(handlers, decoderName, targetDecoderName)
                || !isBefore(handlers, encoderName, targetEncoderName)) {
            return false;
        }

        if (!preVia) {
            // ensure we're not too far before our target (i.e. after compress/decompress) as otherwise we would only catch compressed packets
            return (!handlers.contains(MINECRAFT_DECOMPRESS_NAME) || isBefore(handlers, MINECRAFT_DECOMPRESS_NAME, decoderName))
                    && (!handlers.contains(MINECRAFT_COMPRESS_NAME) || isBefore(handlers, MINECRAFT_COMPRESS_NAME, encoderName));
        }

        return true;
    }
}
