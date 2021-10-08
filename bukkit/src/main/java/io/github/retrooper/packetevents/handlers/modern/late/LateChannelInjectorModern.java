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
import io.github.retrooper.packetevents.handlers.LateInjector;
import io.github.retrooper.packetevents.handlers.modern.PacketDecoderModern;
import io.github.retrooper.packetevents.handlers.modern.PacketEncoderModern;
import io.github.retrooper.packetevents.handlers.modern.early.ServerConnectionInitializerModern;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;

public class LateChannelInjectorModern implements LateInjector {
    @Override
    public void inject() {

    }

    @Override
    public void eject() {

    }

    @Override
    public void injectPlayer(Object player) {
        ChannelAbstract channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
        ServerConnectionInitializerModern.postInitChannel(channel.rawChannel());
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

    //TODO Look into this approach, maybe instead make the variable in the decoder just default to GAME
    //TODO and implement changeConnectionState properly
    @Override
    public ConnectionState getConnectionState(ChannelAbstract channel) {
        return ConnectionState.PLAY;
    }

    @Override
    public void changeConnectionState(ChannelAbstract channel, ConnectionState connectionState) {

    }
}