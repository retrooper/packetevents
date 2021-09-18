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

package io.github.retrooper.packetevents.utils.dependencies.viaversion;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.bukkit.handlers.BukkitDecodeHandler;
import com.viaversion.viaversion.bukkit.platform.BukkitViaInjector;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.reflection.ReflectionObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.*;

public class ViaVersionAccessorImpl implements ViaVersionAccessor {
    @Override
    public int getProtocolVersion(Player player) {
        return Via.getAPI().getPlayerVersion(player);
    }

    public void addDecoderAfterVia(Object ch, MessageToMessageDecoder<?> customDecoder) {
        Channel channel = (Channel) ch;
        ChannelHandler decoder = channel.pipeline().get("decoder");
        if (decoder instanceof CustomBukkitDecodeHandler) {
            CustomBukkitDecodeHandler customBukkitDecodeHandler = (CustomBukkitDecodeHandler) decoder;
            customBukkitDecodeHandler.addCustomDecoder(customDecoder);
        } else if (decoder instanceof BukkitDecodeHandler) {
            ReflectionObject reflectionObject = new ReflectionObject(decoder);
            UserConnection userConnectionInfo = reflectionObject.read(0, UserConnection.class);
            ByteToMessageDecoder minecraftDecoder = reflectionObject.read(0, ByteToMessageDecoder.class);
            CustomBukkitDecodeHandler customBukkitDecodeHandler = new CustomBukkitDecodeHandler(userConnectionInfo, minecraftDecoder, decoder);
            customBukkitDecodeHandler.addCustomDecoder(customDecoder);
            channel.pipeline().replace("decoder", "decoder", customBukkitDecodeHandler);
            System.out.println("REPLACED like a lil' sussy baka");
            System.out.println("NEW HANDLERS: " + Arrays.toString(((Channel) channel).pipeline().names().toArray(new String[0])));
            //TODO load before via and have em' wrap our decoder
        } else if (ClassUtil.getClassSimpleName(decoder.getClass()).equals("CustomBukkitDecodeHandler")) {
            ReflectionObject reflectionObject = new ReflectionObject(decoder);
            //TODO Test multiple packetevents instances that have shaded in diff locations
            List<MessageToMessageDecoder<?>> customDecoders = reflectionObject.readList(0);
            ByteToMessageDecoder minecraftDecoder = reflectionObject.read(0, ByteToMessageDecoder.class);
            UserConnection userConnection = reflectionObject.read(0, UserConnection.class);

            ChannelHandler oldBukkitDecoder = reflectionObject.readObject(0, ChannelHandler.class);

            CustomBukkitDecodeHandler customBukkitDecodeHandler = new CustomBukkitDecodeHandler(userConnection, minecraftDecoder, oldBukkitDecoder);
            customBukkitDecodeHandler.customDecoders.addAll(customDecoders);
            customBukkitDecodeHandler.addCustomDecoder(customDecoder);
            channel.pipeline().replace("decoder", "decoder", customBukkitDecodeHandler);
        }
    }
}
