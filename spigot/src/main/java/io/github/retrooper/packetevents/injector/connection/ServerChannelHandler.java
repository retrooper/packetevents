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
import com.github.retrooper.packetevents.util.PEVersion;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Version;

import java.util.Map;

public class ServerChannelHandler extends ChannelInboundHandlerAdapter {
    public static final PEVersion MODERN_NETTY_VERSION = new PEVersion(4, 1, 24);
    public static boolean CHECKED_NETTY_VERSION;
    public static PEVersion NETTY_VERSION;

    private static PEVersion resolveNettyVersion() {
        Map<String, Version> nettyArtifacts = Version.identify();
        Version version = nettyArtifacts.getOrDefault("netty-common", nettyArtifacts.get("netty-all"));
        if (version != null) {
            String stringVersion = version.artifactVersion();

            // Remove the ".Final" from the version by just removing any words (non numbers or dots)
            stringVersion = stringVersion.replaceAll("[^\\d.]", "");

            // Make sure stringVersion only contains 3 values like 4.2.0 but not 4.2.0.2
            String[] splitVersion = stringVersion.split("\\.");
            if (splitVersion.length > 3) {
                stringVersion = splitVersion[0] + "." + splitVersion[1] + "." + splitVersion[2];
            }

            // If the string ends with a dot, remove it
            stringVersion = stringVersion.endsWith(".") ? stringVersion.substring(0, stringVersion.length() - 1) : stringVersion;

            return PEVersion.fromString(stringVersion);
        }
        return null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = (Channel) msg;
        //Resolve netty version only once.
        if (NETTY_VERSION == null && !CHECKED_NETTY_VERSION) {
            NETTY_VERSION = resolveNettyVersion();
            CHECKED_NETTY_VERSION = true;
        }

        //Depends on netty version. If we cannot resolve that we just check server version.
        if ((NETTY_VERSION != null && NETTY_VERSION.isNewerThan(MODERN_NETTY_VERSION))
                || SpigotReflectionUtil.V_1_12_OR_HIGHER) {
            channel.pipeline().addLast(PacketEvents.SERVER_CHANNEL_HANDLER_NAME, new PreChannelInitializer_v1_12());
        } else {
            channel.pipeline().addFirst(PacketEvents.SERVER_CHANNEL_HANDLER_NAME, new PreChannelInitializer_v1_8());
        }
        super.channelRead(ctx, msg);
    }
}
