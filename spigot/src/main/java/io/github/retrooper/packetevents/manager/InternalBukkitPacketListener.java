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
        } else {
            super.onPacketReceive(event);
        }
    }
}
