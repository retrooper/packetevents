/*
 * This file is part of ProtocolLib - https://github.com/retrooper/packetevents
 * Copyright (C) ProtocolLib
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.comphenix.protocol.injector.netty;

import com.comphenix.protocol.events.NetworkMarker;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.NetworkProcessor;
import com.comphenix.protocol.reflect.VolatileField;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.reflection.ReflectionObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.*;

public class CustomChannelInjector extends ChannelInjector {
    public ChannelInjector originalChannelInjector = null;
    public CustomChannelInjector(Player player, Object networkManager, Channel channel, ChannelListener channelListener, InjectionFactory factory) {
        super(player, networkManager, channel, channelListener, factory);
    }

    public static CustomChannelInjector construct(Object rawInjector) {
        ChannelInjector injector = (ChannelInjector) rawInjector;
        //Copy all fields from original injector into this one via lots of reflection
        ReflectionObject reflectChannelInjector = new ReflectionObject(injector);
        Field networkManagerField = Reflection.getField(injector.getClass(), "networkManager");
        Object networkManager = null;
        try {
            networkManager = networkManagerField.get(injector);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        ChannelListener channelListener = reflectChannelInjector.read(0, ChannelListener.class);
        InjectionFactory factory = reflectChannelInjector.read(0, InjectionFactory.class);
        CustomChannelInjector customChannelInjector = new CustomChannelInjector(injector.getPlayer(),  networkManager, injector.getChannel(),
                channelListener, factory);

        customChannelInjector.originalChannelInjector = injector;

        ReflectionObject reflectCustomChannelInjector = new ReflectionObject(customChannelInjector);

        reflectCustomChannelInjector.write(Player.class, 1, reflectChannelInjector.read(1, Player.class));
        reflectCustomChannelInjector.writeString(0, reflectChannelInjector.readString(0));

        Field playerConnectionField = Reflection.getField(injector.getClass(), "playerConnection");
        Field packetMarkerField = Reflection.getField(injector.getClass(), "packetMarker");
        try {
            Object playerConnection = playerConnectionField.get(injector);
            playerConnectionField.set(customChannelInjector, playerConnection);

            Object packetMarker = packetMarkerField.get(injector);
            packetMarkerField.set(customChannelInjector, packetMarker);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        reflectCustomChannelInjector.write(PacketEvent.class, 0, reflectChannelInjector.read(0, PacketEvent.class));
        reflectCustomChannelInjector.write(PacketEvent.class, 1, reflectChannelInjector.read(1, PacketEvent.class));

        reflectCustomChannelInjector.write(PacketFilterQueue.class, 0, reflectChannelInjector.read(0, PacketFilterQueue.class));

        reflectCustomChannelInjector.write(ByteToMessageDecoder.class, 0, reflectChannelInjector.read(0, ByteToMessageDecoder.class));
        reflectCustomChannelInjector.write(MessageToByteEncoder.class, 0, reflectChannelInjector.read(0, MessageToByteEncoder.class));


        reflectCustomChannelInjector.write(Deque.class, 0, reflectChannelInjector.read(0, Deque.class));

        reflectCustomChannelInjector.writeBoolean(0, reflectChannelInjector.readBoolean(0));
        reflectCustomChannelInjector.writeBoolean(1, reflectChannelInjector.readBoolean(1));

        return customChannelInjector;
    }
}
