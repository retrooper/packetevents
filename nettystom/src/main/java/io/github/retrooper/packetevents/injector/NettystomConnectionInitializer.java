/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package io.github.retrooper.packetevents.injector;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.UserConnectEvent;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.github.retrooper.packetevents.handler.PacketDecoder;
import io.github.retrooper.packetevents.handler.PacketEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Registers a nettystom connection with packetevents.
 *
 * <p>Call {@link #initChannel(Channel, ConnectionState, ClientVersion)} once per
 * connection (typically right after nettystom adds its own {@code frame-decoder} /
 * {@code handler}).
 *
 * <p><b>Important:</b> nettystom performs compression and encryption inside
 * {@code PlayerSocketConnection} rather than as Netty pipeline handlers, so the
 * {@link PacketDecoder}/{@link PacketEncoder} added here must be placed where the buffer
 * holds plaintext, single packets. See the module README for the recommended wiring.
 */
@ApiStatus.Internal
public final class NettystomConnectionInitializer {

    private NettystomConnectionInitializer() {
    }

    /**
     * Registers the connection, fires {@link UserConnectEvent} and adds the packetevents
     * handlers right after nettystom's {@code frame-decoder}.
     *
     * @return the created {@link User}, or {@code null} if the connect event was cancelled.
     */
    public static @Nullable User initChannel(Channel channel, ConnectionState state, ClientVersion clientVersion) {
        return initChannel(channel, state, clientVersion, "frame-decoder", "frame-decoder");
    }

    /**
     * Registers the connection and adds the packetevents handlers relative to the given
     * pipeline anchors.
     *
     * @param decoderAfter  name of the handler the inbound decoder is added <i>after</i>
     * @param encoderBefore name of the handler the outbound encoder is added <i>before</i>
     */
    public static @Nullable User initChannel(Channel channel, ConnectionState state, ClientVersion clientVersion,
                                             String decoderAfter, String encoderBefore) {
        User user = new User(channel, state, clientVersion, new UserProfile(null, null));

        UserConnectEvent connectEvent = new UserConnectEvent(user);
        PacketEvents.getAPI().getEventManager().callEvent(connectEvent);
        if (connectEvent.isCancelled()) {
            channel.unsafe().closeForcibly();
            return null;
        }

        PacketEvents.getAPI().getProtocolManager().setUser(channel, user);

        PacketSide side = PacketEvents.getAPI().getInjector().getPacketSide();
        channel.pipeline().addAfter(decoderAfter, PacketEvents.DECODER_NAME, new PacketDecoder(side, user));
        channel.pipeline().addBefore(encoderBefore, PacketEvents.ENCODER_NAME, new PacketEncoder(side, user));

        channel.closeFuture().addListener((ChannelFutureListener) future ->
                PacketEventsImplHelper.handleDisconnection(user.getChannel(), user.getUUID()));
        return user;
    }

    /**
     * Removes the packetevents handlers from the channel pipeline, if present.
     */
    public static void destroyChannel(Channel channel) {
        if (channel.pipeline().get(PacketEvents.DECODER_NAME) != null) {
            channel.pipeline().remove(PacketEvents.DECODER_NAME);
        }
        if (channel.pipeline().get(PacketEvents.ENCODER_NAME) != null) {
            channel.pipeline().remove(PacketEvents.ENCODER_NAME);
        }
    }
}
