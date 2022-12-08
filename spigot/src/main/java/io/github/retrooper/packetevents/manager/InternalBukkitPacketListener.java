package io.github.retrooper.packetevents.manager;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import io.github.retrooper.packetevents.util.protocolsupport.ProtocolSupportUtil;
import io.github.retrooper.packetevents.util.viaversion.ViaVersionUtil;

import java.net.InetSocketAddress;

public class InternalBukkitPacketListener extends com.github.retrooper.packetevents.manager.InternalPacketListener {
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        User user = event.getUser();
        if (event.getPacketType() == PacketType.Handshaking.Client.HANDSHAKE) {
            Object channel = event.getChannel();
            InetSocketAddress address = event.getSocketAddress();
            WrapperHandshakingClientHandshake handshake = new WrapperHandshakingClientHandshake(event);
            ConnectionState nextState = handshake.getNextConnectionState();
            ClientVersion clientVersion = handshake.getClientVersion();
            if (ViaVersionUtil.isAvailable()) {
                clientVersion = ClientVersion.getById(ViaVersionUtil.getProtocolVersion(user));
            } else if (ProtocolSupportUtil.isAvailable()) {
                clientVersion = ClientVersion.getById(ProtocolSupportUtil.getProtocolVersion(user.getAddress()));
            }
            if (clientVersion == ClientVersion.UNKNOWN) {
                return;
            }
            //Update client version for this event call(and user)
            user.setClientVersion(clientVersion);
            PacketEvents.getAPI().getLogManager().debug("Processed " + address.getHostString() + ":" + address.getPort() + "'s client version. Client Version: " + clientVersion.getReleaseName());
            event.getPostTasks().add(() -> {
                //Transition into the LOGIN OR STATUS connection state
                PacketEvents.getAPI().getInjector().changeConnectionState(channel, nextState);
                PacketEvents.getAPI().getLogManager().debug("Transitioned " + address.getHostString() + ":" + address.getPort() + " into the " + nextState + " state!");
            });
        }
    }
}
