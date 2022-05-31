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

package io.github.retrooper.packetevents.manager.protocol;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.ProtocolVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import io.github.retrooper.packetevents.util.protocolsupport.ProtocolSupportUtil;
import io.netty.buffer.ByteBuf;

import java.util.List;

public class ProtocolManagerImpl implements ProtocolManager {
    private ProtocolVersion platformVersion;

    //TODO Implement
    private ProtocolVersion resolveVersionNoCache() {
        return ProtocolVersion.UNKNOWN;
    }

    @Override
    public ProtocolVersion getPlatformVersion() {
        if (platformVersion == null) {
            platformVersion = resolveVersionNoCache();
        }
        return platformVersion;
    }

    @Override
    public void sendPacket(Object channel, Object byteBuf) {
        if (ChannelHelper.isOpen(channel)) {
            //When ProtocolSupport is available, and we send a message through their logic handler,
            //we must retain the message.
            if (ProtocolSupportUtil.isAvailable() && byteBuf instanceof ByteBuf) {
                ((ByteBuf) byteBuf).retain();
            }
            ChannelHelper.writeAndFlush(channel, byteBuf);
        } else {
            ((ByteBuf) byteBuf).release();
        }
    }

    @Override
    public void sendPacketSilently(Object channel, Object byteBuf) {
        if (ChannelHelper.isOpen(channel)) {
            //Only call the encoders after ours in the pipeline.
            //Here we do not need to retain when ProtocolSupport is present
            ChannelHelper.writeAndFlushInContext(channel, PacketEvents.ENCODER_NAME, byteBuf);
        } else {
            ((ByteBuf) byteBuf).release();
        }
    }

    @Override
    public void writePacket(Object channel, Object byteBuf) {
        if (ChannelHelper.isOpen(channel)) {
            //Write to all encoders.
            //When ProtocolSupport is available, and we send a message through their logic handler,
            //we must retain the message.
            if (ProtocolSupportUtil.isAvailable() && byteBuf instanceof ByteBuf) {
                ((ByteBuf) byteBuf).retain();
            }
            ChannelHelper.write(channel, byteBuf);
        } else {
            ((ByteBuf) byteBuf).release();
        }
    }

    @Override
    public void writePacketSilently(Object channel, Object byteBuf) {
        if (ChannelHelper.isOpen(channel)) {
            //Only call the encoders after ours in the pipeline
            //Here we do not need to retain when ProtocolSupport is present
            ChannelHelper.writeInContext(channel, PacketEvents.ENCODER_NAME, byteBuf);
        } else {
            ((ByteBuf) byteBuf).release();
        }
    }

    @Override
    public void receivePacket(Object channel, Object byteBuf) {
        if (ChannelHelper.isOpen(channel)) {
            //TODO Have we given ViaVersion a thought?
            List<String> handlerNames = ChannelHelper.pipelineHandlerNames(channel);
            if (handlerNames.contains("ps_decoder_transformer")) {
                //We want to skip ProtocolSupport's translation handlers,
                //because the buffer is fit for the current server-version
                ChannelHelper.fireChannelReadInContext(channel, "ps_decoder_transformer", byteBuf);
            } else if (handlerNames.contains("decompress")) {
                //We will have to just skip through the minecraft server's decompression handler
                ChannelHelper.fireChannelReadInContext(channel, "decompress", byteBuf);
            } else {
                if (handlerNames.contains("decrypt")) {
                    //We will have to just skip through the minecraft server's decryption handler
                    //We don't have to deal with decompressing, as that handler isn't currently in the pipeline
                    ChannelHelper.fireChannelReadInContext(channel, "decrypt", byteBuf);
                } else {
                    //No decompressing nor decrypting handlers are present
                    //You cannot fill this buffer up with chunks of packets,
                    //since we skip the packet-splitter handler.
                    ChannelHelper.fireChannelReadInContext(channel, "splitter", byteBuf);
                }
            }
        } else {
            ((ByteBuf) byteBuf).release();
        }
    }

    @Override
    public void receivePacketSilently(Object channel, Object byteBuf) {
        //Receive the packet for all handlers after our decoder
        //TODO Consider viaversion when we are in play state
        ChannelHelper.fireChannelReadInContext(channel, PacketEvents.DECODER_NAME, byteBuf);
    }

    @Override
    public ClientVersion getClientVersion(Object channel) {
        User user = getUser(channel);
        if (user.getClientVersion() == null) {
            return ClientVersion.UNKNOWN;
        }
        return user.getClientVersion();
    }
}
