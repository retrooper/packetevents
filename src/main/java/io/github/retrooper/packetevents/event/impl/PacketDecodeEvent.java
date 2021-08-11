package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.eventtypes.CancellableEvent;
import io.github.retrooper.packetevents.event.eventtypes.PlayerEvent;
import io.github.retrooper.packetevents.protocol.PacketSide;
import io.github.retrooper.packetevents.protocol.PacketState;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.protocol.PacketTypeAbstract;
import io.github.retrooper.packetevents.utils.bytebuf.ByteBufAbstract;
import io.github.retrooper.packetevents.utils.channel.ChannelUtils;
import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;

public class PacketDecodeEvent extends PacketEvent implements PlayerEvent, CancellableEvent {
    private final Object channel;
    private final Player player;
    private ClientVersion version;
    private boolean cancel;
    private ByteBufAbstract byteBuf;
    private final int packetIDNum;

    public PacketDecodeEvent(Object channel, Player player, ByteBufAbstract byteBuf) {
        this.channel = channel;
        this.player = player;
        this.version = PacketEvents.get().getPlayerManager().clientVersions.get(channel);
        if (this.version == null) {
            this.version = ClientVersion.UNKNOWN;
        }
        this.byteBuf = byteBuf.duplicate();
        PacketWrapper packetWrapper = new PacketWrapper(this.byteBuf);
        this.packetIDNum = packetWrapper.readVarInt();
    }

    public PacketDecodeEvent(Object channel, Player player, Object rawByteBuf) {
        this.channel = channel;
        this.player = player;
        this.version = PacketEvents.get().getPlayerManager().clientVersions.get(channel);
        if (this.version == null) {
            this.version = ClientVersion.UNKNOWN;
        }
        this.byteBuf = PacketEvents.get().getServerManager().generateByteBufAbstract(rawByteBuf);
        PacketWrapper packetWrapper = new PacketWrapper(this.byteBuf);
        this.packetIDNum = packetWrapper.readVarInt();
    }

    public int getPacketID() {
        return packetIDNum;
    }

    @Nullable
    public PacketTypeAbstract getPacketType() {
        PacketState state = getState();
        return PacketType.getById(PacketSide.CLIENT, state, version, packetIDNum);
    }

    public PacketState getState() {
        return PacketEvents.get().getInjector().getPacketState(channel);
    }

    public Object getChannel() {
        return channel;
    }

    public InetSocketAddress getSocketAddress() {
        return ChannelUtils.getSocketAddress(channel);
    }

    public ClientVersion getClientVersion() {
        return version;
    }

    public void setClientVersion(ClientVersion version) {
        this.version = version;
        PacketEvents.get().getPlayerManager().clientVersions.put(channel, version);
    }

    @Override
    public void call(PacketListenerAbstract listener) {
        listener.onPacketDecode(this);
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
    @Override
    public Player getPlayer() {
        return player;
    }

    public ByteBufAbstract getByteBuf() {
        return this.byteBuf;
    }

    public void setByteBuf(ByteBufAbstract byteBuf) {
        this.byteBuf = byteBuf;
    }
}
