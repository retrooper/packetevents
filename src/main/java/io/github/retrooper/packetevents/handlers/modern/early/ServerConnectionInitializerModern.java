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

package io.github.retrooper.packetevents.handlers.modern.early;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.handlers.modern.PacketDecoderModern;
import io.github.retrooper.packetevents.handlers.modern.PacketEncoderModern;
import io.netty.channel.Channel;

import java.util.Arrays;

public class ServerConnectionInitializerModern {
    public static void postInitChannel(Channel channel) {
        channel.pipeline().addAfter("splitter", PacketEvents.get().decoderName, new PacketDecoderModern());
        channel.pipeline().addBefore("encoder", PacketEvents.get().encoderName, new PacketEncoderModern());
        System.out.println("HANDLERS: " + Arrays.toString(channel.pipeline().names().toArray(new String[0])));
    }

    public static void postDestroyChannel(Channel channel) {
        channel.pipeline().remove(PacketEvents.get().decoderName);
        channel.pipeline().remove(PacketEvents.get().encoderName);
    }
}
