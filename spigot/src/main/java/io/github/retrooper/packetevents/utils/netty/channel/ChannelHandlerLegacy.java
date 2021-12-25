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

import com.github.retrooper.packetevents.netty.channel.ChannelHandlerAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelHandlerContextAbstract;
import net.minecraft.util.io.netty.channel.ChannelHandler;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;

public class ChannelHandlerLegacy implements ChannelHandlerAbstract {
    private final ChannelHandler channelHandler;

    public ChannelHandlerLegacy(Object rawChannelHandler) {
        this.channelHandler = (ChannelHandler) rawChannelHandler;
    }

    @Override
    public Object rawChannelHandler() {
        return channelHandler;
    }

    @Override
    public void handlerAdded(ChannelHandlerContextAbstract ctx) throws Exception {
        channelHandler.handlerAdded((ChannelHandlerContext) ctx.rawChannelHandlerContext());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContextAbstract ctx) throws Exception {
        channelHandler.handlerRemoved((ChannelHandlerContext) ctx.rawChannelHandlerContext());
    }
}
