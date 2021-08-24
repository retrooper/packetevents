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

package io.github.retrooper.packetevents.injector.modern;

import io.github.retrooper.packetevents.PacketEvents;
import io.netty.channel.*;

import java.util.Arrays;

public class PacketCompressionHandlerModern extends ChannelDuplexHandler {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.write(msg);

        if (msg.getClass().getSimpleName().equals("PacketLoginOutSetCompression")) {
            System.out.println("GOT: " + msg.getClass().getSimpleName());
            System.out.println("PIPELINE: " + Arrays.toString(ctx.pipeline().names().toArray(new String[0])));
            if (ctx.pipeline().get("decompress") != null) {
                String decoderName = PacketEvents.get().decoderName;;
                ChannelHandler handler = ctx.pipeline().remove(decoderName);
                ctx.pipeline().addAfter("decompress", decoderName, handler);
                System.out.println("Added our handler after decompress: " + Arrays.toString(ctx.pipeline().names().toArray(new String[0])));
            }
            else {
                System.out.println("what");
            }
        }
    }
}
