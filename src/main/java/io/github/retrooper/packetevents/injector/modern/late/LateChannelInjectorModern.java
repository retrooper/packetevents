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

package io.github.retrooper.packetevents.injector.modern.late;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.injector.LateInjector;
import io.github.retrooper.packetevents.injector.modern.PacketDecoderModern;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.bukkit.entity.Player;

public class LateChannelInjectorModern implements LateInjector {
    @Override
    public void inject() {

    }

    @Override
    public void eject() {

    }

    @Override
    public void injectPlayer(Player player) {
        Channel channel = (Channel) PacketEvents.get().getPlayerUtils().getChannel(player);
        PacketDecoderModern packetDecoderModern = new PacketDecoderModern((ByteToMessageDecoder) channel.pipeline().get("decoder"));
        channel.pipeline().replace("decoder", "decoder",packetDecoderModern);
    }

    private PacketDecoderModern getDecoder(Object rawChannel) {
        Channel channel = (Channel) rawChannel;
        ChannelHandler decoder = channel.pipeline().get("decoder");
        if (decoder instanceof PacketDecoderModern) {
            return (PacketDecoderModern) decoder;
        }
        else {
            return null;
        }
    }


    @Override
    public void ejectPlayer(Player player) {
        Object channel = PacketEvents.get().getPlayerUtils().getChannel(player);
        if (channel != null) {
            Channel chnl = (Channel) channel;
            try {
                chnl.pipeline().replace("decoder", "decoder", getDecoder(channel).minecraftDecoder);
            } catch (Exception ex) {

            }
        }
    }

    @Override
    public boolean hasInjected(Player player) {
        Object channel = PacketEvents.get().getPlayerUtils().getChannel(player);
        if (channel == null) {
            return false;
        }
        PacketDecoderModern decoder = getDecoder(channel);
        return decoder != null && decoder.player != null;
    }

    @Override
    public void writePacket(Object ch, Object rawNMSPacket) {
        Channel channel = (Channel) ch;
        channel.write(rawNMSPacket);
    }

    @Override
    public void flushPackets(Object ch) {
        Channel channel = (Channel) ch;
        channel.flush();
    }

    @Override
    public void sendPacket(Object rawChannel, Object packet) {
        Channel channel = (Channel) rawChannel;
        channel.pipeline().writeAndFlush(packet);
    }
}