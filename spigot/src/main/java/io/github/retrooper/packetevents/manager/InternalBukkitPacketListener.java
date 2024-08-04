package io.github.retrooper.packetevents.manager;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.NibbleArray3d;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_16.Chunk_v1_9;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import com.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChunkData;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetSlot;
import io.github.retrooper.packetevents.util.protocolsupport.ProtocolSupportUtil;
import io.github.retrooper.packetevents.util.viaversion.ViaVersionUtil;

import java.net.InetSocketAddress;
import java.util.Collections;

public class InternalBukkitPacketListener extends com.github.retrooper.packetevents.manager.InternalPacketListener {
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        User user = event.getUser();
        //Only handle Handshake uniquely on Bukkit
        if (event.getPacketType() == PacketType.Handshaking.Client.HANDSHAKE) {
            InetSocketAddress address = event.getSocketAddress();
            WrapperHandshakingClientHandshake handshake = new WrapperHandshakingClientHandshake(event);
            ConnectionState nextState = handshake.getNextConnectionState();
            ClientVersion clientVersion = handshake.getClientVersion();

            PacketEvents.getAPI().getLogManager().debug("Read handshake version for " + address.getHostString() + ":" + address.getPort() + " as " + clientVersion);

            if (ViaVersionUtil.isAvailable()) {
                clientVersion = ClientVersion.getById(ViaVersionUtil.getProtocolVersion(user));
                PacketEvents.getAPI().getLogManager().debug("Read ViaVersion version for " + address.getHostString() + ":" + address.getPort() + " as " + clientVersion + " with UUID=" + user.getUUID());
            } else if (ProtocolSupportUtil.isAvailable()) {
                clientVersion = ClientVersion.getById(ProtocolSupportUtil.getProtocolVersion(user.getAddress()));
                PacketEvents.getAPI().getLogManager().debug("Read ProtocolSupport version for " + address.getHostString() + ":" + address.getPort() + " as " + clientVersion);
            }
            if (clientVersion == ClientVersion.UNKNOWN) {
                PacketEvents.getAPI().getLogManager().debug("Client version for " + address.getHostString() + ":" + address.getPort() + " is unknown!");
            }
            //Update client version for this event call(and user)
            user.setClientVersion(clientVersion);
            PacketEvents.getAPI().getLogManager().debug("Processed " + address.getHostString() + ":" + address.getPort() + "'s client version. Client Version: " + clientVersion.getReleaseName());
            //Transition into LOGIN or STATUS connection state immediately, to remain in sync with vanilla
            user.setConnectionState(nextState);
        } else if (event.getPacketType() == PacketType.Play.Client.CHAT_COMMAND_UNSIGNED
                || event.getPacketType() == PacketType.Play.Client.CHAT_COMMAND) {
            EntityData data = new EntityData(17, EntityDataTypes.BYTE, (byte) 0b1111110);
            WrapperPlayServerEntityMetadata metadata = new WrapperPlayServerEntityMetadata(
                    user.getEntityId(), Collections.singletonList(data));
            user.sendPacket(metadata);
            user.sendMessage("sent entity data for yourself");
        } else {
            super.onPacketReceive(event);
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.CHUNK_DATA) {
            WrapperPlayServerChunkData chunk = new WrapperPlayServerChunkData(event);
            BaseChunk[] layers = chunk.getColumn().getChunks();
            for (int i = 0; i < layers.length; i++) {
                if (layers[i] == null) {
                    layers[i] = BaseChunk.create();
                    if (layers[i] instanceof Chunk_v1_9) {
                        ((Chunk_v1_9) layers[i]).setBlockLight(new NibbleArray3d(4096));
                        if (event.getUser().getDimension().getId() == 0) {
                            ((Chunk_v1_9) layers[i]).setSkyLight(new NibbleArray3d(4096));
                        }
                    }
                }
                layers[i].set(0, 0, 0, StateTypes.AIR.createBlockState());
                layers[i].set(0, 1, 0, StateTypes.STONE.createBlockState());
            }
            event.markForReEncode(true);
        } else if (event.getPacketType() == PacketType.Play.Server.CHAT_MESSAGE) {
            WrapperPlayServerChatMessage message = new WrapperPlayServerChatMessage(event);
            System.out.println(message.getMessage().getType().getName());
        } else if (event.getPacketType() == PacketType.Play.Server.SET_SLOT) {
            WrapperPlayServerSetSlot packet = new WrapperPlayServerSetSlot(event);
            System.out.println(packet.getItem());
            System.out.println(packet.getItem().getComponents().getPatches().keySet());
            packet.getItem().getComponent(ComponentTypes.BANNER_PATTERNS)
                    .ifPresent(layers -> System.out.println(layers.getLayers().get(0).getPattern().getName()));
        } else {
            super.onPacketSend(event);
        }
    }
}
