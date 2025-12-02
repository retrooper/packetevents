/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package io.github.retrooper.packetevents.factory.fabric;

import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.ProtocolVersion;
import io.github.retrooper.packetevents.impl.netty.manager.protocol.ProtocolManagerAbstract;
import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;

public class FabricProtocolManager extends ProtocolManagerAbstract {

    private final boolean invert;

    public FabricProtocolManager(EnvType environment) {
        this.invert = environment == EnvType.CLIENT;
    }

    private void receivePacket0(Object channel, Object byteBuf) {
        if (ChannelHelper.isOpen(channel)) {
            if (ChannelHelper.pipelineHandlerNames(channel).contains("decompress")) {
                ChannelHelper.fireChannelReadInContext(channel, "decompress", byteBuf);
            } else {
                ChannelHelper.fireChannelRead(channel, byteBuf);
            }
        } else {
            ((ByteBuf) byteBuf).release();
        }
    }

    @Override
    public ProtocolVersion getPlatformVersion() {
        return ProtocolVersion.UNKNOWN; // TODO implement platform version
    }

    @Override
    public void sendPacket(Object channel, Object byteBuf) {
        if (this.invert) {
            this.receivePacket0(channel, byteBuf);
        } else {
            super.sendPacket(channel, byteBuf);
        }
    }

    @Override
    public void sendPacketSilently(Object channel, Object byteBuf) {
        if (this.invert) {
            super.receivePacketSilently(channel, byteBuf);
        } else {
            super.sendPacketSilently(channel, byteBuf);
        }
    }

    @Override
    public void writePacket(Object channel, Object byteBuf) {
        if (this.invert) {
            this.receivePacket0(channel, byteBuf);
        } else {
            super.writePacket(channel, byteBuf);
        }
    }

    @Override
    public void writePacketSilently(Object channel, Object byteBuf) {
        if (this.invert) {
            super.receivePacketSilently(channel, byteBuf);
        } else {
            super.writePacketSilently(channel, byteBuf);
        }
    }

    @Override
    public void receivePacket(Object channel, Object byteBuf) {
        if (this.invert) {
            // no way to specify wether to flush or not, so just don't
            super.writePacket(channel, byteBuf);
        } else {
            this.receivePacket0(channel, byteBuf);
        }
    }

    @Override
    public void receivePacketSilently(Object channel, Object byteBuf) {
        if (this.invert) {
            // no way to specify wether to flush or not, so just don't
            super.writePacketSilently(channel, byteBuf);
        } else {
            super.receivePacketSilently(channel, byteBuf);
        }
    }
}
