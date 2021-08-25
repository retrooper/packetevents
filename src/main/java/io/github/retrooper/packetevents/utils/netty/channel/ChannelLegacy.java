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

package io.github.retrooper.packetevents.utils.netty.channel;

import io.github.retrooper.packetevents.utils.netty.channel.pipeline.ChannelPipelineAbstract;
import io.github.retrooper.packetevents.utils.netty.channel.pipeline.ChannelPipelineLegacy;
import net.minecraft.util.io.netty.channel.Channel;

import java.net.SocketAddress;

public class ChannelLegacy implements ChannelAbstract {
    private final Channel channel;
    public ChannelLegacy(Object rawChannel) {
        this.channel = (Channel) rawChannel;
    }

    @Override
    public Object rawChannel() {
        return channel;
    }

    @Override
    public boolean isOpen() {
        return channel.isOpen();
    }

    @Override
    public boolean isRegistered() {
        return channel.isRegistered();
    }

    @Override
    public boolean isActive() {
        return channel.isActive();
    }

    @Override
    public SocketAddress localAddress() {
        return channel.localAddress();
    }

    @Override
    public SocketAddress remoteAddress() {
        return channel.remoteAddress();
    }

    @Override
    public boolean isWritable() {
        return channel.isWritable();
    }

    @Override
    public ChannelPipelineAbstract pipeline() {
        return new ChannelPipelineLegacy(channel.pipeline());
    }
}
