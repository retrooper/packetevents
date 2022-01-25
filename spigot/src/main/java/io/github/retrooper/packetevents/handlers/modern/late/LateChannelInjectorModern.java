/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package io.github.retrooper.packetevents.handlers.modern.late;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.protocol.player.User;
import io.github.retrooper.packetevents.handlers.LateInjector;
import io.github.retrooper.packetevents.handlers.modern.PacketDecoderModern;
import io.github.retrooper.packetevents.handlers.modern.PacketEncoderModern;
import io.github.retrooper.packetevents.handlers.modern.early.ServerConnectionInitializerModern;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import org.jetbrains.annotations.Nullable;

public class LateChannelInjectorModern implements LateInjector {
    @Override
    public void inject() {

    }

    @Override
    public void eject() {

    }

    @Override
    public void updateUser(ChannelAbstract channel, User user) {
        PacketEncoderModern encoder = getEncoder(channel);
        if (encoder != null) {
            encoder.user = user;
        }

        PacketDecoderModern decoder = getDecoder(channel);
        if (decoder != null) {
            decoder.user = user;
        }
    }

    @Override
    public void injectPlayer(Object player, @Nullable ConnectionState connectionState) {
        ChannelAbstract channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
        if (connectionState == null) {
            connectionState = ConnectionState.PLAY;
        }
        ServerConnectionInitializerModern.postInitChannel(channel.rawChannel(), connectionState);
    }

    private PacketDecoderModern getDecoder(ChannelAbstract ch) {
        Channel channel = (Channel) ch.rawChannel();
        ChannelHandler decoder = channel.pipeline().get(PacketEvents.DECODER_NAME);
        if (decoder instanceof PacketDecoderModern) {
            return (PacketDecoderModern) decoder;
        } else {
            return null;
        }
    }

    private PacketEncoderModern getEncoder(ChannelAbstract ch) {
        Channel channel = (Channel) ch.rawChannel();
        ChannelHandler decoder = channel.pipeline().get(PacketEvents.ENCODER_NAME);
        if (decoder instanceof PacketEncoderModern) {
            return (PacketEncoderModern) decoder;
        } else {
            return null;
        }
    }


    @Override
    public void ejectPlayer(Object player) {
        ChannelAbstract channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
        if (channel != null) {
            try {
                ServerConnectionInitializerModern.postDestroyChannel(channel.rawChannel());
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public boolean hasInjected(Object player) {
        ChannelAbstract channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
        if (channel == null) {
            return false;
        }
        PacketDecoderModern decoder = getDecoder(channel);
        PacketEncoderModern encoder = getEncoder(channel);
        return decoder != null && decoder.player != null &&
                encoder != null && encoder.player != null;
    }

    @Override
    public ConnectionState getConnectionState(ChannelAbstract channel) {
        PacketEncoderModern encoder = getEncoder(channel);
        if (encoder != null) {
            return encoder.user.getConnectionState();
        }
        return null;
    }

    @Override
    public void changeConnectionState(ChannelAbstract channel, ConnectionState connectionState) {
        PacketEncoderModern encoder = getEncoder(channel);
        if (encoder != null) {
            encoder.user.setConnectionState(connectionState);
        }
    }
}