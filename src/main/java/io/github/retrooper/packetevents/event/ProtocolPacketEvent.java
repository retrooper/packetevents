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

package io.github.retrooper.packetevents.event;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.type.CancellableEvent;
import io.github.retrooper.packetevents.event.type.PlayerEvent;
import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.protocol.ConnectionState;
import io.github.retrooper.packetevents.protocol.PacketSide;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.protocol.PacketTypeCommon;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAbstract;
import io.github.retrooper.packetevents.utils.netty.channel.ChannelAbstract;
import io.github.retrooper.packetevents.utils.wrapper.PacketWrapperUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;

public abstract class ProtocolPacketEvent extends PacketEvent implements PlayerEvent, CancellableEvent {
    private final ChannelAbstract channel;
    private final InetSocketAddress socketAddress;
    private final Player player;


    private ConnectionState connectionState;
    private ClientVersion clientVersion;
    private ServerVersion serverVersion;

    private final ByteBufAbstract byteBuf;
    private final int packetID;
    private final PacketTypeCommon packetType;

    private boolean cancel;

    public ProtocolPacketEvent(PacketSide packetSide, Object channel, Player player, Object rawByteBuf) {
        this(packetSide, ChannelAbstract.generate(channel), player, ByteBufAbstract.generate(rawByteBuf));
    }


    public ProtocolPacketEvent(PacketSide packetSide, ChannelAbstract channel, Player player, ByteBufAbstract byteBuf) {
        this.channel = channel;
        this.socketAddress = (InetSocketAddress) channel.remoteAddress();
        this.player = player;

        this.connectionState = PacketEvents.get().getInjector().getConnectionState(channel.rawChannel());

        ClientVersion version = PacketEvents.get().getPlayerManager().clientVersions.get(channel.rawChannel());
        if (version == null) {
            if (player != null) {
                //Possibly ask a soft-dependency(for example, ViaVersion) for the client version.
                version = PacketEvents.get().getPlayerManager().getClientVersion(player);
            }
        }

        if (version == null) {
            version = ClientVersion.UNKNOWN;
        }
        this.clientVersion = version;

        this.serverVersion = PacketEvents.get().getServerManager().getVersion();

        this.byteBuf = byteBuf;
        this.packetID = PacketWrapperUtils.readVarInt(byteBuf);
        this.packetType = PacketType.getById(packetSide, connectionState, clientVersion, packetID);
    }

    public ChannelAbstract getChannel() {
        return channel;
    }

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    @Nullable
    @Override
    public Player getPlayer() {
        return player;
    }

    public ConnectionState getConnectionState() {
        return connectionState;
    }

    public void setConnectionState(ConnectionState connectionState) {
        this.connectionState = connectionState;
        PacketEvents.get().getInjector().changeConnectionState(channel, connectionState);
    }

    public ClientVersion getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(@NotNull ClientVersion clientVersion) {
        this.clientVersion = clientVersion;
    }

    public ServerVersion getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(@NotNull ServerVersion serverVersion) {
        this.serverVersion = serverVersion;
    }

    public ByteBufAbstract getByteBuf() {
        return byteBuf;
    }

    public void setByteBuf(ByteBufAbstract byteBuf) {
        this.byteBuf.clear().writeBytes(byteBuf.readBytes(byteBuf.readableBytes()));
    }

    @Deprecated
    public int getPacketID() {
        return packetID;
    }

    @Nullable
    public PacketTypeCommon getPacketType() {
        return packetType;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean val) {
        this.cancel = val;
    }
}
