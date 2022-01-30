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

package com.github.retrooper.packetevents.event;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public abstract class ProtocolPacketEvent<T> extends PacketEvent implements PlayerEvent<T>, CancellableEvent {
    private final ChannelAbstract channel;
    private final User user;
    private final T player;
    private ByteBufAbstract byteBuf;
    private final int packetID;
    private final PacketTypeCommon packetType;
    private ServerVersion serverVersion;
    private boolean cancel;
    private PacketWrapper<?> lastUsedWrapper;
    private final List<Runnable> postTasks = new ArrayList<>();

    public ProtocolPacketEvent(PacketSide packetSide, Object channel, User user, T player, Object rawByteBuf) {
        this(packetSide,
                PacketEvents.getAPI().getNettyManager().wrapChannel(channel),
                user,
                player,
                PacketEvents.getAPI().getNettyManager().wrapByteBuf(rawByteBuf));
    }

    public ProtocolPacketEvent(PacketSide packetSide, ConnectionState connectionState, Object channel, User user,
                               T player, Object rawByteBuf) {
        this(packetSide,
                connectionState,
                PacketEvents.getAPI().getNettyManager().wrapChannel(channel),
                user,
                player,
                PacketEvents.getAPI().getNettyManager().wrapByteBuf(rawByteBuf));
    }


    public ProtocolPacketEvent(PacketSide packetSide, ChannelAbstract channel, User user, T player, ByteBufAbstract byteBuf) {
        this(packetSide, user.getConnectionState(), channel, user, player, byteBuf);
    }

    public ProtocolPacketEvent(PacketSide packetSide, ConnectionState connectionState, ChannelAbstract channel,
                               User user, T player, ByteBufAbstract byteBuf) {
        this.channel = channel;
        this.user = user;
        this.player = player;

        if (user.getClientVersion() == null) {
            ClientVersion version = null;
            if (player != null) {
                //Possibly ask a soft-dependency(for example, ViaVersion) for the client version.
                version = PacketEvents.getAPI().getPlayerManager().getClientVersion(player);
            }
            if (version == null) {
                version = ClientVersion.UNKNOWN;
            }

            if (version != null) {
                user.setClientVersion(version);
            }
        }


        this.serverVersion = PacketEvents.getAPI().getServerManager().getVersion();

        this.byteBuf = byteBuf;
        this.packetID = readVarInt(byteBuf);
        this.packetType = PacketType.getById(packetSide, connectionState, this.serverVersion, packetID);
    }

    public ProtocolPacketEvent(int packetID, PacketTypeCommon packetType, ServerVersion serverVersion, InetSocketAddress socketAddress, ChannelAbstract channel,
                               User user, T player, ByteBufAbstract byteBuf) {
        this.channel = channel;
        this.user = user;
        this.player = player;
        this.serverVersion = serverVersion;
        this.byteBuf = byteBuf;
        this.packetID = packetID;
        this.packetType = packetType;
    }

    private static int readVarInt(ByteBufAbstract byteBuf) {
        byte b0;
        int i = 0;
        int j = 0;
        do {
            b0 = byteBuf.readByte();
            i |= (b0 & Byte.MAX_VALUE) << j++ * 7;
            if (j > 5)
                throw new RuntimeException("VarInt too big");
        } while ((b0 & 128) == 128);
        return i;
    }

    public ChannelAbstract getChannel() {
        return channel;
    }

    public InetSocketAddress getSocketAddress() {
        return ((InetSocketAddress) channel.remoteAddress());
    }

    @NotNull
    public User getUser() {
        return user;
    }

    @Nullable
    @Override
    public T getPlayer() {
        return player;
    }

    public ConnectionState getConnectionState() {
        return user.getConnectionState();
    }

    //TODO Possibly put clientversion inside User class and remove here
    public ClientVersion getClientVersion() {
        return user.getClientVersion();
    }

    public void setClientVersion(@NotNull ClientVersion clientVersion) {
        user.setClientVersion(clientVersion);
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
        this.byteBuf = byteBuf;
    }

    @Deprecated
    public int getPacketId() {
        return packetID;
    }

    public PacketTypeCommon getPacketType() {
        return packetType;
    }

    @Deprecated
    public String getPacketName() {
        return ((Enum<?>) packetType).name();
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean val) {
        this.cancel = val;
    }

    @Nullable
    public PacketWrapper<?> getLastUsedWrapper() {
        return lastUsedWrapper;
    }

    public void setLastUsedWrapper(@Nullable PacketWrapper<?> lastUsedWrapper) {
        this.lastUsedWrapper = lastUsedWrapper;
    }

    public List<Runnable> getPostTasks() {
        return postTasks;
    }
}
