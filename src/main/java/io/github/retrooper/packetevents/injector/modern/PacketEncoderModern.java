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

import io.github.retrooper.packetevents.utils.bytebuf.ByteBufModern;
import io.github.retrooper.packetevents.utils.bytebuf.ByteBufUtil;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PacketEncoderModern extends MessageToByteEncoder {
    private static Method ENCODE_METHOD;
    public final MessageToByteEncoder previousEncoder;
    public MessageToByteEncoder minecraftEncoder;
    public boolean isPreviousEncoderMinecraft = false;

    private void load() {
        if (ENCODE_METHOD == null) {
            try {
                ENCODE_METHOD = MessageToByteEncoder.class.getDeclaredMethod("encode", ChannelHandlerContext.class, Object.class, ByteBuf.class);
                ENCODE_METHOD.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }


    public PacketEncoderModern(MessageToByteEncoder previousEncoder) {
        load();
        this.previousEncoder = previousEncoder;
        if (ClassUtil.getClassSimpleName(previousEncoder.getClass()).equals("PacketEncoder")) {
            isPreviousEncoderMinecraft = true;
        } else {
            try {
                minecraftEncoder = (MessageToByteEncoder) Reflection.getField(previousEncoder.getClass(), MessageToByteEncoder.class, 0).get(previousEncoder);
            System.out.println("FOUND MC: " + minecraftEncoder.getClass().getSimpleName());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object o, ByteBuf byteBuf) throws Exception {
        if (!(o instanceof ByteBuf)) {
            //First call minecraft's encoder, we need minecraft to transform the packet into a bytebuf.
            try {
                System.out.println("CALLING MC'S ENCODER FIRST: " + minecraftEncoder.getClass().getSimpleName());
                ENCODE_METHOD.invoke((isPreviousEncoderMinecraft ? this.previousEncoder : this.previousEncoder), ctx, o, byteBuf);
            } catch (InvocationTargetException ex) {
                if (ex.getCause() instanceof Exception) {
                    throw (Exception) ex.getCause();
                } else if (ex.getCause() instanceof Error) {
                    throw (Error) ex.getCause();
                }
            }
        } else {
            //Oh.. its already in bytebuf form
            byteBuf.writeBytes((ByteBuf) o);
        }


        //Now after we have processed the bytebuf, we can pass it onto ViaVersion and other custom encoders.
      /*  if (!isPreviousEncoderMinecraft) {
            try {
                System.out.println("ARE WE CALLING VIA NOW? : " + this.previousEncoder.getClass().getSimpleName() + ", OBJECT TYPE: " + o.getClass().getSimpleName());
                //ENCODE_METHOD.invoke(this.previousEncoder, ctx, o, byteBuf);
                ENCODE_METHOD.invoke(this.previousEncoder, ctx, o, byteBuf);
            } catch (InvocationTargetException ex) {
                if (ex.getCause() instanceof Exception) {
                    throw (Exception) ex.getCause();
                } else if (ex.getCause() instanceof Error) {
                    throw (Error) ex.getCause();
                }
            }
        }*/
    }
}
