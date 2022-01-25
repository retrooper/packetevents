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

package io.github.retrooper.packetevents.handlers.legacy.late;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import io.github.retrooper.packetevents.handlers.LateInjector;
import io.github.retrooper.packetevents.handlers.legacy.PacketDecoderLegacy;
import io.github.retrooper.packetevents.handlers.legacy.PacketEncoderLegacy;
import io.github.retrooper.packetevents.handlers.legacy.early.ServerConnectionInitializerLegacy;
import net.minecraft.util.io.netty.channel.Channel;
import net.minecraft.util.io.netty.channel.ChannelHandler;
import org.jetbrains.annotations.Nullable;

public class LateChannelInjectorLegacy implements LateInjector {
    @Override
    public void inject() {

    }

    @Override
    public void eject() {

    }

    @Override
    public void updateUser(ChannelAbstract channel, User user) {
        PacketEncoderLegacy encoder = getEncoder(channel);
        if (encoder != null) {
            encoder.user = user;
        }

        PacketDecoderLegacy decoder = getDecoder(channel);
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
        ServerConnectionInitializerLegacy.postInitChannel(channel.rawChannel(), connectionState);
    }

    private PacketDecoderLegacy getDecoder(ChannelAbstract ch) {
        Channel channel = (Channel) ch.rawChannel();
        ChannelHandler decoder = channel.pipeline().get(PacketEvents.DECODER_NAME);
        if (decoder instanceof PacketDecoderLegacy) {
            return (PacketDecoderLegacy) decoder;
        } else {
            return null;
        }
    }

    private PacketEncoderLegacy getEncoder(ChannelAbstract ch) {
        Channel channel = (Channel) ch.rawChannel();
        ChannelHandler decoder = channel.pipeline().get(PacketEvents.ENCODER_NAME);
        if (decoder instanceof PacketEncoderLegacy) {
            return (PacketEncoderLegacy) decoder;
        } else {
            return null;
        }
    }


    @Override
    public void ejectPlayer(Object player) {
        ChannelAbstract channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
        if (channel != null) {
            try {
                ServerConnectionInitializerLegacy.postDestroyChannel(channel.rawChannel());
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
        PacketDecoderLegacy decoder = getDecoder(channel);
        PacketEncoderLegacy encoder = getEncoder(channel);
        return decoder != null && decoder.player != null &&
                encoder != null && encoder.player != null;
    }

    @Override
    public ConnectionState getConnectionState(ChannelAbstract channel) {
        PacketEncoderLegacy encoder = getEncoder(channel);
        if (encoder != null) {
            return encoder.user.getConnectionState();
        }
        return null;
    }

    @Override
    public void changeConnectionState(ChannelAbstract channel, ConnectionState connectionState) {
        PacketEncoderLegacy encoder = getEncoder(channel);
        if (encoder != null) {
            encoder.user.setConnectionState(connectionState);
        }
    }
}