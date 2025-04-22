package io.github.retrooper.packetevents.manager;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import io.github.retrooper.packetevents.impl.netty.manager.player.PlayerManagerAbstract;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractFabricPlayerManager extends PlayerManagerAbstract {

    private final PacketEventsAPI<?> packetEventsAPI;

    public AbstractFabricPlayerManager(PacketEventsAPI<?> packetEventsAPI) {
        this.packetEventsAPI = packetEventsAPI;
    }

    @Override
    public ConnectionState getConnectionState(@NotNull Object player) throws IllegalStateException {
        return getUser(player).getConnectionState();
    }

    @Override
    public void sendPacket(@NotNull Object player, @NotNull Object byteBuf) {
        packetEventsAPI.getProtocolManager().sendPacket(getChannel(player), byteBuf);
    }
    @Override
    public void sendPacket(@NotNull Object player, @NotNull PacketWrapper<?> wrapper) {
        packetEventsAPI.getProtocolManager().sendPacket(getChannel(player), wrapper);
    }

    @Override
    public void sendPacketSilently(@NotNull Object player, @NotNull Object byteBuf) {
        packetEventsAPI.getProtocolManager().sendPacketSilently(getChannel(player), byteBuf);
    }

    @Override
    public void sendPacketSilently(@NotNull Object player, @NotNull PacketWrapper<?> wrapper) {
        packetEventsAPI.getProtocolManager().sendPacketSilently(getChannel(player), wrapper);
    }

    @Override
    public void writePacket(@NotNull Object player, @NotNull Object byteBuf) {
        packetEventsAPI.getProtocolManager().writePacket(getChannel(player), byteBuf);
    }

    @Override
    public void writePacket(@NotNull Object player, @NotNull PacketWrapper<?> wrapper) {
        packetEventsAPI.getProtocolManager().writePacket(getChannel(player), wrapper);
    }

    @Override
    public void writePacketSilently(@NotNull Object player, @NotNull Object byteBuf) {
        packetEventsAPI.getProtocolManager().writePacketSilently(getChannel(player), byteBuf);
    }

    @Override
    public void writePacketSilently(@NotNull Object player, @NotNull PacketWrapper<?> wrapper) {
        packetEventsAPI.getProtocolManager().writePacketSilently(getChannel(player), wrapper);
    }

    @Override
    public void receivePacket(Object player, Object byteBuf) {
        packetEventsAPI.getProtocolManager().receivePacket(getChannel(player), byteBuf);
    }

    @Override
    public void receivePacket(Object player, PacketWrapper<?> wrapper) {
        packetEventsAPI.getProtocolManager().receivePacket(getChannel(player), wrapper);
    }

    @Override
    public void receivePacketSilently(Object player, Object byteBuf) {
        packetEventsAPI.getProtocolManager().receivePacketSilently(getChannel(player), byteBuf);
    }

    @Override
    public void receivePacketSilently(Object player, PacketWrapper<?> wrapper) {
        packetEventsAPI.getProtocolManager().receivePacketSilently(getChannel(player), wrapper);
    }

    public abstract void disconnectPlayer(ServerPlayerEntity serverPlayerEntity, String message);
}
