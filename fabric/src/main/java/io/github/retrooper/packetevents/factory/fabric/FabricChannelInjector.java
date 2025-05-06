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

package io.github.retrooper.packetevents.factory.fabric;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.player.User;
import io.github.retrooper.packetevents.handler.PacketDecoder;
import io.github.retrooper.packetevents.handler.PacketEncoder;
import io.netty.channel.Channel;
import net.fabricmc.api.EnvType;
import net.minecraft.world.entity.player.Player;

import static com.github.retrooper.packetevents.PacketEvents.DECODER_NAME;
import static com.github.retrooper.packetevents.PacketEvents.ENCODER_NAME;

public class FabricChannelInjector implements ChannelInjector {

    private final PacketSide packetSide;

    public FabricChannelInjector(EnvType environment) {
        this.packetSide = switch (environment) {
            case SERVER -> PacketSide.SERVER;
            case CLIENT -> PacketSide.CLIENT;
        };
    }

    @Override
    public void inject() {
        // NO-OP
    }

    @Override
    public void uninject() {
        // NO-OP
    }

    @Override
    public boolean isPlayerSet(Object ch) {
        if (ch == null) return false;
        Channel channel = (Channel) ch;
        PacketEncoder encoder = (PacketEncoder) channel.pipeline().get(ENCODER_NAME);
        if (encoder.player != null) return true;

        PacketDecoder decoder = (PacketDecoder) channel.pipeline().get(DECODER_NAME);
        return decoder.player != null;
    }

    @Override
    public void updateUser(Object channel, User user) {
        if (!PacketEvents.getAPI().getProtocolManager().hasChannel(channel)) {
            return; // this channel isn't injected by packetevents
        }
        Channel ch = (Channel) channel;
        ((PacketDecoder) ch.pipeline().get(DECODER_NAME)).user = user;
        ((PacketEncoder) ch.pipeline().get(ENCODER_NAME)).user = user;
    }

    @Override
    public void setPlayer(Object channel, Object player) {
        if (!PacketEvents.getAPI().getProtocolManager().hasChannel(channel)) {
            return; // this channel isn't injected by packetevents
        }
        Channel ch = (Channel) channel;
        ((PacketDecoder) ch.pipeline().get(DECODER_NAME)).player = (Player) player;
        ((PacketEncoder) ch.pipeline().get(ENCODER_NAME)).player = (Player) player;
    }

    @Override
    public boolean isProxy() {
        return false;
    }

    @Override
    public PacketSide getPacketSide() {
        return this.packetSide;
    }
}
