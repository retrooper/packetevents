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


import io.github.retrooper.packetevents.utils.MinecraftReflectionUtil;
import io.github.retrooper.packetevents.utils.netty.ChannelInboundHandlerUtil_Legacy;
import io.github.retrooper.packetevents.utils.netty.ChannelInboundHandlerUtil_Modern;

public class ChannelInboundHandlerUtil {
    public static void handlerChannelRead(Object handler, Object ctx, Object msg) {
        if (MinecraftReflectionUtil.USE_MODERN_NETTY_PACKAGE) {
            ChannelInboundHandlerUtil_Modern.handlerChannelRead(handler, ctx, msg);
        } else {
            ChannelInboundHandlerUtil_Legacy.handlerChannelRead(handler, ctx, msg);
        }
    }
}
